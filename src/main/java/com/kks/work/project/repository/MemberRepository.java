package com.kks.work.project.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kks.work.project.vo.Member;

@Mapper
public interface MemberRepository {

	@Insert("""

			INSERT INTO `member`
			SET regDate = NOW(),
			updateDate = NOW(),
			loginId = #{loginId},
			loginPw = #{loginPw},
			`name` = #{name},
			gender = #{gender},
			birthday = #{birthday},
			email = #{email},
			cellphoneNum = #{cellphoneNum},
			salt = #{salt}

			""")
	public void doJoin(String loginId, String loginPw, String name, int gender,  String birthday, String email, String cellphoneNum, String salt);

	@Select("SELECT LAST_INSERT_ID()")
	public int getLastInsertId();

	@Select("""

			SELECT *
			FROM `member`
			WHERE id = #{id}

			""")
	public Member getMemberById(int id);

	@Select("""
			SELECT *
			FROM `member`
			WHERE loginId = #{id}
			""")
	public Member getMemberByLoginId(String loginId);

	
}
