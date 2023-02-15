package com.kks.work.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kks.work.project.vo.ShoppingCart;

@Mapper
public interface ShoppingCartRepository {
	
	// 장바구니 등록
	@Insert("""
			INSERT INTO shopCart
			SET productId = #{productId},
			productCnt = #{productCnt},
			memberId = #{memberId}
			
			""")
	
	public void insertShoppingCart(int productId, int productCnt, int memberId);

	
	
	//loginedMemberId로 장바구니 불러오기
	
	@Select("""
			SELECT S.* , P.productName , P.productPrice , P.productCategory
			FROM `shopCart` AS S
			INNER JOIN `product` AS P
			on S.productId = P.id
			WHERE memberId = #{loginedid}
			""")
	
	public List<ShoppingCart> showCart(int loginedid);

	
	//cartId로 장바구니 지우기
	
	@Delete("""
			Delete 
			FROM `shopCart`
			WHERE cartId = #{cartId}
			
			""")
	
	public int delCart(int cartId);






	@Update ("""
			UPDATE shopCart 
				SET productCnt = productCnt+1
				WHERE cartId = #{cartId}
			
			""")
	public int incrQuantity(int cartId);


	@Update ("""
			UPDATE shopCart 
				SET productCnt = productCnt-1
				WHERE cartId = #{cartId}
			
			""")
	public int decrQuantity(int cartId);


	@Select ("""
			SELECT * from shopCart
			where cartID = #{cartId}
			""")
	
	
	public ShoppingCart showCartBycartId(int cartId);
	
	
}
