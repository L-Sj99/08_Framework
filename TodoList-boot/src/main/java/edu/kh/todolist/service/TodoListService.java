package edu.kh.todolist.service;

import java.util.Map;

import edu.kh.todolist.dto.Todo;

public interface TodoListService {

	/** 할 일 목록 조회 + 완료된 할 일 개수
	 * @return
	 */
	Map<String, Object> selectTodoList();
	
	/** 할 일 상세조회하기
	 * @param listTitle
	 * @return
	 */
	String selectList(String listTitle);

	/** 할 일 수정하기
	 * @param todo
	 * @return
	 */
	int updateTodo(Todo todo);

	
}
