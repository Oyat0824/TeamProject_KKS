package com.kks.work.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.MemberRepository;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Member;

@Service
public class MemberService {
	@Value("${custom.siteMainUri}")
	private String siteMainUri;
	@Value("${custom.siteName}")
	private String siteName;
	
	private MemberRepository memberRepository;
	private AttrService attrService;
	private MailService mailService;

	@Autowired
	public MemberService(MemberRepository memberRepository, AttrService attrService, MailService mailService) {
		this.memberRepository = memberRepository;
		this.attrService = attrService;
		this.mailService = mailService;
	}

// 서비스 메서드
	// 회원가입
	public ResultData<Integer> doJoin(String loginId, String loginPw, String name, String gender, String birthday, String email , String cellphoneNum, String salt) {
		// 로그인 아이디 중복체크
		Member existsMember = getMemberByLoginId(loginId);
		if (existsMember != null) {
			return ResultData.from("F-8", Utility.f("이미 존재하는 아이디(%s)입니다.", loginId));
		}
		
		// 이름, 이메일 중복체크
		existsMember = getMemberByNameAndEmail(name, email);
		if (existsMember != null) {
			return ResultData.from("F-9", Utility.f("이미 사용중인 이름(%s)과 이메일(%s)입니다.", name, email));
		}
	
		memberRepository.doJoin(loginId, loginPw, name, gender, birthday, email, cellphoneNum, salt);

		int id = memberRepository.getLastInsertId();
		
		return ResultData.from("S-1", "회원가입이 완료되었습니다", "id", id);
	}

	// 아이디를 통해 멤버 가져오기
	public Member getMemberById(int id) {
		return memberRepository.getMemberById(id);
	}
	
	// 아이디를 통해 멤버 및 스토어 정보 가져오기
	public Member getMemberAndStoreById(int id) {
		return memberRepository.getMemberAndStoreById(id);
	}

	// 로그인 아이디를 통해 멤버 가져오기
	public Member getMemberByLoginId(String loginId) {
		return memberRepository.getMemberByLoginId(loginId);
	}

	// 로그인 아이디를 통해 멤버 가져오기
	public Member getMemberByNameAndEmail(String name, String email) {
		return memberRepository.getMemberByNameAndEmail(name, email);
	}
	
	// 인증키 생성
	public String genMemberModifyAuthKey(int loginedMemberId) {
		String memberModifyAuthKey = Utility.getTempPassword(10);
		attrService.setValue("member", loginedMemberId, "extra", "memberModifyAuthKey", memberModifyAuthKey,
				Utility.getDateStrLater(60 * 5));

		return memberModifyAuthKey;
	}

	// 인증키 확인
	public ResultData<?> chkMemberModifyAuthKey(int loginedMemberId, String memberModifyAuthKey) {
		String saved = attrService.getValue("member", loginedMemberId, "extra", "memberModifyAuthKey");
		
		if (saved.equals(memberModifyAuthKey) == false) {
			return ResultData.from("F-1", "일치하지 않거나 만료된 인증코드입니다.");
		}

		return ResultData.from("S-1", "정상 인증코드입니다");
	}
	
	// 회원정보 수정
	public void doModify(int loginedMemberId, String email, String cellphoneNum) {
		memberRepository.doModify(loginedMemberId, email, cellphoneNum);
		attrService.remove("member", loginedMemberId, "extra", "memberModifyAuthKey");
	}
	
	// 비밀번호 수정
	public void doPasswordModify(int loginedMemberId, String loginPw, String salt) {
		memberRepository.doPasswordModify(loginedMemberId, loginPw, salt);
		attrService.remove("member", loginedMemberId, "extra", "memberModifyAuthKey");
	}
	
	// 임시 비밀번호 보내기
	public ResultData<?> notifyTempLoginPwByEmail(Member member) {
		String title = "[" + siteName + "] 임시 패스워드 발송";
		String tempPassword = Utility.getTempPassword(8);
		String salt = Utility.getTempPassword(20);
		String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";
		body += "<a href=\"" + siteMainUri + "/usr/member/login\" target=\"_blank\">로그인 하러가기</a>";

		ResultData<?> sendRd = mailService.send(member.getEmail(), title, body);

		if (sendRd.isFail()) {
			return sendRd;
		}

		setTempPassword(member, tempPassword, salt);

		return ResultData.from("S-1", "계정의 이메일주소로 임시 패스워드가 발송되었습니다");
	}
	
	// 임시 비밀번호 생성
	private void setTempPassword(Member member, String tempPassword, String salt) {
		memberRepository.doPasswordModify(member.getId(), Utility.getEncrypt(tempPassword, salt), salt);
	}

}
