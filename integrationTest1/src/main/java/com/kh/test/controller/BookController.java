package com.kh.test.controller;

import java.awt.print.Book;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.test.service.BookService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@RequiredArgsConstructor
@RequestMapping("book")
public class BookController {
	
	private final BookService service;
	
	@ResponseBody
	@GetMapping("selectAllList")
	public List<Book> selectAllList() {
		return service.selectAllList();
	}
	
}