package com.kks.work.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
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
import com.kks.work.project.vo.PurchaseList;
import com.kks.work.project.vo.Review;
import com.kks.work.project.vo.Rq;
import com.kks.work.project.vo.Store;

@Controller
public class UsrProductController {
	private StoreService storeService;
	private ProductService productService;
	private Rq rq;
	private GenFileService genFileService;

	@Autowired
	public UsrProductController(StoreService storeService, ProductService productService, Rq rq,
			GenFileService genFileService) {
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
	public String doRegister(int id, String storeModifyAuthKey, String productName, String productPrice,
			String productCategory, String productStock, int productDlvy, String productCourier, String productCourierCode,
			String productDlvyPrice, String productBody, MultipartRequest multipartRequest) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id,
				storeModifyAuthKey);

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
		if (Utility.isEmpty(productCourier) || Utility.isEmpty(productCourierCode)) {
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

		if (chkFileCnt >= fileNum) {
			return Utility.jsHistoryBack("상품 이미지를 하나라도 등록해야합니다.");
		}

		int productId = productService.registerProduct(productName, productPrice, productCategory, productStock,
				productDlvy, productCourier, productCourierCode, productDlvyPrice, productBody, id);

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
		List<Review> reviews = productService.getReviews(storeId, id);
		
		model.addAttribute("store", store);
		model.addAttribute("categorys", categorys);
		model.addAttribute("product", product);
		model.addAttribute("fileList", fileList);
		model.addAttribute("reviews", reviews);

		return "/usr/product/view";
	}

