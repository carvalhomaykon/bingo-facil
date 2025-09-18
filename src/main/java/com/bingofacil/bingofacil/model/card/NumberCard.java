package com.bingofacil.bingofacil.model.card;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.DependsOn;

@Entity
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

    @ManyToOne
    @JoinColumn(name="card_id")
    private Card card;


    @ManyToOne
    @JoinColumn(name = "number_id")
    private NumberBingo number;

}
