package com.bingofacil.bingofacil.controllers;

import com.bingofacil.bingofacil.dtos.LoginResponse;
import com.bingofacil.bingofacil.dtos.user.LoginUserDTO;
import com.bingofacil.bingofacil.dtos.user.UserDTO;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.security.JwtService;
import com.bingofacil.bingofacil.services.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO user) {
        User newUser = this.userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> users = this.userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id){
        User user = this.userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Long id){
        this.userService.removeUser(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id, @RequestBody UserDTO user){
        User editUser = this.userService.editUser(id, user);
        return new ResponseEntity<>(editUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate (@RequestBody LoginUserDTO loginUserDTO){
        LoginResponse loginResponse = userService.authenticate(loginUserDTO);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
