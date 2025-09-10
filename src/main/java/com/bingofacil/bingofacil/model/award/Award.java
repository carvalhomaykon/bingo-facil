package com.bingofacil.bingofacil.model.award;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="awards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer amount;

    @Enumerated(EnumType.STRING)
    private StyleAward styleAward;

}
