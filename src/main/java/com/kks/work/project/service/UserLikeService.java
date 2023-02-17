package com.kks.work.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.MemberRepository;
import com.kks.work.project.repository.UserLikeRepository;
import com.kks.work.project.vo.Rq;

@Service
public class UserLikeService {
	
	
	private UserLikeRepository userLikeRepository;
	private Rq rq;
	private MailService mailService;

	@Autowired
	public UserLikeService(UserLikeRepository userLikeRepository, Rq rq) {
		this.userLikeRepository = userLikeRepository;
		this.rq = rq;
		
	}
	
	public void addUserLike(int loginedMemberId, int productId) {
	
		userLikeRepository.addUserLike(loginedMemberId, productId);
		 return;
	}

	public void removeUserLike(int loginedMemberId, int relId) {
		 userLikeRepository.removeUserLike(loginedMemberId, relId);
		return;
	}

	public int isUserLike(int loginedMemberId , int productId) {
		return userLikeRepository.isUserLike(loginedMemberId,productId);
		
	}

}
