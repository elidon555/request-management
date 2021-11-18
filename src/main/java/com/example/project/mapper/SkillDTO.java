package com.example.project.mapper;

import com.example.project.database.entity.SkillEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillDTO {

    private String skill;

    public static SkillEntity toEntity(SkillDTO skillDTO){
        return SkillEntity.builder()
                .skill(skillDTO.skill)
                .build();
    }

    public static SkillDTO toDto(SkillEntity skillEntity){
        return SkillDTO.builder()
                .skill(skillEntity.getSkill())
                .build();
    }

    public static List<SkillEntity> toEntityList(List<SkillDTO> skillDTOS){
        return skillDTOS.stream().map(SkillDTO::toEntity).collect(Collectors.toList());
    }

    public static List<SkillDTO> toDtoList (List<SkillEntity> skillEntities){
        return skillEntities.stream().map(SkillDTO::toDto).collect(Collectors.toList());
    }

}
