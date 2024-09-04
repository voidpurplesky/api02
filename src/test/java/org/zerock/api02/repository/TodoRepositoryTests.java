package org.zerock.api02.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.api02.domain.Todo;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void insert() {

        IntStream.rangeClosed(1, 100).forEach(i -> {
            Todo todo = Todo.builder()
                    .title("Todo..."+i)
                    .dueDate(LocalDate.of(2024, (i%12)+1, (i%30)+1))
                    .writer("user"+(i%10))
                    .complete(false)
                    .build();

            todoRepository.save(todo);
        });
    }
/*
Hibernate:
    create table tbl_todo_api (
        tno bigint not null auto_increment,
        complete bit not null,
        due_date date,
        title varchar(255),
        writer varchar(255),
        primary key (tno)
    ) engine=InnoDB


Hibernate:
    insert
    into
        tbl_todo_api
        (complete, due_date, title, writer)
    values
        (?, ?, ?, ?)
    returning tno
 */
}
