package com.kks.work.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class PurchaseList{
	private int id;
	private String regDate;
	private String updateDate;
	private int productId;
	private int productCnt;
	private int storeId;
	private int memberId;
	private String impUID;
	private String orderNum;
	private String name;
	private String cellphoneNum;
	private String cellphoneNum2;
	private String zipNo;
	private String roadAddr;
	private String addrDetail;
	private String dlvyMemo;
	private String waybill;
	private int ordCheck;
	private int confirm;
	private int cancel;
	
	private int ROWNUM;
	
	// 상품 정보
	private String productName;
	private String productPrice;
	private int productDlvy;
	private String productDlvyPrice;
	
	// 스토어 정보
	private String storeName;
	
	// 멤버 정보
	private String sellerTel;
}
