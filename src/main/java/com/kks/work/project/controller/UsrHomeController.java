package com.kks.work.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kks.work.project.service.GenFileService;
import com.kks.work.project.service.ProductService;
import com.kks.work.project.service.StoreService;
import com.kks.work.project.vo.Product;
import com.kks.work.project.vo.Rq;
import com.kks.work.project.vo.Store;

@Controller
public class UsrHomeController {
	private StoreService storeService;
	private ProductService productService;

	@Autowired
	public UsrHomeController(StoreService storeService, ProductService productService, Rq rq) {
		this.storeService = storeService;
		this.productService = productService;
	}
	
	@RequestMapping("/usr/home/main")
	public String showMain(Model model) {
		List<Store> stores = storeService.getStores("", 5, 1, "");
		List<Product> Products = productService.getExposureProducts("", 5, 1, "");

		model.addAttribute("stores", stores);
		model.addAttribute("Products", Products);
		
		return "usr/home/main";
	}

	@RequestMapping("/")
	public String showRoot() {
		return "redirect:/usr/home/main";
	}
}