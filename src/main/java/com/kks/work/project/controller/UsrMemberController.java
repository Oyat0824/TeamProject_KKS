package com.kks.work.project.controller;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.kks.work.project.service.GenFileService;
import com.kks.work.project.service.MemberService;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Member;
import com.kks.work.project.vo.Rq;

@Controller
public class UsrMemberController {
	private MemberService memberService;
	private Rq rq;
	private GenFileService genFileService;

	@Autowired
	public UsrMemberController(MemberService memberService, Rq rq, GenFileService genFileService) {
		this.memberService = memberService;
		this.rq = rq;
		this.genFileService = genFileService;
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
	public String doJoin(String loginId, String loginPw, String loginPwChk, String name, String gender, String birthday, String email, String cellphoneNum, MultipartRequest multipartRequest) {
		// 정규식
		String reg_num = "^01([0|1|6|7|8|9])-([0-9]{3,4})-([0-9]{4})$"; // 휴대폰 번호
		String reg_email = "^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$"; // 길이까지 확실한 검증
		String reg_id = "^[A-Za-z]{1}[A-Za-z0-9_-]{3,19}$"; // 반드시 영문으로 시작 숫자+언더바/하이픈 허용 4~20자리
		
		// 유효성 검사
		if (Utility.isEmpty(loginId)) {
			 return Utility.jsHistoryBack("아이디를 입력해주세요!");
		}
		if (!Pattern.matches(reg_id, loginId)) {
			return Utility.jsHistoryBack("4글자 이상으로 영문 + 숫자 조합으로 입력해주세요.\\n" + "언더바(_), 하이픈(-)도 조합으로 사용가능합니다.");
		}
		if (Utility.isEmpty(loginPw)) {
			return Utility.jsHistoryBack("비밀번호를 입력해주세요!");
		}
		if (Utility.isEmpty(loginPwChk)) {
			return Utility.jsHistoryBack("비밀번호 확인을 입력해주세요!");
		}
		if (Utility.isEmpty(name)) {
			return Utility.jsHistoryBack("이름을 입력해주세요!");
		}
		if (Utility.isEmpty(gender) || !gender.equals("male") && !gender.equals("female")) {
			return Utility.jsHistoryBack("성별을 선택해주세요!");
		}
		if (Utility.isEmpty(birthday)) {
			return Utility.jsHistoryBack("생년월일을 입력해주세요!");
		}
		if (Utility.isEmpty(email)) {
			return Utility.jsHistoryBack("이메일을 입력해주세요!");
		}
		if (!Pattern.matches(reg_email, email)) {
			return Utility.jsHistoryBack("이메일 형식이 틀렸습니다.\\n" + "ex) abc123@email.com" );
		}
		if (Utility.isEmpty(cellphoneNum)) {
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
		
		// 프로필 사진이 있다면 업로드
		int memberId = (int) doJoinRd.getData1();

		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, memberId);
			}
		}
		
		// 회원가입 후 해당 멤버 객체를 불러옴
		Member member = memberService.getMemberById(memberId);
		
