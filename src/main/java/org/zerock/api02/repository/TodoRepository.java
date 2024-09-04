package org.zerock.api02.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.api02.domain.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
