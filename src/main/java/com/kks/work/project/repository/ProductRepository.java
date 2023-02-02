package com.kks.work.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
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
			productCategory = #{productCategory},
			productStock = #{productStock},
			productBody = #{productBody},
			storeId = #{storeId}
			""")
	public void registerProduct(String productName, String productPrice, String productCategory, String productStock, String productBody, int storeId);

	@Select("SELECT LAST_INSERT_ID()")
	public int getLastInsertId();
	
	// storeId 와 id를 통해 상품 상세 정보 가져오기
	@Select("""
			SELECT P.*, C.name AS categoryName
			FROM product AS P
			LEFT JOIN category AS C
			ON C.id = P.productCategory
			WHERE P.storeId = #{storeId}
			AND P.id = #{id};
			""")
	public Product getProductByStoreIdAndId(int storeId, int id);
	
	// 상품 목록 개수
	@Select("""
			<script>
				SELECT COUNT(*)
				FROM product
				WHERE 1 = 1
				<if test="searchKeyword != ''">
					AND productName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
			</script>
			""")
	public int getProductsCount(String searchKeyword);
	
	// 판매자 입장에서의 상품관리를 위한 상품 리스트
	@Select("""
			<script>
				SELECT SUB.*, C.name AS categoryName
				FROM(
					SELECT @ROWNUM := @ROWNUM + 1 AS ROWNUM, P.*
					FROM product AS P, (SELECT @ROWNUM := 0 ) AS TEMP
					WHERE P.storeId = #{id}
					) SUB
				LEFT JOIN category AS C
				ON C.id = SUB.productCategory	
				<if test="searchKeyword != ''">
					WHERE productName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
				ORDER BY SUB.ROWNUM DESC
				LIMIT #{limitStart}, #{itemsInAPage}
			</script>
			""")
	public List<Product> getProducts(int id, String searchKeyword, int itemsInAPage, int limitStart);
	
	// 유저입장에서 보는 상품 리스트
	@Select("""
			<script>
				SELECT * FROM product
				WHERE 1 = 1
				<if test="searchKeyword != ''">
					AND storeName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
				ORDER BY id DESC
				LIMIT #{limitStart}, #{itemsInAPage}
			</script>
			""")
	public List<Product> getExposureProducts(String searchKeyword, int itemsInAPage, int limitStart);

	// 상품 가져오기
	@Select("""
			SELECT *
			FROM product
			WHERE id = #{id}
			""")
	public Product getProduct(int id);

	// 상품 수정
	@Update("""
			<script>
				UPDATE product
				<set>
					updateDate = NOW(),
					<if test="productName != null">
						productName = #{productName},
					</if>
					<if test="productPrice != null">
						productPrice = #{productPrice},
					</if>
					<if test="productCategory != null">
						productCategory = #{productCategory},
					</if>
					<if test="productStock != null">
						productStock = #{productStock},
					</if>
					<if test="productBody != null">
						productBody = #{productBody}
					</if>
				</set>
				WHERE id = #{id}
			</script>
			""")
	public void doModify(int id, String productName, String productPrice, String productCategory, String productStock,
			String productBody);
	
	// 등록된 상품 삭제
	@Delete("""
				DELETE FROM product
				WHERE id = #{id}
			""")
	public void deleteProduct(int id);

}
