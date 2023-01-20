<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Product Modify" />
<%@ include file="../common/head.jsp"%>
<script>
	// Submit 전 검증
	const ProductModify__submit = function(form) {
		// 파일 검증
		const deleteProductImgFileInput = form["deleteFile__product__0__extra__productImg__1"];

		if (deleteProductImgFileInput.checked) {
			form["file__product__0__extra__productImg__1"].value = '';
		}

		const maxSizeMb = 10;
		const maxSize = maxSizeMb * 1024 * 1024;

		const ProductImgFileInput = form["file__product__0__extra__productImg__1"];

		if (productImgFileInput.value) {
			if (productImgFileInput.files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요");
				productImgFileInput.focus();

				return;
			}
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
		
		if($("#productImg").attr("src") == "${rq.getProfileFallbackImgUri()}") {
			$("#productImg").parent().siblings("div.delBtn").remove();
		}
	})
</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<form action="doModify" method="POST" enctype="multipart/form-data" onsubmit="return ProductModify__submit(this);">
			<input type="hidden" name="id" value="${store.id}" /> <input type="hidden" name="productModifyAuthKey"
				value="${param.productModifyAuthKey}" />
			<div class="table-box-type-1">
				<table class="table table-zebra w-full">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>상품 가격</th>
							<td><input class="file-input file-input-bordered w-full border-gray-400" name="productPrice" />${product.productPrice}원</td>
						</tr>
						<tr>
							<th>상품 카테고리</th>
							<td><input class="file-input file-input-bordered w-full border-gray-400" name="productCetegory" />${product.productCetegory}</td>
						</tr>
						<tr>
							<th>상품 재고</th>
							<td><input class="file-input file-input-bordered w-full border-gray-400" name="productStock" />${product.productStock}개</td>
						</tr>
						<tr>
							<th>상품 이미지</th>
							<td>
								<div>
									<img id="storeImg" class="object-cover mx-auto" style="width: 100px; height: 100px" src="${rq.getImgUri('product', product.id, 'productImg')}" alt="" />
								</div>
								<div class="mt-2 delBtn">
									<label class="cursor-pointer inline-flex"> <span class="label-text mr-2 mt-1">이미지 삭제</span> <input
										class="ckeckbox" type="checkbox" name="deleteFile__product__0__extra__productImg__1" value="Y" />
									</label>
								</div>
								<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="file-input file-input-bordered border-gray-400" name="file__product__0__extra__productImg__1" type="file" />
							</td>
						</tr>
				
						<tr>
							<th>상품 소개</th>
							<td><textarea style="height: 300px" class="input w-full text-lg border-gray-400" name="productBody"
									placeholder="상품 소개">${product.productBody}</textarea></td>
						</tr>
						<tr>
							<td colspan="2">
								<button class="btn btn-outline btn-accent w-full">
									<i class="fa-solid fa-product"></i>상품 정보 수정
								</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>

		<div class="btns flex justify-between mt-5">
			<button class="btn btn-primary" onclick="history.back();"><i class="fa-solid fa-right-from-bracket"></i>뒤로가기</button>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>