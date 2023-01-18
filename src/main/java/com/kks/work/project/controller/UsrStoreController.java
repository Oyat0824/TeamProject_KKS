package com.kks.work.project.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	// 존재하는 스토어인지 체크
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
	    
	    ResultData<?> actorCanMDRd = storeService.actorCanMD(rq.getLoginedMemberId(), store);

		if (actorCanMDRd.isFail()) {
			return rq.jsReturnOnView(actorCanMDRd.getMsg(), true);
		}
	    
	    model.addAttribute("store", store);
	    
	    return "/usr/store/view";
	}
	
	// 패스워드 확인 페이지
	@RequestMapping("/usr/store/chkPassword")
	public String showChkPassword(int id) {
		Store store = storeService.getForPrintStoreById(rq.getLoginedMemberId(), id);
		
		ResultData<?> actorCanModifyRD = storeService.actorCanMD(rq.getLoginedMemberId(), store);
	    
	    if (actorCanModifyRD.isFail()) {
	    	return rq.jsReturnOnView(actorCanModifyRD.getMsg(), true);
	    }
		
		return "usr/store/chkPassword";
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
		Store store = storeService.getStoreById(id);
		
	    ResultData<?> chkStoreModifyAuthKeyRd = storeService.chkStoreModifyAuthKey(rq.getLoginedMemberId(), storeModifyAuthKey);
	    
	    if (chkStoreModifyAuthKeyRd.isFail()) {
			return rq.jsReturnOnView(chkStoreModifyAuthKeyRd.getMsg(), true);
		}
	    
	    model.addAttribute("store", store);
	    
	    return "/usr/store/modify";
	}
	
	// 스토어 수정
	@RequestMapping("/usr/store/doModify")
	@ResponseBody
	public String doModify(HttpServletRequest req, int id, String storeModifyAuthKey, String storeDesc, MultipartRequest multipartRequest) {
		if (Utility.isEmpty(storeModifyAuthKey)) {
			return Utility.jsHistoryBack("스토어 수정 인증코드가 필요합니다.");
		}
		
		ResultData<?> chkStoreModifyAuthKeyRd = storeService.chkStoreModifyAuthKey(rq.getLoginedMemberId(), storeModifyAuthKey);
	    
	    if (chkStoreModifyAuthKeyRd.isFail()) {
			return rq.jsReturnOnView(chkStoreModifyAuthKeyRd.getMsg(), true);
		}
		
		Store store = storeService.getStoreById(id);

		ResultData<?> actorCanMDRd = storeService.actorCanMD(rq.getLoginedMemberId(), store);

		if (actorCanMDRd.isFail()) {
			return Utility.jsHistoryBack(actorCanMDRd.getMsg());
		}
		
		if (req.getParameter("deleteFile__store__0__extra__storeLogo__1") != null) {
			genFileService.deleteGenFiles("store", id, "extra", "storeLogo", 1);
		}
		
		if (req.getParameter("deleteFile__store__0__extra__storeImg__1") != null) {
			genFileService.deleteGenFiles("store", id, "extra", "storeImg", 1);
		}
		
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
}

