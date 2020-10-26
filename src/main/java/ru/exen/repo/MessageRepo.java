package ru.exen.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.exen.domain.Message;
import ru.exen.domain.User;

public interface MessageRepo extends JpaRepository<Message, Long> {
	
	@EntityGraph(attributePaths = { "comments" })
	Page<Message> findByAuthorIn(List<User> users, Pageable pageable);
}
