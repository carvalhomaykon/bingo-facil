package com.bingofacil.bingofacil.repositories.card;

import com.bingofacil.bingofacil.model.card.NumberBingo;
import com.bingofacil.bingofacil.model.card.NumberCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NumberCardRepository extends JpaRepository<NumberCard, Long> {
    List<NumberCard> findAllByCardId(Long cardId);
}
