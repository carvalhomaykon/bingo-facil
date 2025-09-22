package com.bingofacil.bingofacil.model.card;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.DependsOn;

@Entity
@Table(name="numbers_card")
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

    // Posição na matriz (linha/coluna da cartela)
    @Column(name="row_index")
    private int row;

    @Column(name="col_index")
    private int column;

    // Se já foi sorteado/marcado
    @Column(nullable = false)
    private boolean marked = false;

}
