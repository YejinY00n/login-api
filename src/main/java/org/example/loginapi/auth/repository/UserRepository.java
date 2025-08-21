package org.example.loginapi.auth.repository;

import java.util.Optional;

import org.example.loginapi.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUsername(String username);

	Optional<User> findByUsername(String username);
}
