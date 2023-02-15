<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Store View" />
<%@ include file="../common/head.jsp"%>
<!-- 슬릭 슬라이더 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick.css"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick-theme.css"/>

<script>
$(function() {
	// 슬릭 초기 설정
	$(".slider").on('init', function(event, slick){
		if(slick.slideCount == 0) {
			$("#storeBanner").empty();
			return false;
		}
		
		 for(let i = 0; i < slick.slideCount; i++) {
			$(".dots").append("<div class='cursor-pointer'></div>");
			$(".dots").children("div").eq(0).addClass("active");
		}
	})
	
	$(".slicks").slick({
		autoplay: true,
		autoplaySpeed: 5000,
		fade: true,
		cssEase: 'linear',
		dots: false,
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
	$(".dots").children("div").click(function () {
		let dot_num = $(this).index();

		$(".slicks").slick("slickGoTo", dot_num);
	});
	
	$('.slicks').on('beforeChange', function(event, slick, currentSlide, nextSlide){
		$(".dots").children("div").eq(nextSlide).addClass("active")
		$(".dots").children("div").eq(nextSlide).siblings().removeClass("active")
	})
});
</script>

<!-- 스토어 헤더 -->
<%@ include file="../common/storeHead.jsp"%>

<!-- 스토어 배너 부분 -->
<section id="storeBanner" class="mt-5">
	<div class="slider relative w-full">
		<!-- 스토어에 배너 등록하기 하고 갯수에 따라서 이미지 출력 -->
		<div class="slicks h-96">
			<c:forEach var="file" items="${fileList}">
				<div><img class="block w-full h-96" src="${rq.getImgUri('store', store.id, 'bannerImg', file.fileNo)}" alt=""></div>
			</c:forEach>
		</div>
		
		<div class="slider-btn flex justify-between w-full absolute top-1/2 -translate-y-2/4 text-6xl text-white invisible opacity-0">
			<div id="l-btn" class="ml-5 cursor-pointer"><i class="fa-solid fa-chevron-left text-outline"></i></div>
			<div id="r-btn" class="mr-5 cursor-pointer"><i class="fa-solid fa-chevron-right text-outline"></i></div>
		</div>
		
		<div class="dots flex justify-center w-full absolute bottom-5"></div>
	</div>
</section>

<!-- 베스트 부분 -->
<section id="storeBest" class="mt-5">
	<div class="container mx-auto">
		<h2 class="text-center font-bold text-3xl">베스트 상품</h2>
		<div class="flex justify-center">
			<c:forEach var="item" items="${bestProducts}">
				<div class="flex flex-col items-center p-5 hover:bg-gray-50">
					<div class="img_area">
						<a href="/usr/product/view?storeId=${item.storeId }&id=${item.id}">
							<img class="w-60 h-60 border-2 border-gray-400" src="${rq.getImgUri('product', item.id, 'productImg')}" alt="" />
						</a>
					</div>
					
					<div class="info_area mt-2">
						<div class="text-2xl">
							<a class="font-bold" href="/usr/product/view?storeId=${item.storeId }&id=${item.id}">${item.productName}</a>
						</div>
						<div>
							<span class="text-lg font-bold"><fmt:formatNumber value="${item.productPrice}" pattern="#,###" />원</span>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</section>

<!-- 신상품 부분 -->
<section id="storeBest" class="mt-10">
	<div class="container mx-auto">
		<h2 class="text-center font-bold text-3xl">신상품</h2>
		<div class="flex justify-center">
			<c:forEach var="item" items="${newProducts}">
				<div class="flex flex-col items-center p-5 hover:bg-gray-50">
					<div class="img_area">
						<a href="/usr/product/view?storeId=${item.storeId }&id=${item.id}">
							<img class="w-60 h-60 border-2 border-gray-400" src="${rq.getImgUri('product', item.id, 'productImg')}" alt="" />
						</a>
					</div>
					
					<div class="info_area mt-2">
						<div class="text-2xl">
							<a class="font-bold" href="/usr/product/view?storeId=${item.storeId }&id=${item.id}">${item.productName}</a>
						</div>
						<div>
							<span class="text-lg font-bold"><fmt:formatNumber value="${item.productPrice}" pattern="#,###" />원</span>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</section>

<section id="storeInfo" class="mt-10">
	<div class="container mx-auto">
		<table class="table border-2 w-3/6 mx-auto">
			<colgroup>
				<col width="200" />
			</colgroup>
			
			<tbody>
				<tr>
					<th>스토어 등록일</th>
					<td>${store.regDate}</td>
				</tr>
				<tr>
					<th>스토어 이름</th>
					<td>${store.storeName}</td>
				</tr>
				<tr>
					<th>스토어 소개</th>
					<td>${store.getForPrintDesc()}</td>
				</tr>
				<tr>
					<th>판매자 이름</th>
					<td>${store.sellerName}</td>
				</tr>
			</tbody>
		</table>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>