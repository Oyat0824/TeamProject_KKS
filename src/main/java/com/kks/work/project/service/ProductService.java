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
	private AttrService attrService;
	private ProductRepository productRepository;
	
	@Autowired
	public ProductService(ProductRepository productRepository, AttrService attrService) {
		this.productRepository = productRepository;
		this.attrService = attrService;
	}
	
// 서비스 메서드
	// 상품 등록	
	public ResultData<Integer> registerProduct(String productName, String productPrice, String productCetegory, String productStock, String productBody, int storeId, int memberId) {

		productRepository.registerProduct(productName, productPrice, productCetegory, productStock, productBody, storeId, memberId);
		productRepository.productStateChange(storeId);
		
		int id = productRepository.getLastInsertId();
				
		return ResultData.from("S-1", Utility.f("%d번 상품이 등록되었습니다.", id), "id", id);
	}
	
	// storeId로 상품 정보 가져오기
	public Product getProductByStoreId(int storeId) {
		return productRepository.getProductByStoreId(storeId);
	}
	
	// 상품 이름으로 상품 정보 가져오기
	public Product getProductByProductName(String productName) {
		return productRepository.getProductByProductName(productName);
	}
	
	// 페이징 처리
	public List<Product> getProducts(String searchKeywordTypeCode, String searchKeyword, int itemsInAPage, int page) {
		int limitStart = (page - 1) * itemsInAPage;

		return productRepository.getProducts(searchKeywordTypeCode, searchKeyword, limitStart, itemsInAPage);
	}

	// 가게 수 카운트 
	public int getProductsCount(String searchKeywordTypeCode, String searchKeyword) { 
		return productRepository.getProductsCount(searchKeywordTypeCode, searchKeyword); 
	}
	
	public void doModify(int id, int loginedMemberId, String productBody) {
		productRepository.doModify(id, loginedMemberId, productBody);
		attrService.remove("product", loginedMemberId, "extra", "productModifyAuthKey");	
	}
	
	// 상품 상세보기
	public Product getForPrintProductById(int loginedMemberId, int id) {
		Product product = productRepository.getForPrintProductById(id);
		
		actorCanChangeData(id, product);
		
		return product;
	}
	
	// 검증
	public ResultData<?> actorCanMD(int storeId, Product product) {
		if (product == null) {
			return ResultData.from("F-1", "해당 상품은 존재하지 않습니다.");
		}
				
		if (product.getStoreId() != storeId) {
			return ResultData.from("F-A", "해당 상품정보에 대한 권한이 없습니다.");
		}
				
			return ResultData.from("S-1", "가능");
		}
			
	private void actorCanChangeData(int storeId, Product product) {
		if(product == null) {
			return;
		}
				
		ResultData<?> actorCanChangeDataRd = actorCanMD(storeId, product);
				
		product.setActorCanChangeData(actorCanChangeDataRd.isSuccess());
				
	}
	
	// 인증키 생성
	public String genProductModifyAuthKey(int loginedMemberId) {
		String ProductModifyAuthKey = Utility.getTempPassword(10);
		attrService.setValue("store", loginedMemberId, "extra", "ProductModifyAuthKey", ProductModifyAuthKey,
			Utility.getDateStrLater(60 * 5));

		return ProductModifyAuthKey;
		}

	public ResultData<?> chkProductModifyAuthKey(int loginedMemberId, String productModifyAuthKey) {
		String saved = attrService.getValue("product", loginedMemberId, "extra", "productModifyAuthKey");
		
		if (saved.equals(productModifyAuthKey) == false) {
			return ResultData.from("F-1", "일치하지 않거나 만료된 인증코드입니다.");
		}

		return ResultData.from("S-1", "정상 인증코드입니다");
	}
	
	
}