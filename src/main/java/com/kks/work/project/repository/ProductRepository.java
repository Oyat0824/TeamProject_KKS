package com.kks.work.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kks.work.project.vo.Product;

@Mapper
public interface ProductRepository {
	
	// 상품 등록
	@Insert("""
			INSERT INTO product
			SET regDate = NOW(),
			updateDate = NOW(),
			p_Name = #{p_Name},
			p_Price = #{p_Price},
			p_Cetegory = #{p_Cetegory},
			p_Stock = #{p_Stock},
			p_Img = #{p_Img},
			p_Desc = #{p_Desc},
			stroeId = #{stroeId}
			""")
	public void registerProduct(String p_Name, String p_Price, String p_Cetegory, String p_Stock, String p_Img,
			String p_Desc);
	
	@Select("SELECT LAST_INSERT_ID()")
	public int getLastInsertId();
	
	// StoreId를 통해 상품 정보 가져오기
		@Select("""
				SELECT *
				FROM product
				WHERE storeId = #{storeId}
				""")
	public Product getProductByStoreId(int storeId);

	// p_Name을 통해 상품 정보 가져오기
	@Select("""
			SELECT *
			FROM product
			WHERE p_Name = #{p_Name}
			""")	
	public Product getProductByProductNam(String p_Name);

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
	public List<Product> getProducts(String authLevel, String searchKeywordTypeCode, String searchKeyword, int limitStart,
			int itemsInAPage);

	@Select("""
			<script>
				SELECT COUNT(*)
					FROM product
					WHERE 1 = 1
					<if test="authLevel != 0">
						AND authLevel = #{authLevel}
					</if>
					<if test="searchKeyword != ''">
						<choose>
							<when test="searchKeywordTypeCode == 'stroeId'">
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
	public int getProductsCount(String authLevel, String searchKeywordTypeCode, String searchKeyword);

	
}
