package com.mindhub.user_service.service.implementations;

import com.mindhub.user_service.dtos.NewUserRequestDTO;
import com.mindhub.user_service.dtos.PatchUserRequestDTO;
import com.mindhub.user_service.dtos.UserDTO;
import com.mindhub.user_service.exceptions.InvalidUserException;
import com.mindhub.user_service.exceptions.UserNotFoundException;
import com.mindhub.user_service.models.RoleType;
import com.mindhub.user_service.models.User;
import com.mindhub.user_service.repositories.UserRepository;
import com.mindhub.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO getUserByIdRequest(Long id) throws UserNotFoundException {
        return new UserDTO(getUserById(id));
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserDTO getUserByEmailRequest(String email) throws UserNotFoundException {
        return new UserDTO(getUserByEmail(email));
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserDTO> getAllUsersRequest() {
        return getAllUsers().stream().map(UserDTO::new).toList();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO createNewUserRequest(NewUserRequestDTO userRequestDTO) {
        return new UserDTO(createNewUser(userRequestDTO));
    }

    @Override
    public User createNewUser(NewUserRequestDTO userRequestDTO) {
        return userRepository.save(new User(
                userRequestDTO.email(),
                userRequestDTO.password(),
                userRequestDTO.username(),
                userRequestDTO.role() != null ? userRequestDTO.role() : RoleType.USER));
    }

    @Override
    public UserDTO patchUserRequest(Long id, PatchUserRequestDTO patchUser) throws UserNotFoundException, InvalidUserException {
        return new UserDTO(patchUser(id, patchUser));
    }

    @Override
    public User patchUser(Long id, PatchUserRequestDTO patchUser) throws UserNotFoundException, InvalidUserException {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        makePatchUpdated(user, patchUser);
        return user;
    }

    private void makePatchUpdated(User user, PatchUserRequestDTO patchUser) throws InvalidUserException {
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
