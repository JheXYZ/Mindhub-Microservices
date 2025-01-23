package com.mindhub.user_service.models;

import com.mindhub.user_service.validations.NoWhitespaces;
import com.mindhub.user_service.validations.UniqueEmail;
import com.mindhub.user_service.validations.ValidEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;


@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NoWhitespaces
    @Column(nullable = false)
    private String username;

    @NotNull
    @ValidEmail
    @UniqueEmail
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    @NoWhitespaces
    private String password;

    private RoleType role = RoleType.USER;

    public User() {
    }

    public User(String email, String password, String username, RoleType role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
