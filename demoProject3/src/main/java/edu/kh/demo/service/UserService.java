package edu.kh.demo.service;

import java.util.List;

import edu.kh.demo.dto.UserDto;

public interface UserService {

	/** 사용자 이름 조회
	 * @param userNo
	 * @return userName
	 */
	String selectUserName(int userNo);

	/** 사용자 전체 조회
	 * @return
	 */
	List<UserDto> selectAll();

	/** userNo가 일치하는 사용자 조회
	 * @param userNo
	 * @return
	 */
	UserDto selectUser(int userNo);

	/** 사용자 정보 수정하기
	 * @param user
	 * @return
	 */
	int updateUser(UserDto user);
	
	/** 사용자 정보 삭제하기
	 * @param userNo
	 * @return
	 */
	int deleteUser(int userNo);

	/** 사용자 추가하기
	 * @param user
	 * @return
	 */
	int insertUser(UserDto user);
	


}
