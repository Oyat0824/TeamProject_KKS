package com.kks.work.project.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 데이터의 정보를 JSON으로 뽑아주는 클래스
 * */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultData<DT> {
	private String resultCode;
	private String msg;
	private String data1Name;
	private DT data1;
	private String data2Name;
	private DT data2;
	
	/**
	 * @param resultCode 성공/실패 코드
	 * @param msg 성공/실패에 따른 보여줄 메시지
	 * @param data1Name data1 의 이름
	 * @param data1 여러 값
	 * 
	 * @return 매개변수에 넣은 값을 토대로 나온 JSON
	 * 
	 * */
	public static <DT> ResultData<DT> from(String resultCode, String msg, String data1Name, DT data1) {
		ResultData<DT> rd = new ResultData<>();
		rd.resultCode = resultCode;
		rd.msg = msg;
		rd.data1Name = data1Name;
		rd.data1 = data1;
		
		return rd;
	}
	
	public static <DT> ResultData<DT> from(String resultCode, String msg) {
		return from(resultCode, msg, null, null);
	}
	
	public void setData2(String data2Name, DT data2) {
		this.data2Name = data2Name;
		this.data2 = data2;
	}
	
	/**
	 * ResultData 성공
	 * */
	public boolean isSuccess() {
		return this.resultCode.startsWith("S-");
	}
	
	/**
	 * ResultData 실패
	 * */
	public boolean isFail() {
		return isSuccess() == false;
	}
}
