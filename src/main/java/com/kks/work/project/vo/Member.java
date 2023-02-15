package com.kks.work.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Member{
	private int id;
	private String regDate;
	private String updateDate;
	private String loginId;
	private String loginPw;
	private String salt;
	private String name;
	private String zipNo;
	private String roadAddr;
	private String addrDetail;
	private String email;
	private String cellphoneNum;
	private String gender;
	private String birthday;
	private int memberType;
	private int storeState;
	private boolean delStatus;
	private int delStatus;
	private String delDate;
	
	private int storeId;
	
	public String delStatusStr() {
		if(delStatus == false) {
			return "미삭제";
		}
		return "삭제";
	}

	public String delDateStr() {
		if(delDate == null) {
			return "없음";
		}
		return delDate.substring(2, 16);
	}
	
}
