package com.kks.work.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attr {
	private int id;
	private String regDate;
	private String updateDate;
	private String expireDate;
	private String relTypeCode;
	private int relId;
	private String typeCode;
	private String type2Code;
	private String value;
}
