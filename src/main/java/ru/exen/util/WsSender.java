package ru.exen.util;

import java.util.function.BiConsumer;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ru.exen.Dto.EventType;
import ru.exen.Dto.ObjectType;
import ru.exen.Dto.WsEventDto;

@Component
public class WsSender {
	private final SimpMessagingTemplate template;
	private final ObjectMapper mapper;
	
	public WsSender(SimpMessagingTemplate template, ObjectMapper mapper) {
		this.template = template;
		this.mapper = mapper;
	}
	
	public <T> BiConsumer<EventType, T> getSender(ObjectType objectType, Class view) {
		ObjectWriter writer = mapper.setConfig(mapper.getSerializationConfig())
				.writerWithView(view);
		
			return (EventType eventType ,T payload) -> {
				String value = null;
				try {
					value = writer.writeValueAsString(payload);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				template.convertAndSend("/topic/activity", new WsEventDto(objectType, eventType, value));
			};
	}
}
