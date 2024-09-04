package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 컨트롤러 역할(요청, 응답 제어) 명시 + Bean 등록
public class MainController {
	
	// "/" 주소 요청 (== localhost, 최상위 주소) 시 매핑하여 처리하는 메서드
	// -> index.html로 응답하는 것이 아닌 해당 메서드에서 요청 처리/응답 수행
	
	// 장점 : Java를 거쳐서 메인페이지가 보여짐 -> 추가 셋팅 값, DB 조회 값을 위임된 HTML에서 출력 가능
	// == 메인 페이지에서부터 DB 조회 값이 보여지게됨
	@RequestMapping("/") 
	public String mainPage() {
		// 사용하는 템플릿 엔진 : Thymeleaf
		// Thymeleaf를 사용하는 프로젝트에서 forward 시
		// 제공하는 접두사 : classpath:/templates/
		// 제공하는 접미사 : .html
		
		// classpath:/templates/common/main.html 파일로 forward
		return "common/main";
	}
}
