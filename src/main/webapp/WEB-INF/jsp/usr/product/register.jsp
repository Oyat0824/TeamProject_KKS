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
		// 배송 방식 선택
		form.productDlvy.value = form.productDlvy.value.trim();
		if(form.productDlvy.value.length == 0) {
			alert("배송 방식을 선택해주세요!");
			
			return false;
		}
		// 배송사 선택
		form.productCourier.value = form.productCourier.value.trim();
		if(form.productCourier.value.length == 0) {
			alert("배송사를 선택해주세요!");
			
			return false;
		}
		// 배송비 작성
		form.productDlvyPrice.value = form.productDlvyPrice.value.trim();
		if(form.productDlvy.value == 1 && form.productDlvyPrice.value.length == 0) {
			alert("배송비용을 적어주세요!");
			
			return false;
		}
		
		// 파일 검증
		const maxSizeMb = 10;
		const maxSize = maxSizeMb * 1024 * 1024;
		
		const pImgFileList = [];
		
		pImgFileList.push(form["file__product__0__extra__productImg__1"]);
		pImgFileList.push(form["file__product__0__extra__productImg__2"]);
		pImgFileList.push(form["file__product__0__extra__productImg__3"]);
		
		let chkFileCnt = 0;
		
		for(i = 0; i < pImgFileList.length; i++) {
			if(pImgFileList[i].value && pImgFileList[i].files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요");
				pImgFileList[i].focus();
				
				return false;
			} else if(!pImgFileList[i].value) {
				chkFileCnt += 1;
			}
		}
		
		if(chkFileCnt >= pImgFileList.length) {
			alert("상품 이미지를 하나라도 등록해야합니다.");
			return false;
		}
	
		const editor = $(form).find(".toast-ui-editor").data("data-toast-editor");
		const markdown = editor.getMarkdown().trim();
		const html = editor.getHTML().trim();
		
		if(markdown.length == 0) {
		  alert("내용을 입력해주세요");
		  editor.focus();
		  return false;
		}
		
		// 콤마 제거
		form.productPrice.value = uncomma(form.productPrice.value);
		form.productStock.value = uncomma(form.productStock.value);
		form.productDlvyPrice.value = uncomma(form.productDlvyPrice.value);
		
		form.productBody.value = html;
	}
	
	// 숫자에 천 단위 Comma 찍기
	const updateTextView = function(obj){
		let num = getNumber(obj.val());
		
		if(num == 0){
			obj.val("");
		} else {
			obj.val(num.toLocaleString());
		}
	}
	// 숫자만 입력하게
	const getNumber = function(str){
		let arr = str.split("");
		let out = new Array();
		
		for(let cnt = 0; cnt < arr.length; cnt++){
			if(isNaN(arr[cnt]) == false){
				out.push(arr[cnt]);
			}
		}
		
		return Number(out.join(""));
	}
	// Comma 제거
	const uncomma = function(str) {
        str = String(str);
        return str.replace(/[^\d]+/g, "");
    }
	
	// 택배사 목록 API
	const SmartDlvyGetData = async () => {
		const url = `https://info.sweettracker.co.kr/api/v1/companylist`;
		
		$.ajax({
			type: "GET",
			url: url,
			data: {t_key: "PeejEA9In6DkVstXuCzaaw"},
			dataType: "json",
			success: function(data) {
				$(data.Company).each(function(idx, data) {
					$("select[name=productCourier]").append(`<option value="\${data.Name}">\${data.Name}</option>`);
				})
			},
			error: function(e) {
				alert("정보를 불러오는데 실패했습니다.");
				console.log(e);
			}
		})
	}
	
	$(function(){
		$(".price, .stock").on("keyup",function(){
			updateTextView($(this));
		});
		
		$(".DlvyBox > a").click(function(){
			let dlvy = $(this).attr("data-dlvy");
			
			$("input[name=productDlvy]").val(dlvy);
			
			$(this).addClass("btn-accent");
			$(this).siblings().removeClass("btn-accent");
			
			if(dlvy == 0) {
				$("input[name=productDlvyPrice]").val(0);
			} else {
				$("input[name=productDlvyPrice]").val("");
			}
		});

		// SmartDlvyGetData();
	});
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
									<input class="price input input-bordered w-full text-lg" type="text" name="productPrice" placeholder="상품 가격을 입력해주세요." />
								</td>
							</tr>
							<tr>
								<th>상품 카테고리</th>
								<td>
									<select class="select select-bordered w-full" name="productCategory">
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
									<input class="stock input input-bordered w-full text-lg" type="text" name="productStock" placeholder="상품 재고를 입력해주세요." />
								</td>
							</tr>
							<tr>
								<th>배송 방식</th>
								<td>
									<input type="hidden" name="productDlvy" class="btn w-full" value="" />
									<div class="DlvyBox flex">
										<a class="btn flex-1 mr-1" href="javascript:(0)" data-dlvy="0">무료 배송</a>
										<a class="btn flex-1 ml-1" href="javascript:(0)" data-dlvy="1">유료 배송</a>
									</div>
									<label class="input-group mt-3">
										<span>택배사</span>
										<select class="select select-bordered flex-1" name="productCourier">
											<option value="">없음</option>
											<option value="테스트">테스트</option>
										</select>
									</label>
									<label class="input-group mt-3">
										<span>배송비용</span>
										<input class="price input input-bordered w-full text-lg" name="productDlvyPrice" type="text" placeholder="배송 비용" />
									</label>
								</td>
							</tr>
							<tr>
								<th>상품 이미지</th>
								<td>
									<div class="flex items-center">
										<span class="badge p-3 mr-3">첨부파일 #1</span>
										<input accept="image/gif, image/jpeg, image/png" class="w-full mb-2 file-input file-input-bordered" name="file__product__0__extra__productImg__1" type="file" />
									</div>
									<div class="flex items-center">
										<span class="badge p-3 mr-3">첨부파일 #2</span>
										<input accept="image/gif, image/jpeg, image/png" class="w-full mb-2 file-input file-input-bordered" name="file__product__0__extra__productImg__2" type="file" />
									</div>
									<div class="flex items-center">
										<span class="badge p-3 mr-3">첨부파일 #3</span>
										<input accept="image/gif, image/jpeg, image/png" class="w-full mb-2 file-input file-input-bordered" name="file__product__0__extra__productImg__3" type="file" />
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