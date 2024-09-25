package com.kh.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.kh.test.dto.Member;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MemberController {

	@PostMapping("member/select")
	public String selectMember(HttpServletRequest req, @ModelAttribute Member member) {

		// Model 아니여도 HttpServletRequest 이용해서 request scope 세팅 가능
		req.setAttribute("memberName", member.getMemberName());
		req.setAttribute("memberAge", member.getMemberAge());
		req.setAttribute("memberAddress", member.getMemberAddress());

		return "member/select";
	}
}