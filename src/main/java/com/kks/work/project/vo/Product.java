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
	private String productName;
	private String productCetegory;
	private String productStock;
	private String productImg;
	private String productBody;
	private int stroeId;
}
