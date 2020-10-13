package ru.exen.repo;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.exen.domain.Message;

public interface MessageRepo extends JpaRepository<Message, Long> {
	
	@EntityGraph(attributePaths = { "comments" })
	List<Message> findAll();
}
