package com.bingofacil.bingofacil.services;

import com.bingofacil.bingofacil.dtos.user.UserDTO;
import com.bingofacil.bingofacil.model.user.User;
import com.bingofacil.bingofacil.repositories.user.UserRepository;
import com.bingofacil.bingofacil.services.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("You should successfully register a user when the data is valid.")
    void registerUserWhenSuccess(){
        // ARRANGE
        UserDTO dto = new UserDTO("Maykon", "maykon@gmail.com", "maykon", "91999999999", null);
        User userEsperado = new User(1L, "Maykon", "maykon@gmail.com", "senhaCriptografada", "91999999999", null, null, null);

        // Define o comportamento dos mocks para evitar NullPointerException
        org.mockito.Mockito.when(passwordEncoder.encode(dto.password())).thenReturn("senhaCriptografada");
        org.mockito.Mockito.when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class))).thenReturn(userEsperado);

        // ACT
        User result = userService.createUser(dto);

        // ASSERT
        assertNotNull(result);
        assertEquals("Maykon", result.getUsername());
        assertEquals(1L, result.getId());
    }

}
