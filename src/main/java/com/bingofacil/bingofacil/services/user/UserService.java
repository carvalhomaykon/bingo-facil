package com.bingofacil.bingofacil.services.user;

import com.bingofacil.bingofacil.dtos.project.UserDTO;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user){
        this.userRepository.save(user);
    }

    public User createUser(UserDTO user){
        User newUser = new User(user);
        this.saveUser(newUser);
        return newUser;
    }

    public User editUser(Long id, UserDTO dto){
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setTelephone(dto.telephone());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
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

}
