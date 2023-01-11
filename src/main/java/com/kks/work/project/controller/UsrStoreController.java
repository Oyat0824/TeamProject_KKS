package com.kks.work.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kks.work.project.service.StoreService;
import com.kks.work.project.vo.Rq;

@Controller
public class UsrStoreController {
	private StoreService storeService;
	private Rq rq;

	@Autowired
	public UsrStoreController(StoreService storeService, Rq rq) {
		this.storeService = storeService;
		this.rq = rq;
	}
	
// 액션 메서드
	// 상점 등록 페이지
	@RequestMapping("/usr/store/register")
	public String showRegister() {
		return "/usr/store/register";
	}
	
	// 상점 등록
}

