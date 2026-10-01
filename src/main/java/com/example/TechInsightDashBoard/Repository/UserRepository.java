package com.example.TechInsightDashBoard.Repository;
import com.example.TechInsightDashBoard.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    String findPasswordByEmail(String email);

    UserEntity findByEmail(String email);
}
