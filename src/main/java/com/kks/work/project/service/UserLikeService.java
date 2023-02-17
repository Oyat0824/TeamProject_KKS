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


	public void doUserLike(int loginedMemberId, int id, String relTypeCode,  int like) {
		userLikeServiceRepository.doUserLike(loginedMemberId, id, relTypeCode, like);
	}


	public void delUserLike(int loginedMemberId, String relTypeCode, int id) {
		userLikeServiceRepository.delUserLike(loginedMemberId, relTypeCode, id);
		
	}

}
