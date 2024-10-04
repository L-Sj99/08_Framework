package com.kh.test.service;

import java.awt.print.Book;
import java.io.Console;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.test.dto.Student;
import com.kh.test.mapper.StudentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
	
	private final StudentMapper mapper;
	
	@Override
	public List<Student> selectAllList() {
		return mapper.selectAllList();
	}
}
