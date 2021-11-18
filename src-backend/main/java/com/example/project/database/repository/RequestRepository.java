package com.example.project.database.repository;

import com.example.project.database.entity.RequestEntity;
import com.example.project.database.entity.User;

import com.example.project.mapper.RequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    List<RequestEntity> findAllByUser (User user);
    List<RequestEntity> findAllByCreatedBy (String requestDTO);
    List<RequestEntity> findAllById(Long id);


//    @Query(value=" SELECT DATE_FORMAT(`start_date`, '%Y-%m') as `date` COUNT(*) as `count` FROM `request_entity` GROUP BY MONTH(`start_date`) ORDER BY `count` DESC",nativeQuery=true)
//    List<RequestEntity> returnAllDates();

}
