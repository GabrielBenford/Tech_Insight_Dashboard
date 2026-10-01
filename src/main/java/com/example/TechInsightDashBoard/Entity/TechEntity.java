package com.example.TechInsightDashBoard.Entity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "technologies")
public class TechEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tech_name", nullable = false, length = 150)
    private String techName;

    @Column(name = "history", nullable = false, columnDefinition = "TEXT")
    private String history;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_technology_user"))
    private UserEntity user;
}
