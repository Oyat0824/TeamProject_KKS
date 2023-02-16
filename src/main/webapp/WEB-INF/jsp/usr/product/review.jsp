<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="pageTitle" value="Product Review" />
<!-- 노말라이즈, 라이브러리 -->
<!-- 제이쿼리, 제이쿼리UI 불러오기 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<!-- 테일윈드, 데이지 UI 불러오기 -->
<script src="https://cdn.tailwindcss.com"></script>
<link href="https://cdn.jsdelivr.net/npm/daisyui@2.45.0/dist/full.css" rel="stylesheet" type="text/css" />

<script>
	// 폼 넘기기 전 검증
	const chkSubmit = function(form) {
		// 평점 검증
		form.rating.value = form.rating.value.trim();
		if(form.rating.value > 5 || form.rating.value <= 0) {
			alert("평점을 입력해주세요!");
			
			return false;
		}
		
		// 리뷰 글자 수 검증
		if(form.reviewBody.value.length < 10) {
			alert("최소 10자는 작성해주세요!");
			
			return false;
		}
		if(chkTextByte() == false) {
			alert("최대 5000Byte 까지만 작성 가능합니다.");
			
			return false;
		}
		
		// 파일 검증
		const maxSizeMb = 10;
		const maxSize = maxSizeMb * 1024 * 1024;
		
		const rImgFileList = [];
		
		rImgFileList.push(form["file__review__0__extra__reviewImg__1"]);
		rImgFileList.push(form["file__review__0__extra__reviewImg__2"]);
		rImgFileList.push(form["file__review__0__extra__reviewImg__3"]);
		
		for(i = 0; i < rImgFileList.length; i++) {
			if(rImgFileList[i].value && rImgFileList[i].files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요");
				rImgFileList[i].focus();
				
				return false;
			}
		}
	}

	// 글자수(Byte) 세기 함수
	const chkTextByte = function() {
		const maxByte = 5000; //최대 5000바이트
		const text_val = $("#textBox").val(); //입력한 문자
		const text_len = text_val.length; //입력한 문자수
		
		let totalByte = 0;
		
		for (let i = 0; i < text_len; i++) {
			const each_char = text_val.charAt(i);
			const uni_char = escape(each_char); //유니코드 형식으로 변환
	
			if (uni_char.length > 4) {
				// 한글 : 2Byte
				totalByte += 2;
			} else {
				// 영문,숫자,특수문자 : 1Byte
				totalByte += 1;
			}
		}
	
		if (totalByte > maxByte) {
			$("#errMsg").text("최대 5000Byte 까지만 작성 가능합니다.");
			$('#nowByte').text(totalByte);
			$('#nowByte').css("color", "red");
			
			return false;
		} else {
			$("#errMsg").empty();
			$('#nowByte').text(totalByte);
			$('#nowByte').css("color", "green");
		}
	}
	
	const clickClose = function() {
		window.close();
	}
	
	$(function() {
		// 글자수 세기 작동
		$("#textBox").keydown(function (obj) {
			chkTextByte();
		});
		$("#textBox").keyup(function (obj) {
			chkTextByte();
		});
	})
</script>

<section class="bg-gray-200">
	<div class="flex flex-col w-full">
		<div class="flex items-center justify-center relative w-full h-12 border-b bg-white">
			<h3 class="text-xl font-bold">리뷰쓰기</h3>
			<span class="absolute top-1/2 right-5 -translate-y-1/2 text-2xl cursor-pointer" onclick="clickClose();">X</span>
		</div>
		<div class="flex items-center w-full p-2 bg-white">
			<div class="thumnail mr-2">
				<a href="view?id=${product.id}&storeId=${store.id}" target="_blank">
					<img class="w-24 h-24 p-1 border rounded-lg" src="${rq.getImgUri('product', product.id, 'productImg', 1)}" />
				</a>
			</div>
			<div class="desc flex-auto">
				<div><span class="text-sm text-gray-500">주문번호 : ${purchase.orderNum}</span></div>
				<div><span class="text-sm text-gray-500">[SB_STORE] ${store.storeName}</span></div>
				<div><span class="whitespace-nowrap">${product.productName}</span></div>
			</div>		
		</div>
		
		<form action="createReview" onsubmit="return chkSubmit(this);" method="POST" enctype="multipart/form-data">
			<input type="hidden" name="storeId" value="${store.id }" />
			<input type="hidden" name="productId" value="${product.id }" />
			<input type="hidden" name="purchaseId" value="${purchase.id}" />
			<input type="hidden" name="memberId" value="${rq.getLoginedMemberId()}" />
			
			<div class="flex flex-col items-center justify-center w-full mt-2 py-6 border-b bg-white">
				<h3 class="text-xl font-bold">상품은 만족하셨나요?</h3>
				<div class="rating rating-lg my-2">
					<input type="radio" name="rating" class="mask mask-star-2 bg-orange-400" value="1" checked />
					<input type="radio" name="rating" class="mask mask-star-2 bg-orange-400" value="2" />
					<input type="radio" name="rating" class="mask mask-star-2 bg-orange-400" value="3" />
					<input type="radio" name="rating" class="mask mask-star-2 bg-orange-400" value="4" />
					<input type="radio" name="rating" class="mask mask-star-2 bg-orange-400" value="5" />
				</div>
				<div>
					<span class="text-gray-400">선택하세요.</span>
				</div>
			</div>
			<div class="flex flex-col items-center justify-center w-full mt-2 py-6 border-b bg-white">
				<h3 class="text-xl font-bold">어떤 점이 좋았나요?</h3>
				<div class="w-full p-5">
					<textarea id="textBox" class="input w-full text-lg border-gray-400 p-2 text-base" style="height: 150px; resize: none;" name="reviewBody" placeholder="최소 10자 이상 입력해주세요."></textarea>
					<div class="flex justify-between items-center mt-2">
						<p id="errMsg" class="text-sm text-red-500"></p>
						<p><span id="nowByte" style="color: green;">0</span>&nbsp;/&nbsp;5000Bytes</p>
					</div>
				</div>
				
				<div class="flex flex-col items-center">
					<div class="px-5">
						<input accept="image/gif, image/jpeg, image/png" class="w-full mb-2 file-input file-input-bordered" name="file__review__0__extra__reviewImg__1" type="file" />
						<input accept="image/gif, image/jpeg, image/png" class="w-full mb-2 file-input file-input-bordered" name="file__review__0__extra__reviewImg__2" type="file" />
						<input accept="image/gif, image/jpeg, image/png" class="w-full mb-2 file-input file-input-bordered" name="file__review__0__extra__reviewImg__3" type="file" />
					</div>
					<div>
						<span class="text-sm text-red-500">상품과 무관한 사진/동영상을 첨부한 리뷰는 통보없이 삭제됩니다.</span>
					</div>
				</div>
			</div>
		
			<div class="flex w-full mt-2 p-4 bg-white">
				<a class="flex-auto mx-1 btn btn-outline" onclick="clickClose();">취소</a>
				<button class="flex-auto mx-1 btn btn-success">등록</button>
			</div>
		</form>
	</div>
</section>
