package com.kks.work.project.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.kks.work.project.service.GenFileService;
import com.kks.work.project.service.StoreService;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Rq;
import com.kks.work.project.vo.Store;

@Controller
public class UsrStoreController {
	private StoreService storeService;
	private Rq rq;
	private GenFileService genFileService;

	@Autowired
	public UsrStoreController(StoreService storeService, Rq rq, GenFileService genFileService) {
		this.storeService = storeService;
		this.rq = rq;
		this.genFileService = genFileService;
	}
	
// 액션 메서드
	// 가게 등록 페이지
	@RequestMapping("/usr/store/register")
	public String showRegister() {
		return "/usr/store/register";
	}
	
	// 가게 등록
	@RequestMapping("/usr/store/doRegister") // 주소
	@ResponseBody // 실행할 몸통
	public String doRegister(String storeName, String storeDesc, MultipartRequest multipartRequest) {
		// 유효성 검사
		if (Utility.isEmpty(storeName)) {
			return Utility.jsHistoryBack("가게 이름을 입력해주세요!");
		}

		ResultData<Integer> registerStoreRd = storeService.registerStore(storeName, storeDesc, rq.getLoginedMemberId());
		
		// 상점 등록 실패
		if (registerStoreRd.isFail()) {
			return Utility.jsHistoryBack(registerStoreRd.getMsg());
		}

		int id = registerStoreRd.getData1();
		
		// 프로필 사진이 있다면 업로드
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, id);
			}
		}

		return Utility.jsReplace(Utility.f("가게가 등록됐습니다!", id), Utility.f("store?id=%d", id));
	}
	
	// 존재하는 가게인지 체크
	@RequestMapping("/usr/store/getStoreNameDup")
	@ResponseBody
	public ResultData<String> getLoginIdDup(String storeName) {
		// 유효성 검사
		if(Utility.isEmpty(storeName)) {
			return ResultData.from("F-1", "가게 이름을 입력해주세요");
		}
		
		Store store = storeService.getStoreByStoreName(storeName);
		
		if (store != null) {
			return ResultData.from("F-2", "이미 존재하는 가게 이름입니다.", "storeName", storeName);
		}
		
		return ResultData.from("S-1", "사용 가능한 가게 이름입니다.", "storeName", storeName);
	}
}

