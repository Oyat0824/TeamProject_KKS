package com.kks.work.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kks.work.project.service.AdmNoticeService;
import com.kks.work.project.service.GenFileService;
import com.kks.work.project.service.MemberService;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Notice;
import com.kks.work.project.vo.Rq;

@Controller
public class AdmNoticeController {
	private AdmNoticeService admnoticeService;
	private MemberService memberService;
	private GenFileService genFileService;
	private Rq rq;
	
	// 의존성 주입 - 객체만들지 않아도 됨
	@Autowired 
	public AdmNoticeController(AdmNoticeService admnoticeService, MemberService memberService, GenFileService genFileService, Rq rq){
		this.admnoticeService = admnoticeService;
		this.memberService = memberService;
		this.genFileService = genFileService;
		this.rq = rq;
	}
	
// 액션 메서드
	// 공지 작성 페이지
	@RequestMapping("/admin/notice/write")
	public String showWrite() {
		return "/admin/notice/write";
	}
	
	// 공지 등록
	@RequestMapping("/admin/notice/doWrite")
	@ResponseBody 					
	public String doWrite(String title, String body) {
		
		// 유효성 검사
		if(Utility.isEmpty(title)) { 
			return Utility.jsHistoryBack("제목을 입력해주세요.");
		}
		if(Utility.isEmpty(body)) { 
			return Utility.jsHistoryBack("내용을 입력해주세요.");
		}
		
		ResultData<Integer> writeNoticeRd = admnoticeService.writeNotice(rq.getLoginedMemberId(), title, body);
			
		int id = writeNoticeRd.getData1();
		
		return Utility.jsReplace(Utility.f("%d번 공지글이 생성되었습니다", id), Utility.f("detail?id=%d", id));
	}
	
	// 상세보기
	@RequestMapping("/admin/notice/detail")
	public String showDetail(Model model, int id) { 

		Notice notice = admnoticeService.getForPrintNotice(rq.getLoginedMemberId(), id);

		model.addAttribute("notice", notice); 

		return "/admin/notice/detail";
	}
	
	// 공지 리스트
	@RequestMapping("/admin/notice/list")
	public String showList(Model model, 
			@RequestParam(defaultValue = "1") int page, 
			@RequestParam(defaultValue = "title") String searchKeywordTypeCode,
			@RequestParam(defaultValue = "") String searchKeyword) {
		
		if(page <= 0) { 
			return rq.jsReturnOnView("페이지번호가 올바르지 않습니다.", true);
		}
	
		int noticesCount = admnoticeService.getNoticesCount(searchKeywordTypeCode, searchKeyword);
		
		int itemsInAPage = 10;
		
		int pagesCount = (int) Math.ceil((double) noticesCount / itemsInAPage);

		List<Notice> notices = admnoticeService.getNotices(searchKeywordTypeCode, searchKeyword, itemsInAPage, page);
		 
		model.addAttribute("notices", notices); 
		model.addAttribute("noticesCount", noticesCount);
		model.addAttribute("page", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("searchKeywordTypeCode", searchKeywordTypeCode);
		model.addAttribute("searchKeyword", searchKeyword);
		
		return "/admin/notice/list";
	} 
	
	// 공지 삭제
	@RequestMapping("/admin/notice/doDelete")
	@ResponseBody 
	public String doDelete(int id) {

		Notice notice = admnoticeService.getNotice(id);

		ResultData actorCanMDRd = admnoticeService.actorCanMD(rq.getLoginedMemberId(), notice); 
		
		if(actorCanMDRd.isFail()) {  // 실패시에 
			return Utility.jsHistoryBack(actorCanMDRd.getMsg());
		}
		
		admnoticeService.deleteNotice(id);
																		// 경로
		return Utility.jsReplace(Utility.f("해당 공지를 삭제했습니다.", id), "list?id=1");
	}
	
	// 공지 수정
	@RequestMapping("/admin/notice/modify") 	
	public String showModify(Model model, int id) { 
		
		Notice notice = admnoticeService.getForPrintNotice(rq.getLoginedMemberId(), id);
		
		// 현재 수정이 가능한가 체크
		ResultData actorCanMDRd = admnoticeService.actorCanMD(rq.getLoginedMemberId(), notice); 
		
		if(actorCanMDRd.isFail()) {  // 실패시에 
			return rq.jsReturnOnView(actorCanMDRd.getMsg(), true);
		}
		
		model.addAttribute("notice", notice);
		
		return "/admin/notice/modify";
	}

	// 공지 수정
	@RequestMapping("/admin/notice/doModify") 
	@ResponseBody 
	public String doModify(int id, String title, String body) { 
		
		Notice notice = admnoticeService.getNotice(id);
		
		// 현재 수정이 가능한가 체크
		ResultData actorCanMDRd = admnoticeService.actorCanMD(rq.getLoginedMemberId(), notice); 
		
		if(actorCanMDRd.isFail()) {  // 실패시에 
			return Utility.jsHistoryBack(actorCanMDRd.getMsg());
		}
		
		admnoticeService.modifyNotice(id, title, body);
		
		return Utility.jsReplace(Utility.f("공지를 수정했습니다.", id), Utility.f("detail?id=%d", id));
	}
	
	// 공지 조회 수
	@RequestMapping("/admin/notice/doIncreaseViewCountRd")
	@ResponseBody
	public ResultData<Integer> doIncreaseViewCountRd(int id) { 
		
		ResultData<Integer> increaseViewCountRd = admnoticeService.increaseViewCount(id);
		
		if(increaseViewCountRd.isFail()) { // 실패한 경우
			return increaseViewCountRd;
		}
		
		ResultData<Integer> rd = ResultData.from(increaseViewCountRd.getResultCode(), increaseViewCountRd.getMsg(), "viewCount", admnoticeService.getNoticeViewCount(id));
		
		rd.setData2("id", id);
		
		return rd;
	}
			
}