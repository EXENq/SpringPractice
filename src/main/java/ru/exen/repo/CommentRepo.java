package ru.exen.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.exen.domain.Comment;

public interface CommentRepo extends JpaRepository<Comment, Long> {

}
