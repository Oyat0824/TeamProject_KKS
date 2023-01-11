package com.kks.work.project.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kks.work.project.vo.Member;

@Mapper
public interface MemberRepository {
	// 회원가입
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
	public void doJoin(String loginId, String loginPw, String name, String gender,  String birthday, String email, String cellphoneNum, String salt);

	@Select("SELECT LAST_INSERT_ID()")
	public int getLastInsertId();

	// 아이디를 통해 멤버 가져오기
	@Select("""
			SELECT *
			FROM `member`
			WHERE id = #{id}
			""")
	public Member getMemberById(int id);

	// 로그인 아이디를 통해 멤버 가져오기
	@Select("""
			SELECT *
			FROM `member`
			WHERE loginId = #{loginId}
			""")
	public Member getMemberByLoginId(String loginId);

	// 이름과 이메일이 같은 멤버 가져오기
	@Select("""
			SELECT *
			FROM `member`
			WHERE `name` = #{name}
			AND email = #{email}
			""")
	public Member getMemberByNameAndEmail(String name, String email);
	
	// 회원정보 수정
	@Update("""
			<script>
				UPDATE `member`
					<set>
						updateDate = NOW(),
						<if test="cellphoneNum != null">
							cellphoneNum = #{cellphoneNum},
						</if>
						<if test="email != null">
							email = #{email}
						</if>
					</set>
					WHERE id = #{loginedMemberId}
			</script>
			""")
	public void doModify(int loginedMemberId, String email, String cellphoneNum);

	// 비밀번호 수정
	@Update("""
			UPDATE `member`
			SET updateDate = NOW(),
			loginPw = #{loginPw},
			salt = #{salt}
			WHERE id = #{loginedMemberId}
			""")
	public void doPasswordModify(int loginedMemberId, String loginPw, String salt);
}
