package com.kks.work.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kks.work.project.vo.Category;
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
	
	// id를 통해 스토어 가져오기
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
	
	// 스토어 목록 개수
	@Select("""
			<script>
				SELECT COUNT(*)
				FROM store
				WHERE 1 = 1
				<if test="searchKeyword != ''">
					AND storeName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
			</script>
			""")
	public int getStoresCount(String searchKeyword);
	
	// 스토어 목록
	@Select("""
			<script>
				SELECT S.*, M.name AS sellerName
				FROM store AS S
				INNER JOIN `member` AS M
				ON S.memberId = M.id
				WHERE 1 = 1
				<if test="searchKeyword != ''">
					AND storeName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
				ORDER BY S.id DESC
				LIMIT #{limitStart}, #{itemsInAPage}
			</script>
			""")
	public List<Store> getStores(String searchKeyword, int itemsInAPage, int limitStart);
	
	// 카테고리가져오기 (id)
	@Select("""
			SELECT * FROM category
			WHERE id = #{id}
			""")
	public Category getCategory(int id);

	// 카테고리 목록 가져오기 (StoreId)
	@Select("""
			SELECT * FROM category
			WHERE storeId = #{storeId}
			ORDER BY orderNo ASC
			""")
	public List<Category> getCategorysByStoreId(int storeId);
	
	// 카테고리 가져오기 (StoreId, orderNo)
	@Select("""
			SELECT * FROM category
			WHERE storeId = #{storeId}
			AND orderNo = #{orderNo}
			""")
	public Category getCategoryByStoreIdAndOrderNo(int orderNo, int storeId);
	
	// 카테고리 갯수 가져오기
	@Select("""
			SELECT COUNT(*) FROM category
			WHERE storeId = #{storeId}
			""")
	public int getCategoryCntByStoreId(int storeId);

	// 카테고리 등록
	@Insert("""
			INSERT INTO category
			SET `name` = #{name},
			orderNo = #{orderNo},
			storeId = #{storeId}
			""")
	public void registerCategory(String name, int orderNo, int storeId);
	
	// 카테고리 수정
	@Update("""
			<script>
				UPDATE category
				<set>
					<if test="name != null">
						name = #{name},
					</if>
					<if test="orderNo != null">
						orderNo = #{orderNo}
					</if>
				</set>
				WHERE id = #{id}
			</script>
			""")
	public void doCategoryModify(int id, String name, int orderNo);

	// 카테고리 삭제
	@Delete("""
			DELETE FROM category
			WHERE id = #{id}
			""")
	public void doCategoryDelete(int id);

	@Select("""
			SELECT * FROM store
			""")
	public int getStoreId();
}
