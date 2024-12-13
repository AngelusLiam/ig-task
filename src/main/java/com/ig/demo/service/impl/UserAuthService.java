package com.ig.demo.service.impl;

import com.ig.demo.entity.User;
import com.ig.demo.repository.UserRepository;
import com.ig.demo.security.CustomUsernamePasswordAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Component
public class UserAuthService {

    private final UserRepository repository;

    public User getLoggedUser() {
        return repository.findById(Long.valueOf(getUserId())).orElseThrow();
    }

    public String getUsername() {
        return getCustomAuthentication().getUsername();
    }

    public Integer getUserId() {
        return getCustomAuthentication().getId();
    }

    public CustomUsernamePasswordAuthenticationToken getCustomAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUsernamePasswordAuthenticationToken) authentication;
    }

    public User findUser(Integer idUser) {
        return repository.findById(Long.valueOf(idUser)).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not found"));
    }
}
