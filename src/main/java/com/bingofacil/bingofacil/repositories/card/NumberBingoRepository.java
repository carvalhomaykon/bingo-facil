package com.bingofacil.bingofacil.repositories.card;

import com.bingofacil.bingofacil.model.card.NumberBingo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NumberBingoRepository extends JpaRepository<NumberBingo, Long> {
    Optional<NumberBingo> findByValueAndLetter(Integer value, String letter);
}
