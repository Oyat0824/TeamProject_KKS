package com.kks.work.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.MemberRepository;
import com.kks.work.project.util.ResultData;
import com.kks.work.project.util.Utility;
import com.kks.work.project.vo.Member;

@Service
public class MemberService {
	private MemberRepository memberRepository;

	@Autowired
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
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

	// 로그인 아이디를 통해 멤버 가져오기
	public Member getMemberByLoginId(String loginId) {
		return memberRepository.getMemberByLoginId(loginId);
	}

	// 로그인 아이디를 통해 멤버 가져오기
	public Member getMemberByNameAndEmail(String name, String email) {
		return memberRepository.getMemberByNameAndEmail(name, email);
	}

}
