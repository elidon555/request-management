package com.example.project.services.request.filterRequest;

import com.example.project.mapper.RequestDTO;
import com.example.project.mapper.RequestFilterParam;


import java.util.List;

public interface FilterRequest {
    List<RequestDTO> filterRequest(RequestFilterParam requestFilterParam);
}
