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
import com.kks.work.project.service.StoreService;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Product;
import com.kks.work.project.vo.Rq;
import com.kks.work.project.vo.Store;

@Controller
public class UsrProductController {
	private ProductService productService;
	private Rq rq;
	private GenFileService genFileService;
	private StoreService storeService;

	@Autowired
	public UsrProductController(ProductService productService, Rq rq, GenFileService genFileService, StoreService storeService) {
		this.productService = productService;
		this.rq = rq;
		this.genFileService = genFileService;
		this.storeService = storeService;
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

		ResultData<Integer> registerProductRd = productService.registerProduct(productName, productPrice, productCetegory, productStock, productBody, rq.getLoginedMemberId(), storeService.getStoreId());
		
		// 상품 등록 실패
		if (registerProductRd.isFail()) {
			return Utility.jsHistoryBack(registerProductRd.getMsg());
		}

		int id = registerProductRd.getData1();

		return Utility.jsReplace(Utility.f("상품이 등록됐습니다!", id), Utility.f("view?id=%d", id));
	}

	// 상품 상세보기
	@RequestMapping("/usr/product/view")
	public String showView(Model model, int id) {
		Product product = productService.getForPrintProductById(rq.getLoginedMemberId(), storeService.getStoreId(), id);

		model.addAttribute("product", product);
		  
		return "/usr/product/view";
	}
	
	// 상품 리스트
	@RequestMapping("/usr/product/list")
	public String showProductList(Model model, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String searchKeyword,
			@RequestParam(defaultValue = "20") int itemsNum) {
		
		if(page <= 0) { // 페이징이 0보다 작을 경우
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다.", true);
		}
		
		// 현재 등록된 상품 수
		int productsCount = productService.getProductsCount(searchKeyword);
		// 한 페이지에 나올 스토어 수
		int itemsInAPage = itemsNum;
		// 상품 수에 따른 페이지 수 계산
		int pagesCount = (int) Math.ceil(productsCount / (double) itemsInAPage);

		List<Product> products = productService.getProducts(searchKeyword, itemsInAPage, page);

		model.addAttribute("products", products);
		model.addAttribute("productsCount", productsCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("searchKeyword", searchKeyword);
		
		return "/usr/product/list";
	}

	// 상품 수정 페이지 
		@RequestMapping("/usr/product/modify")
		public String showModify(Model model, int id, String productModifyAuthKey) {
			// 인증키 검사
			ResultData<?> chkProductModifyAuthKeyRd = productService.chkProductModifyAuthKey(rq.getLoginedMemberId(), productModifyAuthKey);
			    
		    if (chkProductModifyAuthKeyRd.isFail()) {
		    	return rq.jsReturnOnView(chkProductModifyAuthKeyRd.getMsg(), false, "/usr/home/main");
			}
			
		    // 본인 스토어 검사
			Store store = storeService.getStoreById(id);
		    
		    ResultData<?> actorCanModifyRD = storeService.actorCanMD(rq.getLoginedMemberId(), store);
		    
		    if (actorCanModifyRD.isFail()) {
		    	return rq.jsReturnOnView(actorCanModifyRD.getMsg(), true);
		    }
		    
		    model.addAttribute("store", store);
		    
		    return "/usr/product/modify";
		}
		
		// 상품 수정
		@RequestMapping("/usr/product/doModify")
		@ResponseBody
		public String doModify(HttpServletRequest req, int id, String productModifyAuthKey, String productPrice, String productCetegory, String productStock, String productBody, MultipartRequest multipartRequest) {
			// 인증키 검사
			ResultData<?> chkProductModifyAuthKeyRd = productService.chkProductModifyAuthKey(rq.getLoginedMemberId(), productModifyAuthKey);
		    
		    if (chkProductModifyAuthKeyRd.isFail()) {
				return Utility.jsReplace(chkProductModifyAuthKeyRd.getMsg(), "/usr/home/main");
			}
			
		    // 본인 스토어 검사
			Store store = storeService.getStoreById(id);

			ResultData<?> actorCanMDRd = storeService.actorCanMD(rq.getLoginedMemberId(), store);

			if (actorCanMDRd.isFail()) {
				return Utility.jsHistoryBack(actorCanMDRd.getMsg());
			}
			
			// 이미지 삭제 체크 되있다면 삭제
			
			if (req.getParameter("deleteFile__product__0__extra__productImg__1") != null) {
				genFileService.deleteGenFiles("product", id, "extra", "productImg", 1);
			}
			
			// 이미지 업로드
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			
			for (String fileInputName : fileMap.keySet()) {
				MultipartFile multipartFile = fileMap.get(fileInputName);

				if (multipartFile.isEmpty() == false) {
					genFileService.save(multipartFile, id);
				}
			}

			productService.doModify(id, rq.getLoginedMemberId(), storeService.getStoreId(), productPrice, productCetegory, productStock, productBody);

			return Utility.jsReplace("상품정보를 수정했습니다!", Utility.f("view?id=%d", id));
		}
		
}

