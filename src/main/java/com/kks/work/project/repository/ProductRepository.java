package com.kks.work.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kks.work.project.vo.Product;

@Mapper
public interface ProductRepository {
	
	// 상품 등록
	@Insert("""
			INSERT INTO product
			SET regDate = NOW(),
			updateDate = NOW(),
			productName = #{productName},
			productPrice = #{productPrice},
			productCetegory = #{productCetegory},
			productStock = #{productStock},
			productBody = #{productBody},
			memberId = #{memberId},
			storeId = #{storeId}
			""")
	public void registerProduct(String productName, String productPrice, String productCetegory, String productStock, String productBody, int storeId, int memberId);
	
	// 스토어 등록 상태 변경
	@Update("""
			UPDATE store
			SET productState = 1
			WHERE id = #{storeId}
			""")
	public void productStateChange(int storeId);
	
	@Select("SELECT LAST_INSERT_ID()")
	public int getLastInsertId();
	
	// StoreId를 통해 상품 정보 가져오기
		@Select("""
				SELECT *
				FROM product
				WHERE storeId = #{storeId}
				""")
	public Product getProductByStoreId(int storeId);

	// ProductName을 통해 상품 정보 가져오기
	@Select("""
			SELECT *
			FROM product
			WHERE productName = #{productName}
			""")	
	public Product getProductByProductName(String productName);
	
	// storeId를 통해 상품 가져오기, 상점명도 함께
	@Select("""
			SELECT P.*, S.storeName AS storeName
			FROM product AS P
			INNER JOIN store AS S
			ON P.storeId = S.id
			WHERE p.id = #{id}
			""")
	public Product getForPrintProductById(int id);
	
	@Select("""
			<script>
				SELECT COUNT(*) 
					FROM product 
					WHERE 1 = 1
					<if test="searchKeyword != ''">
						<choose> <!-- 다중조건 -->
							<when test="searchKeywordTypeCode == 'productName'">
								AND title LIKE CONCAT('%', #{searchKeyword}, '%')
							</when>
							<when test="searchKeywordTypeCode == 'productCetegory'">
								AND body LIKE CONCAT('%', #{searchKeyword}, '%')
							</when>
							<otherwise>
								AND (
									storeName LIKE CONCAT('%', #{searchKeyword}, '%')
									OR body LIKE CONCAT('%', #{searchKeyword}, '%')
									)
							</otherwise>
						</choose>
					</if>
			</script>
			""")
	public List<Product> getProducts(String searchKeywordTypeCode, String searchKeyword, int limitStart,
			int itemsInAPage);

	@Select("""
			<script>
				SELECT COUNT(*)
					FROM product
					WHERE 1 = 1
					<if test="searchKeyword != ''">
						<choose>
							<when test="searchKeywordTypeCode == 'storeId'">
								AND stroeId LIKE CONCAT('%', #{searchKeyword}, '%')
							</when>
							<when test="searchKeywordTypeCode == 'productName'">
								AND productName LIKE CONCAT('%', #{searchKeyword}, '%')
							</when>
							<when test="searchKeywordTypeCode == 'productCetegory'">
								AND productCetegory LIKE CONCAT('%', #{searchKeyword}, '%')
							</when>
							<otherwise>
								AND (
										stroeId LIKE CONCAT('%', #{searchKeyword}, '%')
										OR productName LIKE CONCAT('%', #{searchKeyword}, '%')
										OR productCetegory LIKE CONCAT('%', #{searchKeyword}, '%')
									)
							</otherwise>
						</choose>
					</if>
				</script>
			""")
	public int getProductsCount(String searchKeywordTypeCode, String searchKeyword);

	// 상품 정보 수정
	@Update("""
			<script>
				UPDATE product
				<set>
					updateDate = NOW(),
					<if test="productBody != null">
						productBody = #{productBody}
					</if>
				</set>
				WHERE id = #{id}
				AND memberId = #{loginedMemberId}
			</script>
			""")
	public void doModify(int id, int loginedMemberId, String productBody);

	
}
