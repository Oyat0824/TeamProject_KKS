package com.kks.work.project.repository;

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
			productName = #{productName},
			productPrice = #{productPrice},
			productCetegory = #{productCetegory},
			productStock = #{productStock},
			productBody = #{productBody},
			stroeId = #{stroeId}
			""")
	public void registerProduct(String productName, String productPrice, String productCetegory, String productStock, String productBody);
	
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
			WHERE productName = #{productName}
			""")	
	public Product getProductByProductNam(String productName);

	
}
