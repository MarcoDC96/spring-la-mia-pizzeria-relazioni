package org.learning.springpizza.repository;

import org.learning.springpizza.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
    List<Pizza> findByNameContaining(String name);
}
