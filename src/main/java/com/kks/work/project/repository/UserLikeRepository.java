package com.kks.work.project.repository;

import org.apache.ibatis.annotations.Mapper;

import com.kks.work.project.vo.UserLike;

@Mapper
public interface UserLikeRepository {

	UserLike getUserLike(int loginedMemberId, String relTypeCode, int id);

	Object doUserLike(int loginedMemberId, String relTypeCode, int id);

}
