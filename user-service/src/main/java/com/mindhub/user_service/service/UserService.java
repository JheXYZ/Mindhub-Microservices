package com.mindhub.user_service.service;

import com.mindhub.user_service.dtos.NewUserRequestDTO;
import com.mindhub.user_service.dtos.PatchUserRequestDTO;
import com.mindhub.user_service.dtos.UserDTO;
import com.mindhub.user_service.exceptions.InvalidUserException;
import com.mindhub.user_service.exceptions.UserNotFoundException;
import com.mindhub.user_service.models.UserEntity;

import java.util.List;

public interface UserService {

    UserDTO getUserByIdRequest(Long id) throws UserNotFoundException;

    UserEntity getUserById(Long id) throws UserNotFoundException;

    List<UserDTO> getAllUsersRequest();

    List<UserEntity> getAllUsers();

    UserDTO createNewUserRequest(NewUserRequestDTO userRequestDTO);

    UserEntity createNewUser(NewUserRequestDTO userRequestDTO);

    UserDTO patchUserRequest(Long id, PatchUserRequestDTO patchUser) throws UserNotFoundException, InvalidUserException;

    UserEntity patchUser(Long id, PatchUserRequestDTO patchUser) throws UserNotFoundException, InvalidUserException;

    UserDTO getUserByEmailRequest(String email) throws UserNotFoundException;

    UserEntity getUserByEmail(String email) throws UserNotFoundException;

    void deleteUserByIdRequest(Long id) throws UserNotFoundException;

    void deleteUserById(Long id) throws UserNotFoundException;
}
