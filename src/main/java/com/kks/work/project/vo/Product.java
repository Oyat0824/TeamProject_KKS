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
	private String productBody;
	private int stroeId;
	
	private boolean actorCanChangeData;
	private String sellerName;
	
	public String setActorCanChangeData(boolean success) {
		return this.productBody.replaceAll("\n", "<br />");	
	}
	
}
