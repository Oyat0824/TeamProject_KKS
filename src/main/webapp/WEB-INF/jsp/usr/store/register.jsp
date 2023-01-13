<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Store Register" />
<%@ include file="../common/head.jsp"%>
<script>
	let validStoreName = null;
	
	// Submit 전 검증
	const StoreRegister__submit = function(form) {
		// 상점 이름 검증
		form.storeName.value = form.storeName.value.trim();
		if(form.storeName.value.length == 0) {
			alert("가게 이름을 입력해주세요.");
			form.storeName.focus();
			
			return false;
		}
		if(form.storeName.value == validStoreName) {
			alert("이미 사용중인 가게이름입니다.");
			form.storeName.focus();
			
			return false;
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
		<form action="doRegister" onsubmit="return StoreRegister__submit(this);">
			<div class="table-box-type-1">
				<table class="table table-zebra w-full">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>가게 이름</th>
							<td>
								<input onblur="return errorMsg(this);" class="input input-ghost w-full text-lg border-gray-400" type="text" name="storeName" placeholder="상점 이름을 적어주세요." />
								<div class="errorMsg mt-2 font-bold text-red-500 text-sm"></div>
							</td>
						</tr>
						<tr>
							<th>가게 로고</th>
							<td><input class="file-input file-input-bordered w-full border-gray-400" type="file" name="storeLogo" /></td>
						</tr>
						<tr>
							<th>가게 이미지</th>
							<td><input class="file-input file-input-bordered w-full border-gray-400" type="file" name="storeImg" /></td>
						</tr>
						<tr>
							<th>가게 설명</th>
							<td><textarea class="input w-full text-lg border-gray-400" name="storeDesc" placeholder="가게 소개 글" /></textarea></td>
						</tr>
						<tr>
							<td colspan="2"><button class="btn btn-outline btn-accent w-full"><i class="fa-solid fa-store"></i>가게 등록</button></td>
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