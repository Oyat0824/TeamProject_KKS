package com.kks.work.project.controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kks.work.project.service.MemberService;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Member;
import com.kks.work.project.vo.Rq;

@Controller
public class UsrMemberController {
	private MemberService memberService;
	private Rq rq;

	@Autowired
	public UsrMemberController(MemberService memberService, Rq rq) {
		this.memberService = memberService;
		this.rq = rq;
	}

// 액션 메서드
	// 회원가입 페이지
	@RequestMapping("/usr/member/join")
	public String showJoin() {
		return "usr/member/join";
	}
	
	// 회원가입
	@RequestMapping("/usr/member/doJoin")
	@ResponseBody
	public String doJoin(String loginId, String loginPw, String loginPwChk, String name, String gender, String birthday, String email, String cellphoneNum) {
		// 정규식
		String reg_num = "^01([0|1|6|7|8|9])-([0-9]{3,4})-([0-9]{4})$"; // 휴대폰 번호
		String reg_email = "^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$"; // 길이까지 확실한 검증
		String reg_id = "^[A-Za-z]{1}[A-Za-z0-9_-]{3,19}$"; // 반드시 영문으로 시작 숫자+언더바/하이픈 허용 4~20자리
		
		// 유효성 검사
		if (Utility.empty(loginId)) {
			 return Utility.jsHistoryBack("아이디를 입력해주세요!");
		}
		if (!Pattern.matches(reg_id, loginId)) {
			return Utility.jsHistoryBack("4글자 이상으로 영문 + 숫자 조합으로 입력해주세요.\\n" + "언더바(_), 하이픈(-)도 조합으로 사용가능합니다.");
		}
		if (Utility.empty(loginPw)) {
			return Utility.jsHistoryBack("비밀번호를 입력해주세요!");
		}
		if (Utility.empty(loginPwChk)) {
			return Utility.jsHistoryBack("비밀번호 확인을 입력해주세요!");
		}
		if (Utility.empty(name)) {
			return Utility.jsHistoryBack("이름을 입력해주세요!");
		}
		if (Utility.empty(gender) || !gender.equals("male") && !gender.equals("female")) {
			return Utility.jsHistoryBack("성별을 선택해주세요!");
		}
		if (Utility.empty(birthday)) {
			return Utility.jsHistoryBack("생일을 입력해주세요!");
		}
		if (Utility.empty(email)) {
			return Utility.jsHistoryBack("이메일을 입력해주세요!");
		}
		if (!Pattern.matches(reg_email, email)) {
			return Utility.jsHistoryBack("이메일 형식이 틀렸습니다.\\n" + "ex) abc123@email.com" );
		}
		if (Utility.empty(cellphoneNum)) {
			return Utility.jsHistoryBack("전화번호를 입력해주세요!");
		}
		if (!Pattern.matches(reg_num, cellphoneNum)) {
			return Utility.jsHistoryBack("전화번호 형식이 틀렸습니다.\\n" + "ex) 010-1234-5678");
		}
		if (loginPw.equals(loginPwChk) == false) {
			return Utility.jsHistoryBack("비밀번호와 비밀번호 확인 부분이 일치하지 않습니다!");
		}

		// SHA256_SALT 생성
		String salt = Utility.getTempPassword(20);
		
		// 회원가입 실행
		ResultData<Integer> doJoinRd = memberService.doJoin(loginId, Utility.getEncrypt(loginPw, salt), name, gender, birthday, email, cellphoneNum, salt);
		
		// 회원가입 실패
		if (doJoinRd.isFail()) {
			return Utility.jsHistoryBack(doJoinRd.getMsg());
		}
		
		Member member = memberService.getMemberById( (int) doJoinRd.getData1() );
		
		return Utility.jsReplace(Utility.f("%s님 가입을 축하드립니다.", member.getName()), "/");
	}
	
	// 존재하는 회원인지 체크
	@RequestMapping("/usr/member/getLoginIdDup")
	@ResponseBody
	public ResultData<String> getLoginIdDup(String loginId) {
		// 유효성 검사
		if(Utility.empty(loginId)) return ResultData.from("F-1", "아이디를 입력해주세요");
		
		Member member = memberService.getMemberByLoginId(loginId);
		
		if (member != null) {
			return ResultData.from("F-2", "이미 존재하는 아이디입니다.", "loginId", loginId);
		}
		
		return ResultData.from("S-1", "사용 가능한 아이디입니다.", "loginId", loginId);
	}
	
	// 로그인 페이지
	@RequestMapping("/usr/member/login")
	public String showLogin() {
		return "usr/member/login";
	}

	// 로그인
	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw) {
		// 유효성 검사
		if (Utility.empty(loginId)) {
			return Utility.jsHistoryBack("아이디를 입력해주세요!");
		}
		if (Utility.empty(loginPw)) {
			return Utility.jsHistoryBack("비밀번호를 입력해주세요!");
		}
		
		// 로그인ID 로 멤버가 존재하는지 확인
		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null) {
			return Utility.jsHistoryBack(Utility.f("존재하지 않는 아이디(%s)입니다.", loginId));
		}

		if (member.getLoginPw().equals(Utility.getEncrypt(loginPw, member.getSalt())) == false) {
			return Utility.jsHistoryBack("비밀번호가 일치하지 않습니다!");
		}
		
		// Rq 객체에 멤버를 넣음 및 생성
		rq.login(member);

		return Utility.jsReplace(Utility.f("%s님 환영합니다.", member.getName()), "/");
	}
}
