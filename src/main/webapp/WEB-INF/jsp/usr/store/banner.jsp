<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Store Banner" />
<c:set var="menuName" value="banner" />
<%@ include file="../common/head.jsp"%>

<script>
	const BannerWrite__submitForm = function(form) {
		form.orderNo.value = form.orderNo.value.trim();
		
		if(form.orderNo.value == "") {
			alert("순서를 입력해주세요.");
			form.orderNo.focus();
			
			return false;
		}
		
		// 파일 검증
		const maxSizeMb = 10;
		const maxSize = maxSizeMb * 1024 * 1024;

		const bImgFileList = [];
		
		bImgFileList.push(form["file__store__0__extra__bannerImg__1"]);
		bImgFileList.push(form["file__store__0__extra__bannerImg__2"]);
		bImgFileList.push(form["file__store__0__extra__bannerImg__3"]);
		
		for(i = 0; i < bImgFileList.length; i++) {
			if(bImgFileList[i].value && bImgFileList[i].files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요");
				bImgFileList[i].focus();
				
				return false;
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
	
	// 이미지 삭제 시 물어보기
	const confirmDelBanner = function(fileNo) {
		let uri = "delBanner?id=${param.id}&storeModifyAuthKey=${param.storeModifyAuthKey}&fileNo=" + fileNo;
		let chkDel = false;
		
		chkDel = confirm(`배너 이미지 #\${fileNo}를 삭제하시겠습니까?`);
		
		if(chkDel) {
			return location.href=uri;
		}
		
		return false;
	}
</script>

<section>
	<div class="flex container mx-auto">
		<%@ include file="../common/sideMenu.jsp"%>
		
		<div class="w-full my-10">
		
			<form action="addBanner" method="POST" enctype="multipart/form-data" onsubmit="return BannerWrite__submitForm(this);">
				<input type="hidden" name="id" value="${param.id}" />
				<input type="hidden" name="storeModifyAuthKey" value="${param.storeModifyAuthKey}" />
				<div class="table-box-type-2">
					<table class="table w-11/12 mx-auto">
						<colgroup>
							<col width="200" />
						</colgroup>
						
						<thead>
							<tr>
								<th colspan="2" class="cHead">배너 관리</th>
							</tr>
						</thead>

						<tbody>					
							<tr>
								<th>배너 이미지 #1</th>
								<td>
									<c:choose>
										<c:when test="${rq.getImgUri('store', param.id, 'bannerImg', 1) == rq.getProfileFallbackImgUri()}">
											<div>
												<img id="bannerImg" class="w-full h-40 object-fill mb-3 " src="${rq.getProfileFallbackImgUri()}" alt="" />
											</div>
											<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="w-full file-input file-input-bordered border-gray-400" name="file__store__0__extra__bannerImg__1" type="file" />
										</c:when>
										<c:otherwise>
											<div>
												<img id="bannerImg" class="w-full h-40 object-fill mb-3 " src="${rq.getImgUri('store', param.id, 'bannerImg', 1)}" alt="" />
											</div>
											<div class="flex flex-col">
												<a class="btn btn-error w-full mb-5" href="javascript:confirmDelBanner(1)">이미지 삭제</a>
												<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="w-full file-input file-input-bordered border-gray-400" name="file__store__0__extra__bannerImg__1" type="file" />
											</div>
								        </c:otherwise>
							        </c:choose>
								</td>
							</tr>
							<tr>
								<th>배너 이미지 #2</th>
								<td>
									<c:choose>
										<c:when test="${rq.getImgUri('store', param.id, 'bannerImg', 2) == rq.getProfileFallbackImgUri()}">
											<div>
												<img id="bannerImg" class="w-full h-40 object-fill mb-3 " src="${rq.getProfileFallbackImgUri()}" alt="" />
											</div>
											<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="w-full file-input file-input-bordered border-gray-400" name="file__store__0__extra__bannerImg__2" type="file" />
										</c:when>
										<c:otherwise>
											<div>
												<img id="bannerImg" class="w-full h-40 object-fill mb-3 " src="${rq.getImgUri('store', param.id, 'bannerImg', 2)}" alt="" />
											</div>
											<div class="flex flex-col">
												<a class="btn btn-error w-full mb-5" href="javascript:confirmDelBanner(2)">이미지 삭제</a>
												<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="w-full file-input file-input-bordered border-gray-400" name="file__store__0__extra__bannerImg__2" type="file" />
											</div>
								        </c:otherwise>
							        </c:choose>
								</td>
							</tr>
							<tr>
								<th>배너 이미지 #3</th>
								<td>
									<c:choose>
										<c:when test="${rq.getImgUri('store', param.id, 'bannerImg', 3) == rq.getProfileFallbackImgUri()}">
											<div>
												<img id="bannerImg" class="w-full h-40 object-fill mb-3 " src="${rq.getProfileFallbackImgUri()}" alt="" />
											</div>
											<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="w-full file-input file-input-bordered border-gray-400" name="file__store__0__extra__bannerImg__3" type="file" />
										</c:when>
										<c:otherwise>
											<div>
												<img id="bannerImg" class="w-full h-40 object-fill mb-3 " src="${rq.getImgUri('store', param.id, 'bannerImg', 3)}" alt="" />
											</div>
											<div class="flex flex-col">
												<a class="btn btn-error w-full mb-5" href="javascript:confirmDelBanner(3)">이미지 삭제</a>
												<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="w-full file-input file-input-bordered border-gray-400" name="file__store__0__extra__bannerImg__3" type="file" />
											</div>
								        </c:otherwise>
							        </c:choose>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="w-11/12 mx-auto mt-5">
					<button class="btn btn-success w-full">
						등록
					</button>
				</div>
			</form>
		</div>
	</div>
</section>

<%@ include file="../common/foot.jsp"%>