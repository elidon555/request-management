package com.example.project.mapper;

import com.example.project.database.entity.RequestEntity;
import com.example.project.database.enums.Status;
import com.example.project.database.enums.AreaOfInterest;
import com.example.project.database.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private Long id;

    private Date startDate;

    private Date endDate;

    private String description;

    private String notes;

    private String createdBy;

    private String updatedBy;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private AreaOfInterest areaOfInterest;

    private User user;

    private List<ResourceDTO> resourceDTOS;

    public static RequestEntity toEntity(RequestDTO requestDTO){
        return RequestEntity.builder()
                .areaOfInterest(requestDTO.areaOfInterest)
                .description(requestDTO.description)
                .endDate(requestDTO.endDate)
                .id(requestDTO.id)
                .notes(requestDTO.notes)
                .startDate(requestDTO.startDate)
                .status(requestDTO.status)
                .createdBy(requestDTO.createdBy)
                .updatedBy(requestDTO.updatedBy)
                .build();
    }

    public static RequestDTO toDto(RequestEntity requestEntity){
        return RequestDTO.builder()
                .areaOfInterest(requestEntity.getAreaOfInterest())
                .description(requestEntity.getDescription())
                .endDate(requestEntity.getEndDate())
                .id(requestEntity.getId())
                .notes(requestEntity.getNotes())
                .startDate(requestEntity.getStartDate())
                .status(requestEntity.getStatus())
                .createdBy(requestEntity.getCreatedBy())
                .updatedBy(requestEntity.getUpdatedBy())
                .resourceDTOS(ResourceDTO.toDtoList(requestEntity.getResourceEntities()))
                .build();

    }

    public static List<RequestEntity> toEntityList(List<RequestDTO> requestDTOS){
        return requestDTOS.stream().map(RequestDTO::toEntity).collect(Collectors.toList());
    }

    public static List<RequestDTO> toDtoList(List<RequestEntity> requestEntities){
        return requestEntities.stream().map(RequestDTO::toDto).collect(Collectors.toList());
    }
}
