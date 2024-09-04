package org.zerock.api02.service;

import jakarta.transaction.Transactional;
import org.zerock.api02.dto.TodoDTO;

@Transactional
public interface TodoService {

    Long register(TodoDTO todoDTO);

}
