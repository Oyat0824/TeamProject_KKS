<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Product Modify" />
<c:set var="menuName" value="modify" />
<%@ include file="../common/head.jsp"%>
<script>
	// Submit 전 검증
	const ProductModify__submit = function(form) {
		// 파일 검증
		const deleteProductImgFileInput = form["deleteFile__store__0__extra__productImg__1"];

		if (deleteProductImgFileInput.checked) {
			form["file__store__0__extra__storeImg__1"].value = '';
		}

		const maxSizeMb = 10;
		const maxSize = maxSizeMb * 1024 * 1024;

		const ProductImgFileInput = form["file__store__0__extra__productImg__1"];

		if (ProductImgFileInput.value) {
			if (ProductImgFileInput.files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요");
				ProductImgFileInput.focus();

				return false;
			}
		}
		
		if (chkTextByte() == false) {
			alert("최대 100Byte까지만 입력가능합니다.");
			
			return false;
		}
	}

	// 이미지 미리보기 기능
	const imgChg = function(e) {
		const selectedFile = e.files[0];
		const fileReader = new FileReader();

		fileReader.readAsDataURL(selectedFile);

		fileReader.onload = function() {
			$(e).siblings("div").children("img").attr("src", fileReader.result);
		};
	}
	
	// 글자수(Byte) 세기 함수
	const chkTextByte = function() {
		const maxByte = 100; //최대 100바이트
		const text_val = $("#textBox").val(); //입력한 문자
		const text_len = text_val.length; //입력한 문자수
		const rows = $("#textBox").val().split('\n').length;
		const maxRows = 4;
		
		if(rows > maxRows) {
			alert("4줄 까지만 가능합니다.");
			modifedText = $("#textBox").val().split("\n").slice(0, maxRows);
			$("#textBox").val(modifedText.join("\n"));
			
			return false;
		}
		
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
			$("#textBoxMsg").text("최대 100Byte까지만 입력가능합니다.");
			$('#nowByte').text(totalByte);
			$('#nowByte').css("color", "red");
			
			return false;
		} else {
			$("#textBoxMsg").empty();
			$('#nowByte').text(totalByte);
			$('#nowByte').css("color", "green");
		}
	}

	$(function() {
		if ($("#storeLogo").attr("src") == "${rq.getProfileFallbackImgUri()}") {
			$("#storeLogo").parent().siblings("div.delBtn").remove();
		}

		if ($("#storeImg").attr("src") == "${rq.getProfileFallbackImgUri()}") {
			$("#storeImg").parent().siblings("div.delBtn").remove();
		}
		
		// 글자수 세기 작동
		chkTextByte();
		
		$("#textBox").keydown(function (obj) {
			chkTextByte();
		});
		$("#textBox").keyup(function (obj) {
			chkTextByte();
		});
	})
</script>

<section>
	<div class="flex container mx-auto">

		<div class="w-full my-10">
			<form action="doModify" method="POST" enctype="multipart/form-data" onsubmit="return ProductModify__submit(this);">
				<input type="hidden" name="id" value="${product.id}" />
				<input type="hidden" name="productModifyAuthKey" value="${param.productModifyAuthKey}" />
				<div class="table-box-type-2">
					<table class="table w-11/12 mx-auto">
						<colgroup>
							<col width="200" />
						</colgroup>

						<tbody>

							<tr>
								<th>상품 이미지</th>
								<td>
									<div>
										<img id="productImg" class="object-cover mx-auto mb-3" style="width: 250px; height: 250px"
											src="${rq.getImgUri('product', product.id, 'productImg')}" alt="" />
									</div>
									<div class="mb-1 delBtn">
										<label class="cursor-pointer inline-flex"> <span class="label-text mr-2">이미지 삭제</span> <input
											class="ckeckbox" type="checkbox" name="deleteFile__product__0__extra__productImg__1" value="Y" />
										</label>
									</div> <input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png"
									class="file-input file-input-bordered border-gray-400" name="file__product__0__extra__productImg__1" type="file" />
								</td>
							</tr>
							<tr>
								<th>상품 가격</th>
								<td class="relative">
									<div class="mb-2 text-red-500"><p id="textBoxMsg"></p> </div>
									<textarea id="textBox" class="input w-full text-lg border-gray-400 p-2 text-base" style="height: 150px; resize: none;" name="productPrice"
										placeholder="상품 가격">${product.productPrice}</textarea>
									<div class="textLengthWrap flex">
										<p><span id="nowByte" style="color: green;">0</span>&nbsp;/&nbsp;100bytes</p>
									</div>
								</td>
							</tr>
							<tr>
								<th>상품 카테고리</th>
								<td class="relative">
									<div class="mb-2 text-red-500"><p id="textBoxMsg"></p> </div>
									<textarea id="textBox" class="input w-full text-lg border-gray-400 p-2 text-base" style="height: 150px; resize: none;" name="productCetegory"
										placeholder="상품 카테고리">${product.productCetegory}</textarea>
									<div class="textLengthWrap flex">
										<p><span id="nowByte" style="color: green;">0</span>&nbsp;/&nbsp;100bytes</p>
									</div>
								</td>
							</tr>
							<tr>
								<th>상품 재고</th>
								<td class="relative">
									<div class="mb-2 text-red-500"><p id="textBoxMsg"></p> </div>
									<textarea id="textBox" class="input w-full text-lg border-gray-400 p-2 text-base" style="height: 150px; resize: none;" name="productStock"
										placeholder="상품 재고">${product.productStock}</textarea>
									<div class="textLengthWrap flex">
										<p><span id="nowByte" style="color: green;">0</span>&nbsp;/&nbsp;100bytes</p>
									</div>
								</td>
							</tr>
							<tr>
								<th>상품 소개</th>
								<td class="relative">
									<div class="mb-2 text-red-500"><p id="textBoxMsg"></p> </div>
									<textarea id="textBox" class="input w-full text-lg border-gray-400 p-2 text-base" style="height: 150px; resize: none;" name="productBody"
										placeholder="상품 소개">${product.productBody}</textarea>
									<div class="textLengthWrap flex">
										<p><span id="nowByte" style="color: green;">0</span>&nbsp;/&nbsp;100bytes</p>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="w-11/12 mx-auto mt-5">
					<button class="btn btn-success w-full">
						상품 수정
					</button>
				</div>
			</form>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>