package edu.kh.project.sse.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.sse.dto.Notification;

@Mapper
public interface SseMapper {

	/** 알림 삽입 후, 알림 받을 회원 번호 + 알림 개수 반환
	 * @param notification
	 * @return
	 */
	int insertNotification(Notification notification);

	/** 알림을 받아야 하는 회원번호 + 안읽은 알람 개수 조회
	 * @param notificationNo
	 * @return
	 */
	Map<String, Object> selectReceiveMember(int notificationNo);

	/** 로그인한 회원의 알림 목록 조회
	 * @param memberNo
	 * @return list
	 */
	List<Notification> selectNotificationList(int memberNo);
	
}
