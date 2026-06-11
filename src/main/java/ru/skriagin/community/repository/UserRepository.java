package ru.skriagin.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skriagin.community.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
