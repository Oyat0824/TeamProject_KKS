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
		List<Category> categorys = storeService.getCategorysByStoreId(id);
		
		model.addAttribute("categorys", categorys);
		
		return "/usr/product/register";
	}
	
	// 상품 등록
	@RequestMapping("/usr/product/doRegister")
	@ResponseBody
	public String doRegister(int id, String storeModifyAuthKey, String productName, String productPrice, String productCategory, String productStock, String productBody, MultipartRequest multipartRequest) {
		// 인증키 검사
		ResultData<?> chkStoreModifyAuthKeyRd = storeService.chkStoreModifyAuthKey(rq.getLoginedMemberId(), storeModifyAuthKey);
	    
	    if (chkStoreModifyAuthKeyRd.isFail()) {
			return Utility.jsReplace(chkStoreModifyAuthKeyRd.getMsg(), "/usr/home/main");
		}
		
	    // 본인 스토어 검사
		Store store = storeService.getStoreById(id);

		ResultData<?> actorCanMDRd = storeService.actorCanMD(rq.getLoginedMemberId(), store);

		if (actorCanMDRd.isFail()) {
			return Utility.jsHistoryBack(actorCanMDRd.getMsg());
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
		if (Utility.isEmpty(productBody)) {
			return Utility.jsHistoryBack("상품 내용을 입력해주세요!");
		}
		
		int productId = productService.registerProduct(productName, productPrice, productCategory, productStock, productBody, id);
		
		// 스토어 이미지가 있다면 업로드
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
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
		Product product = productService.getProductByStoreIdAndId(storeId, id);
		List<GenFile> fileList = genFileService.getGenFiles("product", id, "extra", "productImg");
		
		model.addAttribute("product", product);
		model.addAttribute("fileList", fileList);
		  
		return "/usr/product/view";
	}
	
	// 상품 리스트
	@RequestMapping("/usr/product/list")
	public String showProductList(Model model, int id, String storeModifyAuthKey,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String searchKeyword,
			@RequestParam(defaultValue = "20") int itemsNum) {
		// 인증키 검사
		ResultData<?> chkStoreModifyAuthKeyRd = storeService.chkStoreModifyAuthKey(rq.getLoginedMemberId(), storeModifyAuthKey);
		    
		if (chkStoreModifyAuthKeyRd.isFail()) {
			return rq.jsReturnOnView(chkStoreModifyAuthKeyRd.getMsg(), false, "/usr/home/main");
		}
		
		// 본인 스토어 검사
		Store store = storeService.getStoreById(id);
		
		ResultData<?> actorCanModifyRD = storeService.actorCanMD(rq.getLoginedMemberId(), store);
		
		if (actorCanModifyRD.isFail()) {
			return rq.jsReturnOnView(actorCanModifyRD.getMsg(), true);
		}
		
		if(page <= 0) {
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다.", true);
		}
		
		// 현재 등록된 상품 수
		int productsCount = productService.getProductsCount(searchKeyword);
		// 한 페이지에 나올 스토어 수
		int itemsInAPage = itemsNum;
		// 상품 수에 따른 페이지 수 계산
		int pagesCount = (int) Math.ceil(productsCount / (double) itemsInAPage);

		List<Product> products = productService.getProducts(id, searchKeyword, itemsInAPage, page);

		model.addAttribute("products", products);
		model.addAttribute("productsCount", productsCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("searchKeyword", searchKeyword);
		
		return "/usr/product/list";
	}

	// 상품 수정 페이지 
	@RequestMapping("/usr/product/modify")
	public String showModify(Model model, int id, int storeId) {
		// 본인 스토어 검사
		Store store = storeService.getStoreById(storeId);
	    
	    ResultData<?> actorCanModifyRD = storeService.actorCanMD(rq.getLoginedMemberId(), store);
	    
	    if (actorCanModifyRD.isFail()) {
	    	return rq.jsReturnOnView(actorCanModifyRD.getMsg(), true);
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
	public String doModify(HttpServletRequest req, int storeId, int id, String productName, String productPrice, String productCategory, String productStock, String productBody, MultipartRequest multipartRequest) {
		// 본인 스토어 검사
		Store store = storeService.getStoreById(storeId);

		ResultData<?> actorCanMDRd = storeService.actorCanMD(rq.getLoginedMemberId(), store);

		if (actorCanMDRd.isFail()) {
			return Utility.jsHistoryBack(actorCanMDRd.getMsg());
		}
		
		// 이미지 삭제 체크 되있다면 삭제
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

		productService.doModify(id, productName, productPrice, productCategory, productStock, productBody);

		return Utility.jsReplace("상품정보를 수정했습니다!", Utility.f("view?storeId=%d&id=%d", storeId, id));
	}
	
	// 등록 상품 삭제
	@RequestMapping("/usr/product/doDelete")
	@ResponseBody 
	public String doDelete(int id, int storeId, String storeModifyAuthKey) {
		
		// 인증키 검사
		ResultData<?> chkStoreModifyAuthKeyRd = storeService.chkStoreModifyAuthKey(rq.getLoginedMemberId(), storeModifyAuthKey);
				    
		if (chkStoreModifyAuthKeyRd.isFail()) {
			return rq.jsReturnOnView(chkStoreModifyAuthKeyRd.getMsg(), false, "/usr/home/main");
		}
		
		// 본인 스토어 검사
		Store store = storeService.getStoreById(storeId);

		ResultData<?> actorCanMDRd = storeService.actorCanMD(rq.getLoginedMemberId(), store);
		
		if(actorCanMDRd.isFail()) {  // 실패시에 
			return Utility.jsHistoryBack(actorCanMDRd.getMsg());
		}

		genFileService.deleteGenFiles("product", id, "extra", "productImg", 1);
		genFileService.deleteGenFiles("product", id, "extra", "productImg", 2);
		genFileService.deleteGenFiles("product", id, "extra", "productImg", 3);
		
		productService.deleteProduct(id);														
		
		return Utility.jsReplace("등록된 상품을 삭제했습니다!", Utility.f("list?id=%d&storeModifyAuthKey=%s", storeId, storeModifyAuthKey));

	} 
		
}