		return Utility.jsReplace(Utility.f("%s님 가입을 축하드립니다.", member.getName()), "/");
	}
	
	// 존재하는 회원인지 체크
	@RequestMapping("/usr/member/getLoginIdDup")
	@ResponseBody
	public ResultData<String> getLoginIdDup(String loginId) {
		// 유효성 검사
		if(Utility.isEmpty(loginId)) return ResultData.from("F-1", "아이디를 입력해주세요");
		
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
		if (Utility.isEmpty(loginId)) {
			return Utility.jsHistoryBack("아이디를 입력해주세요!");
		}
		if (Utility.isEmpty(loginPw)) {
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
	
	// 로그아웃
	@RequestMapping("/usr/member/doLogout")
	@ResponseBody
	public String doLogout() {
		rq.logout();
	
		return Utility.jsReplace("로그아웃 완료!", "/");
	}
	
	// 마이 페이지
	@RequestMapping("/usr/member/myPage")
	public String showMyPage() {
		return "usr/member/myPage";
	}
	
	// 패스워드 확인 페이지
	@RequestMapping("/usr/member/chkPassword")
	public String showCheckPassword() {
		return "usr/member/chkPassword";
	}

	// 패스워드 확인
	@RequestMapping("/usr/member/doChkPassword")
	@ResponseBody
	public String doCheckPassword(String loginPw) {
		// 유효성 검사
		if (Utility.isEmpty(loginPw)) {
			return Utility.jsHistoryBack("비밀번호를 입력해주세요!");
		}

		if (rq.getLoginedMember().getLoginPw().equals(Utility.getEncrypt(loginPw, rq.getLoginedMember().getSalt())) == false) {
			return Utility.jsHistoryBack("비밀번호가 일치하지 않습니다!");
		}

		String memberModifyAuthKey = memberService.genMemberModifyAuthKey(rq.getLoginedMemberId());

		return Utility.jsReplace("", Utility.f("modify?memberModifyAuthKey=%s", memberModifyAuthKey));
	}
	
	// 회원정보 수정 페이지
	@RequestMapping("/usr/member/modify")
	public String showModify(String memberModifyAuthKey) {
		if (Utility.isEmpty(memberModifyAuthKey)) {
			return rq.jsReturnOnView("회원 수정 인증코드가 필요합니다.", true);
		}

		ResultData<?> chkMemberModifyAuthKeyRd = memberService.chkMemberModifyAuthKey(rq.getLoginedMemberId(), memberModifyAuthKey);

		if (chkMemberModifyAuthKeyRd.isFail()) {
			return rq.jsReturnOnView(chkMemberModifyAuthKeyRd.getMsg(), false, "chkPassword");
		}

		return "usr/member/modify";
	}
	
	// 회원정보 수정
	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public String doModify(HttpServletRequest req, String memberModifyAuthKey, String email, String cellphoneNum, MultipartRequest multipartRequest) {
		if (Utility.isEmpty(memberModifyAuthKey)) {
			return Utility.jsHistoryBack("회원 수정 인증코드가 필요합니다.");
		}

		ResultData<?> chkMemberModifyAuthKeyRd = memberService.chkMemberModifyAuthKey(rq.getLoginedMemberId(), memberModifyAuthKey);

		if (chkMemberModifyAuthKeyRd.isFail()) {
			return Utility.jsReplace(chkMemberModifyAuthKeyRd.getMsg(), "chkPassword");
		}

		// 유효성 검사
		if (Utility.isEmpty(email)) {
			return Utility.jsHistoryBack("이메일을 입력해주세요!");
		}
		if (Utility.isEmpty(cellphoneNum)) {
			return Utility.jsHistoryBack("전화번호를 입력해주세요!");
		}
		
		// 이미지 삭제 체크 했다면 삭제
		if (req.getParameter("deleteFile__member__0__extra__profileImg__1") != null) {
			ResultData<?> delFileChkRd = genFileService.deleteGenFiles("member", rq.getLoginedMemberId(), "extra", "profileImg", 1);
			
			if (delFileChkRd.isFail()) {
				return Utility.jsHistoryBack(delFileChkRd.getMsg());
			}
		}

		// 이미지 파일이 있다면 업로드
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		for (String fileInputName : fileMap.keySet()) {
			MultipartFile multipartFile = fileMap.get(fileInputName);

			if (multipartFile.isEmpty() == false) {
				genFileService.save(multipartFile, rq.getLoginedMemberId());
			}
		}

		memberService.doModify(rq.getLoginedMemberId(), email, cellphoneNum);

		return Utility.jsReplace("회원정보가 수정됐습니다.", "myPage");
	}
	
	// 패스워드 수정 페이지
	@RequestMapping("/usr/member/passwordModify")
	public String passwordModify(String memberModifyAuthKey) {
		if (Utility.isEmpty(memberModifyAuthKey)) {
			return rq.jsReturnOnView("회원 수정 인증코드가 필요합니다.", true);
		}

		ResultData<?> chkMemberModifyAuthKeyRd = memberService.chkMemberModifyAuthKey(rq.getLoginedMemberId(), memberModifyAuthKey);

		if (chkMemberModifyAuthKeyRd.isFail()) {
			return rq.jsReturnOnView(chkMemberModifyAuthKeyRd.getMsg(), false, "chkPassword");
		}

		return "usr/member/passwordModify";
	}

	// 패스워드 수정
	@RequestMapping("/usr/member/doPasswordModify")
	@ResponseBody
	public String doPasswordModify(String memberModifyAuthKey, String loginPw, String loginPwChk) {
		if (Utility.isEmpty(memberModifyAuthKey)) {
			return Utility.jsHistoryBack("회원 수정 인증코드가 필요합니다.");
		}

		ResultData<?> chkMemberModifyAuthKeyRd = memberService.chkMemberModifyAuthKey(rq.getLoginedMemberId(), memberModifyAuthKey);

		if (chkMemberModifyAuthKeyRd.isFail()) {
			return Utility.jsReplace(chkMemberModifyAuthKeyRd.getMsg(), "chkPassword");
		}

		if (Utility.isEmpty(loginPw)) {
			return Utility.jsHistoryBack("새 비밀번호를 입력해주세요!");
		}
		if (Utility.isEmpty(loginPwChk)) {
			return Utility.jsHistoryBack("새 비밀번호 확인을 입력해주세요!");
		}
		if (loginPw.equals(loginPwChk) == false) {
			return Utility.jsHistoryBack("비밀번호가 일치하지 않습니다.");
		}

		String salt = Utility.getTempPassword(20);
		
		memberService.doPasswordModify(rq.getLoginedMemberId(), Utility.getEncrypt(loginPw, salt), salt);

		return Utility.jsReplace("비밀번호가 수정됐습니다.", "myPage");
	}
	
	// 아이디 찾기 페이지
	@RequestMapping("/usr/member/findLoginId")
	public String findLoginId() {
		return "usr/member/findLoginId";
	}
	
	// 아이디 찾기
	@RequestMapping("/usr/member/doFindLoginId")
	@ResponseBody
	public String doFindLoginId(String name, String email) {
		if (Utility.isEmpty(name)) {
			return Utility.jsHistoryBack("이름을 입력해주세요");
		}
		if (Utility.isEmpty(email)) {
			return Utility.jsHistoryBack("이메일을 입력해주세요");
		}

		Member member = memberService.getMemberByNameAndEmail(name, email);

		if (member == null) {
			return Utility.jsHistoryBack("입력하신 정보와 일치하는 회원이 없습니다");
		}

		return Utility.jsReplace(Utility.f("회원님의 아이디는 [ %s ] 입니다", member.getLoginId()), "login");
	}
	
	// 비밀번호 찾기 페이지
	@RequestMapping("/usr/member/findLoginPw")
	public String findLoginPw() {
		return "usr/member/findLoginPw";
	}
	
	// 비밀번호 찾기
	@RequestMapping("/usr/member/doFindLoginPw")
	@ResponseBody
	public String doFindLoginPw(String loginId, String name, String email) {
		if (Utility.isEmpty(loginId)) {
			return Utility.jsHistoryBack("아이디를 입력해주세요");
		}
		if (Utility.isEmpty(name)) {
			return Utility.jsHistoryBack("이름을 입력해주세요");
		}
		if (Utility.isEmpty(email)) {
			return Utility.jsHistoryBack("이메일을 입력해주세요");
		}

		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null) {
			return Utility.jsHistoryBack("입력하신 정보와 일치하는 회원이 없습니다");
		}

		if (member.getName().equals(name) == false) {
			return Utility.jsHistoryBack("이름이 일치하지 않습니다");
		}

		if (member.getEmail().equals(email) == false) {
			return Utility.jsHistoryBack("이메일이 일치하지 않습니다");
		}

		ResultData<?> notifyTempLoginPwByEmailRd = memberService.notifyTempLoginPwByEmail(member);

		return Utility.jsReplace(notifyTempLoginPwByEmailRd.getMsg(), "login");
	}
}
