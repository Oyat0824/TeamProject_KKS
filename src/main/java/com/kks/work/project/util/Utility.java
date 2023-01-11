package com.kks.work.project.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;

public class Utility {
	/**
	 * 매개변수로 받은 값이 비어있는지 체크합니다.
	 * 
	 * @param obj 값이 비어있는지 확인할 변수
	 * @return 비어있다면 true, 들어있다면 false
	 */
	public static boolean empty(Object obj) {
		// 매개 변수 값이 NULL 이라면
		if (obj == null) return true;

		// String 으로 형 변환
		String str = (String) obj;

		return str.trim().length() == 0;
	}

	/**
	 * 문자열을 포맷 후 리턴합니다.
	 * 
	 * @param format 출력타입을 포함한 문자열을 적습니다. ( "안녕하세요, %d" )
	 * @param args   출력타입 형태에 맞는 값을 넣습니다.
	 * 
	 * @return 출력타입에 맞춰 합쳐진 문자열이 리턴됩니다.
	 */
	public static String f(String format, Object... args) {
		return String.format(format, args);
	}

	/**
	 * 메시지가 있다면 메시지를 보여주고 페이지를 뒤로 이동시킵니다.<br />
	 * 메시지가 없다면 메시지는 보여주지 않고 뒤로 이동시킵니다.
	 * 
	 * @param msg 알림창에 보여줄 메시지
	 * 
	 */
	public static String jsHistoryBack(String msg) {
		if (msg == null) msg = "";

		return Utility.f("""
				<script>
					const msg = "%s".trim();

					if(msg.length > 0) {
						alert(msg);
					}

					history.back();
				</script>
				""", msg);
	}

	/**
	 * 메시지가 있다면 메시지를 보여주고 페이지를 이동시킵니다.<br />
	 * 메시지가 없다면 메시지는 보여주지 않고 이동시킵니다.
	 * 
	 * @param msg 알림창에 보여줄 메시지
	 * @param uri 이동시킬 페이지 주소
	 */
	public static String jsReplace(String msg, String uri) {
		if (msg == null) msg = "";
		if (uri == null) uri = "";

		return Utility.f("""
				<script>
					const msg = "%s".trim();

					if(msg.length > 0) {
						alert(msg);
					}

					location.replace("%s");
				</script>
				""", msg, uri);
	}

	/**
	 * 지정된 시간 이후의 시간 표기
	 * 
	 * @param seconds 초
	 * 
	 * @return seconds가 계산 된 이후의 시간
	 */
	public static String getDateStrLater(long seconds) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String dateStr = format.format(System.currentTimeMillis() + seconds * 1000);

		return dateStr;
	}

	/**
	 * 무작위 문자열 생성
	 * 
	 * @param length 문자열 길이
	 * 
	 * @return 문자열 길이에 맞춰서 나온 무작위 문자열
	 */
	public static String getTempPassword(int length) {
		// Number : 0
		int leftLimit = 48;
		// Letter : z
		int rightLimit = 122;

		// 랜덤 객체 생성
		SecureRandom sRandom = new SecureRandom();
		
		// 랜덤 난수 생성
		String generatedString = sRandom.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(length)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		
		return generatedString;
	}

	/**
	 * SHA256 암호화 + 무작위 난수
	 * 
	 * @param base 암호화할 문자열
	 * @param salt 무작위 난수 값
	 * 
	 * @return SHA256 형식에 더해 무작위 난수를 뿌려 보안에 더 강화된 난수 값 출력
	 */
	public static String getEncrypt(String base, String salt) {
		String result = "";
		
		try {
			// 1. SHA256 알고리즘 객체 생성
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			// 2. base와 salt 합친 문자열에 SHA256 적용
			md.update((base + salt).getBytes("UTF-8"));
			byte[] pwdSalt = md.digest();
			
			// 3. byte To String (10진수의 문자열로 변경)
			StringBuffer sb = new StringBuffer();
			for (byte b : pwdSalt) {
				sb.append(String.format("%02x", b));
			}
			
			result = sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
