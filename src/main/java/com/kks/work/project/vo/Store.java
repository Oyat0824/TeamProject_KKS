package com.kks.work.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Store{
	private int id;
	private String regDate;
	private String updateDate;
	private String storeName;
	private String storeLogo;
	private String storeImg;
	private String storeDesc;
	private int memberId;
}
