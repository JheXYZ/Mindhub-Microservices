package com.mindhub.user_service.controllers;

import com.mindhub.user_service.configs.JwtUtils;
import com.mindhub.user_service.dtos.LoginUserDTO;
import com.mindhub.user_service.dtos.NewUserRequestDTO;
import com.mindhub.user_service.dtos.RegisterUserDTO;
import com.mindhub.user_service.dtos.UserDTO;
import com.mindhub.user_service.exceptions.UserNotFoundException;
import com.mindhub.user_service.models.RoleType;
import com.mindhub.user_service.models.UserEntity;
import com.mindhub.user_service.repositories.UserRepository;
import com.mindhub.user_service.service.UserService;
import com.mindhub.user_service.validations.ValidEmail;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.mindhub.user_service.configs.RabbitMQConfig.USER_CREATE_USER_KEY;
import static com.mindhub.user_service.configs.RabbitMQConfig.USER_EXCHANGE;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginUserDTO loginRequest, boolean isCreated) throws UserNotFoundException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserEntity user = userService.getUserByEmail(authentication.getName());
        String jwt = jwtUtils.createToken(authentication.getName(), user.getId(), user.getRole().toString());
        return isCreated ? ResponseEntity.status(HttpStatus.CREATED).body(jwt) : ResponseEntity.ok(jwt);
    }

    @PostMapping("/register")
    @Transactional(rollbackOn = AmqpException.class)
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterUserDTO registerUser) throws UserNotFoundException {
        NewUserRequestDTO newUserRequestDTO = new NewUserRequestDTO(registerUser.email(), registerUser.password(), registerUser.username(), RoleType.USER);
        UserEntity newUser = userService.createNewUser(newUserRequestDTO);
        amqpTemplate.convertAndSend(USER_EXCHANGE, USER_CREATE_USER_KEY, new UserDTO(newUser));
        return authenticateUser(new LoginUserDTO(newUser.getEmail(), newUserRequestDTO.password()), true);
    }
}
