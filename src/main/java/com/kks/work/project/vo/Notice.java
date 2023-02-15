package com.kks.work.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
	private int id;
	private String regDate;
	private String updateDate;
	private int memberId;
	private String title;
	private String body;
	private String writerName;
	private int viewCount;
	private boolean actorCanChangeData;
	
	public String getForPrintBody() {
		return this.body.replaceAll("\n", "<br> ");
	}

}
