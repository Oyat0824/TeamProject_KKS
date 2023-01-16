package com.kks.work.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Product{
	private int id;
	private String regDate;
	private String updateDate;
	private String p_Name;
	private String p_Cetegory;
	private String p_Stock;
	private String p_Img;
	private String p_Desc;
	private int stroeId;
}
