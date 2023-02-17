package com.kks.work.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kks.work.project.util.Utility;
import com.kks.work.project.service.ProductService;
import com.kks.work.project.service.StoreService;
import com.kks.work.project.service.UserLikeService;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.vo.Rq;
import com.kks.work.project.vo.Store;
import com.kks.work.project.vo.UserLike;

@Controller
public class UserLikeController {

	private UserLikeService userLikeService;
	private StoreService storeService;
	private ProductService productService;
	private Rq rq;

	@Autowired
	public UserLikeController(UserLikeService userLikeService, StoreService storeService, ProductService productService,
			Rq rq) {
		this.userLikeService = userLikeService;
		this.storeService = storeService;
		this.productService = productService;
		this.rq = rq;
	}

	@RequestMapping("/usr/store/getUserLikePoint")
	@ResponseBody
	public ResultData<UserLike> getUserLikePoint(int id, String relTypeCode) {
		
		Store store = storeService.getStoreById(id);
		
		if(store == null) { return ResultData.from("F-1", "해당  스토어는 존재하지 않습니다"); }
		
		UserLike userLike = userLikeService.getUserLike(rq.getLoginedMemberId(),
		relTypeCode, id);
		
		return ResultData.from("S-1", "스토어 찜하기 성공", "userLike", userLike);

	}

	// 찜하기
	@RequestMapping("/usr/store/doUserLike")
	@ResponseBody
	public String doUserLike(int id, String relTypeCode, int like) {

		Store store = storeService.getStoreById(id);

		if (store == null) {
			return Utility.jsHistoryBack("해당  스토어는 존재하지 않습니다");
		}

		UserLike userLike = userLikeService.getUserLike(rq.getLoginedMemberId(), relTypeCode, id);
		
		userLikeService.doUserLike(rq.getLoginedMemberId(), like, relTypeCode, id);
		
		if(like == 1) {
			return Utility.jsReplace(Utility.f("%d번 스토어 찜하기", id), Utility.f("../store/detail?id=%d", id));
		} else {
			return Utility.jsReplace(Utility.f("%d번 스토어 찜하기", id), Utility.f("../store/detail?id=%d", id));
		}

	}
	
	// 찜 취소
	@RequestMapping("/usr/store/remUserLike")
	@ResponseBody
	public String delUserLike(int id, String relTypeCode, int like) {
		
		Store store = storeService.getStoreById(id);
		
		if (store == null) {
			return Utility.jsHistoryBack("해당  스토어는 존재하지 않습니다");
		}
		
		UserLike userLike = userLikeService.getUserLike(rq.getLoginedMemberId(), relTypeCode, id);
		
		if(userLike.getGetLikeStore() == 0) {
			return Utility.jsHistoryBack("취소할거 없음");
		}
		
		userLikeService.delUserLike(rq.getLoginedMemberId(), relTypeCode, id);
		
		if(like == 1) {
			return Utility.jsReplace(Utility.f("해당 스토어 찜 취소", id), Utility.f("../store/detail?id=%d", id));
		} else {
			return Utility.jsReplace(Utility.f("해당 스토어 찜 취소", id), Utility.f("../store/detail?id=%d", id));
		}
	}
	
}
