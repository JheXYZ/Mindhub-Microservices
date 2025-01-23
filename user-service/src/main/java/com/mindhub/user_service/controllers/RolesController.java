package com.mindhub.user_service.controllers;

import com.mindhub.user_service.models.RoleType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RolesController {

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<RoleType> getAllRoles(){
        return List.of(RoleType.values());
    }
}
