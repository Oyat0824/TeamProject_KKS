package com.kks.work.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kks.work.project.service.GenFileService;
import com.kks.work.project.service.MemberService;
import com.kks.work.project.service.ProductService;
import com.kks.work.project.service.StoreService;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Member;
import com.kks.work.project.vo.Product;
import com.kks.work.project.vo.Rq;
import com.kks.work.project.vo.Store;

@Controller
public class AdmController {
	private StoreService storeService;
	private ProductService productService;
	private MemberService memberService;
	private Rq rq;
	private GenFileService genFileService;

	@Autowired
	public AdmController(StoreService storeService, ProductService productService, MemberService memberService, Rq rq, GenFileService genFileService) {
		this.storeService = storeService;
		this.productService = productService;
		this.memberService = memberService;
		this.rq = rq;
		this.genFileService = genFileService;
	}
	
// 액션 메서드
	
	// 관리자 페이지
	@RequestMapping("/admin/AdminPage")
	public String showMyPage() {
		return "/admin/AdminPage";
	}
		
	// 멤버 리스트
	@RequestMapping("/admin/member/list")
	public String showMemberList(Model model, 
			@RequestParam(defaultValue = "0") int memberType,
			@RequestParam(defaultValue = "loginId,name") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword, @RequestParam(defaultValue = "1") int page) {

		if (page <= 0) {
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다!", true);
		}
		
		// 멤버 수
		int membersCount = memberService.getMembersCount(memberType, searchKeywordTypeCode, searchKeyword) - 1;
		// 한 페이지에 나올 멤버 수
		int itemsInAPage = 10;
		// 멤버 수에 따른 페이지 수 계산
		int pagesCount = (int) Math.ceil((double) membersCount / itemsInAPage);

		List<Member> members = memberService.getMembers(memberType, searchKeywordTypeCode, searchKeyword, itemsInAPage,
				page);

		model.addAttribute("memberType", memberType);
		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("page", page);
		model.addAttribute("members", members);
		model.addAttribute("membersCount", membersCount);
		model.addAttribute("pagesCount", pagesCount);

		return "/admin/member/list";
	}
	
	// 관리자 권한으로 회원 선택 삭제기능
	@RequestMapping("/admin/member/doDeleteMembers")
	@ResponseBody
	public String doDeleteMembers(@RequestParam(defaultValue = "") String ids) {

		if (Utility.isEmpty(ids)) {
			return Utility.jsHistoryBack("선택한 회원이 없습니다!");
		}

		if (ids.equals("9")) {
			return Utility.jsHistoryBack("관리자 계정은 삭제할 수 없습니다!");
		}

		List<Integer> memberIds = new ArrayList<>();

		for (String idStr : ids.split(",")) {
			memberIds.add(Integer.parseInt(idStr));
		}

		memberService.deleteMembers(memberIds);

		return Utility.jsReplace("선택한 회원이 삭제되었습니다!", "list");
	}
	
	// 상점 리스트
	@RequestMapping("/admin/store/list") 
	public String showStoreList(Model model,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "storeName, memberId") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword,
			@RequestParam(defaultValue = "10") int itemsNum) {

		if (page <= 0) {
			return rq.jsReturnOnView("페이지 번호가 올바르지 않습니다!", true);
		}

		// 스토어 수
		int storesCount = storeService.getStoresAdmCount(searchKeyword, searchKeywordTypeCode);
		// 한 페이지에 나올 스토어 수
		int itemsInAPage = itemsNum;
		// 스토어 수에 따른 페이지 수 계산
		int pagesCount = (int) Math.ceil(storesCount / (double) itemsInAPage);

		List<Store> stores = storeService.getStoresAdm(searchKeywordTypeCode, searchKeyword, itemsInAPage, page);

		model.addAttribute("stores", stores);
		model.addAttribute("storesCount", storesCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);

		return "/admin/store/list";
	}
	
	// 관리자 권한으로 스토어 삭제
	@RequestMapping("/admin/store/doDeleteStores")
	@ResponseBody 
	public String doDeleteStores(@RequestParam(defaultValue = "") String ids) {
			
		if (Utility.isEmpty(ids)) {
			return Utility.jsHistoryBack("선택한 상품이 없습니다!");
		}

		List<Integer> storeIds = new ArrayList<>();

		for (String idStr : ids.split(",")) {
			storeIds.add(Integer.parseInt(idStr));
		}

		storeService.AdmdeleteStore(storeIds);
																			
		return Utility.jsReplace("등록된 상품을 삭제했습니다!", "list");

	} 
	
	// 상품 리스트
	@RequestMapping("/admin/product/list")
	public String showProductList(Model model,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "storeName, productName") String searchKeywordTypeCode, 
			@RequestParam(defaultValue = "") String searchKeyword,
			@RequestParam(defaultValue = "10") int itemsNum) {
		
		if(page <= 0) {
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다!", true);
		}
		
		// 현재 등록된 상품 수
		int productsCount = productService.getMyStoreProductsAdmCount(searchKeywordTypeCode, searchKeyword);
		// 한 페이지에 나올 상품 수
		int itemsInAPage = itemsNum;
		// 상품 수에 따른 페이지 수 계산
		int pagesCount = (int) Math.ceil(productsCount / (double) itemsInAPage);

		List<Product> products = productService.getProductsAdm(searchKeywordTypeCode, searchKeyword, itemsInAPage, page);

		model.addAttribute("products", products);
		model.addAttribute("productsCount", productsCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("searchKeyword", searchKeyword);
		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		
		return "/admin/product/list";
	}
	
	// 관리자 권한으로 상품 삭제
	@RequestMapping("/admin/product/doDeleteProducts")
	@ResponseBody 
	public String doDeleteProducts(@RequestParam(defaultValue = "") String ids) {
		
		if (Utility.isEmpty(ids)) {
			return Utility.jsHistoryBack("선택한 상품이 없습니다!");
		}

		List<Integer> productIds = new ArrayList<>();

		for (String idStr : ids.split(",")) {
			productIds.add(Integer.parseInt(idStr));
		}

		productService.AdmdeleteProduct(productIds);
																		
		return Utility.jsReplace("등록된 상품을 삭제했습니다!", "list");

	} 
			
}