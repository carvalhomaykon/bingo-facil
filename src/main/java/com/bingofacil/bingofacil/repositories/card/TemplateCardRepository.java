package com.bingofacil.bingofacil.repositories.card;
import com.bingofacil.bingofacil.model.card.Card;
import com.bingofacil.bingofacil.model.card.TemplateCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateCardRepository extends JpaRepository<TemplateCard, Long> {
    TemplateCard findByProjectId(Long projectId);
}
