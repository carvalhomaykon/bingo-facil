package com.bingofacil.bingofacil.repositories.award;

import com.bingofacil.bingofacil.model.award.Award;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardRepository extends JpaRepository<Award, Long> {
    List<Award> findByProjectId(Long projectId);
}
