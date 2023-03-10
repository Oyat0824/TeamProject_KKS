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
	private String storeDesc;
	private int memberId;
	private boolean delStoreStatus;
	private String delStoreDate;
	
	private String sellerName;
	private int reviewCnt;
	private int purchaseCnt;
	
	public String getForPrintDesc() {
		return this.storeDesc.replaceAll("\n", "<br />");
	}
	
	public String delStatusStr() {
		if(delStoreStatus == false) {
			return "미삭제";
		}
		return "삭제";
	}

	public String delDateStr() {
		if(delStoreDate == null) {
			return "없음";
		}
		return delStoreDate.substring(2, 16);
	}
	
}
