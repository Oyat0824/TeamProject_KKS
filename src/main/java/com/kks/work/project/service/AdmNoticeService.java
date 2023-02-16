package com.kks.work.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.AdmNoticeRepository;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Notice;

@Service
public class AdmNoticeService {

	private AdmNoticeRepository admnoticeRepository;
	
	@Autowired
	public AdmNoticeService(AdmNoticeRepository admnoticeRepository) {
		this.admnoticeRepository = admnoticeRepository;
	}
	
	
	public Notice getNotice(int id) {
		return admnoticeRepository.getNotice(id);
	}
	
	// 공지글 작성
	public ResultData<Integer> writeNotice(int memberId, String title, String body) {
		admnoticeRepository.writeNotice(memberId, title, body);
		int id = admnoticeRepository.getLastInsertId();
		
		return ResultData.from("S-1", Utility.f("%d번 공지글이 생성되었습니다.", id), "id", id);
	}

	public Notice getForPrintNotice(int loginedMemberId, int id) {
		Notice notice = admnoticeRepository.getForPrintNotice(id);
		
		actorCanChangeData(loginedMemberId, notice); 
		
		return notice;
	}

	private void actorCanChangeData(int loginedMemberId, Notice notice) {
		if(notice == null) { 
			return;
		}
		
		ResultData actorCanChangeDataRd = actorCanMD(loginedMemberId, notice);
		
		notice.setActorCanChangeData(actorCanChangeDataRd.isSuccess());
		
	}

	public ResultData actorCanMD(int loginedMemberId, Notice notice) {
		if(notice == null) { 
			return ResultData.from("F-1", Utility.f("해당 공지글은 존재하지 않습니다."));
		}
		
		return ResultData.from("S-1", "가능 ");
	}

	// 공지 리스트 카운트
	public int getNoticesCount(String searchKeywordTypeCode, String searchKeyword) {
		return admnoticeRepository.getNoticesCount(searchKeywordTypeCode, searchKeyword);
	}

	// 공지 리스트 페이징
	public List<Notice> getNotices(String searchKeywordTypeCode, String searchKeyword, int itemsInAPage, int page) {
		int limitStart = (page - 1) * itemsInAPage;

		return admnoticeRepository.getNotices(searchKeywordTypeCode, searchKeyword, limitStart, itemsInAPage);
	}

	// 공지 삭제
	public void deleteNotice(int id) {
		admnoticeRepository.deleteNotice(id);	
	}

	// 공지 수정
	public void modifyNotice(int id, String title, String body) {
		admnoticeRepository.modifyNotice(id, title, body);
		
	}

	// 조회 카운트
	public ResultData<Integer> increaseViewCount(int id) {
		int affectedRowsCount =  admnoticeRepository.increaseViewCount(id);
		
		// 공지가 없는 경우
		if(affectedRowsCount == 0) { 
			return ResultData.from("F-1", "해당 공지는 존재하지 않습니다.", "affectedRowsCount", affectedRowsCount);
		}
		
		return ResultData.from("S-1", "조회 수 증가", "affectedRowsCount", affectedRowsCount);
	}

	// 조회수 가져오기
	public Integer getNoticeViewCount(int id) {
		return admnoticeRepository.getNoticeViewCount(id);
	}
	
}
