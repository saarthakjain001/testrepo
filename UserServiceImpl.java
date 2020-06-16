package com.example.userservice.service;

import java.util.Map;

import com.example.userservice.domain.UserDetails;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.exceptions.InvalidUsernamePasswordException;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    UserServiceImpl(UserRepository repository) {
        this.repository = repository;

    }

//TODO ensure that the same user does not already exist
    @Override
    public UserDetails AddUser(UserDetails user) {
        return repository.save(user);
    }

    @Override
    public UserDetails ShowDetails(Map<String, String> detail) {
        UserDetails user = repository.findByUsername(detail.get("username"));
        return user;
    }

//TODO 30-6-2020 Validate whether the entered mobile number is correct before updating it 
    @Override
    public UserDetails UpdateContact(Map<String, String> detail) {
        UserDetails user = repository.findByUsername(detail.get("username"));
        user.setMobileNumber(Long.parseLong(detail.get("contact")));
        repository.save(user);
        return user;

    }


    @Override
    public Boolean validateUser(Map<String, String> detail) {
        UserDetails user = repository.findByUsernameAndPassword(detail.get("username"), detail.get("password"));
        if (user == null) {
            return false;
        }
        return true;
    }

}