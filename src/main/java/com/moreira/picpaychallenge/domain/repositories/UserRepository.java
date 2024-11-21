package com.moreira.picpaychallenge.domain.repositories;

import com.moreira.picpaychallenge.domain.entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(@NotBlank(message = "Email is mandatory.") String email);

    boolean existsByDocument(@NotBlank(message = "Document is mandatory.") String document);
}
