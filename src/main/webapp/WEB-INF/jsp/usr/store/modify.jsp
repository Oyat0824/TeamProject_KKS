<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Store Modify" />
<%@ include file="../common/head.jsp"%>
<script>
	// Submit 전 검증
	const StoreModify__submit = function(form) {
		// 파일 검증
		const deleteStoreLogoFileInput = form["deleteFile__store__0__extra__storeLogo__1"];
		const deleteStoreImgFileInput = form["deleteFile__store__0__extra__storeImg__1"];

		if (deleteStoreLogoFileInput.checked) {
			form["file__store__0__extra__storeLogo__1"].value = '';
		}
		if (deleteStoreImgFileInput.checked) {
			form["file__store__0__extra__storeImg__1"].value = '';
		}

		const maxSizeMb = 10;
		const maxSize = maxSizeMb * 1024 * 1024;

		const storeLogoFileInput = form["file__store__0__extra__storeLogo__1"];
		const storeImgFileInput = form["file__store__0__extra__storeImg__1"];

		if (storeLogoFileInput.value) {
			if (storeLogoFileInput.files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요");
				storeLogoFileInput.focus();

				return;
			}
		}

		if (storeImgFileInput.value) {
			if (storeImgFileInput.files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요");
				storeImgFileInput.focus();

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
	
	$(function(){
		if($("#storeLogo").attr("src") == "${rq.getProfileFallbackImgUri()}") {
			$("#storeLogo").parent().siblings("div.delBtn").remove();
		}
		
		if($("#storeImg").attr("src") == "${rq.getProfileFallbackImgUri()}") {
			$("#storeImg").parent().siblings("div.delBtn").remove();
		}
	})
</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<form action="doModify" method="POST" enctype="multipart/form-data" onsubmit="return StoreModify__submit(this);">
			<input type="hidden" name="id" value="${store.id}" /> <input type="hidden" name="storeModifyAuthKey"
				value="${param.storeModifyAuthKey}" />
			<div class="table-box-type-1">
				<table class="table table-zebra w-full">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>스토어 로고</th>
							<td>
								<div>
									<img id="storeLogo" class="object-cover mx-auto" style="width: 160px; height: 50px" src="${rq.getImgUri('store', store.id, 'storeLogo')}" alt="" />
								</div>
								<div class="mt-2 delBtn">
									<label class="cursor-pointer inline-flex"> <span class="label-text mr-2 mt-1">이미지 삭제</span> <input
										class="ckeckbox" type="checkbox" name="deleteFile__store__0__extra__storeLogo__1" value="Y" />
									</label>
								</div>
								<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="file-input file-input-bordered border-gray-400" name="file__store__0__extra__storeLogo__1" type="file" />
							</td>
						</tr>
						<tr>
							<th>스토어 이미지</th>
							<td>
								<div>
									<img id="storeImg" class="object-cover mx-auto" style="width: 100px; height: 100px" src="${rq.getImgUri('store', store.id, 'storeImg')}" alt="" />
								</div>
								<div class="mt-2 delBtn">
									<label class="cursor-pointer inline-flex"> <span class="label-text mr-2 mt-1">이미지 삭제</span> <input
										class="ckeckbox" type="checkbox" name="deleteFile__store__0__extra__storeImg__1" value="Y" />
									</label>
								</div>
								<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="file-input file-input-bordered border-gray-400" name="file__store__0__extra__storeImg__1" type="file" />
							</td>
						</tr>
						<tr>
							<th>스토어 소개</th>
							<td><textarea style="height: 300px" class="input w-full text-lg border-gray-400" name="storeDesc"
									placeholder="스토어 소개">${store.storeDesc}</textarea></td>
						</tr>
						<tr>
							<td colspan="2">
								<button class="btn btn-outline btn-accent w-full">
									<i class="fa-solid fa-store"></i>스토어 수정
								</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>

		<div class="btns flex justify-between mt-5">
			<button class="btn btn-primary" onclick="history.back();">뒤로가기</button>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>