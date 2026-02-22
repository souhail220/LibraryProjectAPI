package com.library.libraryprojectapi.authentication.service;

import com.library.libraryprojectapi.authentication.models.UserDTO;
import com.library.libraryprojectapi.data.entities.User;
import com.library.libraryprojectapi.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Page<UserDTO> getUsers(int page, int size) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));
        return userPage.map(user -> modelMapper.map(user, UserDTO.class));
    }
}
