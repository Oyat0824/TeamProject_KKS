package com.kks.work.project.vo;

import java.time.LocalDateTime;

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
	private String name;
	private String gender;
	private String birthday;
	private String email;
	private String cellphoneNum;
	private int memberType;
	private int storeState;
	private String salt;
	private String delStatus;
	private LocalDateTime delDate;
	
	private int storeId;
}
