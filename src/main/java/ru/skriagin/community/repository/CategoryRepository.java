package ru.skriagin.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skriagin.community.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
