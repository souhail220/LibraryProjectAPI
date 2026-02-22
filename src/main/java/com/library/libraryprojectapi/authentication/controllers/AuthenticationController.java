package com.library.libraryprojectapi.authentication.controllers;

import com.library.libraryprojectapi.authentication.models.LoginCredentials;
import com.library.libraryprojectapi.authentication.models.RegisterUserDTO;
import com.library.libraryprojectapi.authentication.service.AuthenticationService;
import com.library.libraryprojectapi.data.models.APIResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponse<?>> registerUser(@Valid @RequestBody RegisterUserDTO userDTO){
        APIResponse<?> apiResponse = authenticationService.registerUser(userDTO);
        if(apiResponse.isSuccess()){
            return ResponseEntity.ok(apiResponse);
        }
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @PostMapping("/loging")
    public ResponseEntity<APIResponse<?>> login(
            @Valid @RequestBody LoginCredentials loginCredentials, HttpServletResponse response
    ){
        APIResponse<?> apiResponse = authenticationService.loginUser(loginCredentials, response);
        return ResponseEntity.ok(apiResponse);
    }
}
