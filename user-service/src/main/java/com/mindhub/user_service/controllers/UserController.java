package com.mindhub.user_service.controllers;

import com.mindhub.user_service.dtos.NewUserRequestDTO;
import com.mindhub.user_service.dtos.PatchUserRequestDTO;
import com.mindhub.user_service.dtos.UserDTO;
import com.mindhub.user_service.exceptions.InvalidUserException;
import com.mindhub.user_service.exceptions.UserNotFoundException;
import com.mindhub.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsersRequest();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserById(@PathVariable Long id) throws UserNotFoundException {
        return userService.getUserByIdRequest(id);
    }

    @GetMapping(params = "email")
    public UserDTO getUserByEmail(@RequestParam String email) throws UserNotFoundException {
        return userService.getUserByEmailRequest(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createNewUser(@Valid @RequestBody NewUserRequestDTO userRequestDTO){
        return userService.createNewUserRequest(userRequestDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO patchUser(@PathVariable Long id, @Valid @RequestBody PatchUserRequestDTO patchUser) throws UserNotFoundException, InvalidUserException {
        return userService.patchUserRequest(id, patchUser);
    }


}
