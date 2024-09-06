package edu.kh.todolist.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.todolist.dto.Todo;
import edu.kh.todolist.service.TodoListService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller // Controller임을 명시 + Bean 등록
public class MainController {
	@Autowired // 의존성 주입(DI)
	private TodoListService service;
	@RequestMapping("/") // 최상위 주소 매핑(GET, POST 가리지 않음)
	public String mainPage(Model model) {
		Map<String, Object> map = service.selectTodoList();
		
		// map에 담신 값 꺼내놓기
		List<Todo> todoList = (List<Todo>)map.get("todoList");
		int completeCount = (int)map.get("completeCount");
		
		// 조회결과 request scope에 추가
		model.addAttribute("todoList", todoList);
		model.addAttribute("completeCount", completeCount);
		
		// classpath:/templates/cpmmon/main.html forward
		return "common/main";
	}
	// 할 일 상세조회하기
	@GetMapping("selectList/{listTitle}")
	public String selectList(@RequestParam("listNo") int listNo, Model model) {
		
		// 사용자 이름 조회 서비스 호출 후 결과 반환 받기
		String listTitle = service.selectList(listNo);
		
		// 조회 결과를 model에 추가
		model.addAttribute("listTitle", listTitle);
		
		// classpath:/templates/user/searchName.html		
		return "/common/searchList";
	}
	
	// 할 일 수정하기
	@PostMapping("updateTodo/{listTitle}")
	public String updateTodo(@PathVariable("listNo") int listNo, @ModelAttribute Todo todo, RedirectAttributes ra) {

			int result = service.updateTodo(todo);
			
			// 수정 결과에 따라 메시지 지정
			String message = null;
			if(result > 0) message = "수정이 되었습니다.";
			else message = "수정이 실패하였습니다.";

			ra.addFlashAttribute("message", message);
			
			// 상세 조회 페이지 redirect
			return "redirect:/common/check/" + todo;
		}
}
