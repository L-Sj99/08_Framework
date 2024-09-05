package edu.kh.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.demo.mapper.TestMapper;

// @Service
// - Service역할(비즈니스 로직 처리)임 명시
// - Bean 등록(== Spring이 관리하는 객체 == IOC)
@Service
public class UserServiceImpl implements UserService{
	/* @Autowired
	 * - 등록된 Bean 중에서 자료형이 같은 Bean을 얻어와 필드에 대입 == DI(의존성 주입)
	 */
	@Autowired
	private TestMapper mapper;
	
	@Override
	public String selectUserName(int userNo) {
		return mapper.selectUserName(userNo);
	}
}
