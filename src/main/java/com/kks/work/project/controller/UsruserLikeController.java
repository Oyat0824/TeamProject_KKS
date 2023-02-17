package com.kks.work.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kks.work.project.service.UserLikeService;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Rq;

@Controller
public class UsruserLikeController {
	private UserLikeService userLikeService;
	private Rq rq;
	

	@Autowired
	public UsruserLikeController(UserLikeService userLikeService, Rq rq) {
		this.userLikeService = userLikeService;
		this.rq = rq;
	
	}

// 액션 메서드
	@RequestMapping("/usr/userLike/setUserLike")
	@ResponseBody
	public ResultData<Integer> setUserLike(int productId) {
		System.out.println("?????");
		//해당 아이디의 멤버가 해당 상품을 찜했는지 안했는지 확인
		int isUserLike = userLikeService.isUserLike(rq.getLoginedMemberId(),productId);
		
		System.out.println(isUserLike);
		
		//isUserLike가 1이면 찜을 했으므로 데이터 삭제
		if(isUserLike == 1) {
			System.out.println("========1");
			userLikeService.removeUserLike(rq.getLoginedMemberId(), productId);
			
			//ResultData에 0이 담기면 찜 데이터 없음
			return ResultData.from("찜 데이터 삭제성공" , "찜삭제" , "찜데이터 존재여부" , 0);
			
			 
		}
		
		
		//isUserLike가 1이면 찜을 했으므로 데이터 추가
		else  {
			System.out.println("========2");
			userLikeService.addUserLike(rq.getLoginedMemberId(), productId);
			
			//ResultData에 1이 담기면 찜 데이터 있음
			return ResultData.from("찜 데이터 추가성공" , "찜추가" , "찜데이터 존재여부" , 1);
		}
		
		
		
	}
	
	
	
	
	
	
	
	
/*	// 찜목록 추가
	@RequestMapping("/usr/userLike/add")
	@ResponseBody
	public String addUserLike(String relTypeCode , int relId ) {
		
		userLikeService.addUserLike(rq.getLoginedMemberId(), relTypeCode , relId);
		 return Utility.jsHistoryBack("해당상품을 찜했습니다.");
	
	}
	
	//찜목록 삭제
	@RequestMapping("/usr/userLike/remove")
	@ResponseBody
	public String removeUserLike(String relTypeCode, int relId) {
		userLikeService.removeUserLike(rq.getLoginedMemberId(), relTypeCode , relId);
		return Utility.jsHistoryBack("해당상품 찜을 해제했습니다.");
	}
	
*/	

	
	
}


