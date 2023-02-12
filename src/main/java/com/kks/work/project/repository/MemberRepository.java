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
			salt = #{salt},
			`name` = #{name},
			zipNo = #{zipNo},
			roadAddr = #{roadAddr},
			addrDetail = #{addrDetail},
			email = #{email},
			cellphoneNum = #{cellphoneNum},
			gender = #{gender},
			birthday = #{birthday}
			""")
	public void doJoin(String loginId, String loginPw, String salt, 
			String name, String zipNo, String roadAddr, String addrDetail, String email, String cellphoneNum, String gender, String birthday);

	@Select("SELECT LAST_INSERT_ID()")
	public int getLastInsertId();

	// 아이디를 통해 멤버 가져오기
	@Select("""
			SELECT *
			FROM `member`
			WHERE id = #{id}
			""")
	public Member getMemberById(int id);

	// 아이디를 통해 멤버 및 스토어 정보 가져오기
	@Select("""
			SELECT M.*, S.id AS storeId
			FROM `member` AS M
			INNER JOIN store AS S
			ON M.id = S.memberId
			WHERE M.id = #{id}
			AND M.memberType = 6
			AND M.storeState = 1
			""")
	public Member getMemberAndStoreById(int id);
		
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
					<if test="zipNo != null">
						zipNo = #{zipNo},
					</if>
					<if test="roadAddr != null">
						roadAddr = #{roadAddr},
					</if>
					<if test="addrDetail != null">
						addrDetail = #{addrDetail},
					</if>
					<if test="email != null">
						email = #{email},
					</if>
					<if test="cellphoneNum != null">
						cellphoneNum = #{cellphoneNum}
					</if>
				</set>
				WHERE id = #{loginedMemberId}
			</script>
			""")
	public void doModify(int loginedMemberId, String zipNo, String roadAddr, String addrDetail, String email, String cellphoneNum);

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
