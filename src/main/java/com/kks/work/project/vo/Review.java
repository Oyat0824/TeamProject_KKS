package com.kks.work.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class Review{
	private int id;
	private String regDate;
	private String updateDate;
	private int storeId;
	private int productId;
	private int memberId;
	private int rating;
	private String reviewBody;
	
	private String name;
	
	public String getForPrintReviewBody() {
		return this.reviewBody.replaceAll("\n", "<br />");
	}
}
