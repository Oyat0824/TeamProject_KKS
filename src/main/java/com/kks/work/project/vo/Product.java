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
	private String productPrice;
	private String productCategory;
	private String productStock;
	private String productBody;
	private int storeId;
	
	private int ROWNUM;
	private boolean actorCanChangeData;
	private String storeName;
	private String categoryName;
	
	public String getForPrintBody() {
		return this.productBody.replaceAll("\n", "<br />");
	}
	
}
