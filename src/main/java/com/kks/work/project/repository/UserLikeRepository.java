package com.kks.work.project.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserLikeRepository {
	
	@Insert("""
			INSERT INTO `userLike`
			SET regDate = NOW(),
			updateDate = NOW(), 
			relId = #{productId},
			memberId = #{loginedMemberId},
			relTypeCode = 'product'
			
			""")

public void addUserLike(int loginedMemberId, int productId); 
	
	
	@Insert("""
			DELETE FROM `userLike`
			WHERE memberId = #{loginedMemberId}
			AND relId = #{relId}
			AND relTypeCode = 'product'
			
			
			""")

	
	
	public void removeUserLike(int loginedMemberId, int relId);


	
	
	
	@Select("""
			SELECT EXISTS (SELECT * FROM userLike
							WHERE memberId = #{loginedMemberId}
							AND relId = #{productId}
							AND relTypeCode = 'product'
							);
			"""
			
			)
			
			
	public int isUserLike(int loginedMemberId , int productId);
			
			
}
	
	
	
	
