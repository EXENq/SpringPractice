package ru.exen.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.exen.domain.Message;

public interface MessageRepo extends JpaRepository<Message, Long> {
	
}
