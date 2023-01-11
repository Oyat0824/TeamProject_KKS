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

	public int doJoin(String loginId, String loginPw, String name, int gender, String birthday, String cellphoneNum, String email) {
		memberRepository.doJoin(loginId, loginPw, name, gender, birthday, cellphoneNum, email);

		return memberRepository.getLastInsertId();
	}

	public Member getMemberById(int id) {
		return memberRepository.getMemberById(id);
	}

}
