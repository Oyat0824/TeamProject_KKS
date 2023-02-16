<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="pageTitle" value="Product View" />
<%@ include file="../common/head.jsp"%>
<%@ include file="../../usr/common/toastUiEditorLib.jsp"%>
<!-- 슬릭 슬라이더 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick.css"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick-theme.css"/>

<script>
const ProductBuy__submit = function() {
	if(${rq.loginedMemberId == 0}) {
		alert("로그인 후 이용해주세요!");
		
		return false;
	}
	
	let form = document.getElementById("productBuy");
	// 상품 수량 검증
	form.productCnt.value = form.productCnt.value.trim();
	if(form.productCnt.value.length == 0) {
		alert("상품 수량을 입력해주세요.");
		form.productCnt.focus();
		
		return false;
	}
	// 상품 가격 검증
	form.productPrice.value = form.productPrice.value.trim();
	if(form.productPrice.value.length == 0) {
		alert("상품 수량을 입력해주세요.");
		
		return false;
	}
	if(form.productPrice.value != ${product.productPrice}) {
		alert("잘못된 정보입니다.");
		
		return false;
	}
	
	form.submit();
}

//글자수(Byte) 세기 함수
const chkMaxInputNum = function() {
	const minNum = 1;
	const maxNum = ${product.productStock};
	let numVal = $(".numInput").val();

    if(numVal.replace(/[0-9]/g, "").length > 0) {
        $(".numInput").val(minNum);
    }
    if(numVal < minNum) {
    	$(".numInput").val(minNum);
    }
    if(numVal > maxNum) {
		$(".numInput").val(maxNum);
	}
	
    numVal = $(".numInput").val()
    
	$(".pCnt").html(numVal);
    
    const totalPrice = numVal * ${product.productPrice};
    const commaNum = totalPrice.toLocaleString();
    
	$(".pPrice").html(commaNum)
}

$(function() {
	// 슬릭 초기 설정
	$(".slider").on('init', function(event, slick){
		if(slick.slideCount == 0) {
			$("#storeBanner").empty();
			return false;
		}
		if(slick.slideCount == 1) {
			$(".slider-btn").empty();
		}
		
		$(".photos").children("div").eq(0).addClass("active");
	})
	
	$(".slicks").slick({
		autoplay: false,
		fade: false,
		dots: false,
		speed: 0,
		arrows: false
	})
	
	// 슬라이드 이동 버튼
	$("#l-btn").click(function(){
		$('.slicks').slick('slickPrev');
	})

	$("#r-btn").click(function(){
		$('.slicks').slick('slickNext');
	})
	
	// 닷츠 버튼
	$(".photos").children("div").hover(function () {
		let dot_num = $(this).index();

		$(".slicks").slick("slickGoTo", dot_num);
	});
	
	$('.slicks').on('beforeChange', function(event, slick, currentSlide, nextSlide){
		$(".photos").children("div").eq(nextSlide).addClass("active")
		$(".photos").children("div").eq(nextSlide).siblings().removeClass("active")
	})
	
	
	// 버튼 클릭 시 숫자 증감 및 가격 증가
	$(".numBtn.minus").click(function(){
		let num = Number($(".numInput").val());
		$(".numInput").val(num-=1);
		chkMaxInputNum();
	});
	$(".numBtn.plus").click(function(){
		let num = Number($(".numInput").val());
		$(".numInput").val(num+=1);
		chkMaxInputNum();
	});
	$(".numInput").on("keyup", function(){
		chkMaxInputNum();
	});
	
	$(".rImg").each(function(idx, item) {
		if($(item).attr("src") == "${rq.getProfileFallbackImgUri()}") {
			$(item).parent().remove();
		}
	});
});
</script>

<!-- 스토어 헤더 -->
<%@ include file="../common/storeHead.jsp"%>

