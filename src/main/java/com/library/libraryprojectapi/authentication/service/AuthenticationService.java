package com.library.libraryprojectapi.authentication.service;

import com.library.libraryprojectapi.authentication.models.LoginCredentials;
import com.library.libraryprojectapi.data.entities.User;
import com.library.libraryprojectapi.authentication.models.RegisterUserDTO;
import com.library.libraryprojectapi.data.models.APIResponse;
import com.library.libraryprojectapi.data.models.RoleName;
import com.library.libraryprojectapi.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
            JwtService jwtService, ModelMapper modelMapper
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
    }

    private APIResponse<?> validateUser(RegisterUserDTO userDTO){
        if(userRepository.existsByEmail(userDTO.getEmail())){
            return APIResponse.error("User with this email already exists");
        } else if(userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())){
            return APIResponse.error("User with this phone number already exists");
        }
        else if(userDTO.getRole() != RoleName.CLIENT){
            return APIResponse.error("You role is invalid");
        }
        return APIResponse.success(userDTO, "User is valid");
    }

    public APIResponse<?> registerUser(RegisterUserDTO userDTO){
        // Validate User
        APIResponse<?> validateMessage = validateUser(userDTO);
        if(!validateMessage.isSuccess()){
            return validateMessage;
        }

        // Save User
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return APIResponse.success(user);
    }

    public APIResponse<?> loginUser(@Valid LoginCredentials loginCredentials, HttpServletResponse response) {
        User user = userRepository.findByEmail(loginCredentials.getEmail());
        if(user == null){
            return APIResponse.error("User doesn't exist");
        }
        return authenticate(user, loginCredentials.getEmail(), loginCredentials.getPassword(), response);
    }

    private APIResponse<String> authenticate(User user, String email, String password, HttpServletResponse response){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            if(!authentication.isAuthenticated()){
                return APIResponse.error("Invalid password");
            }
            String token = jwtService.generateToken(user);
            response.setHeader("Authorization", token);
            return APIResponse.success("Successfully authenticated");
        } catch (AuthenticationException e) {
            return APIResponse.error(e.getMessage());
        }
    }
}
