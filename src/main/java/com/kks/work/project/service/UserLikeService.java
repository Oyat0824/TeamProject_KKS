package com.kks.work.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kks.work.project.repository.UserLikeRepository;
import com.kks.work.project.vo.UserLike;

@Service
public class UserLikeService {
	
	private UserLikeRepository userLikeServiceRepository;
	 
	
	@Autowired 
	public UserLikeService(UserLikeRepository userLikeServiceRepository) { 
		this.userLikeServiceRepository = userLikeServiceRepository;
	}


	public UserLike getUserLike(int loginedMemberId, String relTypeCode, int id) {
		return userLikeServiceRepository.getUserLike(loginedMemberId, relTypeCode, id);
	}


	public void doUserLike(int loginedMemberId, String relTypeCode, int id, int liked) {
		userLikeServiceRepository.doUserLike(loginedMemberId, relTypeCode, id, liked);
	}


	public void delUserLike(int loginedMemberId, String relTypeCode, int id) {
		userLikeServiceRepository.delUserLike(loginedMemberId, relTypeCode, id);
		
	}

}
