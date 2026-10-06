package com.example.TechInsightDashBoard.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "search_history")
public class SearchHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "technology_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_search_history_technology"))
    private TechEntity technology;

    @Column(name = "provider", nullable = false, length = 30)
    private String provider;

    @Column(name = "query", nullable = false, columnDefinition = "TEXT")
    private String query;

    @Column(name = "result_count")
    private Integer resultCount;

    @Column(name = "searched_at", nullable = false)
    private Instant searchedAt;
}
