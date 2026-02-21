package com.ccn.sns.sns_project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccn.sns.sns_project.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
