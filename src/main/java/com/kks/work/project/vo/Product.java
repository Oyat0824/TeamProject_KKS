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
	private int productDlvy;
	private String productCourier;
	private String productCourierCode;
	private String productDlvyPrice;
	private String productBody;
	private int storeId;
	private boolean delProductStatus;
	private String delProductDate;
	
	private int ROWNUM;
	private String storeName;
	private String categoryName;
	
	public String getForPrintBody() {
		return this.productBody.replaceAll("\n", "<br />");
	}
	
	public String delStatusStr() {
		if(delProductStatus == false) {
			return "미삭제";
		}
		return "삭제";
	}

	public String delDateStr() {
		if(delProductDate == null) {
			return "없음";
		}
		return delProductDate.substring(2, 16);
	}
	
}
