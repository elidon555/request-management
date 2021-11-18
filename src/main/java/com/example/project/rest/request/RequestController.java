package com.example.project.rest.request;


import com.example.project.database.enums.Status;
import com.example.project.database.repository.RequestRepository;
import com.example.project.mapper.RequestDTO;
import com.example.project.mapper.RequestFilterParam;
import com.example.project.services.UserDetailsImpl;
import com.example.project.services.request.RequestUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class RequestController {
    private final RequestUseCase requestUseCase;
    private final RequestRepository rp;
    private final RequestRepository requestEntityRepository;

    @Bean
    public AuthenticationTrustResolver trustResolver() {
        return new AuthenticationTrustResolver() {

            @Override
            public boolean isRememberMe(final Authentication authentication) {
                return false;
            }

            @Override
            public boolean isAnonymous(final Authentication authentication) {
                return false;
            }
        };
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/request")
    public RequestDTO saveRequest(@RequestBody final RequestDTO requestDTO) {


        return requestUseCase.createRequest(requestDTO);
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/requests")
    public List < RequestDTO > getRequestByUser() {

            return requestUseCase.getRequestByUser();
    }




    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/requests/{id}")
    public List < RequestDTO > getRequestById(@PathVariable @NotNull @DecimalMin("0") Set<Long> id) {
        return RequestDTO.toDtoList(requestEntityRepository.findAllById(id));
    }





    @ResponseBody
    @GetMapping("/user")
    public String userInfo(Authentication authentication) {

        String userName = authentication.getName();

        return userName;
    }


    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(@RequestBody Principal principal) {
        return principal.getName();
    }



    @DeleteMapping("/delete-request")
    public void deleteRequest(@RequestBody RequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (hasRole("Admin") && requestDTO.getStatus().equals(Status.IN_CHARGE) && !requestDTO.getUpdatedBy().equals(authentication.getName())) {
            return;
        }

        requestUseCase.deleteRequest(requestDTO);
    }

    @PostMapping("/update-request")

    public RequestDTO updateRequest(@RequestBody final RequestDTO requestDTO) {


//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (requestDTO.getStatus().equals(Status.REJECTED)) {
//            return null; //error
//        }
//        //if role requester, status in progress
//        if (hasRole("Requester") && requestDTO.getStatus().equals(Status.IN_PROGRESS) && !requestDTO.getCreatedBy().equals(authentication.getName())) {
//            return null; //error
//        }
//        //if role admin,status in charge, updated by not the admin who updated it
//        if (hasRole("Admin") && requestDTO.getStatus().equals(Status.IN_CHARGE) && !requestDTO.getUpdatedBy().equals(authentication.getName())) {
//            return null;
//        }
//        if (hasRole("Requester") && requestDTO.getStatus().equals(Status.IN_CHARGE)) {
//            return null;
//        }


        return requestUseCase.updateRequest(requestDTO);

    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/request/filter")
    public List < RequestDTO > filterRequest(@RequestBody RequestFilterParam requestFilterParam) {
        return requestUseCase.filterRequest(requestFilterParam);
    }



    private boolean hasRole(String role) {
        Collection < GrantedAuthority > authorities = (Collection < GrantedAuthority > )
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean hasRole = false;
        for (GrantedAuthority authority: authorities) {
            hasRole = authority.getAuthority().equals(role);
            if (hasRole) {
                break;
            }
        }
        return hasRole;
    }


}
