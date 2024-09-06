package edu.kh.todolist.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todolist.dto.Todo;

@Mapper // 자동으로 상속받은 클래스 생성 후 Bean으로 등록
public interface TodoListMapper {

	/** 할 일 목록 조회
	 * @return todoList
	 */
	List<Todo> selectTodoList();

	/** 환료된 할 일 개수 조회
	 * @return completeCount
	 */
	int selectCompleteCount();

	/** 할 일 수정
	 * @param todo
	 * @return
	 */
	int updateTodo(Todo todo);

}
