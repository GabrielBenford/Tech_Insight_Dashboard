package com.example.TechInsightDashBoard.Repository;

import com.example.TechInsightDashBoard.Entity.SearchHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistoryEntity, Long> {
    List<SearchHistoryEntity> findAllByTechnologyIdAndTechnologyUserIdOrderBySearchedAtDesc(Long technologyId, Long userId);
}
