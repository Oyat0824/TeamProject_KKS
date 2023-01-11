package com.kks.work.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kks.work.project.service.MemberService;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Member;

@Controller
public class UsrMemberController {

	private MemberService memberService;

	@Autowired 
	public UsrMemberController(MemberService memberService){
		this.memberService = memberService;
	}

// 액션 메서드
//	회원가입
	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public Object doJoin(String loginId, String loginPw, String name, int gender, String birthday,  String email, String cellphoneNum) {
		
		// 유효성 검사(공백)
		if(loginId == null || loginId.trim().length() == 0) { 
			return "아이디를 입력해주세요.";
		}
		if(loginPw == null || loginPw.trim().length() == 0) { 
			return "비밀번호를 입력해주세요.";
		}
		if(name == null || name.trim().length() == 0) { 
			return "이름을 입력해주세요.";
		}
		if(gender == 0 ) { 
			return "성별을 입력해주세요.";
		}
		if(birthday == null || birthday.trim().length() == 0) { 
			return "생년월일을 입력해주세요.";
		}
		if(email == null || email.trim().length() == 0) { 
			return "이메일을 입력해주세요.";
		}
		if(cellphoneNum == null || cellphoneNum.trim().length() == 0) { 
			return "핸드폰 번호를 입력해주세요.";
		}
		
		String salt = Utility.getTempPassword(20);
		
		int id = memberService.doJoin(loginId, loginPw, name, gender, birthday, email, cellphoneNum, salt);
		
		if(id == -1) { // memberService에서 중복 로그인 아이디가 걸렸을시
			return "이미 사용중인 아이디 입니다.";
		}

		Member member = memberService.getMemberById(id);

		return member;
	}
	
}
