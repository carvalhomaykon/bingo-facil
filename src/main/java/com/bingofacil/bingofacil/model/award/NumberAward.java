package com.bingofacil.bingofacil.model.award;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="NumberAward")
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
    private Integer number;
    private String letter;
    private Award award;

}
