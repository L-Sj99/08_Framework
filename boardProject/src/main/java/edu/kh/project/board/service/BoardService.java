package edu.kh.project.board.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.Comment;

public interface BoardService {

	/** 게시글 목록 조회
	 * @param boardCode
	 * @param cp
	 * @return map
	 */
	Map<String, Object> selectBoardList(int boardCode, int cp);

	/** 게시글 상세 조회
	 * @param map
	 * @return board
	 */
	Board selectDetail(Map<String, Integer> map);

	/** 게시글 조회수 1증가
	 * @param boardNo
	 * @return result
	 */
	int updateReadCount(int boardNo);

	/** 게시글 좋아요
	 * @param boardNo
	 * @param memberNo
	 * @return map
	 */
	Map<String, Object> boardLike(int boardNo, int memberNo);

	/** DB에서 모든 게시판 종류를 조회
	 * @return
	 */
	List<Map<String, String>> selectBoardTypeList();

	/** 댓글 목록 조회
	 * @param boardNo
	 * @return commentList
	 */
	List<Comment> selectCommentList(int boardNo);

}