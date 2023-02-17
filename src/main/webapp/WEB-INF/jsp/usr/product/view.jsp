<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Product View" />
<%@ include file="../common/head.jsp"%>
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
	
});



function setUserLike(productId) {
	
	$.get('/usr/userLike/setUserLike', {
		productId : productId,
		
		ajaxMode : 'Y'
	}, function(data){
	
		console.log("asdada", data);
		
// 	if (data.data1 == 1) {
// 		$('#userLike').
// 	}
	
// 	else {
// 		<i class="fa-regular fa-heart"></i>
// 	}
	
// 	$('.' + data.data1.cartId).val(data.data1.productCnt);
// 	document.getElementById('totalPrice').innerText=data.totalPriceSum;				
					
	}, 'json');
}



</script>

<!-- 스토어 헤더 -->
<%@ include file="../common/storeHead.jsp"%>

<!-- 빵조각 메뉴 -->
<div class="flex container mx-auto my-2 text-sm breadcrumbs">
	<ul>
		<li><a href="/usr/store/view?id=${param.storeId }">홈</a></li> 
		<li><a>${product.categoryName}</a></li> 
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
						<a class="btn flex-1 mr-1" href="#" onclick = "setUserLike(${product.id});"><i id="userLike" class="fa-solid fa-heart mr-1"></i>찜하기</a>
						<a class="btn flex-1 ml-1" href=""><i class="fa-solid fa-basket-shopping mr-1"></i> 장바구니</a>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- 
		상품 상세정보, 리뷰 탭 을 만들어야함
		상품 상세정보에는 상품 테이블에 있는 정보를 토대로 게시물 내용을 보여줌
		리뷰 탭에는 리뷰 목록을 페이지로 만들어서 보여줘야함 (페이지 작업 해야함)
		
		구매건수는 주문목록(구매확정)된거를 기반으로 해당 상품을 찾아서 기록
		리뷰건수는 해당상품으로 찾아서 카운트 후 기록
		찜하기도 만들어야함, 스토어 찜도 비슷하게 만들어야함
	 -->
	
	<div class="container mx-auto border border-t-0 p-5">
		
	</div>
</section>
<%@ include file="../common/foot.jsp"%>