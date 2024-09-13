package edu.kh.project.myPage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.dto.Member;
import edu.kh.project.myPage.service.MyPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



/* @SessionAttributes 용도
 * 1. Model을 이용하여 값을 request -> session으로 scope 변경
 * 2. @SessionAttribute(s 없음)를 이용해 @SessionAttribute"s"로 인해 session에 등록된 값을 얻어올 수 있음
 */
@SessionAttributes({"loginMember"})
@Slf4j
@Controller // Controller임을 명시 + Bean 등록
@RequestMapping("myPage")
public class MyPageController {
	
	@Autowired // 서비스 의존성 주입(DI)
	private MyPageService service;
	
	/** 마이 페이지(내 정보) 페이지로 화면 전환
	 * @param loginMember : 세션에 저장된 로그인한 회원 정보
	 * @return model : 데이터 전달용 객체(request), 전달 받은 String을 반환하는 객체
	 */
	@GetMapping("info")
	public String info(@SessionAttribute("loginMember") Member loginMember, Model model) {
		
		// 로그인 회원 정보에 주소가 있을 경우
		if(loginMember.getMemberAddress() != null) {
			// 주소를 "," 기준으로 쪼개서 String[] 형태로 반환
			String[] arr = loginMember.getMemberAddress().split(",");
			// "04540,서울 중구 남대문로 120,2층" -> {"04540", "서울 중구 남대문로 120", "2층"}
			model.addAttribute("postcode", arr[0]);
			model.addAttribute("address", arr[1]);
			model.addAttribute("detailAddress", arr[2]);
		}
		
		return "myPage/myPage-info";
	}
	
	/** 내 정보 수정
	 * @param inputMember : 수정할 닉네임, 전화번호, 주소
	 * @param loginMember : 현재 로그인 된 회원 정보, session에 저장된 Member 객체의 주소 반환됨
	 * 										== session에 저장된 MEmber 객체의 데이터를 수정할 수 있음
	 * @param ra : 리다이렉트 시 request scope로 값 전달
	 * @return
	 */
	@PostMapping("info")
	public String updateInfo(@ModelAttribute Member inputMember,
													@SessionAttribute("loginMember") Member loginMember,
													RedirectAttributes ra) {
		/* @SessionAttribute("key")
		 * - @SessionAttribute"s"를 통해 session에 올라간 값을 얻어오는 어노테이션
		 * 사용 방법 1) 클래스 위에 @SessionAttribute"s" 어노테이션을 작성하고
		 * 해당 클래스에서 꺼내 사용할 값의 key를 작성 -> 세션에서 값을 미리 얻어와 놓음
		 * 사용방법 2) 필요한 메서드 매개변수에 @SessionAttribute("key")를 작성하면
		 * 해당 key와 일치하는 session 값을 얻어와서 대입
		 */
		
		// 1. inputMember에 로그인된 회원 번호를 추가
		int memberNo = loginMember.getMemberNo();
		inputMember.setMemberNo(memberNo); // 회원 번호, 닉네임, 전화번호, 주소가 들어가있음
		
		// 2. 회원 정보 수정 서비스 호출 후 결과 반환 받기
		int result = service.updateInfo(inputMember);
		
		// 3. 수정 결과에 따라 message 지정
		String message = null;
		if(result > 0) {
			message = "수정이 되었습니다.";
			
			// 4. 수정 성공 시 session에 저장된 로그인 회원 정보를 수정 값으로 변경해서 DB와 같은 데이터를 가지게함
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());
		}	else message = "수정에 실패하였습니다.";
		
		ra.addFlashAttribute("message", message);
		// -> footer.html 조각에서 slert() 수행
		
		return "redirect:info"; // /myPage/info GET 방식 요청(상대경로로 작성)
	}
	/** (비동기) 닉네임 중복 검사
	 * @param input
	 * @return 0 : 중복X / 1 : 중복O
	 */
	@ResponseBody // 응답 본문(ajax 코드)에 값 그대로 반환
	@GetMapping("checkNickname")
	public int checkNickname(@RequestParam("input") String input) {
		return service.checkNickname(input);
	}
	
}
