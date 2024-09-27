package edu.kh.project.common.util;

import java.text.SimpleDateFormat;

public class FileUtil {
	
	// 파일명 뒤에 붙는 숫자(1~99999 반복)
	public static int seqNum = 1;
	
	/** 전달 받은 원본 파일명을 시간 형태의 파일명으로 변경
	 * apple.jag -> 20240927093712_00001.jpg
	 * @param originalFileName
	 * @return
	 */
	public static String rename(String originalFileName) {
		
		// 1) 확장자 추출(.jpg)
		
		// 원본 파일명 뒤에서부터 검색해서 처음 찾은 "."의 index
		int index = originalFileName.lastIndexOf(".");
		
		// 원본 파일명 "." 부터 끝까지 잘라낸 문자열 == .확장자
		String ext = originalFileName.substring(index);
		
		// 2) 현재 시간을 "yyyyMMddHHmmss" 형태의 문자열로 반환받기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		// new java.util.Date() : 혀냊 시간을 저장한 객체 생성
		String time = sdf.format(new java.util.Date());
		
		// 3) seqNum을 이용한 숫자 생성
		String number = String.format("%05d", seqNum); // %05d : 정수가 들어갈 5칸 짜리 오른쪽 정렬 패턴, 빈 칸엔 0을 채움
		seqNum++; // 1 증가
		if(seqNum == 100000) seqNum = 1; // 1~99999 반복
		// 20240927094732_00001.jpg
		return time + "_" + number + ext;
	}
}
