package edu.kh.project.email.service;

import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import edu.kh.project.common.util.RedisUtil;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

/* @RequiredArgsConstructor == final 필드에 Bean 의존성 주입 한번에 하기
 * - 필드 중 final 키워드가 작성된 필드 값을 초기화 하기 위한 생성자를 자동완성하는 Lombok 제공 어노테이션
 * + final 필드 초기화를 위한 생성자 매개변수에 자동으로 같은 자료형의 Bean을 의존성 주입함 */ 

/* 이메일 관련 비즈니스 로직 처리 클래스 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

	// 상수형 필드 선언
	private final JavaMailSender mailSender; // EmailConfig 내용이 적용된 이메일 발송이 가능한 객체
	private final RedisUtil redisUtil; // Redis(inMemory DB) CRUD 할 수 있는 기능을 제공하는 객체(Bean)
	private final SpringTemplateEngine templateEngine; // 자바에서 타임리프를 사용할 수 있게하는 객체(Bean), html 코드를 Java로 읽어올 수 있음
	
	/* 이메일 발송 */
	@Override
	public int sendEmail(String htmlName, String email) {
		try {
			String emailTitle = null; // 발송되는 이메일 제목
			String authKey = createAuthKey(); // 생성된 인증번호가 저장되는 변수
			
			// 이메일 발송 시 사용할 html 파일의 이름에 따라 이메일 제목, 내용을 다르게 설정
			switch(htmlName) {
			case "signUp" : emailTitle = "[boardProject] 회원 가입 인증번호 입니다."; break;
			case "findPw" : emailTitle = "[boardProject] 비밀번호 찾기 인증번호 입니다."; break;
			}
			// ** 중요 : 메일 발송 ** // 
			MimeMessage mimeMessage = mailSender.createMimeMessage(); // 메일 발송 객체
			
			// MimeMessageHelper : Spring에서 제공하는 메일 발송 도우미
			/* 매개변수 1(MimeMessage)
			 * 매개변수 2(true) : 이메일에 파일 첨부 여부
			 * 매개변수 3(UTF-8) : 발송되는 이메일의 문자 인코딩 지정 */
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setTo(email); // 받는 사람 이메일 세팅
			helper.setSubject(emailTitle); // 이메일 제목 세팅
			
			// 지정된 HTML 파일에 authKey가 첨부된 후 HTML 코드 전체가 하나의 String으로 변환되어서 반환받은 후 변환된 String을 mail 내용으로 셋팅
			helper.setText(loadHtml(authKey, htmlName), true); // 이메일 내용 세팅
			// 매개변수 1("안에 들어갈 내용") : 이메일 내용
			// 매개변수 2(true) : HTML 코드 해석 여부 지정(true == 해석 O)
			
			// CID(content-ID)를 이용해 메일에 이미지 첨부
			helper.addInline("logo", new ClassPathResource("static/images/logo.png"));
			mailSender.send(mimeMessage); // 메일 발송하기
			redisUtil.setValue(email, authKey, 60 * 3); // Redis에 이메일, 인증번호 저장(3분 후 만료)
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0; // 예외 발생 시 == 실패 == 0 반환
		} 
		return 1; // 예외 발생 안되면 == 성공 == 1 반환
	}

	/** 인증번호 생성 (영어 대문자 + 소문자 + 숫자 6자리)
	 * @return authKey
	 */
	public String createAuthKey() {
		String key = "";
			for (int i = 0; i < 6; i++) {
				int sel1 = (int) (Math.random() * 3); // 0:숫자 / 1,2:영어
				if (sel1 == 0) {
					int num = (int) (Math.random() * 10); // 0~9
					key += num;
			} else {
				char ch = (char) (Math.random() * 26 + 65); // A~Z
				int sel2 = (int) (Math.random() * 2); // 0:소문자 / 1:대문자
				if (sel2 == 0) {
					ch = (char) (ch + ('a' - 'A')); // 대문자로 변경
			}
				key += ch;
			}
		}
		return key;
	}
	
	// HTML 파일을 읽어와 String으로 변환 (타임리프 적용)
	public String loadHtml(String authKey, String htmlName) {
		
		// org.tyhmeleaf.Context 선택
		Context context = new Context();
		
		//타임리프가 적용된 HTML에서 사용할 값 추가
		context.setVariable("authKey", authKey);
		
		// templates/email 폴더에서 htmlName과 같은 .html 파일 내용을 읽어와 String으로 변환
		return templateEngine.process("email/" + htmlName, context);
	}

	/* 인증번호 확인하기 */
	@Override
	public boolean checkAuthKey(Map<String, String> map) {
		// map에 저장된 값 꺼내오기
		String email = map.get("email");
		String authKey = map.get("authKey");
		
		// 1) Redis에 key가 입력된 email과 같은 데이터가 있는지 확인
		if(redisUtil.hasKey(email) == false) { // 없을 경우
			return false;			
		} 
		// 2) Resid에 같은 key가 있다면 value를 얻어와 입력 받은 인증번호와 비교 
		return redisUtil.getValue(email).equals(authKey);
	}
}

/* 의존성 주입 방법 1 : 필드 이용
 * @Autowired
 * public 자료형 필드명;
 */

/* 의존성 주입 방법 2 : setter 이용
 * @Autowired
 * private 자료형 필드명; 
 * public void set필드명(자료형 변수명) {
 * 	this.필드명 = 변수명;
 *	} */

/* 추천 하는 방법
 * 의존성 주입 방법 3 : 생성자를 이용하는 방법(필드 많을 때)
 * private 자료형 필드명1;
 * private 자료형 필드명2;
 * @Autowired
 * public 클래스명(자료형 변수명1, 자료형 변수명2) {
 * this.필드명1 = 변수명1;
 * this.필드명2 = 변수명2;
 *	} */