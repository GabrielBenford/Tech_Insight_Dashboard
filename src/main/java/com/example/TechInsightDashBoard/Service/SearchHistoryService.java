package com.example.TechInsightDashBoard.Service;

import com.example.TechInsightDashBoard.DTO.TechDTO.SearchHistoryResponseDTO;
import com.example.TechInsightDashBoard.Entity.SearchHistoryEntity;
import com.example.TechInsightDashBoard.Entity.TechEntity;
import com.example.TechInsightDashBoard.Repository.SearchHistoryRepository;
import com.example.TechInsightDashBoard.Repository.TechRepository;
import com.example.TechInsightDashBoard.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final TechRepository techRepository;

    public void recordIfLinked(Long userId, Long technologyId, String provider, String query, Integer resultCount) {
        if (userId == null && technologyId == null) {
            return;
        }
        if (userId == null || technologyId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "userId and technologyId must be provided together");
        }

        TechEntity technology = techRepository.findById(technologyId)
                .filter(tech -> tech.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Technology", technologyId));

        SearchHistoryEntity history = SearchHistoryEntity.builder()
                .technology(technology)
                .provider(provider)
                .query(query)
                .resultCount(resultCount)
                .searchedAt(Instant.now())
                .build();

        searchHistoryRepository.save(history);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<SearchHistoryResponseDTO> findByUserAndTechnology(Long userId, Long technologyId) {
        if (!techRepository.existsByIdAndUserId(technologyId, userId)) {
            throw new ResourceNotFoundException("Technology not found for the given user", technologyId);
        }
        return searchHistoryRepository
                .findAllByTechnologyIdAndTechnologyUserIdOrderBySearchedAtDesc(technologyId, userId)
                .stream()
                .map(history -> new SearchHistoryResponseDTO(
                        history.getId(),
                        history.getTechnology().getId(),
                        history.getTechnology().getTechName(),
                        history.getProvider(),
                        history.getQuery(),
                        history.getResultCount(),
                        history.getSearchedAt()))
                .toList();
    }
}
