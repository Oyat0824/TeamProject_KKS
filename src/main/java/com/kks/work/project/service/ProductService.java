package com.kks.work.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.ProductRepository;
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
	public int registerProduct(String productName, String productPrice, String productCategory,
			String productStock, int productDlvy, String productCourier, String productDlvyPrice, String productBody, int storeId) {
		productRepository.registerProduct(productName, productPrice, productCategory, productStock, productDlvy, productCourier, productDlvyPrice, productBody, storeId);
		
		int id = productRepository.getLastInsertId();
				
		return id;
	}
	
	// 상품 상세보기
	public Product getProductByStoreIdAndId(int storeId, int id) {
		Product product = productRepository.getProductByStoreIdAndId(storeId, id);
		
		return product;
	}
	
	// 자신의 스토어 상품 목록, 개수 구하기
	public int getMyStoreProductsCount(int id, String searchKeyword) {
		return productRepository.getMyStoreProductsCount(id, searchKeyword);
	}
	
	// 상품 목록, 개수 구하기
	public int getProductsCount(String searchKeyword) {
		return productRepository.getProductsCount(searchKeyword);
	}

	// 판매자 입장에서의 상품관리를 위한 상품 리스트 
	public List<Product> getProducts(int id, String searchKeyword, int itemsInAPage, int page) {
		int limitStart = (page - 1) * itemsInAPage;
		
		return productRepository.getProducts(id, searchKeyword, itemsInAPage, limitStart);
	}
	
	// 유저입장에서 보는 상품 리스트
	public List<Product> getExposureProducts(String searchKeyword, int itemsInAPage, int page) {
		int limitStart = (page - 1) * itemsInAPage;
		
		return productRepository.getExposureProducts(searchKeyword, itemsInAPage, limitStart);
	}

	// 상품 가져오기
	public Product getProduct(int id) {
		return productRepository.getProduct(id);
	}

	// 상품 수정
	public void doModify(int id, String productName, String productPrice, String productCategory, String productStock,
			int productDlvy, String productCourier, String productDlvyPrice, String productBody) {
		productRepository.doModify(id, productName, productPrice, productCategory, productStock, productDlvy, productCourier, productDlvyPrice, productBody);
	}
	
	// 등록된 상품 삭제
	public void deleteProduct(int id) {
		productRepository.deleteProduct(id);	
	}

	// 상품 구매
	public void buyProduct(int id, int storeId, int memberId, String impUID) {
		productRepository.buyProduct(id, storeId, memberId, impUID);
	}

}