package edu.kh.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.project.Book;

@Controller
@RequestMapping("book")
public class BookController {
	
	@GetMapping("select")
	 public String selectBook(@ModelAttribute Book book, Model model) {
		 model.addAttribute("title", book.getTitle());
		 model.addAttribute("writer", book.getWriter());
		 model.addAttribute("price", book.getPrice());
		 
		return "book/result";
	 }

}
