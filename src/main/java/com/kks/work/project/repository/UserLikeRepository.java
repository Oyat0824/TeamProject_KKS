package com.kks.work.project.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kks.work.project.vo.UserLike;

@Mapper
public interface UserLikeRepository {

	// UserLike 가져오기
	@Select("""
			SELECT * FROM userLike
			WHERE relTypeCode = #{relTypeCode} 
			AND memberId = #{loginedMemberId}
			AND relId = #{id}
			""")
	UserLike getUserLike(int loginedMemberId, String relTypeCode, int id);

	// UserLike 추가(찜하기)
	@Insert("""
			INSERT INTO userLike
				SET regDate = NOW()
					updateDate = NOW()
					memberId = #{loginedMemberId}
					relTypeCode = #{relTypeCode}
					relId = #{id}
					like = #{like}
			""")
	void doUserLike(int loginedMemberId, int id, String relTypeCode, int like);

	// UserLike 추가(찜취소)
	@Delete("""
			DELETE FROM userLike
				WHERE relTypeCode = #{relTypeCode}
				AND memberId = #{loginedMemberId}
				AND relId = #{id}
			""")
	void delUserLike(int loginedMemberId, String relTypeCode, int id);

}
