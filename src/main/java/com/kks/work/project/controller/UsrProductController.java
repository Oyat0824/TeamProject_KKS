package com.kks.work.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kks.work.project.service.ProductService;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Product;
import com.kks.work.project.vo.Rq;
import com.kks.work.project.vo.Store;

@Controller
public class UsrProductController {
	private ProductService productService;
	private Rq rq;

	@Autowired
	public UsrProductController(ProductService productService, Rq rq) {
		this.productService = productService;
		this.rq = rq;
	}
	
// 액션 메서드
	// 상품 등록 페이지
	@RequestMapping("/usr/product/register")
	public String showRegister() {
		return "/usr/product/register";
	}
	
	// 상품 등록
	@RequestMapping("/usr/product/doRegister") // 주소
	@ResponseBody // 실행할 몸통
	public String doRegister(String productName, String productPrice, String productCetegory, String productStock, String productImg, String productBody) {
		// 유효성 검사
		if (Utility.empty(productName)) {
			return Utility.jsHistoryBack("상품 이름을 입력해주세요!");
		}

		ResultData<Integer> registerProductRd = productService.registerProduct(productName, productPrice, productCetegory, productStock, productImg, productBody, rq.getLoginedMemberId());
		
		// 상품 등록 실패
		if (registerProductRd.isFail()) {
			return Utility.jsHistoryBack(registerProductRd.getMsg());
		}

		int id = registerProductRd.getData1();

		return Utility.jsReplace(Utility.f("상품이 등록됐습니다!", id), Utility.f("product?id=%d", id));
	}
	
	// 존재하는 상품인지 체크
	@RequestMapping("/usr/product/getProductNameDup")
	@ResponseBody
	public ResultData<String> getLoginIdDup(String productName) {
		// 유효성 검사
		if(Utility.empty(productName)) {
			return ResultData.from("F-1", "상품 이름을 입력해주세요");
		}
		
		Product product = productService.getProductByProductName(productName);
		
		if (product != null) {
			return ResultData.from("F-2", "이미 존재하는 상품 이름입니다.", "productName",productName);
		}
		
		return ResultData.from("S-1", "사용 가능한 상품 이름입니다.", "productName", productName);
	}
	
	@RequestMapping("/usr/product/list")
	public String showProductList(Model model,@RequestParam(defaultValue = "0") String authLevel, @RequestParam(defaultValue = "1") int page, 
			@RequestParam(defaultValue = "stroeId ,productName, productCetegory") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword) {
		
		if(page <= 0) { // 페이징이 0보다 작을 경우
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다.", true);
		}
		
		int productsCount = productService.getProductsCount(authLevel, searchKeywordTypeCode, searchKeyword);
		
		int itemsInAPage = 10;
		
		int pagesCount = (int) Math.ceil((double) productsCount / itemsInAPage);

		List<Product> products = productService.getProducts(authLevel, searchKeywordTypeCode, searchKeyword, itemsInAPage, page);
		
		model.addAttribute("authLevel", authLevel);
		model.addAttribute("products", products);
		model.addAttribute("productsCount", productsCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);
		
		return "usr/product/list";
	}
	
	
}

