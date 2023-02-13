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
import com.kks.work.project.vo.Category;
import com.kks.work.project.vo.GenFile;
import com.kks.work.project.vo.Product;
import com.kks.work.project.vo.Rq;
import com.kks.work.project.vo.Store;

@Controller
public class UsrProductController {
	private StoreService storeService;
	private ProductService productService;
	private Rq rq;
	private GenFileService genFileService;

	@Autowired
	public UsrProductController(StoreService storeService, ProductService productService, Rq rq, GenFileService genFileService) {
		this.storeService = storeService;
		this.productService = productService;
		this.rq = rq;
		this.genFileService = genFileService;
	}
	
// 액션 메서드
	// 상품 등록 페이지
	@RequestMapping("/usr/product/register")
	public String showRegister(Model model, int id) {
		// 본인 스토어 검사
		ResultData<Store> actorCanModifyRD = storeService.actorCanMD(rq.getLoginedMemberId(), id);
	    
	    if (actorCanModifyRD.isFail()) {
	    	return rq.jsReturnOnView(actorCanModifyRD.getMsg(), true);
	    }
		
		List<Category> categorys = storeService.getCategorysByStoreId(id);
		
		model.addAttribute("categorys", categorys);
		
		return "/usr/product/register";
	}
	
	// 상품 등록
	@RequestMapping("/usr/product/doRegister")
	@ResponseBody
	public String doRegister(int id, String storeModifyAuthKey,
			String productName, String productPrice, String productCategory, String productStock, int productDlvy, String productCourier, String productDlvyPrice, String productBody,
			MultipartRequest multipartRequest) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id, storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return Utility.jsReplace(storeVerifyTestRD.getMsg(), "/usr/home/main");
	    }
		
		// 유효성 검사
		if (Utility.isEmpty(productName)) {
			return Utility.jsHistoryBack("상품 이름을 입력해주세요!");
		}
		if (Utility.isEmpty(productPrice)) {
			return Utility.jsHistoryBack("상품 가격을 입력해주세요!");
		}
		if (Utility.isEmpty(productStock)) {
			return Utility.jsHistoryBack("상품 재고를 입력해주세요!");
		}
		if (Utility.isEmpty(productDlvy)) {
			return Utility.jsHistoryBack("배송 방식을 선택해주세요!");
		}
		if (Utility.isEmpty(productCourier)) {
			return Utility.jsHistoryBack("배송사를 선택해주세요!");
		}
		if (productDlvy == 1 && Utility.isEmpty(productDlvyPrice)) {
			return Utility.jsHistoryBack("배송비용을 적어주세요!");
		}
		if (Utility.isEmpty(productBody)) {
			return Utility.jsHistoryBack("상품 내용을 입력해주세요!");
		}
		
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		int chkFileCnt = 0;
		int fileNum = 0;
		// 이미지가 하나라도 없다면 반환	
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);
			
			fileNum += 1;
			
			if (multipartFile.isEmpty()) {
				chkFileCnt += 1;
			}
		}
		
		if(chkFileCnt >= fileNum) {
			return Utility.jsHistoryBack("상품 이미지를 하나라도 등록해야합니다.");
		}
		
		int productId = productService.registerProduct(productName, productPrice, productCategory, productStock, productDlvy, productCourier, productDlvyPrice, productBody, id);
		
		// 이미지가 있다면 업로드
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, productId);
			}
		}

		return Utility.jsReplace(Utility.f("상품이 등록됐습니다!", id), Utility.f("view?storeId=%d&id=%d", id, productId));
	}

	// 상품 상세보기
	@RequestMapping("/usr/product/view")
	public String showView(Model model, int storeId, int id) {
		Store store = storeService.getStoreById(storeId);
		List<Category> categorys = storeService.getCategorysByStoreId(storeId);
		Product product = productService.getProductByStoreIdAndId(storeId, id);
		List<GenFile> fileList = genFileService.getGenFiles("product", id, "extra", "productImg");
		
		model.addAttribute("store", store);
		model.addAttribute("categorys", categorys);
		model.addAttribute("product", product);
		model.addAttribute("fileList", fileList);
		  
		return "/usr/product/view";
	}
	
	// 상품 목록 페이지 | 판매자 전용
	@RequestMapping("/usr/product/list")
	public String showProductList(Model model, int id, String storeModifyAuthKey,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String searchKeyword,
			@RequestParam(defaultValue = "20") int itemsNum) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id, storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return rq.jsReturnOnView(storeVerifyTestRD.getMsg(), false, "/usr/home/main");
	    }
		
		if(page <= 0) {
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다.", true);
		}
		
		// 현재 등록된 상품 수
		int productsCount = productService.getMyStoreProductsCount(id, searchKeyword);
		// 한 페이지에 나올 스토어 수
		int itemsInAPage = itemsNum;
		// 상품 수에 따른 페이지 수 계산
		int pagesCount = (int) Math.ceil(productsCount / (double) itemsInAPage);

		List<Product> products = productService.getProducts(id, searchKeyword, itemsInAPage, page);

		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("productsCount", productsCount);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("products", products);
		model.addAttribute("page", page);
		
		return "/usr/product/list";
	}
	
	// 상품 목록 페이지 | 사용자
	@RequestMapping("/usr/product/exposurelist")
	public String showProductExposureList(Model model, 
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String searchKeyword,
			@RequestParam(defaultValue = "20") int itemsNum) {
		
		if(page <= 0) {
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다.", true);
		}
		
		// 현재 등록된 상품 수
		int productsCount = productService.getProductsCount(searchKeyword);
		// 한 페이지에 나올 스토어 수
		int itemsInAPage = itemsNum;
		// 상품 수에 따른 페이지 수 계산
		int pagesCount = (int) Math.ceil(productsCount / (double) itemsInAPage);

		List<Product> products = productService.getExposureProducts(searchKeyword, itemsInAPage, page);

		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("productsCount", productsCount);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("products", products);
		model.addAttribute("page", page);
			
		return "/usr/product/exposurelist";
	}

	// 상품 수정 페이지 
	@RequestMapping("/usr/product/modify")
	public String showModify(Model model, int id, int storeId, String storeModifyAuthKey) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), storeId, storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return rq.jsReturnOnView(storeVerifyTestRD.getMsg(), false, "/usr/home/main");
	    }
	    
	    Product product = productService.getProduct(id);
	    List<Category> categorys = storeService.getCategorysByStoreId(storeId);
		
	    model.addAttribute("product", product);
	    model.addAttribute("categorys", categorys);
	    
	    return "/usr/product/modify";
	}
	
	// 상품 수정
	@RequestMapping("/usr/product/doModify")
	@ResponseBody
	public String doModify(HttpServletRequest req, int storeId, int id, String storeModifyAuthKey,
			String productName, String productPrice, String productCategory, String productStock, int productDlvy, String productCourier, String productDlvyPrice, String productBody,
			MultipartRequest multipartRequest) {
		// 본인 스토어 검사
		ResultData<Store> actorCanModifyRD = storeService.actorCanMD(rq.getLoginedMemberId(), storeId);
		
		if (actorCanModifyRD.isFail()) {
			return Utility.jsHistoryBack(actorCanModifyRD.getMsg());
		}
		
		// 이미지 삭제가 체크 되있다면 이미지 삭제
		if (req.getParameter("deleteFile__product__0__extra__productImg__1") != null) {
			genFileService.deleteGenFiles("product", id, "extra", "productImg", 1);
		}
		if (req.getParameter("deleteFile__product__0__extra__productImg__2") != null) {
			genFileService.deleteGenFiles("product", id, "extra", "productImg", 2);
		}
		if (req.getParameter("deleteFile__product__0__extra__productImg__3") != null) {
			genFileService.deleteGenFiles("product", id, "extra", "productImg", 3);
		}
		
		// 이미지 업로드
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, id);
			}
		}
		productService.doModify(id, productName, productPrice, productCategory, productStock, productDlvy, productCourier, productDlvyPrice, productBody);

		return Utility.jsReplace("상품정보를 수정했습니다!", Utility.f("list?id=%d&storeModifyAuthKey=%s", storeId, storeModifyAuthKey));
	}
	
	// 등록 상품 삭제
	@RequestMapping("/usr/product/doDelete")
	@ResponseBody 
	public String doDelete(int id, int storeId, String storeModifyAuthKey) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), storeId, storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return Utility.jsReplace(storeVerifyTestRD.getMsg(), "/usr/home/main");
	    }

		genFileService.deleteGenFiles("product", id, "extra", "productImg", 1);
		genFileService.deleteGenFiles("product", id, "extra", "productImg", 2);
		genFileService.deleteGenFiles("product", id, "extra", "productImg", 3);
		
		productService.deleteProduct(id);												
		
		return Utility.jsReplace("등록된 상품을 삭제했습니다!", Utility.f("list?id=%d&storeModifyAuthKey=%s", storeId, storeModifyAuthKey));
	} 
	
	// 상품 구매 페이지
	@RequestMapping("/usr/product/orderSheet")
	public String showOrderSheet(Model model, int id, int storeId, String productPrice, int productCnt) {   
		Store store = storeService.getStoreById(storeId);
	    Product product = productService.getProduct(id);
	    
	    if(store.getId() != product.getStoreId()) {
	    	return rq.jsReturnOnView("잘못된 정보입니다, 다시 시도해주세요.", true);
	    }
		
	    model.addAttribute("store", store);
	    model.addAttribute("product", product);
	    
	    return "/usr/product/orderSheet";
	}
}

