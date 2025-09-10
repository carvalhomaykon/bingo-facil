package com.bingofacil.bingofacil.model.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameEasy;
    private String cep;
    private String address;
    private String number;
    private String neighborhood;
    private String municipality;
    private String state;

}
