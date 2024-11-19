package com.moreira.picpaychallenge.application.domain.repositories;

import com.moreira.picpaychallenge.application.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
