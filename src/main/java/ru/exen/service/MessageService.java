package ru.exen.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ru.exen.Dto.EventType;
import ru.exen.Dto.MessagePageDto;
import ru.exen.Dto.MetaDto;
import ru.exen.Dto.ObjectType;
import ru.exen.domain.Message;
import ru.exen.domain.User;
import ru.exen.domain.UserSubscription;
import ru.exen.domain.Views;
import ru.exen.repo.MessageRepo;
import ru.exen.repo.UserSubscriptionRepo;
import ru.exen.util.WsSender;

@Service
public class MessageService {
	private static String URL_PATTERN = "https?:\\/\\/?[\\w\\d\\._\\-%\\/\\?=&#]+";
	private static String IMAGE_PATTERN = "\\.(jpeg|jpg|gif|png)$";
	
	private static Pattern URL_REGEX = Pattern.compile(URL_PATTERN, Pattern.CASE_INSENSITIVE);
	private static Pattern IMG_REGEX = Pattern.compile(IMAGE_PATTERN, Pattern.CASE_INSENSITIVE);
	
	private final MessageRepo messageRepo;
	private final UserSubscriptionRepo subscriptionRepo;
	private final BiConsumer<EventType, Message> wsSender;
	
	@Autowired
	public MessageService(WsSender wsSender, MessageRepo messageRepo, UserSubscriptionRepo subscriptionRepo) {
		super();
		this.messageRepo = messageRepo;
		this.subscriptionRepo = subscriptionRepo;
		this.wsSender = wsSender.getSender(ObjectType.MESSAGE, Views.FullMessage.class);
	}
	
	private void fillMeta(Message message) throws IOException {
		String text = message.getText();
		
		Matcher matcher = URL_REGEX.matcher(text);
		
		if (matcher.find()) {
			String url = text.substring(matcher.start(), matcher.end());
			
			matcher = IMG_REGEX.matcher(url);
			
			message.setLink(url);
			
			if(matcher.find()) {
				message.setLinkCover(url);
			} else if(!url.contains("youtu")) {
				MetaDto meta = getMeta(url);
				
				message.setLinkCover(meta.getCover());
				message.setLinkTitle(meta.getTitle());
				message.setLinkDescription(meta.getDescription());
			}
			
		}
	}
	
	private MetaDto getMeta(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements title = doc.select("meta[name$=title], meta[property$=title]");
		Elements description = doc.select("meta[name$=description], meta[property$=description]");
		Elements cover = doc.select("meta[name$=image], meta[property$=image]");
		
		return new MetaDto(
				getContent(title.first()),
				getContent(description.first()),
				getContent(cover.first())
		);
	}	
	
	private String getContent(Element element) {
		return element == null ? "" : element.attr("content");
	}

	public void delete(Message message) {
		messageRepo.delete(message);
		wsSender.accept(EventType.REMOVE, message);
	}

	public Message update(Message messageFromDb, Message message) throws IOException {
        messageFromDb.setText(message.getText());
        fillMeta(messageFromDb);
        Message updatedMessage = messageRepo.save(messageFromDb);

        wsSender.accept(EventType.UPDATE, updatedMessage);

        return updatedMessage;
    }

	public Message create(Message message, User user) throws IOException {
		message.setCreationTime(LocalDateTime.now());
		fillMeta(message);
		message.setAuthor(user);
		Message updatedMessage = messageRepo.save(message);
		
		wsSender.accept(EventType.CREATE, updatedMessage);
		return updatedMessage;
	}

	public MessagePageDto findForUser(Pageable pageable, User user) {
		List<User> channels = subscriptionRepo.findBySubscriber(user)
			.stream()
			.filter(UserSubscription::isActive)
			.map(UserSubscription::getChannel)
			.collect(Collectors.toList());
		
		channels.add(user);
		
		Page<Message> page = messageRepo.findByAuthorIn(channels, pageable);
		return new MessagePageDto(page.getContent(), pageable.getPageNumber(), page.getTotalPages());
	}
}
