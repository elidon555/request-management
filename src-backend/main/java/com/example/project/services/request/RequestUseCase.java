package com.example.project.services.request;

import com.example.project.database.entity.RequestEntity;
import com.example.project.database.entity.ResourceEntity;
import com.example.project.database.entity.SkillEntity;
import com.example.project.database.entity.User;
import com.example.project.database.enums.AreaOfInterest;
import com.example.project.database.enums.Status;
import com.example.project.database.repository.RequestRepository;
import com.example.project.database.repository.ResourceRepository;
import com.example.project.database.repository.SkillsRepository;
import com.example.project.database.repository.UserRepository;
import com.example.project.mapper.RequestDTO;
import com.example.project.mapper.RequestFilterParam;
import com.example.project.mapper.ResourceDTO;
import com.example.project.mapper.SkillDTO;
import com.example.project.services.EmailService;
import com.example.project.services.UserDetailsImpl;
import com.example.project.services.request.createRequest.RequestCreate;
import com.example.project.services.request.deleteRequest.DeleteRequest;
import com.example.project.services.request.filterRequest.FilterRequest;
import com.example.project.services.request.getRequest.RequestByUser;

import com.example.project.services.request.updateRequest.UpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestUseCase implements RequestCreate, RequestByUser, DeleteRequest, UpdateRequest, FilterRequest {

    private final RequestRepository requestEntityRepository;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final SkillsRepository skillsRepository;
    private final EntityManager entityManager;
    private final EmailService emailService;




    @Override
    public RequestDTO createRequest(final RequestDTO requestDTO) {

        if (requestDTO.getStatus() == null)
            requestDTO.setStatus(Status.CREATED);

        RequestEntity entity = RequestDTO.toEntity(requestDTO);
        List<ResourceEntity> resourceEntities = new ArrayList<>();
        requestDTO.getResourceDTOS().forEach(resourceDTO -> {
            resourceEntities.add(ResourceEntity.builder()
                    .resourceNotes(resourceDTO.getResourceNotes())
                    .seniority(resourceDTO.getSeniority())
                    .requestEntity(entity)
                    .skillEntities(SkillDTO.toEntityList(resourceDTO.getSkillDTOS()))
                    .build());
        });
        entity.setResourceEntities(resourceEntities);
        requestEntityRepository.save(entity);

        emailService.sendSimpleMessage(
                adminEmails(),
                "New request made by "+ entity.getCreatedBy()+" with ID: "+entity.getId(),
                "If you want to see the request, please click here: http://localhost:4200/edit-list/"+entity.getId());
        return RequestDTO.toDto(entity);

    }

    @Override
    public List<RequestDTO> getRequestByUser() {

            if(hasRole("ROLE_ADMIN")){
                  return RequestDTO.toDtoList(requestEntityRepository.findAll());
            }

            else {
                   UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                   String username = userDetails.getEmail();
                   return RequestDTO.toDtoList(requestEntityRepository.findAllByCreatedBy(username));
            }
    }



    @Override
    public void deleteRequest(RequestDTO requestDTO) {
        resourceRepository.deleteAll(resourceRepository.findAllByRequestEntity(RequestDTO.toEntity(requestDTO)));
        requestEntityRepository.delete(RequestDTO.toEntity(requestDTO));
    }


    @Override
    public RequestDTO updateRequest(RequestDTO requestDTO) {
        RequestEntity entity = RequestDTO.toEntity(requestDTO);

        List<ResourceEntity> resourceEntities = new ArrayList<>();
        requestDTO.getResourceDTOS().forEach(resourceDTO -> {
            ResourceEntity resourceEntity = ResourceDTO.toEntity(resourceDTO);
            resourceEntity.setSkillEntities(SkillDTO.toEntityList(resourceDTO.getSkillDTOS()));
            resourceEntity.setRequestEntity(entity);
            resourceEntities.add(resourceEntity);
        });
        entity.setResourceEntities(resourceRepository.saveAll(resourceEntities));
        requestEntityRepository.save(entity);
        return RequestDTO.toDto(entity);
    }

    @Override
    public List<RequestDTO> filterRequest(RequestFilterParam requestFilterParam) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RequestEntity> criteriaQuery = criteriaBuilder.createQuery(RequestEntity.class);
        Root<RequestEntity> request = criteriaQuery.from(RequestEntity.class);
        Join<RequestEntity, ResourceEntity> requestResourceJoin = request.join("resourceEntities");
        Join<ResourceEntity, SkillEntity> resourceSkillJoin = requestResourceJoin.join("skillEntities");
        List<Predicate> predicates = new ArrayList<>();

        if (requestFilterParam.getAreaOfInterest() != null)
            predicates.add(criteriaBuilder.equal(request.get("areaOfInterest"),
                    AreaOfInterest.valueOf(requestFilterParam.getAreaOfInterest())));

        if (requestFilterParam.getStartDate() != null)
            predicates.add(criteriaBuilder.equal(request.get("startDate"),
                    requestFilterParam.getStartDate()));

        if (requestFilterParam.getEndDate() != null)
            predicates.add(criteriaBuilder.equal(request.get("endDate"),
                    requestFilterParam.getEndDate()));

        if (requestFilterParam.getStatus() != null)
            predicates.add(criteriaBuilder.equal(request.get("status"),
                    Status.valueOf(requestFilterParam.getStatus())));

        if (requestFilterParam.getCreatedBy() != null)
            predicates.add(criteriaBuilder.equal(request.get("createdBy"),
                    requestFilterParam.getCreatedBy()));

        if (requestFilterParam.getResourceFilterParam()!=null){

            if (requestFilterParam.getResourceFilterParam().getSeniority()!=null)
                predicates.add(criteriaBuilder.equal(requestResourceJoin.get("seniority"),
                        requestFilterParam.getResourceFilterParam().getSeniority()));

            if (requestFilterParam.getResourceFilterParam().getSkillFilterParam()!=null)
                predicates.add(criteriaBuilder.equal(resourceSkillJoin.get("skill"),
                        requestFilterParam.getResourceFilterParam().getSkillFilterParam().getSkill()));}

        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        TypedQuery<RequestEntity> query = entityManager.createQuery(criteriaQuery);
        return RequestDTO.toDtoList(query.getResultList());
    }

    String[] adminEmails(){

        List<User> admins = userRepository.findAllByRoles_Id(2);
        List<String> adminEmails = new ArrayList<>();


        for (User user:admins) {
            adminEmails.add(user.getEmail());
        }

        String[] stringArray = new String[adminEmails.size()];
        for (int i = 0; i < adminEmails.size(); i++) {
            stringArray[i] = adminEmails.get(i);
        }
        return stringArray;
    }


    private boolean hasRole(String role) {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean hasRole = false;
        for (GrantedAuthority authority : authorities) {
            hasRole = authority.getAuthority().equals(role);
            if (hasRole) {
                break;
            }
        }
        return hasRole;
    }


}

