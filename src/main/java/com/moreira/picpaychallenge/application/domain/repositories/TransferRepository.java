package com.moreira.picpaychallenge.application.domain.repositories;

import com.moreira.picpaychallenge.application.domain.entities.Transfer;
import com.moreira.picpaychallenge.application.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Optional<User> findUserByEmail(String email);
}
