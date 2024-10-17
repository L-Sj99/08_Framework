package edu.kh.project.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// HttpServletRequest/Response로 다운 캐스팅
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		// Session 객체 얻어오기
		HttpSession session = req.getSession();
		
		// 로그인이 되어 있지 않은 경우
		if(session.getAttribute("loginMember") == null ) {
			resp.sendRedirect("/"); // 메인 페이지로 리다이렉트
			
		} else {
			chain.doFilter(request, response); // 다음 필터로 이동
		}
	}
}