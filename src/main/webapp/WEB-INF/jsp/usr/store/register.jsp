<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Store Register" />
<%@ include file="../common/head.jsp"%>
<script>
	let validStoreName = null;
	
	// Submit 전 검증
	const StoreRegister__submit = function(form) {
		// 스토어 이름 검증
		form.storeName.value = form.storeName.value.trim();
		if(form.storeName.value.length == 0) {
			alert("스토어 이름을 입력해주세요.");
			form.storeName.focus();
			
			return false;
		}
		if(form.storeName.value == validStoreName) {
			alert("이미 사용중인 스토어이름입니다.");
			form.storeName.focus();
			
			return false;
		}	
		
		// 파일 검증
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
	
	// 에러 메시지
	const errorMsg = function(e) {
		const el = $(e);
		const form = el.closest("form")[0];
		
		if(el.val().length == 0) {
			el.next(".errorMsg").addClass("text-red-500").removeClass("text-green-400").html("필수 정보입니다.");
			return false;
		}
		
		if(el[0].name == "storeName") {
			$.get('getStoreNameDup', {
				storeName : el.val(),
				ajaxMode : 'Y'
			}, function(data){
				if(data.fail) {
					validStoreName = data.data1;
					console.log(validStoreName)
					el.next(".errorMsg").addClass("text-red-500").removeClass("text-green-400").html(`\${data.data1}은(는) \${data.msg}`);
				} else {
					validStoreName = null;
					el.next(".errorMsg").addClass("text-green-400").removeClass("text-red-500").html(`\${data.msg}`);
				}
			}, 'json');
			
			return false;
		}
	}
	
	// 이미지 미리보기 기능
	const imgChg = function(e) {
		const selectedFile = e.files[0];
		const fileReader = new FileReader();
		
		fileReader.readAsDataURL(selectedFile);
		
		fileReader.onload = function () {
			$(e).siblings("div").children("img").attr("src", fileReader.result);
		};
	}

	$(function(){
		// 인풋에 입력 시, 에러 메시지 삭제
		$(".input").on("propertychange change paste input", function() {
			if(!$(this).next(".errorMsg").html() == "") {
				$(this).next(".errorMsg").empty();
			}
		});
	})
</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<form action="doRegister" method="POST" enctype="multipart/form-data" onsubmit="return StoreRegister__submit(this);">
			<div class="table-box-type-1">
				<table class="table table-zebra w-full">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>스토어 이름</th>
							<td>
								<input onblur="return errorMsg(this);" class="input input-ghost w-full text-lg border-gray-400" type="text" name="storeName" placeholder="스토어 이름을 적어주세요." />
								<div class="errorMsg mt-2 font-bold text-red-500 text-sm"></div>
							</td>
						</tr>
						<tr>
							<th>스토어 로고</th>
							<td>
								<div><img id="storeLogo" class="object-cover mx-auto" style="width: 160px; height: 50px" src="${rq.getProfileFallbackImgUri() }" alt="" /></div>
								<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="file-input file-input-bordered border-gray-400" name="file__store__0__extra__storeLogo__1" type="file" />
							</td>
						</tr>
						<tr>
							<th>스토어 이미지</th>
							<td>
								<div><img id="storeImg" class="object-cover mx-auto" style="width: 100px; height: 100px" src="${rq.getProfileFallbackImgUri() }" alt="" /></div>
								<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="file-input file-input-bordered border-gray-400" name="file__store__0__extra__storeImg__1" type="file" />
							</td>
						</tr>
						<tr>
							<th>스토어 소개</th>
							<td><textarea style="height: 300px" class="input w-full text-lg border-gray-400" name="storeDesc" placeholder="스토어 소개"></textarea></td>
						</tr>
						<tr>
							<td colspan="2"><button class="btn btn-outline btn-accent w-full"><i class="fa-solid fa-store"></i>스토어 등록</button></td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
		<div class="btns mt-5">
			<button class="btn btn-primary" onclick="history.back();"><i class="fa-solid fa-right-from-bracket"></i>뒤로가기</button>
		</div>
	</div>
</section>


<%@ include file="../common/foot.jsp"%>