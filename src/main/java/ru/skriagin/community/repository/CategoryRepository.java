package ru.skriagin.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skriagin.community.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
