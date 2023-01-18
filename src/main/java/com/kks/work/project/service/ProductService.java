package com.kks.work.project.service;

import java.util.List;

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
	public ResultData<Integer> registerProduct(String p_Name, String p_Price, String p_Cetegory, String p_Stock,
			String p_Img, String p_Desc, int storeId) {
		// 상품 중복 신청 금지
		Product existsProduct = getProductByStoreId(storeId);
		
			if (existsProduct != null) {
				return ResultData.from("F-1", "가게 신청은 하나만 가능합니다.");
			}
				
			// 상품 이름 중복 체크
			existsProduct = getProductByProductName(p_Name);
			if (existsProduct != null) {
				return ResultData.from("F-2", Utility.f("이미 사용중인 가게 이름(%s)입니다.", p_Name));
			}
				
		productRepository.registerProduct(p_Name, p_Price, p_Cetegory, p_Stock, p_Img, p_Desc);
				
		int id = productRepository.getLastInsertId();
				
		return ResultData.from("S-1", Utility.f("%d번 상품이 등록되었습니다.", id), "id", id);
	}
	
	// storeId로 상품 정보 가져오기
	private Product getProductByStoreId(int storeId) {
		return productRepository.getProductByStoreId(storeId);
	}
	
	// 상품 이름으로 상품 정보 가져오기
	public Product getProductByProductName(String p_Name) {
		return productRepository.getProductByProductNam(p_Name);
	}

	// 상품 수 카운트
	public int getProductsCount(String authLevel, String searchKeywordTypeCode, String searchKeyword) {
		return productRepository.getProductsCount(authLevel, searchKeywordTypeCode, searchKeyword); 
	}

	// 페이징 처리
	public List<Product> getProducts(String authLevel, String searchKeywordTypeCode, String searchKeyword, int itemsInAPage, int page) {
		int limitStart = (page - 1) * itemsInAPage;

		return productRepository.getProducts(authLevel, searchKeywordTypeCode, searchKeyword, limitStart, itemsInAPage);
	}
	
}