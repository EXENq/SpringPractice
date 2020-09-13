package ru.exen.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.exen.domain.User;

public interface UserDetailsRepo extends JpaRepository<User, String>{

}
