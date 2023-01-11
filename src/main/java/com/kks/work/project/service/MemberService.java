package com.kks.work.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.MemberRepository;
import com.kks.work.project.vo.Member;

@Service
public class MemberService {
	
	private MemberRepository memberRepository;

	@Autowired
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public int doJoin(String loginId, String loginPw, String name, int gender, String birthday, String email , String cellphoneNum, String salt) {
		
		Member existsMember = getMemberByLoginId(loginId);

		if(existsMember != null) { // 중복 로그인 아이디 체크
			return -1;
		}
		
		memberRepository.doJoin(loginId, loginPw, name, gender, birthday, email, cellphoneNum, salt);

		return memberRepository.getLastInsertId();
	}

	public Member getMemberByLoginId(String loginId) {
		return memberRepository.getMemberByLoginId(loginId);
	}

	public Member getMemberById(int id) {
		return memberRepository.getMemberById(id);
	}

}
