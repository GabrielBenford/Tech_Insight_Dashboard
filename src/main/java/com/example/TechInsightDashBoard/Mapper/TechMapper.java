package com.example.TechInsightDashBoard.Mapper;

import com.example.TechInsightDashBoard.DTO.TechResponseDTO;
import com.example.TechInsightDashBoard.Entity.TechEntity;

public final class TechMapper {

    private TechMapper() {
    }

    public static TechResponseDTO toResponseDTO(TechEntity entity) {
        return new TechResponseDTO(
                entity.getId(),
                entity.getTechName(),
                entity.getHistory(),
                entity.getUser().getId());
    }
}
