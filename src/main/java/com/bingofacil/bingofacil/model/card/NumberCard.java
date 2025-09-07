package com.bingofacil.bingofacil.model.card;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="numbersCard")
@Table(name="NumbersCard")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class NumberCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String letter;
    private Card card;

}
