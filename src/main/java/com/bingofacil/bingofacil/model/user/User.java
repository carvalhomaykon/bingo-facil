package com.bingofacil.bingofacil.model.user;

import com.bingofacil.bingofacil.dtos.user.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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
    private String username;

    @Column(unique = true)
    private String email;
    private String password;
    private String telephone;

    @OneToOne
    @JoinColumn(name="address_id")
    private Address address;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public User(UserDTO data){
        this.username = data.username();
        this.email = data.email();
        this.password = data.password();
        this.telephone = data.telephone();
        this.address = data.address();
    }
}