<!-- 빵조각 메뉴 -->
<div class="flex container mx-auto my-2 text-sm breadcrumbs">
	<ul>
		<li><a href="/usr/store/view?id=${param.storeId }">홈</a></li> 
		<li><a href="/usr/store/productList?id=${param.storeId }&cate=${product.productCategory}">${product.categoryName}</a></li> 
	</ul>
</div>

<!-- 상품 디자인 -->
<section>
	<div class="container mx-auto flex border">
		<div class="photo-container slider relative w-2/4">
			<div class="slicks">
				<c:forEach var="file" items="${fileList}">
					<div class="photo-box relative w-1/2"><img class="absolute inset-0 w-full h-full p-10" src="${rq.getImgUri('product', product.id, 'productImg', file.fileNo)}" alt=""></div>
				</c:forEach>
			</div>
			
			<div class="slider-btn flex justify-between w-full absolute top-1/2 -translate-y-2/4 text-6xl text-white invisible opacity-0">
				<div id="l-btn" class="ml-5 cursor-pointer"><i class="fa-solid fa-chevron-left text-outline"></i></div>
				<div id="r-btn" class="mr-5 cursor-pointer"><i class="fa-solid fa-chevron-right text-outline"></i></div>
			</div>
			
			<div class="photos flex justify-center w-full mb-10">
				<c:forEach var="file" items="${fileList}">
					<div class="mx-1 border-2 cursor-pointer"><img class="block w-12 h-12 object-fill" src="${rq.getImgUri('product', product.id, 'productImg', file.fileNo)}" alt=""></div>
				</c:forEach>
			</div>
		</div>
		
		<div class="flex flex-col justify-between px-10 py-8 w-2/4">
			<div class="product-info flex flex-col">
				<div class="productName border-b border-dashed pb-5">
					<h2 class="text-3xl font-bold">${product.productName}</h2>
				</div>
				<div class="productPrice mt-5 flex justify-between">
					<div class="text-xl font-bold">상품 가격</div>
					<h3 class="text-right text-2xl font-bold"><fmt:formatNumber value="${product.productPrice}" pattern="#,###" /><span class="text-xl font-bold">원</span></h3>
				</div>
				<div class="productStock mt-1 flex justify-between">
					<div class="text-lg font-bold">상품 재고</div>
					<div class="text-lg font-bold text-gray-600"><fmt:formatNumber value="${product.productStock }" pattern="#,###" /><span class="text-sm font-bold">개</span></div>
				</div>
				<div class="productDlvy border-y py-5 mt-5 flex flex-col">
					<div class="flex">
						<div class="border-r pr-2 mr-2"><span>택배배송</span></div>
						<div><span>${product.productDlvy == 0 ? '무료배송' : '유료배송' }</span></div>
						<div class="ml-1 dot"><span>${product.productCourier }</span></div>
						<c:if test="${product.productDlvy == 1}">
							<div class="ml-1 dot"><span><fmt:formatNumber value="${product.productDlvyPrice}" pattern="#,###" />원</span></div>
						</c:if>
					</div>
					<div class="mt-1"><span class="text-gray-400">제주 추가 3,000원, 제주 외 도서지역 추가 3,000원</span></div>
				</div>
			</div>
			
			<form id="productBuy" action="orderSheet">
				<input type="hidden" name="id" value="${product.id}"/>
				<input type="hidden" name="storeId" value="${store.id}"/>
				<input type="hidden" name="productPrice" value="${product.productPrice}"/>
				<div class="productBtn flex flex-col">
					<div class="flex border border-gray-400">
						<a class="numBtn minus"><span>수량 빼기</span></a>
						<input class="numInput w-full" min="1" max="${product.productStock }" type="number" name="productCnt" value="1" />
						<a class="numBtn plus"><span>수량 추가</span></a>
					</div>
				
					<div class="productPrice flex justify-between my-5 pt-5 border-t">
						<div>
							<span class="font-bold">총 상품 금액</span>
							<span class="tooltip" data-tip="총 상품금액에 배송비는 포함되어 있지 않습니다.&#xa;결제시 배송비가 추가될 수 있습니다."><i class="fa-solid fa-circle-question"></i></span>
						</div>
						
						<div class="flex items-center">
							<div class="border-r pr-2 mr-2 text-gray-400">총 수량 <span class="pCnt">1</span>개</div>
							<div class="text-xl text-red-500 font-bold"><span class="pPrice"><fmt:formatNumber value="${product.productPrice}" pattern="#,###" /></span><span class="text-base">원</span></div>
						</div>
					</div>
				
					<div>
						<c:choose> 
							<c:when test="${product.productStock == 0}">
								<button class="btn w-full" disabled>품절</button>
							</c:when> 
							<c:otherwise>
								<a class="btn btn-success w-full" href="javascript:()" onclick="return ProductBuy__submit();">구매하기</a>
							</c:otherwise> 
						</c:choose> 
					</div>
					<div class="flex mt-1">
						<a class="btn flex-1 mr-1" href=""><i class="fa-solid fa-heart mr-1"></i>찜하기</a>
						<a class="btn flex-1 ml-1" href=""><i class="fa-solid fa-basket-shopping mr-1"></i> 장바구니</a>
					</div>
				</div>
			</form>
		</div>
	</div>

	<div class="container mx-auto border p-5 mt-5">
		<div class="productInfo3">
			<h3 class="font-bold text-xl select-none mb-2 pb-1 border-b">상품정보</h3>
			<table class="table w-1/2 mx-auto border-2">
				<colgroup>
					<col width="160">
					<col width="50%">
					<col width="160">
					<col width="50%">
				</colgroup>
				<tr class="border-b">
					<th class="bg-gray-200 rounded-none">상품이름</th>
					<td>${product.productName }</td>
				</tr>
				<tr>
					<th class="bg-gray-200 rounded-none">상품번호</th>
					<td>${product.id }</td>
					<th class="bg-gray-200 rounded-none">상품 등록일</th>
					<td>${product.regDate }</td>
				</tr>
			</table>
		</div>
		<div class="productBody">
			<div class="toast-ui-viewer" >
				<script type="text/x-template">${product.productBody }</script>
			</div>
		</div>
	</div>
	
