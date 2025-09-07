package com.bingofacil.bingofacil.model.card;

import com.bingofacil.bingofacil.model.project.Project;
import com.bingofacil.bingofacil.model.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name="cards")
@Table(name="cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Project project;
    private User user;
    private StatusCard status;

}
