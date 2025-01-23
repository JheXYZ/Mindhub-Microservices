package com.mindhub.user_service.service;

import com.mindhub.user_service.dtos.NewUserRequestDTO;
import com.mindhub.user_service.dtos.PatchUserRequestDTO;
import com.mindhub.user_service.dtos.UserDTO;
import com.mindhub.user_service.exceptions.InvalidUserException;
import com.mindhub.user_service.exceptions.UserNotFoundException;
import com.mindhub.user_service.models.User;

import java.util.List;

public interface UserService {

    UserDTO getUserByIdRequest(Long id) throws UserNotFoundException;

    User getUserById(Long id) throws UserNotFoundException;

    List<UserDTO> getAllUsersRequest();

    List<User> getAllUsers();

    UserDTO createNewUserRequest(NewUserRequestDTO userRequestDTO);

    User createNewUser(NewUserRequestDTO userRequestDTO);

    UserDTO patchUserRequest(Long id, PatchUserRequestDTO patchUser) throws UserNotFoundException, InvalidUserException;

    User patchUser(Long id, PatchUserRequestDTO patchUser) throws UserNotFoundException, InvalidUserException;
}
