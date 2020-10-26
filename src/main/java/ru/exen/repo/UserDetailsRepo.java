package ru.exen.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.exen.domain.User;

public interface UserDetailsRepo extends JpaRepository<User, String>{
	
	@EntityGraph(attributePaths = { "subscriptions", "subscribers" })
	Optional<User> findById(String id);
	
}
