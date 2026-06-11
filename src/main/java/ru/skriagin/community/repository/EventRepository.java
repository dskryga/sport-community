package ru.skriagin.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skriagin.community.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
