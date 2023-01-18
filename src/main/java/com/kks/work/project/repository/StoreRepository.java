package com.kks.work.project.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kks.work.project.vo.Store;

@Mapper
public interface StoreRepository {
	// 스토어 등록
	@Insert("""
			INSERT INTO store
			SET regDate = NOW(),
			updateDate = NOW(),
			storeName = #{storeName},
			storeDesc = #{storeDesc},
			memberId = #{memberId}
			""")
	public void registerStore(String storeName, String storeDesc, int memberId);
	
	// 스토어 등록 상태 변경
	@Update("""
			UPDATE `member`
			SET storeState = 1
			WHERE id = #{memberId}
			""")
	public void storeStateChange(int memberId);
	
	@Select("SELECT LAST_INSERT_ID()")
	public int getLastInsertId();
	
	// memberId를 통해 스토어 가져오기
	@Select("""
			SELECT *
			FROM store
			WHERE id = #{id}
			""")
	public Store getStoreById(int id);

	// MemberId를 통해 스토어 정보 가져오기
	@Select("""
			SELECT *
			FROM store
			WHERE memberId = #{memberId}
			""")
	public Store getStoreByMemberId(int memberId);
	
	// storeName을 통해 스토어 정보 가져오기
	@Select("""
			SELECT *
			FROM store
			WHERE storeName = #{storeName}
			""")
	public Store getStoreByStoreName(String storeName);
	
	// memberId를 통해 스토어 가져오기, 판매자도 함께
	@Select("""
			SELECT S.*, M.name AS sellerName
			FROM store AS S
			INNER JOIN `member` AS M
			ON S.memberId = M.id
			WHERE s.id = #{id}
			""")
	public Store getForPrintStoreById(int id);
	
	// 스토어 수정
	@Update("""
			<script>
				UPDATE store
				<set>
					updateDate = NOW(),
					<if test="storeDesc != null">
						storeDesc = #{storeDesc}
					</if>
				</set>
				WHERE id = #{id}
				AND memberId = #{loginedMemberId}
			</script>
			""")
	public void doModify(int id, int loginedMemberId, String storeDesc);
}
