package com.bingofacil.bingofacil.model.award;

import com.bingofacil.bingofacil.model.card.NumberBingo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="NumberAward")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class NumberAward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="award_id")
    private Award award;

    @ManyToOne
    @JoinColumn(name = "number_id")
    private NumberBingo number;

}
