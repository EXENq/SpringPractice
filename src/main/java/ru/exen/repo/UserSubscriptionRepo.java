package ru.exen.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.exen.domain.User;
import ru.exen.domain.UserSubscription;
import ru.exen.domain.UserSubscriptionId;

public interface UserSubscriptionRepo extends JpaRepository<UserSubscription, UserSubscriptionId>{
	List<UserSubscription> findBySubscriber(User user);

	UserSubscription findByChannelAndSubscriber(User channel, User subscriber);

	List<UserSubscription> findByChannel(User channel);
}
