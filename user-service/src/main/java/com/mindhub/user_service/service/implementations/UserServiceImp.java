package com.mindhub.user_service.service.implementations;

import com.mindhub.user_service.dtos.NewUserRequestDTO;
import com.mindhub.user_service.dtos.PatchUserRequestDTO;
import com.mindhub.user_service.dtos.UserDTO;
import com.mindhub.user_service.exceptions.InvalidUserException;
import com.mindhub.user_service.exceptions.UserNotFoundException;
import com.mindhub.user_service.models.RoleType;
import com.mindhub.user_service.models.UserEntity;
import com.mindhub.user_service.repositories.UserRepository;
import com.mindhub.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserByIdRequest(Long id) throws UserNotFoundException {
        return new UserDTO(getUserById(id));
    }

    @Override
    public UserEntity getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDTO getUserByEmailRequest(String email) throws UserNotFoundException {
        return new UserDTO(getUserByEmail(email));
    }

    @Override
    public UserEntity getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void deleteUserByIdRequest(Long id) throws UserNotFoundException {
        deleteUserById(id);
    }

    @Override
    public void deleteUserById(Long id) throws UserNotFoundException {
        if (!userRepository.existsById(id))
            throw new UserNotFoundException();
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDTO> getAllUsersRequest() {
        return getAllUsers().stream().map(UserDTO::new).toList();
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO createNewUserRequest(NewUserRequestDTO userRequestDTO) {
        return new UserDTO(createNewUser(userRequestDTO));
    }

    @Override
    public UserEntity createNewUser(NewUserRequestDTO userRequestDTO) {
        return userRepository.save(new UserEntity(
                userRequestDTO.email(),
                passwordEncoder.encode(userRequestDTO.password()),
                userRequestDTO.username(),
                userRequestDTO.role() != null ? userRequestDTO.role() : RoleType.USER));
    }



    @Override
    public UserDTO patchUserRequest(Long id, PatchUserRequestDTO patchUser) throws UserNotFoundException, InvalidUserException {
        return new UserDTO(patchUser(id, patchUser));
    }

    @Override
    public UserEntity patchUser(Long id, PatchUserRequestDTO patchUser) throws UserNotFoundException, InvalidUserException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        makePatchUpdated(user, patchUser);
        return user;
    }

    private void makePatchUpdated(UserEntity user, PatchUserRequestDTO patchUser) throws InvalidUserException {
        if (patchUser == null || patchUser.email() == null && patchUser.role() == null && patchUser.username() == null && patchUser.password() == null)
            throw new InvalidUserException();

        if (patchUser.username() != null)
            user.setUsername(patchUser.username());
        if (patchUser.email() != null)
            user.setEmail(patchUser.email());
        if (patchUser.password() != null)
            user.setPassword(patchUser.password());
        if (patchUser.role() != null)
            user.setRole(patchUser.role());
    }
}
