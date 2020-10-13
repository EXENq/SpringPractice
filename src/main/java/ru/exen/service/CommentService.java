package ru.exen.service;

import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.exen.Dto.EventType;
import ru.exen.Dto.ObjectType;
import ru.exen.domain.Comment;
import ru.exen.domain.User;
import ru.exen.domain.Views;
import ru.exen.repo.CommentRepo;
import ru.exen.util.WsSender;

@Service
public class CommentService {
	private final CommentRepo commentRepo;
	private final BiConsumer<EventType, Comment> wsSender;

	@Autowired
	public CommentService(WsSender wsSender, CommentRepo commentRepo) {
		this.commentRepo = commentRepo;
		this.wsSender = wsSender.getSender(ObjectType.COMMENT, Views.FullComment.class);
	}
	
	public Comment create(Comment comment, User user) {
		comment.setAuthor(user);
		Comment commentFromDB = commentRepo.save(comment);
		
		wsSender.accept(EventType.CREATE, commentFromDB);
		
		return commentFromDB;
	}
}
