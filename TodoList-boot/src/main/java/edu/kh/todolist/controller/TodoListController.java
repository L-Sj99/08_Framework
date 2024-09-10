package edu.kh.todolist.controller;

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
@Controller // Bean 등록 (IOC, 객체 생성 및 관리를 Spring이 함)
@RequestMapping("todo")
public class TodoListController {

	@Autowired // 등록된 Bean 중에서 같은 타입 객체를 주입(DI)
	private TodoListService service;
	
	
	// @ModelAttribute : 제출된 파라미터를 DTO 객체 필드에 대입
	//  							-> 생략도 가능하다!!
	/**
	 * @param todo : 커맨드 객체(제출된 파라미터를 필드에 저장한 DTO 객체)
	 * @param ra   : 리다이렉트(재요청) 시 request scope로 값 전달하는 객체
	 * @return
	 */
	@PostMapping("add")
	public String todoAdd(
			@ModelAttribute Todo todo,
			RedirectAttributes ra) {
		
		// 서비스 호출 후 결과 반환 받기
		int result = service.todoAdd(todo);
		
		String message = null;
		if(result > 0)	message = "할 일 추가 성공";
		else						message = "추가 실패...";
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/"; // 메인 페이지로 리다렉트
	}
	
	/** 상세 조회
	 * @param listNo : 조회할 할 일의 PK 번호(@PathVariable 이용)
	 * @param model  : 데이터 전달용 객체(forward 시 request scope 값 전달)
	 * @param ra : 
	 * @return
	 */
	@GetMapping("check/{listNo}")
	public String listCheck(
		@PathVariable("listNo") int listNo,
		Model model,
		RedirectAttributes ra
		) {
		
		Todo todo = service.listCheck(listNo);
		
		if(todo == null) { // 조회 결과가 없을 경우
			ra.addFlashAttribute("message", "할 일이 존재하지 않습니다");
			return "redirect:/"; // 리다이렉트
		}
		
		// 조회 결과가 있을 경우
		model.addAttribute("todo", todo);
		return "todo/check"; // 요청 위임
	}
	
	/** 완료 여부 변경
	 * @param listNo : 쿼리스트링으로 전달된 listNo 값
	 * @param ra
	 * @return
	 */
	@GetMapping("complete")
	public String todoComplete(
		@RequestParam("listNo") int listNo,
		RedirectAttributes ra
		) {
		
		int result = service.todoComplete(listNo);
		
		String message = null;
		String path = null;
		if(result > 0) 	{
			message = "변경 되었습니다";
			path = "redirect:/todo/check/" + listNo; // 상세 조회
		}
		else						{
			message = "할 일이 존재하지 않습니다";
			path = "redirect:/"; // 메인 페이지로 날려!
		}
		
		ra.addFlashAttribute("message", message);
		return path;
	}
	
	/** 수정 화면 전환
	 * @param listNo
	 * @param model
	 * @param ra
	 * @return
	 */
	@GetMapping("update")
	public String todoUpdate(
		@RequestParam("listNo") int listNo,
		Model model,
		RedirectAttributes ra) {
		
		// 상세 조회 서비스 호출
		Todo todo = service.listCheck(listNo);
		
		if(todo == null) { // todoNo 일치하는 할 일이 없을 경우
			ra.addFlashAttribute("message", "할 일이 존재하지 않습니다");
			return "redirect:/"; // 메인 페이지
		}
		
		model.addAttribute("todo", todo);
		return "todo/update"; // 수정 화면 forward
	}
	
	/** 할 일 수정
	 * @param ra
	 * @param todo : listTitle, listCheck, listNo가 담긴 커맨드 객체
	 * @return
	 */
	@PostMapping("update")
	public String todoUpdate(
		RedirectAttributes ra,
		@ModelAttribute Todo todo
		) {
		
		int result = service.todoUpdate(todo);
		
		String message = null;
		String path = null;
		if(result > 0) {
			message = "수정 성공";
			path = "redirect:/todo/check/" + todo.getListNo();
		} else {
			message = "수정 실패";
			path = "redirect:/todo/update?listNo=" + todo.getListNo();
		}
		
		ra.addFlashAttribute("message", message);
		return path;
	}
	
	/** 할 일 삭제
	 * @param listNo
	 * @param ra
	 * @return 
	 */
	@GetMapping("check")
	public String listCheck(
		@RequestParam("listNo") int listNo,
		RedirectAttributes ra) {
		
		int result = service.todoDelete(listNo);
		
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "삭제 되었습니다";
			path = "redirect:/"; // 메인 페이지
			
		} else {
			message = "삭제 실패";
			path = "redirect:/todo/check/" + listNo;
		}
		
		ra.addFlashAttribute("message", message);
		return path;
	}
	
	
	
	
}