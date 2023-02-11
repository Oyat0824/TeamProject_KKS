package com.kks.work.project.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class ShoppingCart {
	private int cartId; 
	private int productId;
	private String productName;
	private int productPrice; 
	private String productCategory; 
	private int productCount; 
	private int memberId;  
	
}

