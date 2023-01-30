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
	
	private boolean actorCanChangeData;
	private String sellerName;
	
	public String getForPrintDesc() {
		return this.storeDesc.replaceAll("\n", "<br />");
	}
}
