package com.example.project.database.repository;

import com.example.project.database.entity.RequestEntity;
import com.example.project.database.entity.ResourceEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {
    List<ResourceEntity> findAllByRequestEntity(RequestEntity requestEntity);
}
