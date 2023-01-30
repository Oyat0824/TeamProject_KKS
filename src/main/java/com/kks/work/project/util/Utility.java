package com.kks.work.project.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility {
	/**
	 * 인자로 받은 값이 NULL인지 비어있는지 확인합니다.
	 * NULL이 아닌 경우 문자열, List, Map 인지 확인합니다.
	 * 문자열인 경우 길이가 0인지 확인합니다.
	 * List, Map 인 경우 비어있는지 확인합니다.
	 * 
	 * @param data {@link Object}
	 * @return 비어있다면 true, 들어있다면 false
	 */	
	public static boolean isEmpty(Object data) {
		if (data == null) {
			return true;
		}

		if (data instanceof String) {
			String strData = (String) data;

			return strData.trim().length() == 0;
		} else if (data instanceof List) {
			List listData = (List) data;

			return listData.isEmpty();
		} else if (data instanceof Map) {
			Map mapData = (Map) data;

			return mapData.isEmpty();
		}

		return false;
	}
	
	/**
	 * isEmpty 메서드를 사용해 data가 비어있는지 확인 후 defaultValue을 반환합니다.
	 * data가 비어 있지 않으면 원래 data를 반환합니다.
	 * 
	 * @param data 확인할 데이터
	 * @param defaultValue 데이터가 비어있다면 반환할 값
	 * @return 비어 있다면 defaultValue, 비어 있지 않다면 data
	 */	
	public static <T> T ifEmpty(T data, T defaultValue) {
		if (isEmpty(data)) {
			return defaultValue;
		}

		return data;
	}
	
	/**
	 * data가 null인지 확인 후 null이 아니면 data를 반환합니다.
	 * null이면 defaultValue를 반환합니다.
	 * 
	 * @param data 확인할 데이터
	 * @param defaultValue 데이터가 null인 경우 반환할 값
	 * @return 비어 있다면 defaultValue, 비어 있지 않다면 data
	 */	
	public static <T> T ifNull(T data, T defaultValue) {
		return data != null ? data : defaultValue;
	}

	/**
	 * attrName의 속성값이 null인지 확인 후 null이 아니면 속성값을 반환합니다.
	 * null이면 defaultValue를 반환합니다.
	 * 
	 * @param attrName 속성 이름
	 * @param defaultValue 데이터가 null인 경우 반환할 값
	 * @return 비어 있다면 defaultValue, 비어 있지 않다면 data
	 */	
	public static <T> T reqAttr(HttpServletRequest req, String attrName, T defaultValue) {
		return (T) ifNull(req.getAttribute(attrName), defaultValue);
	}
	
	/**
	 * 전달된 개체가 BigInteger, Double, Float, Long, Integer, String 인 경우 int 로 변환 후 반환합니다.
	 * 개체가 이러한 유형이 아닌 경우 두번째 인수로 전달된 defaultValue를 반환합니다.
	 * 
	 * @param object 변환할 인자
	 * @param defaultValue 해당 유형이 아니라면 반환할 값
	 * @return 숫자 유형이라면 int 형변환 후 반환 아니라면, defaultValue 반환
	 */	
	public static int getAsInt(Object object, int defaultValue) {
		if (object instanceof BigInteger) {
			
			return ((BigInteger) object).intValue();
		} else if (object instanceof Double) {
			
			return (int) Math.floor((double) object);
		} else if (object instanceof Float) {
			
			return (int) Math.floor((float) object);
		} else if (object instanceof Long) {
			
			return (int) object;
		} else if (object instanceof Integer) {
			
			return (int) object;
		} else if (object instanceof String) {
			
			return Integer.parseInt((String) object);
		}

		return defaultValue;
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
	 * SHA256 암호화
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
	
	/**
	 * 인자로 받은 객체로 {@link LinkedHashMap}를 생성합니다.
	 * 
	 * @param args map에서 키-값 쌍으로 사용할 객체<br />
	 * 인자로 전달되는 객체의 개수는 짝수여야합니다.<br />
	 * 짝수번째 객체 <키><br />
	 * 홀수번째 객체 <값><br />
	 * 
	 * @return 키-값 쌍을 포함하는 {@link LinkedHashMap}를 반환합니다.
	 * @throws IllegalArgumentException 객체 수가 짝수가 아닌 경우 
	 * @throws ClassCastException 키가 {@link String}이 아닌 경우
	 */
	public static Map<String, Object> mapOf(Object... args) {
		if (args.length % 2 != 0) {
			throw new IllegalArgumentException("인자를 <짝수>개 입력해주세요.");
		}

		int size = args.length / 2;

		Map<String, Object> map = new LinkedHashMap<>();

		for (int i = 0; i < size; i++) {
			int keyIndex = i * 2;
			int valueIndex = keyIndex + 1;

			String key;
			Object value;

			try {
				key = (String) args[keyIndex];
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("키는 String으로 입력해야 합니다. " + e.getMessage());
			}

			value = args[valueIndex];

			map.put(key, value);
		}

		return map;
	}
	
// 파일 관련
	/**
	 * fileName에 있는 . 뒤에 확장자를 추출합니다.
	 * 
	 * @param fileName 파일 이름
	 * 
	 * @return 파일 확장자를 반환합니다.
	 */
	public static String getFileExtFromFileName(String fileName) {
		int pos = fileName.lastIndexOf(".");
		String ext = fileName.substring(pos + 1);

		return ext;
	}
	
	/**
	 * fileName을 통해 파일 유형을 결정합니다.
	 * 
	 * @param fileName 파일 이름
	 * 
	 * @return 파일 유형을 반환합니다.
	 */
	public static String getFileExtTypeCodeFromFileName(String fileName) {
		String ext = getFileExtFromFileName(fileName).toLowerCase();

		switch (ext) {
		case "jpeg":
		case "jpg":
		case "gif":
		case "png":
			return "img";
		case "mp4":
		case "avi":
		case "mov":
			return "video";
		case "mp3":
			return "audio";
		}

		return "etc";
	}

	/**
	 * fileName을 통해 파일 확장자를 가져옵니다.
	 * 
	 * @param fileName 파일 이름
	 * 
	 * @return 파일 확장자를 반환합니다.
	 */
	public static String getFileExtType2CodeFromFileName(String fileName) {
		String ext = getFileExtFromFileName(fileName).toLowerCase();

		switch (ext) {
		case "jpeg":
		case "jpg":
			return "jpg";
		case "gif":
			return ext;
		case "png":
			return ext;
		case "mp4":
			return ext;
		case "mov":
			return ext;
		case "avi":
			return ext;
		case "mp3":
			return ext;
		}

		return "etc";
	}
	
	/**
	 * filePath를 통해 경로에 파일이 있다면 파일을 삭제합니다.
	 * 
	 * @param filePath 파일 경로
	 * 
	 * @return 파일이 없으면 true를 반환합니다.
	 */
	public static boolean deleteFile(String filePath) {
		java.io.File ioFile = new java.io.File(filePath);
		if (ioFile.exists()) {
			return ioFile.delete();
		}

		return true;
	}
	
	/**
	 * @return 현재 시간을 yyyy_MM 형태로 출력합니다.
	 */
	public static String getNowYearMonthDateStr() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");

		String dateStr = dateFormat.format(System.currentTimeMillis());

		return dateStr;
	}
	
	/**
	 * 문자열을 정수 목록으로 변환합니다.
	 * 
	 * @param str 변환할 문자열
	 * @param divideBy 문자열 구분 기호
	 * 
	 * @return 변환된 정수 목록
	 */
	public static List<Integer> getListDividedBy(String str, String divideBy) {
		return Arrays.asList(str.split(divideBy))
					 .stream().map(s -> Integer.parseInt(s.trim()))
				 	 .collect(Collectors.toList());
	}
	
/////////////// 사용 안함
	// JSON 관련
	public static String toJsonStr(Object obj) {
	    ObjectMapper mapper = new ObjectMapper();
	    try {
	        if (obj instanceof Map) {
	            return mapper.writeValueAsString((Map<String, Object>) obj);
	        } else {
	            return mapper.writeValueAsString(obj);
	        }
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	    }

	    return "";
	}
	
	public static <T> T fromJsonStr(String jsonStr, Class<T> cls) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return (T) mapper.readValue(jsonStr, cls);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 숫자 포맷 방식
	public static String numberFormat(int num) {
		DecimalFormat df = new DecimalFormat("###,###,###");

		return df.format(num);
	}

	public static String numberFormat(String numStr) {
		return numberFormat(Integer.parseInt(numStr));
	}

	// 문자열에 숫자가 들어있는지 확인, 하나라도 숫자가 아니라면 false 반환
	public static boolean allNumberString(String str) {
		if (str == null) {
			return false;
		}

		if (str.length() == 0) {
			return true;
		}

		for (int i = 0; i < str.length(); i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}

		return true;
	}

	// 첫번째 문자가 숫자인지 확인
	public static boolean startsWithNumberString(String str) {
		if (str == null) {
			return false;
		}

		if (str.length() == 0) {
			return false;
		}

		return Character.isDigit(str.charAt(0));
	}
	
	// 아이디 길이 조건 체크 (정규식 사용)
	public static boolean isStandardLoginIdString(String str) {
		if (str == null) {
			return false;
		}

		if (str.length() == 0) {
			return false;
		}

		// 조건
		// 5자 이상, 20자 이하로 구성
		// 숫자로 시작 금지
		// _, 알파벳, 숫자로만 구성
		return Pattern.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,19}$", str);
	}
	
	// uri 에 파라미터를 추가합니다.
	public static String getNewUri(String uri, String paramName, String paramValue) {
		uri = getNewUriRemoved(uri, paramName);

		if (uri.contains("?")) {
			uri += "&" + paramName + "=" + paramValue;
		} else {
			uri += "?" + paramName + "=" + paramValue;
		}

		uri = uri.replace("?&", "?");

		return uri;
	}
	
	// uri 에 파라미터를 제거합니다.
	public static String getNewUriRemoved(String uri, String paramName) {
		String deleteStrStarts = paramName + "=";
		int delStartPos = uri.indexOf(deleteStrStarts);

		if (delStartPos != -1) {
			int delEndPos = uri.indexOf("&", delStartPos);

			if (delEndPos != -1) {
				delEndPos++;
				uri = uri.substring(0, delStartPos) + uri.substring(delEndPos, uri.length());
			} else {
				uri = uri.substring(0, delStartPos);
			}
		}

		if (uri.charAt(uri.length() - 1) == '?') {
			uri = uri.substring(0, uri.length() - 1);
		}

		if (uri.charAt(uri.length() - 1) == '&') {
			uri = uri.substring(0, uri.length() - 1);
		}

		return uri;
	}
	
}
