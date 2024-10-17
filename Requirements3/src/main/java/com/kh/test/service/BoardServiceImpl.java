package com.kh.test.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.test.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {	

	@Autowired
	private BoardMapper mapper;
	
	@Override
	public List<Map<String, Object>> selectBoardTypeList() {
		return mapper.selectBoardTypeList();
	}
}
