package com.kh.test.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.test.dto.Student;
import com.kh.test.service.StudentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("student")
public class StudentController {
	
	private final StudentService service;
	
	@ResponseBody
	@GetMapping("selectAllList")
	public List<Student> selectAllList() {
		return service.selectAllList();
	}
	
}