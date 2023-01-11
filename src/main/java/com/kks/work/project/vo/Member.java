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
	private LocalDateTime regDate;
	private LocalDateTime updateDate;
	private String loginId;
	private String loginPw;
	private String name;
	private int gender;
	private String birthday;
	private String cellphoneNum;
	private String email;
	private int memberType;
	private String delStatus;
	private LocalDateTime delDate;
	private String salt;

}