	// 상품 목록 페이지 | 판매자 전용
	@RequestMapping("/usr/product/list")
	public String showProductList(Model model, int id, String storeModifyAuthKey,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "") String searchKeyword,
			@RequestParam(defaultValue = "20") int itemsNum) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id,
				storeModifyAuthKey);

		if (storeVerifyTestRD.isFail()) {
			return rq.jsReturnOnView(storeVerifyTestRD.getMsg(), false, "/usr/home/main");
		}

		if (page <= 0) {
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
	@RequestMapping("/usr/product/exposureList")
	public String showProductExposureList(Model model, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String searchKeyword, @RequestParam(defaultValue = "20") int itemsNum) {

		if (page <= 0) {
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

		return "/usr/product/exposureList";
	}

	// 상품 수정 페이지
	@RequestMapping("/usr/product/modify")
	public String showModify(Model model, int id, int storeId, String storeModifyAuthKey) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), storeId,
				storeModifyAuthKey);

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
	public String doModify(HttpServletRequest req, int storeId, int id, String storeModifyAuthKey, String productName,
			String productPrice, String productCategory, String productStock, int productDlvy, String productCourier, String productCourierCode,
			String productDlvyPrice, String productBody, MultipartRequest multipartRequest) {
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
		productService.doModify(id, productName, productPrice, productCategory, productStock, productDlvy,
				productCourier, productCourierCode, productDlvyPrice, productBody);

		return Utility.jsReplace("상품정보를 수정했습니다!",
				Utility.f("list?id=%d&storeModifyAuthKey=%s", storeId, storeModifyAuthKey));
	}

	// 등록 상품 삭제
	@RequestMapping("/usr/product/doDelete")
	@ResponseBody
	public String doDelete(int id, int storeId, String storeModifyAuthKey) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), storeId,
				storeModifyAuthKey);

		if (storeVerifyTestRD.isFail()) {
			return Utility.jsReplace(storeVerifyTestRD.getMsg(), "/usr/home/main");
		}

		genFileService.deleteGenFiles("product", id, "extra", "productImg", 1);
		genFileService.deleteGenFiles("product", id, "extra", "productImg", 2);
		genFileService.deleteGenFiles("product", id, "extra", "productImg", 3);

		productService.deleteProduct(id);

		return Utility.jsReplace("등록된 상품을 삭제했습니다!",
				Utility.f("list?id=%d&storeModifyAuthKey=%s", storeId, storeModifyAuthKey));
	}

	// 상품 구매 페이지
	@RequestMapping("/usr/product/orderSheet")
	public String showOrderSheet(Model model, int id, int storeId, String productPrice, int productCnt) {
		Store store = storeService.getStoreById(storeId);
		Product product = productService.getProduct(id);

		if (store.getId() != product.getStoreId()) {
			return rq.jsReturnOnView("잘못된 정보입니다, 다시 시도해주세요.", true);
		}

		model.addAttribute("store", store);
		model.addAttribute("product", product);

		return "/usr/product/orderSheet";
	}

	// 상품 구매
	@RequestMapping("/usr/product/buyProduct")
	@ResponseBody
	public String buyProduct(int id, int productCnt, int storeId, int memberId, String impUID, String orderNum,
			String name, String cellphoneNum, String cellphoneNum2,
			String zipNo, String roadAddr, String addrDetail, String dlvyMemo) {
	
		String _token = getToken();
		int buyId = 0;

		try {
			String url = Utility.f("https://api.iamport.kr/payments/%s?_token=%s", impUID, _token);
			
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> repsonse = restTemplate.getForObject(url, Map.class);
			Map<String, Object> innerMap = (Map<String, Object>) repsonse.get("response");
			
			buyId = productService.buyProduct(id, productCnt, storeId, memberId, impUID, orderNum, name, cellphoneNum, cellphoneNum2, zipNo, roadAddr, addrDetail, dlvyMemo);
			
		} catch (Exception e) {
			e.printStackTrace();
			
			return Utility.jsReplace("상품 구매에 실패하였습니다.", Utility.f("/usr/product/view?id=%d&storeId=%d", id, storeId));
		}
		
		return Utility.jsReplace("", Utility.f("/usr/product/orderStatus?id=%d", buyId));
	}
	
	// 주문 상세보기 || 로그인 멤버
	@RequestMapping("/usr/product/orderStatus")
	public String showOrderStatus(Model model, int id) {
		PurchaseList purchase = productService.getPurchase(id);
		int isReview = productService.isReview(purchase.getProductId(), rq.getLoginedMemberId());
		
		if(purchase.getMemberId() != rq.getLoginedMemberId()) {
			return rq.jsReturnOnView("잘못된 정보입니다, 다시 시도해주세요.", false, "usr/main/home");
		}
		
		String url = Utility.f("https://api.iamport.kr/payments/%s?_token=%s", purchase.getImpUID(), getToken());
		
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> repsonse = restTemplate.getForObject(url, Map.class);
		Map<String, Object> innerMap = (Map<String, Object>) repsonse.get("response");
		
		// 지금은 결제 시스템 상황 상 완료로 진행 나중엔 status 값을 받아서 해당 값을 전달, 해당 값에 따라 결제 진행중, 결제 완료 표시
		model.addAttribute("payMethod", innerMap.get("pay_method"));
		model.addAttribute("vbank_name", innerMap.get("vbank_name"));
		model.addAttribute("vbank_holder", innerMap.get("vbank_holder"));
		model.addAttribute("vbank_num", innerMap.get("vbank_num"));
		model.addAttribute("purchase", purchase);
		model.addAttribute("isReview", isReview);
		
		return "/usr/product/orderStatus";
	}
	
	// 주문 확인/배송 조회 페이지 || 로그인 멤버
	@RequestMapping("/usr/product/orderList")
	public String showOrderList(Model model, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String searchKeyword, @RequestParam(defaultValue = "20") int itemsNum) {
		if (page <= 0) {
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다.", true);
		}
		
		// 현재 등록된 상품 수
		int purchaseCount = productService.getPurchaseCount(searchKeyword, rq.getLoginedMemberId());
		// 한 페이지에 나올 스토어 수
		int itemsInAPage = itemsNum;
		// 상품 수에 따른 페이지 수 계산
		int pagesCount = (int) Math.ceil(purchaseCount / (double) itemsInAPage);

		List<PurchaseList> purchaseList = productService.getPurchaseList(searchKeyword, itemsInAPage, page, rq.getLoginedMemberId());

		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("purchaseCount", purchaseCount);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("purchaseList", purchaseList);
		model.addAttribute("page", page);
		
		return "/usr/product/orderList";
	}
	
	// 주문 목록 페이지 || 판매자
	@RequestMapping("/usr/product/sellerOrderList")
	public String showSellerOrderList(Model model, int id, String storeModifyAuthKey,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "") String searchKeyword, @RequestParam(defaultValue = "20") int itemsNum) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), id,
				storeModifyAuthKey);

		if (storeVerifyTestRD.isFail()) {
			return rq.jsReturnOnView(storeVerifyTestRD.getMsg(), false, "/usr/home/main");
		}
		
		if (page <= 0) {
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다.", true);
		}
		
		// 현재 등록된 상품 수
		int orderCount = productService.getOrderCount(searchKeyword, id);
		// 한 페이지에 나올 스토어 수
		int itemsInAPage = itemsNum;
		// 상품 수에 따른 페이지 수 계산
		int pagesCount = (int) Math.ceil(orderCount / (double) itemsInAPage);

		List<PurchaseList> orderList = productService.getOrderList(searchKeyword, itemsInAPage, page, id);

		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("orderCount", orderCount);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("orderList", orderList);
		model.addAttribute("page", page);
		
		return "/usr/product/sellerOrderList";
	}

	// 구매 확정
	@RequestMapping("/usr/product/confirmPurchase")
	@ResponseBody
	public String doConfirmPurchase(int id, int memberId) {
		if(memberId != rq.getLoginedMemberId()) {
			return Utility.jsReplace("잘못된 방식의 접근입니다.", "/usr/home/main");
		}
		
		productService.confirmPurchase(id);
		
		return Utility.jsReplace("", "/usr/product/orderList");
	}
	
	// 주문 취소
	@RequestMapping("/usr/product/cancelPurchase")
	@ResponseBody
	public String doCancelPurchase(int id, int memberId) {
		if(memberId != rq.getLoginedMemberId()) {
			return Utility.jsReplace("잘못된 방식의 접근입니다.", "/usr/home/main");
		}
		
		productService.cancelPurchase(id);
		
		return Utility.jsReplace("", "/usr/product/orderList");
	}
	
	// 주문 취소 | 판매자
	@RequestMapping("/usr/product/sellerCancelPurchase")
	@ResponseBody
	public String doSellerCancelPurchase(int storeId, String storeModifyAuthKey, int orderId, int productId, int ordPCnt) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), storeId, storeModifyAuthKey);

		if (storeVerifyTestRD.isFail()) {
			return rq.jsReturnOnView(storeVerifyTestRD.getMsg(), false, "/usr/home/main");
		}
		
		productService.sellerCancelPurchase(orderId, productId, ordPCnt);
		
		return Utility.jsReplace("주문이 취소됐습니다.", Utility.f("sellerOrderList?id=%d&storeModifyAuthKey=%s", storeId, storeModifyAuthKey));
	}
	
	// 주문 확정, 운송장 번호 작성 | 판매자
	@RequestMapping("/usr/product/checkPurchase")
	@ResponseBody
	public String doCheckPurchase(int storeId, String storeModifyAuthKey, int orderId, int productId, int ordCheck, int ordPCnt, String waybill) {
		// 인증코드 및 본인 스토어 검사
		ResultData<Store> storeVerifyTestRD = storeService.StoreVerifyTest(rq.getLoginedMemberId(), storeId, storeModifyAuthKey);

		if (storeVerifyTestRD.isFail()) {
			return rq.jsReturnOnView(storeVerifyTestRD.getMsg(), false, "/usr/home/main");
		}
		
		productService.checkPurchase(orderId, productId, ordCheck, ordPCnt, waybill);
		
		return Utility.jsReplace("주문을 확정했습니다.", Utility.f("sellerOrderList?id=%d&storeModifyAuthKey=%s", storeId, storeModifyAuthKey));
	}
	
	// 리뷰 작성 페이지 | 구매자
	@RequestMapping("/usr/product/review")
	public String showReview(Model model, int storeId, int productId) {
		Store store = storeService.getStoreById(storeId);
		Product product = productService.getProduct(productId);

		if (store.getId() != product.getStoreId()) {
			return rq.jsReturnOnView("잘못된 정보입니다, 다시 시도해주세요.", true);
		}

		model.addAttribute("store", store);
		model.addAttribute("product", product);
		
		return "/usr/product/review";
	}
	
	// 리뷰 작성
	@RequestMapping("/usr/product/createReview")
	@ResponseBody
	public String doCreateReview(int storeId, int productId, int memberId, int rating, String reviewBody, MultipartRequest multipartRequest) {
		// 유효성 검사
		if (Utility.isEmpty(storeId) || Utility.isEmpty(productId) || Utility.isEmpty(memberId)) {
			return Utility.jsReplace("잘못된 경로로 접속하셨습니다.", "/usr/home/main");
		}
		if (Utility.isEmpty(rating)) {
			return Utility.jsHistoryBack("평점을 입력해주세요.");
		}
		if (Utility.isEmpty(reviewBody)) {
			return Utility.jsHistoryBack("리뷰 내용을 작성해주세요.");
		}
		
		int id = productService.createReview(storeId, productId, memberId, rating, reviewBody);
		
		// 이미지 업로드
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, id);
			}
		}
		
		return Utility.jsClose("리뷰가 등록됐습니다!");
	}
	
	// 토큰 생성
	public String getToken() {
		String apiKey = "8281306145407044";
		String apiSecret = "SFSXtMJviOeWzXxJHsSXUbXY7NV8cYrde5r6C5whuVrnjfJxI6paBcRkxJXIcJFBvSk2iP6hrtjGTOB0";
		String url = "https://api.iamport.kr/users/getToken";
		String _token = "";

		try {
			// https://velog.io/@dogyun-k/RestTemplate%EB%A1%9C-%EB%8B%A4%EB%A5%B8-%EC%84%9C%EB%B2%84%EC%99%80-%ED%86%B5%EC%8B%A0%ED%95%98%EA%B8%B0

			// HTTP 통신 + RESTful API
			RestTemplate restTemplate = new RestTemplate();

			// 헤더 객체 생성, ContentType은 JSON | JSON 데이터로 요청, 주고 받고 하는 것
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			// 바디를 위한 Map 생성
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("imp_key", apiKey);
			requestBody.put("imp_secret", apiSecret);

			// 헤더와 바디를 합치는 과정
			HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

			// 합친것을 토대로 POST 방식으로 요청을 보냄, 서버 응답과 정보를 받아옴
			ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

			// 바디에 담긴 데이터를 불러옴
			Map<String, Object> responseBody = response.getBody();
			Map<String, Object> innerMap = (Map<String, Object>) responseBody.get("response");

			_token = (String) innerMap.get("access_token");
		} catch (Exception e) {
			e.printStackTrace();

			_token = "";
		}

		return _token;
	}
}
