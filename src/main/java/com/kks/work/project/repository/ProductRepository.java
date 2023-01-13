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

	
}
