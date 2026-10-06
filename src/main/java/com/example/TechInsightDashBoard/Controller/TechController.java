package com.example.TechInsightDashBoard.Controller;

import com.example.TechInsightDashBoard.DTO.TechDTO.TechRequestDTO;
import com.example.TechInsightDashBoard.DTO.TechDTO.TechResponseDTO;
import com.example.TechInsightDashBoard.DTO.TechDTO.SearchHistoryResponseDTO;
import com.example.TechInsightDashBoard.Service.SearchHistoryService;
import com.example.TechInsightDashBoard.Service.TechService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Dashboard/user/{userId}/technology")
@RequiredArgsConstructor
public class TechController {

    private final TechService techService;
    private final SearchHistoryService searchHistoryService;

    @GetMapping("/{technologyId}/history")
    public List<SearchHistoryResponseDTO> findSearchHistory(
            @PathVariable Long userId,
            @PathVariable Long technologyId) {
        return searchHistoryService.findByUserAndTechnology(userId, technologyId);
    }

    @PostMapping
    public ResponseEntity<TechResponseDTO> create(
            @PathVariable Long userId,
            @Valid @RequestBody TechRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(techService.create(userId, request));
    }

    @GetMapping
    public List<TechResponseDTO> findAll(@PathVariable Long userId) {
        return techService.findAllByUser(userId);
    }

    @GetMapping("/{id}")
    public TechResponseDTO findById(@PathVariable Long userId, @PathVariable Long id) {
        return techService.findById(userId, id);
    }

    @PutMapping("/{id}")
    public TechResponseDTO update(
            @PathVariable Long userId,
            @PathVariable Long id,
            @Valid @RequestBody TechRequestDTO request) {
        return techService.update(userId, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @PathVariable Long id) {
        techService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
