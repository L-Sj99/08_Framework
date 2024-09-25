package edu.kh.project.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.main.service.MainService;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;

@Controller // 요청 / 응답 제어하는 COntroller 역할 명시 + Bean 등록
@RequiredArgsConstructor
public class MainController {
	private final MainService service;// 서비스 의존성 주입(DI) 되어있음
	
	@RequestMapping("/") // "/" 요청 매핑(method 가리지 않음)
	public String mainPage() {
		// 접두사 : classpath:/templetes/
		// 접미사 : .html
		// -> forward 하려는 파일의 Thymeleaf 접두사, 접미사를 제외한 경로 작성
		return "common/main";
	}
	
	/** 비동기 회원 목록 조회 
	 * @return
	 */
	@ResponseBody
	@GetMapping("/selectMemberList")
	public List<Member> selectMemberList(){
		return service.selectMemberList();
	}
}
