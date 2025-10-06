package com.bingofacil.bingofacil.services.user;

import com.bingofacil.bingofacil.dtos.LoginResponse;
import com.bingofacil.bingofacil.dtos.user.LoginUserDTO;
import com.bingofacil.bingofacil.dtos.user.UserDTO;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import com.bingofacil.bingofacil.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public void saveUser(User user){
        this.userRepository.save(user);
    }

    public User createUser(UserDTO userDTO){

        if (userRepository.findByEmail(userDTO.email()).isPresent()){
            throw new IllegalArgumentException("Email já cadastrado");
        }

        if (userRepository.findByUsername(userDTO.username()).isPresent()){
            throw new IllegalArgumentException("Nome usuário já cadastrado");
        }

        if (userRepository.findByTelephone(userDTO.telephone()).isPresent()){
            throw new IllegalArgumentException("Número de telefone já cadastrado");
        }

        User newUser = new User();
        newUser.setUsername(userDTO.username());
        newUser.setEmail(userDTO.email());
        newUser.setTelephone(userDTO.telephone());
        newUser.setPassword(passwordEncoder.encode(userDTO.password())); // encode da senha
        newUser.setAddress(userDTO.address());

        return this.userRepository.save(newUser);
    }

    public User editUser(Long id, UserDTO dto){
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setTelephone(dto.telephone());
        user.setAddress(dto.address());

        return this.userRepository.save(user);
    }

    public void removeUser(Long id){
        this.userRepository.deleteById(id);
    }

    public List<User> findAllUsers(){
        return this.userRepository.findAll();
    }

    public User findUserById(Long id){
        return this.userRepository.findById(id).orElse(null);
    }

    public LoginResponse authenticate(LoginUserDTO input){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.password()
                )
        );

        String jwtToken = jwtService.generateToken(authentication);
        long expiresIn = JwtService.EXPIRY_SECONDS;

        return new LoginResponse(jwtToken, expiresIn);
    }
}
