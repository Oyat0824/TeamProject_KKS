package com.kks.work.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class purchaseList{
	private int id;
	private String regDate;
	private String updateDate;
	private int productId;
	private int storeId;
	private int memberId;
	private String impUID;
}
