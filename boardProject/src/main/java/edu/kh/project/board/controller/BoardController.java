package edu.kh.project.board.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.Pagination;
import edu.kh.project.board.service.BoardService;
import edu.kh.project.member.dto.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

	private final BoardService service;
	
	/** 게시글 목록 조회
	 * @param boardCode : 게시판 종류 번호
	 * @param cp : 현재 조회하려는 목록의 페이지 번호(필수 아님, 없으면 1)
	 * @param model : forward 시 데이터 전달하는 용도의 객체(request)
	 */
	@GetMapping("{boardCode:[0-9]+}")
	public String selectBoardList(
		@PathVariable("boardCode") int boardCode,
		@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model) {
		
		// 서비스 호출 후 결과 반환 받기
		// - 목록 조회인데 Map으로 반환 받는 이유?
		//  -> 서비스에서 여러 결과를 만들어 내야되는데 메서드는 반환을 1개만 할 수 있기 때문에 Map으로 묶어서 반환 받을 예정
		Map<String, Object> map = service.selectBoardList(boardCode, cp);
		
		// map에 묶여있는 값 풀어놓기
		List<Board> boardList = (List<Board>)map.get("boardList");
		Pagination pagination = (Pagination)map.get("pagination");
		
		// 정상 조회 되었는지 log 확인
//		for(Board b : boardList) log.debug(b.toString());
//		log.debug(pagination.toString());
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("pagination", pagination);
		return "board/boardList";
	}
	
