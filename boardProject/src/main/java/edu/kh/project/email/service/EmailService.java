package edu.kh.project.email.service;

import java.util.Map;

public interface EmailService {

	/** 이메일 인증번호 발송 서비스
	 * @param htmlName
	 * @param email : 입력받은 이메일
	 * @return
	 */
	int sendEmail(String htmlName, String email);

	/** 인증번호 확인하기
	 * @param map
	 * @return
	 */
	boolean checkAuthKey(Map<String, String> map);

}
