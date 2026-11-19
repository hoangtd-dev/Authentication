package com.backend10.ms_login.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend10.ms_login.entities.User;

public interface AuthenticationRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
}
