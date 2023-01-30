<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Product Register" />
<c:set var="menuName" value="productReg" />
<%@ include file="../common/head.jsp"%>
<%@ include file="../common/toastUiEditorLib.jsp" %>

<script>
	// Submit 전 검증
	const ProductRegister__submit = function(form) {
		// 상품 이름 검증
		form.productName.value = form.productName.value.trim();
		if(form.productName.value.length == 0) {
			alert("상품 이름을 입력해주세요.");
			form.productName.focus();
			
			return false;
		}
		// 상품 가격 검증
		form.productPrice.value = form.productPrice.value.trim();
		if(form.productPrice.value.length == 0) {
			alert("상품 가격을 입력해주세요.");
			form.productPrice.focus();
			
			return false;
		}
		// 상품 재고 검증
		form.productStock.value = form.productStock.value.trim();
		if(form.productStock.value.length == 0) {
			alert("상품 재고를 입력해주세요.");
			form.productStock.focus();
			
			return false;
		}
		
		// 파일 검증
		const maxSizeMb = 10;
		const maxSize = maxSizeMb * 1024 * 1024;
		
		const productImgFileInput = [];
		
		productImgFileInput.push(form["file__product__0__extra__productImg__1"]);
		productImgFileInput.push(form["file__product__0__extra__productImg__2"]);
		productImgFileInput.push(form["file__product__0__extra__productImg__3"]);
		
		for(i = 0; i < productImgFileInput.length; i++) {
			if(productImgFileInput[i].value) {
				if (productImgFileInput[i].files[0].size > maxSize) {
					alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요");
					productImgFileInput[i].focus();
					
					return false;
				}
			}
		}
		
		const editor = $(form).find(".toast-ui-editor").data("data-toast-editor");
		const markdown = editor.getMarkdown().trim();
		const html = editor.getHTML().trim();
		
		if(markdown.length == 0) {
		  alert("내용을 입력해주세요");
		  editor.focus();
		  return false;
		}
		  
		form.productBody.value = html;
	}
</script>

<section>
	<div class="flex container mx-auto">
		<%@ include file="../common/sideMenu.jsp"%>

		<div class="w-full my-10">
			<form id="frm" action="doRegister" method="POST" enctype="multipart/form-data" onsubmit="return ProductRegister__submit(this);">
				<input type="hidden" name="id" value="${param.id}" />
				<input type="hidden" name="storeModifyAuthKey" value="${param.storeModifyAuthKey}" />
				<input type="hidden" name="productBody" />
				<div class="table-box-type-2">
					<table class="table w-11/12 mx-auto">
						<colgroup>
							<col width="200" />
						</colgroup>

						<tbody>
							<tr>
								<th>상품 이름</th>
								<td>
									<input class="input input-bordered w-full text-lg" type="text" name="productName" placeholder="상품 이름을 입력해주세요." />
								</td>
							</tr>
							<tr>
								<th>상품 가격</th>
								<td>
									<input class="input input-bordered w-full text-lg" name="productPrice" placeholder="상품 가격을 입력해주세요." />
								</td>
							</tr>
							<tr>
								<th>상품 카테고리</th>
								<td>
									<select class="select select-bordered w-full" data-form="itemsNum" name="productCategory" onchange="return chgForm(this);">
										<option value="">없음</option>
										<c:forEach var="category" items="${categorys}">
											<option value="${category.id }">${category.name }</option>	
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<th>상품 재고</th>
								<td>
									<input class="input input-bordered w-full text-lg" name="productStock" placeholder="상품 재고를 입력해주세요." />
								</td>
							</tr>
							<tr>
								<th>상품 이미지</th>
								<td>
									<div class="flex items-center">
										<span class="badge p-3 mr-3">첨부파일 #1</span>
										<input accept="image/gif, image/jpeg, image/png" class="w-full inline-block mb-2 file-input file-input-bordered" name="file__product__0__extra__productImg__1" type="file" />
									</div>
									<div class="flex items-center">
										<span class="badge p-3 mr-3">첨부파일 #2</span>
										<input accept="image/gif, image/jpeg, image/png" class="w-full inline-block mb-2 file-input file-input-bordered" name="file__product__0__extra__productImg__2" type="file" />
									</div>
									<div class="flex items-center">
										<span class="badge p-3 mr-3">첨부파일 #3</span>
										<input accept="image/gif, image/jpeg, image/png" class="w-full inline-block mb-2 file-input file-input-bordered" name="file__product__0__extra__productImg__3" type="file" />
									</div>
								</td>
							</tr>
							<tr>
								<th>상품 설명</th>
								<td>
									<div class="toast-ui-editor text-left"></div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="w-11/12 mx-auto mt-5">
					<button class="btn btn-success w-full">
						상품 등록
					</button>
				</div>
			</form>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>