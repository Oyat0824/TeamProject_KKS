package com.kks.work.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.ProductRepository;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Product;

@Service
public class ProductService {
	private ProductRepository productRepository;
	
	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
// 서비스 메서드

	// 상품 등록
	public ResultData<Integer> registerProduct(String productName, String productPrice, String productCetegory, String productStock,
			String productImg, String productBody, int storeId) {
		// 상품 중복 신청 금지
		Product existsProduct = getProductByStoreId(storeId);
		
			if (existsProduct != null) {
				return ResultData.from("F-1", "가게 신청은 하나만 가능합니다.");
			}
				
			// 상품 이름 중복 체크
			existsProduct = getProductByProductName(productName);
			if (existsProduct != null) {
				return ResultData.from("F-2", Utility.f("이미 사용중인 가게 이름(%s)입니다.", productName));
			}
				
		productRepository.registerProduct(productName, productPrice, productCetegory, productStock, productImg, productBody);
				
		int id = productRepository.getLastInsertId();
				
		return ResultData.from("S-1", Utility.f("%d번 상품이 등록되었습니다.", id), "id", id);
	}
	
	// storeId로 상품 정보 가져오기
	private Product getProductByStoreId(int storeId) {
		return productRepository.getProductByStoreId(storeId);
	}
	
	// 상품 이름으로 상품 정보 가져오기
	public Product getProductByProductName(String productName) {
		return productRepository.getProductByProductNam(productName);
	}
	
}