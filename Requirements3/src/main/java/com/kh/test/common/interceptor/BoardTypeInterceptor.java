package com.kh.test.common.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kh.test.service.BoardService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BoardTypeInterceptor implements HandlerInterceptor {

	@Autowired
	private BoardService service;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		ServletContext application = request.getServletContext();

		if (application.getAttribute("boardTypeList") == null) {

			List<Map<String, Object>> boardTypeList = service.selectBoardTypeList();

			application.setAttribute("boardTypeList", boardTypeList);
		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

}
