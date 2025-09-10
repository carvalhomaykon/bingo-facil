package com.bingofacil.bingofacil.model.card;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "numbers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NumberBingo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer value; // 1–75

    private String letter; // B, I, N, G, O

}