/** 게시글 상세 조회
 * @param boardCode : 게시판 종류
 * @param boardNo : 게시글 번호
 * @param model : forward 시 request scope 값 전달 객체
 * @param ra : redirect 시 request scope 값 전달 객체
 * @param loginMember : 로그인 한 회원 정보, 로그인 안되어 있으면 null
 * @param req : 요청 관련 데이터를 담고 있는 객체(쿠키 포함)
 * @param resp : 응답 방법을 담고 있는 객체(쿠키 생성, 쿠키를 클라이언트에게 전달)
 * @return
 * @throws ParseException 
 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(@PathVariable("boardCode") int boardCode, @PathVariable("boardNo") int boardNo,
														Model model, RedirectAttributes ra, 
														@SessionAttribute(value="loginMember", required=false) Member loginMember, 
														HttpServletRequest req, HttpServletResponse resp) throws ParseException {
		
		// 1) SQL 수행에 필요한 파라미터들 Map으로 묶기
		Map<String, Integer> map = new HashMap<>();
			map.put("boardCode", boardCode);
			map.put("boardNo", boardNo);
			
			/* 로그인이 되어있는 경우 memberNo를 map에 추가 */
			if(loginMember != null) {
				map.put("memberNo", loginMember.getMemberNo());
			}
		
		// 2) 서비스 호출 후 반환 받기
		Board board = service.selectDetail(map);
		
		/* 게시글 상세 조회 결과가 없을 경우 */
		if(board == null) {
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
			return "redirect:/board/" + boardCode;
		}
		
		//------------------------------------------------------------------------------------------		
		/* 조회 수 증가 코드 */
		
		// 로그인 한 회원이 작성한 글이 아닌 경우 + 비회원
		if(loginMember == null || loginMember.getMemberNo() != board.getMemberNo()) {
			// 요청에 담겨있는 모든 쿠키 얻어오기
			Cookie[] cookies = null;			
			Cookie c = null;
			if (req.getCookies() != null) {
				cookies = req.getCookies();

				// Cookie는 Map 형식(name=value)
				for (Cookie temp : cookies) {
					// 클라이언트로부터 전달 받은 쿠키에 "readBoardNo"라는 key(name)가 존재하는 경우
					// == 기존에 읽은 게시글 번호 저장한 쿠키가 있는 경우
					if (temp.getName().equals("readBoardNo")) {
						c = temp;
						break;
					}
				}
			}
			int result = 0; // service 호출 결과 저장
			// 이전에 "readBoardNo"라는 name을 가지는 쿠키가 없을 경우
			if(c == null) {
				// 새 쿠키 생성 : readBoardNo=[1000][2000][3000]
				c = new Cookie("readBoardNo", "[" + boardNo + "]");
				
				/* DB에서 해당 게시글의 조회수를 1증가 시키는 서비스 호출 */
				result = service.updateReadCount(boardNo);
			} else { // 이 전에 "readBoardNo"라는 name을 가지는 쿠키가 있을 경우
				// readBoardNo=[1000][2000][3000] 모양의 형태로 생성
				
				// 현재 읽은 게시글 번호가 쿠키에 없다면 == 해당 글은 오늘 처음 읽음
				if(c.getValue().contains(boardNo + "") == false ) {
					c.setValue(c.getValue() + "[" + boardNo + "]");
					
					// DB에서 조회수 증가
					result = service.updateReadCount(boardNo);
				}
			}
			// 조회수가 증가된 경우 쿠키 세팅하기
			if(result > 0) {
				// 미리 조회된 조회수와 DB 조회 수 동기화
				board.setBoardCode(board.getReadCount() + 1);
				
				// 읽은 글 번호가 저장된 쿠키(c)가 어떤 주소 요청 시 서버로 전달될지 지정
				c.setPath("/"); // "/" 이하 모든 요청에 쿠키가 포함됨
				
				/* 쿠키의 수명 지정
				 * - 00시 00분 00초가 되면 삭제 == 오늘 23시 59분 59초까지 유지 
				 * - 다음날 00시 00분 00초까지 남은 시간을 계산해서 쿠키에 세팅
				 */
				// Calendar 객체 : 시간을 저장하는 객체
				// Calendar.getInstance() : 현재 시간이 저장된 객체가 반환 됨
				Calendar cal = Calendar.getInstance();
				
				// ex) 2024-10-08 10:14:31 -> 2024-10-09 10:14:31
				cal.add(cal.DATE, 1); // 1일 더하기
//				cal.add(cal.MINUTE, 10); // 10분 단위
				
				// 날짜 데이터를 지정된 포맥의 문자열로 변경하는 객체
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				// java.util.Date import
				Date currentDay = new Date(); // 현재 시간(2024-10-08 10:14:31)
				
				// Calendar 객체 -> Date 타입으로 변환
				Date b = new Date(cal.getTimeInMillis()); // 내일(24시간 후 2024-10-09 10:14:31)
				
				// sdf.format(b) == "2024-10-09"
				// sdf.parse("2024-10-09") == 2024-10-09 00:00:00 Date로 변환
				Date nextDay = sdf.parse(sdf.format(b));
				
				// 다음 날 자정 - 현재 시간의 결과를 초 단위로 얻어오기
				// == 다음 날 0시 0분 0초까지 몇초 남았는지 계산
				long diff = (nextDay.getTime() - currentDay.getTime()) / 1000; // 초단위
				
				// 쿠키 수명 설정
				c.setMaxAge((int)diff);
				
				// 응답 객체에 쿠키 추가 후 응답 시 클라이언트에게 전달할 수 있게 함
				resp.addCookie(c);
			}
		}
		//------------------------------------------------------------------------------------------
		model.addAttribute("board", board);
		
		// 조회된 이미지 목록이 있을 경우
		if(board.getImageList().isEmpty() == false) {
			// 썸네일 X : 0 ~ 3 인덱스
			// 썸네일 O : 1 ~ 4 인덱스
			
			// for문 시작 인덱스 지정
			int start = 0;
			// 썸네일이 있을 경우
			// if(board.getImageList().get(0).getImgOrder() == 0) --> 도 사용 가능
			if(board.getThumbnail() != null) start = 1;
			model.addAttribute("start", start); // 0 또는 1
		}
		
		return "board/boardDetail";
	}
	
	/** 좋아요 체크, 해제
	 * @param boardNo
	 * @return map (check, clear / 좋아요 개수)
	 */
	@ResponseBody
	@PostMapping("like")
	public Map<String, Object> boardLike(@RequestBody int boardNo, @SessionAttribute("loginMember") Member loginMember) {
		int memberNo = loginMember.getMemberNo();
		return service.boardLike(boardNo, memberNo);
	}
	
	
}