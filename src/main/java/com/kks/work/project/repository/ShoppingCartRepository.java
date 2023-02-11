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
			productPrice = #{productPrice},
			productCategory = #{productCategory},
			productCount = #{productCount},
			memberId = #{memberid}
			
			""")
	
	public void insertShoppingCart(int productId, int productPrice, String productCategory, int productCount, int memberid);

	
	
	//loginedMemberId로 장바구니 불러오기
	
	@Select("""
			SELECT *
			FROM `shopCart`
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
				SET productCount = productCount+1
				WHERE cartID = #{cartId}
			
			""")
	public int incrQuantity(String str,int cartId);


	@Update ("""
			UPDATE shopCart 
				SET productCount = productCount-1
				WHERE cartID = #{cartId}
			
			""")
	public int decrQuantity(String str,int cartId);


	@Select ("""
			SELECT * from shopCart
			where cartID = #{cartId}
			""")
	
	
	public ShoppingCart showCartBycartId(int cartId);
	
	
}
