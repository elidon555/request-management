package com.example.project.services;

import com.example.project.database.entity.RequestEntity;
import com.example.project.database.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;


    public List<RequestEntity> listAll() {
        return requestRepository.findAll();
    }

}
