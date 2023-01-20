package com.kks.work.project.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.kks.work.project.service.GenFileService;
import com.kks.work.project.service.ProductService;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Product;
import com.kks.work.project.vo.Rq;

@Controller
public class UsrProductController {
	private ProductService productService;
	private Rq rq;
	private GenFileService genFileService;

	@Autowired
	public UsrProductController(ProductService productService, Rq rq, GenFileService genFileService) {
		this.productService = productService;
		this.rq = rq;
		this.genFileService = genFileService;
	}
	
// 액션 메서드
	// 상품 등록 페이지
	@RequestMapping("/usr/product/register")
	public String showRegister() {
		return "/usr/product/register";
	}
	
	// 상품 등록
	@RequestMapping("/usr/product/doRegister")
	@ResponseBody
	public String doRegister(String productName, String productPrice, String productCetegory, String productStock, String productBody) {
		// 유효성 검사
		if (Utility.isEmpty(productName)) {
			return Utility.jsHistoryBack("상품 이름을 입력해주세요!");
		}

		ResultData<Integer> registerProductRd = productService.registerProduct(productName, productPrice, productCetegory, productStock, productBody, rq.getLoginedMemberId());
		
		// 상품 등록 실패
		if (registerProductRd.isFail()) {
			return Utility.jsHistoryBack(registerProductRd.getMsg());
		}

		int id = registerProductRd.getData1();

		return Utility.jsReplace(Utility.f("상품이 등록됐습니다!", id), Utility.f("product?id=%d", id));
	}
	
	@RequestMapping("/usr/product/list")
	public String showProductList(Model model, @RequestParam(defaultValue = "1") int page, 
			@RequestParam(defaultValue = "stroeId ,productName, productCetegory") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword) {
		
		if(page <= 0) { // 페이징이 0보다 작을 경우
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다.", true);
		}
		
		int productsCount = productService.getProductsCount(searchKeywordTypeCode, searchKeyword);
		
		int itemsInAPage = 10;
		
		int pagesCount = (int) Math.ceil((double) productsCount / itemsInAPage);

		List<Product> products = productService.getProducts(searchKeywordTypeCode, searchKeyword, itemsInAPage, page);
		
		model.addAttribute("products", products);
		model.addAttribute("productsCount", productsCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);
		
		return "usr/product/list";
	}
	
	// 상품 정보 수정 페이지
	@RequestMapping("/usr/product/modify")
	public String showModify(Model model, int id, String productModifyAuthKey) {
		Product product = productService.getProductById(id);
			
		ResultData<?> chkProductModifyAuthKeyRd = productService.chkProductModifyAuthKey(rq.getLoginedMemberId(), productModifyAuthKey);
		    
		if (chkProductModifyAuthKeyRd.isFail()) {
			return rq.jsReturnOnView(chkProductModifyAuthKeyRd.getMsg(), true);
		}
		    
		model.addAttribute("product", product);
		    
		return "/usr/product/modify";
	}
	
	// 상품 정보 수정 페이지
	@RequestMapping("/usr/product/doModify")
	@ResponseBody
	public String doModify(HttpServletRequest req, int id, String productModifyAuthKey, String productBody, MultipartRequest multipartRequest) {
		if (Utility.isEmpty(productModifyAuthKey)) {
			return Utility.jsHistoryBack("상품 수정 인증코드가 필요합니다.");
		}
			
		ResultData<?> chkProductModifyAuthKeyRd = productService.chkProductModifyAuthKey(rq.getLoginedMemberId(), productModifyAuthKey);
		    
		if (chkProductModifyAuthKeyRd.isFail()) {
			return rq.jsReturnOnView(chkProductModifyAuthKeyRd.getMsg(), true);
		}
			
		Product product = productService.getProductById(id);

		ResultData<?> actorCanMDRd = productService.actorCanMD(rq.getLoginedMemberId(), product);

		if (actorCanMDRd.isFail()) {
			return Utility.jsHistoryBack(actorCanMDRd.getMsg());
		}
			
		if (req.getParameter("deleteFile__product__0__extra__productImg__1") != null) {
			genFileService.deleteGenFiles("product", id, "extra", "productImg", 1);
		}
			
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, id);
			}
		}

		productService.doModify(id, rq.getLoginedMemberId(), productBody);

		return Utility.jsReplace("상품정보를 수정했습니다!", Utility.f("view?id=%d", id));
		}
	
}

