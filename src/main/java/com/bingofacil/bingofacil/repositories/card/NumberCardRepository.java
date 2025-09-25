package com.bingofacil.bingofacil.repositories.card;

import com.bingofacil.bingofacil.model.card.NumberBingo;
import com.bingofacil.bingofacil.model.card.NumberCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NumberCardRepository extends JpaRepository<NumberCard, Long> {
    List<NumberCard> findAllByCardId(Long cardId);

    @Query("SELECT nc FROM NumberCard nc " +
            "JOIN nc.card c " +
            "JOIN c.project p " +
            "WHERE p.id = :projectId")
    List<NumberCard> findAllByIdProject(@Param("projectId") Long projectId);

    @Query("SELECT nc FROM NumberCard nc " +
            "JOIN nc.card c " +
            "JOIN c.user u " + // Junta Card com User
            "WHERE u.id = :userId") // Filtra pelo ID do Usuário
    List<NumberCard> findAllByIdUser(@Param("userId") Long userId);
}
