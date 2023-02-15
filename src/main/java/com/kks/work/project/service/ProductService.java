package com.kks.work.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.ProductRepository;
import com.kks.work.project.vo.Product;
import com.kks.work.project.vo.PurchaseList;
import com.kks.work.project.vo.Review;

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
			String productStock, int productDlvy, String productCourier, String productCourierCode, String productDlvyPrice, String productBody, int storeId) {
		productRepository.registerProduct(productName, productPrice, productCategory, productStock, productDlvy, productCourier, productCourierCode, productDlvyPrice, productBody, storeId);
		
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
	
	public int getStoreInProductsCount(String searchKeyword, int id) {
		return productRepository.getStoreInProductsCount(searchKeyword, id);
	}

	// 판매자 입장에서의 상품관리를 위한 상품 리스트 
	public List<Product> getProducts(int id, String searchKeyword, int itemsInAPage, int page) {
		int limitStart = (page - 1) * itemsInAPage;
		
		return productRepository.getProducts(id, searchKeyword, itemsInAPage, limitStart);
	}
	
	// 유저입장에서 보는 상품 리스트
	public List<Product> getExposureProducts(String searchKeyword, int itemsInAPage, int page, String listOrder) {
		int limitStart = (page - 1) * itemsInAPage;
		
		return productRepository.getExposureProducts(searchKeyword, itemsInAPage, limitStart, listOrder);
	}
	
	// 스토어에서 보는 상품 리스트
	public List<Product> getStoreInProducts(String searchKeyword, int itemsInAPage, int page, String listOrder, int cate) {
		int limitStart = (page - 1) * itemsInAPage;
		
		return productRepository.getStoreInProducts(searchKeyword, itemsInAPage, limitStart, listOrder, cate);
	}

	// 상품 가져오기
	public Product getProduct(int id) {
		return productRepository.getProduct(id);
	}

	// 상품 수정
	public void doModify(int id, String productName, String productPrice, String productCategory, String productStock,
			int productDlvy, String productCourier, String productCourierCode, String productDlvyPrice, String productBody) {
		productRepository.doModify(id, productName, productPrice, productCategory, productStock, productDlvy, productCourier, productCourierCode, productDlvyPrice, productBody);
	}
	
	// 등록된 상품 삭제
	public void deleteProduct(int id) {
		productRepository.deleteProduct(id);	
	}

	// 상품 구매
	public int buyProduct(int id, int productCnt, int storeId, int memberId, String impUID, String orderNum,
			String name, String cellphoneNum, String cellphoneNum2,
			String zipNo, String roadAddr, String addrDetail, String dlvyMemo) {
		productRepository.buyProduct(id, productCnt, storeId, memberId, impUID, orderNum, name, cellphoneNum, cellphoneNum2, zipNo, roadAddr, addrDetail, dlvyMemo);
		
		int buyId = productRepository.getLastInsertId();
		
		return buyId;
	}
	
	// 주문 목록, 개수 구하기 || 로그인 멤버
	public int getPurchaseCount(String searchKeyword, int memberId) {
		return productRepository.getPurchaseCount(searchKeyword, memberId);
	}
	
	// 주문 목록 || 로그인 멤버
	public List<PurchaseList> getPurchaseList(String searchKeyword, int itemsInAPage, int page, int loginedMemberId) {
		int limitStart = (page - 1) * itemsInAPage;
		
		return productRepository.getPurchaseList(searchKeyword, itemsInAPage, limitStart, loginedMemberId);
	}
	
	// 주문 목록, 개수 구하기 || 판매자
	public int getOrderCount(String searchKeyword, int storeId) {
		return productRepository.getOrderCount(searchKeyword, storeId);
	}
	
	// 주문 목록 || 판매자
	public List<PurchaseList> getOrderList(String searchKeyword, int itemsInAPage, int page, int storeId) {
		int limitStart = (page - 1) * itemsInAPage;
		
		return productRepository.getOrderList(searchKeyword, itemsInAPage, limitStart, storeId);
	}

	// 주문 상세보기 || 로그인 멤버
	public PurchaseList getPurchase(int id) {
		return productRepository.getPurchase(id);
	}

	// 구매 확정
	public void confirmPurchase(int id) {
		productRepository.confirmPurchase(id);
	}

	// 주문 취소
	public void cancelPurchase(int id) {
		productRepository.cancelPurchase(id);
	}
	
	// 주문 취소 | 판매자
	public void sellerCancelPurchase(int orderId, int productId, int ordPCnt) {
		productRepository.cancelPurchase(orderId);
		productRepository.increaseProductStock(productId, ordPCnt);
		
	}
	// 주문 확인, 운송장 번호 전송
	public void checkPurchase(int orderId, int productId, int ordCheck, int ordPCnt, String waybill) {
		productRepository.checkPurchase(orderId, waybill);
		if (ordCheck == 0) {
			productRepository.decreaseProductStock(productId, ordPCnt);
		}
	}

	// 리뷰 작성
	public int createReview(int storeId, int productId, int purchaseId, int memberId, int rating, String reviewBody) {
		productRepository.createReview(storeId, productId, purchaseId, memberId, rating, reviewBody);
		
		int id = productRepository.getLastInsertId();
		
		return id;
	}

	// 리뷰 존재 확인
	public int isReview(int id, int loginedMemberId) {
		return productRepository.isReview(id, loginedMemberId);
	}

	// 리뷰 수 구하기
	public int getReviewsCount(int id) {
		return productRepository.getReviewsCount(id);
	}
	// 리뷰 평균 점수 구하기
	public double getReviewAvg(int id) {
		return productRepository.getReviewAvg(id);
	}
	// 리뷰 구하기
	public List<Review> getReviews(int storeId, int id, int itemsInAPage, int page) {
		int limitStart = (page - 1) * itemsInAPage;
		
		return productRepository.getReviews(storeId, id, limitStart, itemsInAPage);
	}
	
	// 관리자 입장에서 상품 수 체크
	public int getMyStoreProductsAdmCount(String searchKeywordTypeCode, String searchKeyword) {
		return productRepository.getMyStoreProductsAdmCount(searchKeywordTypeCode, searchKeyword);
	}
		
	// 관리자 입장에서 상품 수에 따른 페이징
	public List<Product> getProductsAdm(String searchKeywordTypeCode, String searchKeyword, int itemsInAPage,
			int page) {
		int limitStart = (page - 1) * itemsInAPage;

		return productRepository.getProductsAdm(searchKeywordTypeCode, searchKeyword, itemsInAPage, limitStart);
	}

	// 관리자 권한으로 상품 삭제
	public void AdmdeleteProduct(List<Integer> productIds) {
		for (int productId : productIds) {
			Product product = getProduct(productId);

			if (product != null) {
				AdmdeleteProduct(product);
			}
		}	
	}

	private void AdmdeleteProduct(Product product) {
		productRepository.AdmdeleteProduct(product.getId());
	}

}