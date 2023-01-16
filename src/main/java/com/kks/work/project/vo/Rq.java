package com.kks.work.project.vo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.kks.work.project.service.MemberService;
import com.kks.work.project.util.Utility;

import lombok.Getter;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Rq {
	@Getter
	private int loginedMemberId;
	@Getter
	private Member loginedMember;

	private HttpServletRequest req;
	private HttpServletResponse res;
	private HttpSession session;

	public Rq(HttpServletRequest req, HttpServletResponse res, MemberService memberService) {
		this.req = req;
		this.res = res;
		this.session = req.getSession();
		
		// 변수 초기화
		int loginedMemberId = 0;
		Member loginedMember = null;
		
		// 세션에 멤버 ID 값이 없다면 집어 넣기
		if (session.getAttribute("loginedMemberId") != null) {
			loginedMemberId = (int) session.getAttribute("loginedMemberId");
			loginedMember = memberService.getMemberById(loginedMemberId);
		}
		
		this.loginedMemberId = loginedMemberId;
		this.loginedMember = loginedMember;
		
	}
	
	// 로그인 시 Rq에 로그인 정보 등록
	public void login(Member member) {
		session.setAttribute("loginedMemberId", member.getId());
	}
	
	// 로그아웃 시 Rq에 로그인 정보 삭제
	public void logout() {
		session.removeAttribute("loginedMemberId");
	}

	// 메시지 출력용
	private void print(String str) {
		try {
			res.getWriter().append(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 인터셉터에서 사용, 메시지 출력
	public void jsPrintHistoryBack(String msg) {
		res.setContentType("text/html; charset=UTF-8");

		print(Utility.jsHistoryBack(msg));
	}
	
	// alert, historyBack, locationReplace 기능 종합
	public String jsReturnOnView(String msg, boolean historyBack, String uri) {
		req.setAttribute("msg", msg);
		req.setAttribute("historyBack", historyBack);
		req.setAttribute("replaceUri", uri);

		return "usr/common/js";
	}
	public String jsReturnOnView(String msg, boolean historyBack) {
		return jsReturnOnView(msg, historyBack, "");
	}
	
	// memberId 로 프로필 사진 Uri 가져오기
	public String getProfileImgUri(int membeId) {
		return "/common/genFile/file/member/" + membeId + "/extra/profileImg/1";
	}

	// 프로필 이미지가 없는 경우
	public String getProfileFallbackImgUri() {
		return "https://via.placeholder.com/160/?text=?";
	}

	// 프로필 이미지가 에러가 난 경우
	public String getProfileFallbackImgOnErrorHtml() {
		return "this.src = '" + getProfileFallbackImgUri() + "'";
	}

	public String getRemoveProfileImgIfNotExitOnErrorHtmlAttr() {
		return "$(this).remove()";
	}
}
