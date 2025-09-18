package com.bingofacil.bingofacil.model.user;

import com.bingofacil.bingofacil.dtos.UserDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String telephone;
    private String firstName;
    private String lastName;

    @OneToOne
    @JoinColumn(name="address_id")
    private Address address;

    public User(UserDTO data){
        this.name = data.name();
        this.email = data.email();
        this.password = data.password();
        this.telephone = data.telephone();
        this.firstName = data.firstName();
        this.lastName = data.lastName();
        this.address = data.address();
    }
}
