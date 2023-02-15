package com.kks.work.project.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kks.work.project.vo.Product;
import com.kks.work.project.vo.PurchaseList;
import com.kks.work.project.vo.Review;

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
			productDlvy = #{productDlvy},
			productCourier = #{productCourier},
			productCourierCode = #{productCourierCode},
			productDlvyPrice = #{productDlvyPrice},
			productBody = #{productBody},
			storeId = #{storeId}
			""")
	public void registerProduct(String productName, String productPrice, String productCategory,
			String productStock, int productDlvy, String productCourier, String productCourierCode, String productDlvyPrice, String productBody, int storeId);

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
	
	// 자신의 스토어 상품 목록, 개수 구하기
	@Select("""
			<script>
				SELECT COUNT(*)
				FROM product
				WHERE 1 = 1
				AND storeId = #{id}
				<if test="searchKeyword != ''">
					AND productName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
			</script>
			""")
	public int getMyStoreProductsCount(int id, String searchKeyword);
	
	// 상품 목록, 개수 구하기
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
	
	// 상품 목록, 개수 구하기 | 스토어 에서
	@Select("""
			<script>
				SELECT COUNT(*)
				FROM product
				WHERE storeId = #{id}
				<if test="searchKeyword != ''">
					AND productName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
			</script>
			""")
	public int getStoreInProductsCount(String searchKeyword, int id);
	
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
	
	// 유저 입장에서 보는 상품 리스트
	@Select("""
			<script>
				SELECT P.*, COUNT(PC.id) AS purchaseCnt
				FROM ( 
					SELECT P.*, COUNT(R.id) AS reviewCnt
					FROM product AS P
					LEFT JOIN review AS R
					ON P.id = R.productId
					GROUP BY P.id
				) AS P
				LEFT JOIN purchaseList AS PC
				ON P.id = PC.productId
				AND PC.confirm = 1
				WHERE 1 = 1
				<if test="searchKeyword != ''">
					AND productName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
				GROUP BY P.id
				<choose>
					<when test="listOrder == 'reviewMany'">
						ORDER BY reviewCnt DESC
					</when>
					<when test="listOrder == 'purchaseMany'">
						ORDER BY purchaseCnt DESC
					</when>
					<when test="listOrder == 'date'">
						ORDER BY regDate ASC
					</when>
					<otherwise>
						ORDER BY P.id DESC
					</otherwise>
				</choose>
				LIMIT #{limitStart}, #{itemsInAPage}
			</script>
			""")
	public List<Product> getExposureProducts(String searchKeyword, int itemsInAPage, int limitStart, String listOrder);

	// 스토어에서 보는 상품 리스트
	@Select("""
			<script>
				SELECT P.*, COUNT(PC.id) AS purchaseCnt
				FROM ( 
					SELECT P.*, COUNT(R.id) AS reviewCnt
					FROM product AS P
					LEFT JOIN review AS R
					ON P.id = R.productId
					WHERE 1 = 1
					GROUP BY P.id
				) AS P
				LEFT JOIN purchaseList AS PC
				ON P.id = PC.productId
				WHERE P.storeId = #{id}
				<if test="searchKeyword != ''">
					AND productName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
				<if test="cate != -1">
					AND productCategory = #{cate}
				</if>
				GROUP BY P.id
				<choose>
					<when test="listOrder == 'reviewMany'">
						ORDER BY reviewCnt DESC
					</when>
					<when test="listOrder == 'purchaseMany'">
						ORDER BY purchaseCnt DESC
					</when>
					<when test="listOrder == 'date'">
						ORDER BY regDate ASC
					</when>
					<otherwise>
						ORDER BY P.id DESC
					</otherwise>
				</choose>
				LIMIT #{limitStart}, #{itemsInAPage}
			</script>
			""")
	public List<Product> getStoreInProducts(String searchKeyword, int itemsInAPage, int limitStart, String listOrder,
			int cate, int id);
	
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
					<if test="productDlvy != null">
						productDlvy = #{productDlvy},
					</if>
					<if test="productCourier != null">
						productCourier = #{productCourier},
						productCourierCode = #{productCourierCode},
					</if>
					<if test="productDlvyPrice != null">
						productDlvyPrice = #{productDlvyPrice},
					</if>
					<if test="productBody != null">
						productBody = #{productBody}
					</if>
				</set>
				WHERE id = #{id}
			</script>
			""")
	public void doModify(int id, String productName, String productPrice, String productCategory, String productStock,
			int productDlvy, String productCourier, String productCourierCode, String productDlvyPrice, String productBody);
	
	// 등록된 상품 삭제
	@Delete("""
				DELETE FROM product
				WHERE id = #{id}
			""")
	public void deleteProduct(int id);

	// 상품 구매 시 구매목록에 추가
	@Insert("""
			INSERT INTO purchaseList
			SET regDate = NOW(),
			updateDate = NOW(),
			productId = #{id},
			productCnt = #{productCnt},
			storeId = #{storeId},
			memberId = #{memberId},
			impUID = #{impUID},
			orderNum = #{orderNum},
			`name` = #{name},
			cellphoneNum = #{cellphoneNum},
			cellphoneNum2 = #{cellphoneNum2},
			zipNo = #{zipNo},
			roadAddr = #{roadAddr},
			addrDetail = #{addrDetail},
			dlvyMemo = #{dlvyMemo}
			""")
	public void buyProduct(int id, int productCnt, int storeId, int memberId, String impUID, String orderNum,
			String name, String cellphoneNum, String cellphoneNum2,
			String zipNo, String roadAddr, String addrDetail, String dlvyMemo);

	// 주문 목록, 개수 구하기
	@Select("""
			<script>
				SELECT COUNT(*)
				FROM purchaseList AS PC
				INNER JOIN product AS P
				ON PC.productId = P.id
				WHERE 1 = 1
				AND PC.memberId = #{memberId}
				<if test="searchKeyword != ''">
					AND P.productName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
			</script>
			""")
	public int getPurchaseCount(String searchKeyword, int memberId);
	
	// 주문 목록 || 로그인 멤버
	@Select("""
			<script>
				SELECT PC.*, COUNT(R.id) AS isReview,
				P.productName AS productName,
				P.productPrice AS productPrice
				FROM purchaseList AS PC
				INNER JOIN product AS P
				ON PC.productId = P.id
				LEFT JOIN review AS R
				ON PC.id = R.purchaseId
				WHERE 1 = 1
				AND PC.memberId = #{loginedMemberId}
				<if test="searchKeyword != ''">
					AND P.productName LIKE CONCAT('%', #{searchKeyword}, '%')
				</if>
				GROUP BY PC.id
				ORDER BY PC.id DESC
				LIMIT #{limitStart}, #{itemsInAPage}
			</script>
			""")
	public List<PurchaseList> getPurchaseList(String searchKeyword, int itemsInAPage, int limitStart, int loginedMemberId);

	// 주문 목록, 개수 구하기 || 판매자
	@Select("""
		<script>
			SELECT COUNT(*)
			FROM purchaseList AS PC
			INNER JOIN product AS P
			ON PC.productId = P.id
			WHERE 1 = 1
			AND PC.storeId = #{storeId}
			<if test="searchKeyword != ''">
				AND P.productName LIKE CONCAT('%', #{searchKeyword}, '%')
			</if>
		</script>
		""")
	public int getOrderCount(String searchKeyword, int storeId);
	
	// 주문 목록 || 판매자
	@Select("""
		<script>
			SELECT SUB.*,
			P.productName AS productName,
			P.productPrice AS productPrice
			FROM(
				SELECT @ROWNUM := @ROWNUM + 1 AS ROWNUM, PC.*
				FROM purchaseList AS PC, (SELECT @ROWNUM := 0 ) AS TEMP
				WHERE PC.storeId = #{storeId}
				) SUB
			INNER JOIN product AS P
			ON SUB.productId = P.id
			<if test="searchKeyword != ''">
				WHERE P.productName LIKE CONCAT('%', #{searchKeyword}, '%')
			</if>
			ORDER BY SUB.ROWNUM DESC
			LIMIT #{limitStart}, #{itemsInAPage}
		</script>
		""")
	public List<PurchaseList> getOrderList(String searchKeyword, int itemsInAPage, int limitStart, int storeId);
	
	// 주문 상세보기 || 로그인 멤버
	@Select("""
			SELECT PC.*,
				S.storeName AS storeName,
				M.cellphoneNum AS sellerTel,
				P.productName AS productName,
				P.productPrice AS productPrice,
				P.productDlvy AS productDlvy,
				P.productDlvyPrice AS productDlvyPrice,
				P.productCourierCode AS productCourierCode
			FROM purchaseList AS PC
			INNER JOIN store AS S
			ON PC.storeId = S.id
			INNER JOIN `member` AS M
			ON S.memberId = M.id
			INNER JOIN product AS P
			ON PC.productId = P.id
			WHERE PC.id = #{id};
			""")
	public PurchaseList getPurchase(int id);
	
	// 구매 확정
	@Update("""
			UPDATE purchaseList
			SET updateDate = NOW(),
				confirm = 1
			WHERE id = #{id}
			""")
	public void confirmPurchase(int id);
	
	// 주문 취소
	@Update("""
		UPDATE purchaseList
		SET updateDate = NOW(),
			cancel = 1
		WHERE id = #{id}
		""")
	public void cancelPurchase(int id);
	@Update("""
		UPDATE product
		SET productStock = productStock + #{ordPCnt}
		WHERE id = #{productId}
		""")
	public void increaseProductStock(int productId, int ordPCnt);

	// 주문 확인, 운송장 번호 전송
	@Update("""
		UPDATE purchaseList
		SET updateDate = NOW(),
			ordCheck = 1,
			waybill = #{waybill}
		WHERE id = #{orderId}
		""")
	public void checkPurchase(int orderId, String waybill);
	@Update("""
		UPDATE product
		SET productStock = productStock - #{ordPCnt}
		WHERE id = #{productId}
		""")
	public void decreaseProductStock(int productId, int ordPCnt);

	// 리뷰 작성
	@Insert("""
		INSERT INTO review
		SET regDate = NOW(),
		updateDate = NOW(),
		storeId = #{storeId},
		productId = #{productId},
		purchaseId = #{purchaseId},
		memberId = #{memberId},
		rating = #{rating},
		reviewBody = #{reviewBody}
		""")
	public void createReview(int storeId, int productId, int purchaseId, int memberId, int rating, String reviewBody);

	// 리뷰 존재 확인
	@Select("""
		SELECT COUNT(*)
		FROM review
		WHERE purchaseId = #{id}
		AND memberId = #{loginedMemberId}
		""")
	public int isReview(int id, int loginedMemberId);

	// 리뷰 목록
	@Select("""
		SELECT R.*, M.name
		FROM review AS R
		INNER JOIN `member` AS M
		ON R.memberId = M.id
		WHERE storeId = #{storeId}
		AND productId = #{id}
		ORDER BY R.id DESC
		LIMIT #{limitStart}, #{itemsInAPage}
		""")
	public List<Review> getReviews(int storeId, int id, int limitStart, int itemsInAPage);
	
	// 리뷰 수 구하기
	@Select("""
		SELECT COUNT(*)
		FROM review
		WHERE productId = #{id}
		""")
	public int getReviewsCount(int id);
	
	// 리뷰 평균 점수 구하기
	@Select("""
		SELECT ROUND(AVG(rating),1)
		FROM review
		WHERE productId = #{id}
		""")
	public double getReviewAvg(int id);
	
	// 관리자의 상품 리스트에서 보는 상품 카운트
	@Select("""
				<script>
					SELECT COUNT(*)
						FROM product
						WHERE 1 = 1
						<if test="searchKeyword != ''">
							<choose>
								<when test="searchKeywordTypeCode == 'productName'">
									AND productName LIKE CONCAT('%', #{searchKeyword}, '%')
								</when>
								<when test="searchKeywordTypeCode == 'storeName'">
									AND storeName LIKE CONCAT('%', #{searchKeyword}, '%')
								</when>
								<otherwise>
									AND (
											productName LIKE CONCAT('%', #{searchKeyword}, '%')
											OR storeName LIKE CONCAT('%', #{searchKeyword}, '%')
										)
								</otherwise>
							</choose>
						</if>
				</script>
				""")
	public int getMyStoreProductsAdmCount(String searchKeywordTypeCode, String searchKeyword);
		
	// 관리자의 상품 리스트에서 상품 검색
	@Select("""
				<script>
					SELECT *
						FROM product
						WHERE 1 = 1
						<if test="searchKeyword != ''">
							<choose>
								<when test="searchKeywordTypeCode == 'productName'">
									AND productName LIKE CONCAT('%', #{searchKeyword}, '%')
								</when>
								<when test="searchKeywordTypeCode == 'storeName'">
									AND storeName LIKE CONCAT('%', #{searchKeyword}, '%')
								</when>
								<otherwise>
									AND (
											productName LIKE CONCAT('%', #{searchKeyword}, '%')
											OR storeName LIKE CONCAT('%', #{searchKeyword}, '%')
										)
								</otherwise>
							</choose>
						</if>
						ORDER BY id DESC
						LIMIT #{limitStart}, #{itemsInAPage}
				</script>
				""")
	public List<Product> getProductsAdm(String searchKeywordTypeCode, String searchKeyword, int itemsInAPage,
				int limitStart);

	// 관리자 권한으로 상품 삭제
	@Update("""
		<script>
			UPDATE product
				<set>
					updateDate = NOW(),
					delProductStatus = 1,
					delProductDate = NOW()
				</set>
				WHERE id = #{id}
		</script>
		""")
	public void AdmdeleteProduct(int id);

}
