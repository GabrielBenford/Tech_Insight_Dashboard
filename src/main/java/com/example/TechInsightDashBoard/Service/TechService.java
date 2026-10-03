package com.example.TechInsightDashBoard.Service;

import com.example.TechInsightDashBoard.DTO.TechDTO.TechRequestDTO;
import com.example.TechInsightDashBoard.DTO.TechDTO.TechResponseDTO;
import com.example.TechInsightDashBoard.Entity.TechEntity;
import com.example.TechInsightDashBoard.Entity.UserEntity;
import com.example.TechInsightDashBoard.Mapper.TechMapper;
import com.example.TechInsightDashBoard.Repository.TechRepository;
import com.example.TechInsightDashBoard.Repository.UserRepository;
import com.example.TechInsightDashBoard.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TechService {

    private final TechRepository techRepository;
    private final UserRepository userRepository;

    @Transactional
    public TechResponseDTO create(Long userId, TechRequestDTO request) {
        UserEntity user = findUser(userId);
        TechEntity technology = TechEntity.builder()
                .techName(request.techName().trim())
                .history(request.history().trim())
                .user(user)
                .build();
        return TechMapper.toResponseDTO(techRepository.save(technology));
    }

    @Transactional
    public List<TechResponseDTO> findAllByUser(Long userId) {
        return techRepository.findAllByUserIdOrderByTechNameAsc(userId)
                .stream()
                .map(TechMapper::toResponseDTO)
                .toList();
    }

    public TechResponseDTO findById(Long userId, Long id) {
        return techRepository.findById(id)
                .filter(technology -> technology.getUser().getId().equals(userId))
                .map(TechMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Technology", id));
    }

    public TechResponseDTO update(Long userId, Long id, TechRequestDTO request) {
        TechEntity technology = techRepository.findById(id)
                .filter(existing -> existing.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Technology", id));
        technology.setTechName(request.techName().trim());
        technology.setHistory(request.history().trim());
        return TechMapper.toResponseDTO(technology);
    }

    public void delete(Long userId, Long id) {
        TechEntity technology = techRepository.findById(id)
                .filter(existing -> existing.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Technology", id));
        techRepository.delete(technology);
    }

    private UserEntity findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}
