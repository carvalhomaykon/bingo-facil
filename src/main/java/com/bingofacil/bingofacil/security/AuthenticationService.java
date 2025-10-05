package com.bingofacil.bingofacil.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;

    public String authenticate (Authentication authentication){
        return jwtService.generateToken(authentication);
    }

}
