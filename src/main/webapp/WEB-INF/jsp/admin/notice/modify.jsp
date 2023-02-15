<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Notice Modify"/>    
<%@ include file="../../usr/common/head.jsp" %>
<%@ include file="../../usr/common/toastUiEditorLib.jsp"%>

<script>
	//파일 검증
	const maxSizeMb = 10;
	const maxSize = maxSizeMb * 1024 * 1024;

	const productImgFileInput = [];

	productImgFileInput.push(form["file__product__0__extra__productImg__1"]);
	productImgFileInput.push(form["file__product__0__extra__productImg__2"]);
	productImgFileInput.push(form["file__product__0__extra__productImg__3"]);


	const deleteProductImgFileInput = [];

	deleteProductImgFileInput.push(form["file__product__0__extra__productImg__1"]);
	deleteProductImgFileInput.push(form["file__product__0__extra__productImg__2"]);
	deleteProductImgFileInput.push(form["file__product__0__extra__productImg__3"]);

	for(i = 0; i < productImgFileInput.length; i++) {
		if(deleteProductImgFileInput[i].checked) {
			productImgFileInput[i].value = '';
		}
	}

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

	//콤마 제거
	form.productPrice.value = uncomma(form.productPrice.value);
	form.productStock.value = uncomma(form.productStock.value);
	form.productDlvyPrice.value = uncomma(form.productDlvyPrice.value);
  
	form.productBody.value = html;
	}

	$(function(){
		if ($("#productImg_1").attr("src") == "${rq.getProfileFallbackImgUri()}") {
			$("#productImg_1").parent().remove();
		}
		if ($("#productImg_2").attr("src") == "${rq.getProfileFallbackImgUri()}") {
			$("#productImg_2").parent().remove();
		}
		if ($("#productImg_3").attr("src") == "${rq.getProfileFallbackImgUri()}") {
			$("#productImg_3").parent().remove();
		}	
	})
</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<form action="doModify" method="POST" onsubmit="submitForm(this); return false;">
			<input type="hidden" name="id" value="${notice.id }"/>
			<input type="hidden" name="body"/>
			<div class="table-box-type-1">
				<table class="table table-zebra w-full">
					<colgroup>
						<col width="200"/>
					</colgroup>

					<tbody>
						<tr>
							<th>번호</th>
							<td><div class="badge">${notice.id}</div></td>
						</tr>
						<tr>
							<th>작성날짜</th>
							<td>${notice.regDate}</td>
						</tr>
						<tr>
							<th>수정날짜</th>
							<td>${notice.updateDate}</td>
						</tr>
						<tr>
							<th>작성자</th>
							<td>${notice.writerName}</td>
						</tr>
						<tr>
							<th>제목</th>
							<td><input class="input input-bordered w-full max-w-xs"  type="text" name="title" placeholder="제목을 입력해주세요." value="${notice.title }"/></td>
						</tr>
						<tr>
							<th>첨부파일</th>
							<td>
								<div class="flex items-center">
									<span class="badge p-3 mr-3">첨부파일 #1</span>
									<input accept="image/gif, image/jpeg, image/png" class="w-full mb-2 file-input file-input-bordered" name="file__notice__0__extra__noticeImg__1" type="file" />
								</div>
								<div class="flex items-center">
									<span class="badge p-3 mr-3">첨부파일 #2</span>
									<input accept="image/gif, image/jpeg, image/png" class="w-full mb-2 file-input file-input-bordered" name="file__notice__0__extra__noticeImg__2" type="file" />
								</div>
								<div class="flex items-center">
									<span class="badge p-3 mr-3">첨부파일 #3</span>
									<input accept="image/gif, image/jpeg, image/png" class="w-full mb-2 file-input file-input-bordered" name="file__notice__0__extra__noticeImg__3" type="file" />
								</div>
							</td>
						</tr>
						<tr>
							<th>내용</th>
							<td>
								<div class="toast-ui-editor">
									<script type="text/x-template">${notice.getForPrintBody() }</script>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<button class="btn btn-active btn-ghost ">수정</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
		
		<div class="btns">
			<button class="btn-text-link btn btn-active btn-ghost"  type="button" onclick="history.back();">뒤로가기</button>	
			<a class="btn-text-link btn btn-active btn-ghost"  onclick="if(confirm('정말 삭제하시겠습니까?') == false) return false;" href="doDelete?id=${article.id }">삭제</a>
		</div>
		
		
	</div>
	
</section>

<%@ include file="../../usr/common/foot.jsp"%>