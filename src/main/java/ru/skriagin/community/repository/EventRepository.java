package ru.skriagin.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skriagin.community.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
