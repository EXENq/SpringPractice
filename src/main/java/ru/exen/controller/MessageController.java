package ru.exen.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import ru.exen.Dto.EventType;
import ru.exen.Dto.ObjectType;
import ru.exen.domain.Message;
import ru.exen.domain.Views;
import ru.exen.repo.MessageRepo;
import ru.exen.util.WsSender;

@RestController
@RequestMapping("message")
public class MessageController {
	private final MessageRepo messageRepo;
	private final BiConsumer<EventType, Message> wsSender;
	
	@Autowired
	public MessageController(WsSender wsSender, MessageRepo messageRepo) {
		super();
		this.messageRepo = messageRepo;
		this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.IdName.class);
	}

	@GetMapping
	@JsonView(Views.IdName.class)
	public List<Message> list() {
		return messageRepo.findAll();
	}
	
	@GetMapping("{id}")
	@JsonView(Views.FullMessage.class)
	public Message getOne(@PathVariable("id") Message message){
		return message;
	}
	
	@PostMapping
	public Message create(@RequestBody Message message){
		message.setCreationTime(LocalDateTime.now());
		Message updatedMessage = messageRepo.save(message);
		
		wsSender.accept(EventType.CREATE, updatedMessage);
		return updatedMessage;
	}
	
	@PutMapping("{id}")
	public Message update(
			@PathVariable("id") Message messageFromDb, 
			@RequestBody Message message
		){
		BeanUtils.copyProperties(message, messageFromDb, "id");
		
		Message updatedMessage = messageRepo.save(messageFromDb);
		wsSender.accept(EventType.UPDATE, updatedMessage);
		
		return updatedMessage;
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Message message) {
		messageRepo.delete(message);
		wsSender.accept(EventType.REMOVE, message);
	}
}