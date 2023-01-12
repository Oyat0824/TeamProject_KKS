package com.kks.work.project.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kks.work.project.vo.Store;

@Mapper
public interface StoreRepository {
	// 가게 등록
	@Insert("""
			INSERT INTO store
			SET regDate = NOW(),
			updateDate = NOW(),
			storeName = #{storeName},
			storeLogo = #{storeLogo},
			storeImg = #{storeImg},
			storeDesc = #{storeDesc},
			memberId = #{memberId}
			""")
	public void registerStore(String storeName, String storeLogo, String storeImg, String storeDesc, int memberId);
	
	@Select("SELECT LAST_INSERT_ID()")
	public int getLastInsertId();

	// MemberId를 통해 가게 정보 가져오기
	@Select("""
			SELECT *
			FROM store
			WHERE memberId = #{memberId}
			""")
	public Store getStoreByMemberId(int memberId);
	
	// storeName을 통해 가게 정보 가져오기
	@Select("""
			SELECT *
			FROM store
			WHERE storeName = #{storeName}
			""")
	public Store getStoreByStoreName(String storeName);
}
