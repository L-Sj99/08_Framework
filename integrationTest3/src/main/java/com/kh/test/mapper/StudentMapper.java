package com.kh.test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.test.dto.Student;

@Mapper
public interface StudentMapper {

	List<Student> selectAllList();

}