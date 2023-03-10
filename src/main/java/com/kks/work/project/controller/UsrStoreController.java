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
public class UsrStoreController {
	private StoreService storeService;
	private ProductService productService;
	private Rq rq;
	private GenFileService genFileService;

	@Autowired
	public UsrStoreController(StoreService storeService, ProductService productService, Rq rq, GenFileService genFileService) {
		this.storeService = storeService;
		this.productService = productService;
		this.rq = rq;
		this.genFileService = genFileService;
	}
	
// 액션 메서드
	// 스토어 등록 페이지
	@RequestMapping("/usr/store/register")
	public String showRegister() {
		return "/usr/store/register";
	}
	
	// 스토어 등록
	@RequestMapping("/usr/store/doRegister") // 주소
	@ResponseBody // 실행할 몸통
	public String doRegister(String storeName, String storeDesc, MultipartRequest multipartRequest) {
		// 유효성 검사
		if (Utility.isEmpty(storeName)) {
			return Utility.jsHistoryBack("스토어 이름을 입력해주세요!");
		}

		ResultData<Integer> registerStoreRd = storeService.registerStore(storeName, storeDesc, rq.getLoginedMemberId());
		
		// 스토어 등록 실패
		if (registerStoreRd.isFail()) {
			return Utility.jsHistoryBack(registerStoreRd.getMsg());
		}

		int id = registerStoreRd.getData1();
		
		// 스토어 이미지가 있다면 업로드
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, id);
			}
		}

		return Utility.jsReplace(Utility.f("스토어가 등록됐습니다!", id), Utility.f("view?id=%d", id));
	}
	
	// 존재하는 스토어인지 체크 | AJAX 용
	@RequestMapping("/usr/store/getStoreNameDup")
	@ResponseBody
	public ResultData<String> getLoginIdDup(String storeName) {
		// 유효성 검사
		if(Utility.isEmpty(storeName)) {
			return ResultData.from("F-1", "스토어 이름을 입력해주세요");
		}
		
		Store store = storeService.getStoreByStoreName(storeName);
		
		if (store != null) {
			return ResultData.from("F-2", "이미 존재하는 스토어 이름입니다.", "storeName", storeName);
		}
		
		return ResultData.from("S-1", "사용 가능한 스토어 이름입니다.", "storeName", storeName);
	}

	// 스토어 상세보기
	@RequestMapping("/usr/store/view")
	public String showView(Model model, int id) {
	    Store store = storeService.getForPrintStoreById(rq.getLoginedMemberId(), id);
	    List<Category> categorys = storeService.getCategorysByStoreId(id);
	    List<GenFile> fileList = genFileService.getGenFiles("store", id, "extra", "bannerImg");
	    List<Product> bestProducts = productService.getStoreInProducts("", 5, 1, "reviewMany", -1, id);
	    List<Product> newProducts = productService.getStoreInProducts("", 5, 1, "", -1, id);
		
	    model.addAttribute("store", store);
	    model.addAttribute("categorys", categorys);
	    model.addAttribute("fileList", fileList);
	    model.addAttribute("bestProducts", bestProducts);
	    model.addAttribute("newProducts", newProducts);
	    
	    return "/usr/store/view";
	}
	
	// 패스워드 확인 페이지
	@RequestMapping("/usr/store/chkPassword")
	public String showChkPassword(int id) {
		ResultData<?> actorCanModifyRD = storeService.actorCanMD(rq.getLoginedMemberId(), id);
	    
	    if (actorCanModifyRD.isFail()) {
	    	return rq.jsReturnOnView(actorCanModifyRD.getMsg(), true);
	    }
	    
		return "/usr/store/chkPassword";
	}

	// 패스워드 확인
	@RequestMapping("/usr/store/doChkPassword")
	@ResponseBody
	public String doChkPassword(int id, String loginPw) {
		// 유효성 검사
		if (Utility.isEmpty(loginPw)) {
			return Utility.jsHistoryBack("비밀번호를 입력해주세요!");
		}

		if (rq.getLoginedMember().getLoginPw().equals(Utility.getEncrypt(loginPw, rq.getLoginedMember().getSalt())) == false) {
			return Utility.jsHistoryBack("비밀번호가 일치하지 않습니다!");
		}
		
		String storeModifyAuthKey = storeService.genStoreModifyAuthKey(rq.getLoginedMemberId());

		return Utility.jsReplace("", Utility.f("modify?id=%d&storeModifyAuthKey=%s", id, storeModifyAuthKey));
	}
	
	// 스토어 수정 페이지
	@RequestMapping("/usr/store/modify")
	public String showModify(Model model, int id, String storeModifyAuthKey) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id, storeModifyAuthKey);
	    
		if (storeVerifyTestRD.isFail()) {
	    	return rq.jsReturnOnView(storeVerifyTestRD.getMsg(), false, "/usr/home/main");
	    }
	    
	    model.addAttribute("store", storeVerifyTestRD.getData1());
	    
	    return "/usr/store/modify";
	}
	
	// 스토어 수정
	@RequestMapping("/usr/store/doModify")
	@ResponseBody
	public String doModify(HttpServletRequest req, int id, String storeModifyAuthKey, String storeDesc, MultipartRequest multipartRequest) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id, storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return Utility.jsReplace(storeVerifyTestRD.getMsg(), "/usr/home/main");
	    }
		
		// 이미지 삭제가 체크 되있다면 이미지 삭제
		if (req.getParameter("deleteFile__store__0__extra__storeLogo__1") != null) {
			genFileService.deleteGenFiles("store", id, "extra", "storeLogo", 1);
		}
		if (req.getParameter("deleteFile__store__0__extra__storeImg__1") != null) {
			genFileService.deleteGenFiles("store", id, "extra", "storeImg", 1);
		}
		
		// 이미지 업로드
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, id);
			}
		}

		storeService.doModify(id, rq.getLoginedMemberId(), storeDesc);

		return Utility.jsReplace("스토어를 수정했습니다!", Utility.f("view?id=%d", id));
	}
	
	// 스토어 목록 페이지
	@RequestMapping("/usr/store/list")
	public String showList(Model model,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String searchKeyword,
			@RequestParam(defaultValue = "20") int itemsNum,
			@RequestParam(defaultValue = "") String listOrder) {
		
		// 랭킹 순(구매 건수가 많은), 리뷰 많은 순, 리뷰 적은 순, 등록일 순으로 쿼리 짜야함

		if (page <= 0) {
			return rq.jsReturnOnView("페이지 번호가 올바르지 않습니다.", true);
		}

		// 스토어 수
		int storesCount = storeService.getStoresCount(searchKeyword);
		// 한 페이지에 나올 스토어 수
		int itemsInAPage = itemsNum;
		// 스토어 수에 따른 페이지 수 계산
		int pagesCount = (int) Math.ceil(storesCount / (double) itemsInAPage);

		List<Store> stores = storeService.getStores(searchKeyword, itemsInAPage, page, listOrder);

		model.addAttribute("stores", stores);
		model.addAttribute("storesCount", storesCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("searchKeyword", searchKeyword);

		return "/usr/store/list";
	}
	
	// 카테고리 페이지
	@RequestMapping("/usr/store/category")
	public String showCategory(Model model, int id, String storeModifyAuthKey) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id, storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return rq.jsReturnOnView(storeVerifyTestRD.getMsg(), false, "/usr/home/main");
	    }
		
		List<Category> categorys = storeService.getCategorysByStoreId(id);
		
		model.addAttribute("storeId", id);
	    model.addAttribute("categorys", categorys);
		
	    return "/usr/store/category";
	}
	
	// 카테고리 추가
	@RequestMapping("/usr/store/addCategory")
	@ResponseBody
	public String addCategory(int id, String storeModifyAuthKey, String name, int orderNo) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id, storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return Utility.jsReplace(storeVerifyTestRD.getMsg(), "/usr/home/main");
	    }
		
		// 유효성 검사
		int categoryCnt = storeService.getCategoryCntByStoreId(id);
		
		if(categoryCnt > 10) {
			return Utility.jsHistoryBack("카테고리는 10개를 초과할 수 없습니다.");
		}
		if(orderNo > 10) {
			return Utility.jsHistoryBack("순서는 10을 초과할 수 없습니다.");
		}
		if (Utility.isEmpty(name)) {
			return Utility.jsHistoryBack("카테고리 이름을 입력해주세요!");
		}
		if (Utility.isEmpty(orderNo)) {
			return Utility.jsHistoryBack("순서를 입력해주세요!");
		}
		
		Category category = storeService.getCategoryByStoreIdAndOrderNo(orderNo, id);
		
		if(category != null) {
			return Utility.jsHistoryBack("순서가 중복 됩니다, 삭제 또는 수정 후 진행해주세요!");
		}
		
		storeService.registerCategory(name, orderNo, id);
		
		return Utility.jsReplace("카테고리를 등록했습니다.", Utility.f("category?id=%d&storeModifyAuthKey=%s", id, storeModifyAuthKey));
	}
	
	// 카테고리 가져오기 | AJAX 용 
	@RequestMapping("/usr/store/getCategory")
	@ResponseBody
	public ResultData<Category> getCategory(int id) {
		Category category = storeService.getCategory(id);

		if (category == null) {
			return ResultData.from("F-1", "존재하지 않는 카테고리입니다.");
		}

		return ResultData.from("S-1", "카테고리 정보 조회 성공", "category", category);
	}
	// 카테고리 가져오기 | AJAX 용 
	@RequestMapping("/usr/store/getCategoryByStoreIdAndOrderNo")
	@ResponseBody
	public ResultData<Category> getCategoryByStoreIdAndOrderNo(int orderNo, int storeId) {
		Category category = storeService.getCategoryByStoreIdAndOrderNo(orderNo, storeId);

		if (category == null) {
			return ResultData.from("F-1", "존재하지 않는 카테고리입니다.");
		}

		return ResultData.from("S-1", "카테고리 정보 조회 성공", "category", category);
	}
	
	// 카테고리 수정
	@RequestMapping("/usr/store/doCategoryModify")
	@ResponseBody
	public String doCategoryModify(int id, String storeModifyAuthKey, String name, int orderNo) {
		// 카테고리 검사
	    Category category = storeService.getCategory(id);

		if (category == null) {
			return Utility.jsHistoryBack("존재하지 않는 카테고리입니다.");
		}
		
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), category.getStoreId(), storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return Utility.jsReplace(storeVerifyTestRD.getMsg(), "/usr/home/main");
	    }
		
		// 유효성 검사
		if(orderNo > 10) {
			return Utility.jsHistoryBack("순서는 10을 초과할 수 없습니다.");
		}
		if (Utility.isEmpty(name)) {
			return Utility.jsHistoryBack("카테고리 이름을 입력해주세요!");
		}
		if (Utility.isEmpty(orderNo)) {
			return Utility.jsHistoryBack("순서를 입력해주세요!");
		}
		
		storeService.doCategoryModify(id, name, orderNo, category.getStoreId());

		return Utility.jsReplace("", Utility.f("category?id=%d&storeModifyAuthKey=%s", category.getStoreId(), storeModifyAuthKey));
	}
	
	// 카테고리 삭제
	@RequestMapping("/usr/store/doCategoryDelete")
	@ResponseBody
	public String doCategoryDelete(int id, String storeModifyAuthKey) {
		// 카테고리 검사
	    Category category = storeService.getCategory(id);

		if (category == null) {
			return Utility.jsHistoryBack("존재하지 않는 카테고리입니다.");
		}
		
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), category.getStoreId(), storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return Utility.jsReplace(storeVerifyTestRD.getMsg(), "/usr/home/main");
	    }

		storeService.doCategoryDelete(id);

		return Utility.jsReplace("", Utility.f("category?id=%d&storeModifyAuthKey=%s", category.getStoreId(), storeModifyAuthKey));
	}
	
	// 배너 등록 페이지
	@RequestMapping("/usr/store/banner")
	public String showBanner(Model model, int id, String storeModifyAuthKey) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id, storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return rq.jsReturnOnView(storeVerifyTestRD.getMsg(), false, "/usr/home/main");
	    }
	
	    return "/usr/store/banner";
	}
	
	// 배너 등록
	@RequestMapping("/usr/store/addBanner")
	@ResponseBody
	public String addBanner(int id, String storeModifyAuthKey, MultipartRequest multipartRequest) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id, storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return Utility.jsReplace(storeVerifyTestRD.getMsg(), "/usr/home/main");
	    }
		
		// 배너 이미지 존재 시 이미지 업로드
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);
			
			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, id);
			}
		}
				
		return Utility.jsReplace("배너가 등록됐습니다.", Utility.f("banner?id=%d&storeModifyAuthKey=%s", id, storeModifyAuthKey));
	}
	
	// 배너 삭제
	@RequestMapping("/usr/store/delBanner")
	@ResponseBody
	public String delBanner(int id, String storeModifyAuthKey, int fileNo) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id, storeModifyAuthKey);

	    if (storeVerifyTestRD.isFail()) {
	    	return Utility.jsReplace(storeVerifyTestRD.getMsg(), "/usr/home/main");
	    }

		genFileService.deleteGenFiles("store", id, "extra", "bannerImg", fileNo);

		return Utility.jsReplace("", Utility.f("banner?id=%d&storeModifyAuthKey=%s", id, storeModifyAuthKey));
	}
	
	
	// 스토어 상품 목록 페이지
	@RequestMapping("/usr/store/productList")
	public String showBanner(Model model, int id,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String searchKeyword,
			@RequestParam(defaultValue = "20") int itemsNum,
			@RequestParam(defaultValue = "") String listOrder,
			@RequestParam(defaultValue = "-1") int cate) {
		Store store = storeService.getForPrintStoreById(rq.getLoginedMemberId(), id);
	    List<Category> categorys = storeService.getCategorysByStoreId(id);

	    if (page <= 0) {
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다.", true);
		}

		// 현재 등록된 상품 수
		int productsCount = productService.getStoreInProductsCount(searchKeyword, id);
		// 한 페이지에 나올 스토어 수
		int itemsInAPage = itemsNum;
		// 상품 수에 따른 페이지 수 계산
		int pagesCount = (int) Math.ceil(productsCount / (double) itemsInAPage);

		List<Product> products = productService.getStoreInProducts(searchKeyword, itemsInAPage, page, listOrder, cate, id);
		
	    model.addAttribute("store", store);
	    model.addAttribute("categorys", categorys);
	    model.addAttribute("productsCount", productsCount);
	    model.addAttribute("searchKeyword", searchKeyword);
	    model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("products", products);
		model.addAttribute("page", page);
		
		
	    return "/usr/store/productList";
	}
}

