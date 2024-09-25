package edu.kh.project.email.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.common.util.RedisUtil;
import edu.kh.project.email.service.EmailService;


@Controller
@RequestMapping("email")
public class EmailController {
	@Autowired // 의존성 주입
	public RedisUtil redisUtil;
	
	@Autowired // 서비스 의존성 주입
	public EmailService service;
	// 레디스 확인하기
	@ResponseBody
	@GetMapping("redisTest") // localhost/email/redisTest?key=name&value=홍길동
	public int redisTest(@RequestParam("key") String key, @RequestParam("value") String value) {
		
		// 전달 받은 key, value를 redis에 set 하기
		redisUtil.setValue(key, value, 60); // 60초 후에 만료
		
		return 1;
	}
	
	/** 이메일 인증번호 발송
	 * @param email : 입력된 이메일
	 * @return : 성공 1, 실패 0
	 */
	@ResponseBody
	@PostMapping("sendAuthKey")
	public int sendAuthKey(@RequestBody String email) {
		return service.sendEmail("signUp", email);
	}
	
	/** 인증번호 확인하기
	 * @param map : 입력 받은 email, authKey가 저장된 map, HttpMessageConverter에 의해 JSON -> Map 자동 변환
	 * @return : 
	 */
	@ResponseBody
	@PostMapping("checkAuthKey")
	public boolean checkAuthKey(@RequestBody Map<String, String> map) {
		return service.checkAuthKey(map);
	}
	
}
