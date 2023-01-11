package com.kks.work.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kks.work.project.service.MemberService;
import com.kks.work.project.vo.Member;

@Controller
public class UsrMemberController {

	private MemberService memberService;

	@Autowired 
	public UsrMemberController(MemberService memberService){
		this.memberService = memberService;
	}

// 액션 메서드
	@RequestMapping("/usr/member/doJoin")
	@ResponseBody 
	public Member doJoin(String loginId, String loginPw, String name, int gender, String birthday, String cellphoneNum, String email) {
		
		int id = memberService.doJoin(loginId, loginPw, name, gender, birthday, cellphoneNum, email);

		Member member = memberService.getMemberById(id);

		return member;
	}
	
}
