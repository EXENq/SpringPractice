package ru.exen.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.exen.domain.Message;
import ru.exen.domain.Views;

@Getter
@Setter
@AllArgsConstructor
@JsonView(Views.FullMessage.class)
public class MessagePageDto {
	private List<Message> messages;
	private int currentPage;
	private int totalPages;
}
