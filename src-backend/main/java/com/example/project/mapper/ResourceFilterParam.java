package com.example.project.mapper;

import com.example.project.database.enums.Seniority;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class ResourceFilterParam {

    private String note;

    @Enumerated(EnumType.STRING)
    private Seniority seniority;

    private SkillFilterParam skillFilterParam;
}
