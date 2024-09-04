package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.demo.dto.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Slf4j // log 필드 생성 및 초기화 자동완성 lombok 어노테이션(print 사용 안하고 콘솔 표시)
@RequestMapping("example") // /example로 시작하는 요청을 매핑
@Controller // 컨트롤러임을 명시 + bean 등록
public class ExampleController {
		// Servlet / JSP 내장 객체(4종류) 데이터 유지 범위(scope)
		// 1) page : 현재 페이지만 접속 유지
		// 2) request : 요청 받은, 요청 위임(forward) 받은 페이지 접속 유지
		// 3) session : 클라이언트가 서버 최초 연결 시 브라우저 종료 전, 세션 시간 만료까지 페이지 유지
		// 4) applicaion : 서버 실행 시 1개만 생성 후 서버가 종료될 때 까지 페이지 유지
		
	/* Mpdel
	 * - reg.springframework.ui 패키지
	 * - Spring에서 데이터를 전달하는 역할의 객체
	 * - 데이터 유지 범위(scope) : 기본 request
	 * - @SessionAttributes와 함께 사용하면 session scope로 변경
	 * 
	 * [Model을 이용해서 값을 셋팅하는 방법]
	 * Model.addAttribute("key", value);
	 */
	@GetMapping("ex1")
	public String ex1(HttpServletRequest req, Model model) {
		// request scope에 값 셋팅
		req.setAttribute("test1", "HttpServletRequest로 셋팅한 값");
		
		// model을 이용해서 request scope 값 셋팅
		model.addAttribute("test2", "Model로 셋팅한 값");
		
		// 단일 값 셋팅(숫자, 문자열)
		model.addAttribute("productName", "아이스 아메리카노");
		model.addAttribute("price", 2000);
		
		// 복수 값 셋팅(배열, List)
		List<String> fruitList = new ArrayList<>();
		fruitList.add("복숭아");
		fruitList.add("딸기");
		fruitList.add("사과");
		fruitList.add("키위");
		model.addAttribute("fruitList", fruitList);
		
		// DTO 객체를 만들어 Model에 셋팅 + 빌더 패턴 사용
		Student std = Student.builder()
												 .studentNo("1111")
												 .name("짱구")
												 .age(15)
												 .build(); // -> 필드 전체가 아닌 일부 초기화 시 활용도가 좋음
		
		log.debug("std : {}", std);
		
		model.addAttribute("std", std);
		
		// DTO 필드 중 List 가 포함되어 있는 경우
		List<String> hobbyList = new ArrayList<>();
		hobbyList.add("독서");
		hobbyList.add("요리");
		hobbyList.add("청소");
		
		Student std2=Student.builder()
												.studentNo("2222")
												.name("철수")
												.age(20)
												.hobbyList(hobbyList)
												.build();
		model.addAttribute("std2", std2);
		
		// classpath:/templates/ex/result1.html 파일로 forward(요청 위임)
		return "ex/result1";
	}
	/** 
	 * @param model : Spring에서 데이터를 전달하는 용도의 객체(기본 scope : request)
	 * @return ex/result2
	 */
	@PostMapping("ex2") // /example/ex2 POST 방식 요청 매핑
	public String ex2(Model model) {
		model.addAttribute("str", "<h3>테스트 중입니다...&times;</h3>");
		
		// classpath:/templates/ex/result1.html 파일로 forward(요청 위임)
		return "ex/result2";
	}
	
}
