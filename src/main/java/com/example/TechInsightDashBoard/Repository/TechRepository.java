package com.example.TechInsightDashBoard.Repository;

import com.example.TechInsightDashBoard.Entity.TechEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechRepository extends JpaRepository<TechEntity, Long> {
    List<TechEntity> findAllByUserIdOrderByTechNameAsc(Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}
