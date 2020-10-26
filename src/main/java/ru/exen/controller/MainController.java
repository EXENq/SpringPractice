package ru.exen.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ru.exen.Dto.MessagePageDto;
import ru.exen.domain.User;
import ru.exen.domain.Views;
import ru.exen.repo.UserDetailsRepo;
import ru.exen.service.MessageService;

@Controller
@RequestMapping("/")
public class MainController {
	private final MessageService messageService;
	private final UserDetailsRepo userDetailsRepo;

	@Value("${spring.profiles.active}")
	private String profile;
	private final ObjectWriter messageWriter;
	private final ObjectWriter profileWriter;

	@Autowired
	public MainController(MessageService messageService, ObjectMapper mapper, UserDetailsRepo userDetailsRepo) {
		this.messageService = messageService;
		this.userDetailsRepo = userDetailsRepo;
		
		ObjectMapper objectMapper = mapper.setConfig(mapper.getSerializationConfig());

		this.messageWriter = objectMapper.writerWithView(Views.FullMessage.class);
		this.profileWriter = objectMapper.writerWithView(Views.FullProfile.class);
	}

	@GetMapping
	public String main(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {
		HashMap<Object, Object> data = new HashMap<>();

		if (user != null) {
			User userFromDB = userDetailsRepo.findById(user.getId()).get();
			String serializedProfile = profileWriter.writeValueAsString(userFromDB);
			model.addAttribute("profile", serializedProfile);
			
			Sort sort = Sort.by(Direction.DESC, "id");
			PageRequest pageRequest = PageRequest.of(0, MessageController.MESSAGES_PER_PAGE, sort);
			MessagePageDto messagePageDto = messageService.findForUser(pageRequest, user);
			
			String messages = messageWriter.writeValueAsString(messagePageDto.getMessages());
			
			model.addAttribute("messages", messages);
			data.put("currentPage", messagePageDto.getCurrentPage());
			data.put("totalPages", messagePageDto.getTotalPages());
		} else {
			model.addAttribute("messages", "[]");
			model.addAttribute("profile", "null");
		}

		model.addAttribute("frontendData", data);
		model.addAttribute("isDevMode", "dev".equals(profile));

		return "index";
	}
}
