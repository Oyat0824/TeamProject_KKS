package com.kks.work.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kks.work.project.service.ProductService;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Product;
import com.kks.work.project.vo.Rq;

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
	public String doRegister(String p_Name, String p_Price, String p_Cetegory, String p_Stock, String p_Img, String p_Desc) {
		// 유효성 검사
		if (Utility.empty(p_Name)) {
			return Utility.jsHistoryBack("상품 이름을 입력해주세요!");
		}

		ResultData<Integer> registerProductRd = productService.registerProduct(p_Name, p_Price, p_Cetegory, p_Stock, p_Img, p_Desc, rq.getLoginedMemberId());
		
		// 상품 등록 실패
		if (registerProductRd.isFail()) {
			return Utility.jsHistoryBack(registerProductRd.getMsg());
		}

		int id = registerProductRd.getData1();

		return Utility.jsReplace(Utility.f("상품이 등록됐습니다!", id), Utility.f("product?id=%d", id));
	}
	
	// 존재하는 상품인지 체크
	@RequestMapping("/usr/store/getProductNameDup")
	@ResponseBody
	public ResultData<String> getLoginIdDup(String p_Name) {
		// 유효성 검사
		if(Utility.empty(p_Name)) {
			return ResultData.from("F-1", "상품 이름을 입력해주세요");
		}
		
		Product product = productService.getProductByProductName(p_Name);
		
		if (product != null) {
			return ResultData.from("F-2", "이미 존재하는 상품 이름입니다.", "p_Name", p_Name);
		}
		
		return ResultData.from("S-1", "사용 가능한 상품 이름입니다.", "p_Name", p_Name);
	}
}