<c:if test="${reviewCount > 0}">
	<div class="container mx-auto border mt-2 p-5">
		<div class="productReview">
			<h3 class="font-bold text-xl select-none mb-2 pb-1 border-b">상품리뷰</h3>
			<div class="flex justify-around mt-2 w-11/12 bg-gray-100 mx-auto p-10">
				<div class="flex flex-col items-center">
					<div>
						<span class="font-bold">사용자 총 평점</span>
					</div>
					<div class="my-5">
						<div class="star-ratings">
							<div class="star-ratings-fill space-x-1 text-3xl" style="width: calc(20% * ${reviewAvg} + 1.5px)">
								<span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>
							</div>
							<div class="star-ratings-base space-x-1 text-3xl">
								<span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>
							</div>
						</div>
					</div>
					<div>
						<span class="text-3xl font-bold">${reviewAvg} / 5</span>
					</div>
				</div>
				<div class="flex flex-col items-center">
					<div>
						<span class="font-bold">전체 리뷰 수</span>
					</div>
					<div class="my-5"><i class="fa-solid fa-comment-dots text-3xl"></i></div>
					<div><span class="text-3xl font-bold">${reviewCount}</span></div>
				</div>
			</div>
			
			<div class="flex flex-col mt-2">
				<c:forEach var="review" items="${reviews}">
					<div class="p-3 border mt-3">
						<div class="flex items-center mt-2">
							<div class="mx-4">
								<img class="w-20 rounded-full" style="aspect-ratio: 1/1" src="${rq.getImgUri('member', review.memberId, 'profileImg', 1)}" alt="" />
							</div>
							<div class="flex-1 pl-5">
								<span class="text-base font-bold">평점 : ${review.rating }점</span>
								<div class="flex items-center">
									<div class="star-ratings">
										<div class="star-ratings-fill space-x-1 text-lg" style="width: calc(20% * ${review.rating })">
											<span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>
										</div>
										<div class="star-ratings-base space-x-1 text-lg">
											<span>★</span><span>★</span><span>★</span><span>★</span><span>★</span>
										</div>
									</div>
								</div>
								<div class="mt-1 text-gray-500">
									<span>${review.name}</span> <span class="dot">${review.regDate.substring(0, 10) } </span>
								</div>
								<div class="mt-3">
									${review.getForPrintReviewBody() }
								</div>
							</div>
						</div>
						<div class="flex mt-8">
							<a href="${rq.getImgUri('review', review.id, 'reviewImg', 1)}" target="_blank">
								<img class="rImg border p-1 w-20 mr-3" style="aspect-ratio: 1/1" src="${rq.getImgUri('review', review.id, 'reviewImg', 1)}" alt="" />
							</a>
							<a href="${rq.getImgUri('review', review.id, 'reviewImg', 2)}" target="_blank">
								<img class="rImg border p-1 w-20 mr-3" style="aspect-ratio: 1/1" src="${rq.getImgUri('review', review.id, 'reviewImg', 2)}" alt="" />
							</a>
							<a href="${rq.getImgUri('review', review.id, 'reviewImg', 3)}" target="_blank">
								<img class="rImg border p-1 w-20" style="aspect-ratio: 1/1" src="${rq.getImgUri('review', review.id, 'reviewImg', 3)}" alt="" />
							</a>
						</div>
					</div>
				</c:forEach>
			</div>
			
			<div class="pageNav flex justify-center mt-5">
				<div class="btn-group">
					<c:set var="maxPageNum" value="5" />
					<fmt:parseNumber var="pageBlock" integerOnly="true" value="${page / maxPageNum}" />
					<c:if test="${page % maxPageNum > 0}"><c:set var="pageBlock" value="${pageBlock + 1}" /></c:if>
					<c:set var="endPage" value="${pageBlock * maxPageNum}" />
					<c:set var="startPage" value="${endPage - (maxPageNum - 1)}" />
					<c:set var="endPage" value="${pagesCount < endPage ? pagesCount : endPage }" />
	
					<c:set var="pageBaseUri" value="&storeId=${store.id }&id=${param.id }" />
	
					<c:if test="${page == 1}">
						<a class="btn btn-sm w-12 btn-disabled">&lt;&lt;</a>
						<a class="btn btn-sm w-12 btn-disabled">&lt;</a>
					</c:if>
					<c:if test="${page > 1}">
						<a class="btn btn-sm w-12" href="?page=1${pageBaseUri}">&lt;&lt;</a>
						<a class="btn btn-sm w-12" href="?page=${page-1}${pageBaseUri}">&lt;</a>
					</c:if>
						
					<c:forEach begin="${startPage }" end="${endPage }" var="i">
						<a class="btn btn-sm w-12 ${page == i ? 'btn-active' : ''}" href="?page=${i}${pageBaseUri}">${i}</a>
					</c:forEach>
	
					<c:if test="${page == pagesCount}">
						<a class="btn btn-sm w-12 btn-disabled">&gt;</a>
						<a class="btn btn-sm w-12 btn-disabled">&gt;&gt;</a>
					</c:if>
					<c:if test="${page < pagesCount}">
						<a class="btn btn-sm w-12" href="?page=${page+1}${pageBaseUri}">&gt;</a>
						<a class="btn btn-sm w-12" href="?page=${pagesCount}${pageBaseUri}">&gt;&gt;</a>
					</c:if>
				</div>
			</div>
		</div>
	</div>
</c:if>
</section>
<%@ include file="../common/foot.jsp"%>