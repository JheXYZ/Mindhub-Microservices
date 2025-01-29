package com.mindhub.user_service.dtos;

import com.mindhub.user_service.models.RoleType;
import com.mindhub.user_service.models.User;

public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private RoleType role;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, RoleType role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }
}
