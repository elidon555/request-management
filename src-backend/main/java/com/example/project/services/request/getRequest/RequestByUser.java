package com.example.project.services.request.getRequest;

import com.example.project.database.entity.User;
import com.example.project.mapper.RequestDTO;


import java.util.List;

public interface RequestByUser {
    List<RequestDTO> getRequestByUser();
}
