package com.bingofacil.bingofacil.repositories.card;

import com.bingofacil.bingofacil.model.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByProjectId(Long projectId);
    List<Card> findAllByUserId(Long userId);
}
