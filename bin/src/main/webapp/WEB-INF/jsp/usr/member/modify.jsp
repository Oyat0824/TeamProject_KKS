<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Member Modify" />
<%@ include file="../common/head.jsp"%>

<script>
	// 정규식
	const reg_num = /^01([0|1|6|7|8|9])-([0-9]{3,4})-([0-9]{4})$/; // 휴대폰 번호
	const reg_email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/; // 길이까지 확실한 검증
	
	// Submit 전 검증
	const MemberModify__submit = function(form) {
		// 파일 검증
		const deleteProfileImgFileInput = form["deleteFile__member__0__extra__profileImg__1"];
		
		if (deleteProfileImgFileInput.checked) {
			form["file__member__0__extra__profileImg__1"].value = '';
		}
		
		const maxSizeMb = 5;
		const maxSize = maxSizeMb * 1024 * 1024;
		
		const profileImgFileInput = form["file__member__0__extra__profileImg__1"];
		if (profileImgFileInput.value) {
			if (profileImgFileInput.files[0].size > maxSize) {
				alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요");
				profileImgFileInput.focus();
				
				return;
			}
		}
		
		// 이메일 검증
		form.email.value = form.email.value.trim();
		if(form.email.value.length == 0) {
			alert("이메일을 입력해주세요.");
			form.email.focus();
			
			return false;
		}
		if(!reg_email.test(form.email.value)) {
			alert("이메일 형식이 틀렸습니다.\n"
				+ "ex) abc123@email.com"
				);
			form.email.focus();
			
			return false;
		}
		
		// 전화번호 검증
		form.cellphoneNum.value = form.cellphoneNum.value.trim();
		if(form.cellphoneNum.value.length == 0) {
			alert("전화번호를 입력해주세요.");
			form.cellphoneNum.focus();
			
			return false;
		}
		if(!reg_num.test(form.cellphoneNum.value)) {
			alert("전화번호 형식이 틀렸습니다.\n"
				+ "ex) 010-1234-5678"
				);
			form.cellphoneNum.focus();
			
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
	}
	
	// 이미지 미리보기 기능
	const imgChg = function(e) {
		const selectedFile = e.files[0];
		const fileReader = new FileReader();
		
		fileReader.readAsDataURL(selectedFile);
		
		fileReader.onload = function () {
			$(e).siblings("div").children("#profileImg").attr("src", fileReader.result);
		};
	}
	
	$(function(){
		// 인풋에 입력 시, 에러 메시지 삭제
		$(".input").on("propertychange change paste input", function() {
			if(!$(this).next(".errorMsg").html() == "") {
				$(this).next(".errorMsg").empty();
			}
		});
		
		// email 부분 영어, 숫자, 특수문자(@_.-) 만 작성되게
		$("input[name=email]").on("input", function(event){
			if (!(event.keyCode >=37 && event.keyCode<=40)) { 
				let inputVal = $(this).val();
		
				$(this).val(inputVal.replace(/[^a-z0-9@_.-]/gi, ""));
			} 
		});
		
		// cellphoneNum 부분 숫자만 입력되게 자동으로 하이픈 삽입
		$("input[name=cellphoneNum]").on("input", function(event){
			if (!(event.keyCode >=37 && event.keyCode<=40)) { 
				let inputVal = $(this).val();
				
				$(this).val(inputVal.replace(/[^0-9]/g, "").replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, ""));
			} 
		});
		
		// 프로필 사진이 없는 경우
		if($("#profileImg").attr("src") == "${rq.getProfileFallbackImgUri()}") {
			$("#profileImg").parent().siblings("div.delBtn").remove();
		}
	})
</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<form action="doModify" method="POST" enctype="multipart/form-data" onsubmit="return MemberModify__submit(this);">
			<input type="hidden" name="memberModifyAuthKey" value="${param.memberModifyAuthKey}" />
			<div class="table-box-type-1">
				<table class="table table-zebra w-full">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>가입일</th>
							<td>${rq.loginedMember.regDate }</td>
						</tr>
						<tr>
							<th>아이디</th>
							<td>${rq.loginedMember.loginId }</td>
						</tr>
						<tr>
							<th>이름</th>
							<td>${rq.loginedMember.name }</td>
						</tr>
						<tr>
							<th>성별</th>
							<td>${rq.loginedMember.gender == "male" ? "남자" : "여자" }</td>
						</tr>
						<tr>
							<th>생년월일</th>
							<td>${rq.loginedMember.birthday }</td>
						</tr>
						<tr>
							<th>프로필 이미지</th>
							<td>
								<div><img id="profileImg" class="w-40 h-40 mx-auto object-cover" src="${rq.getImgUri('member', rq.loginedMemberId, 'profileImg')}" alt="" /></div>
								<div class="mt-2 delBtn">
									<label class="cursor-pointer inline-flex">
										<span class="label-text mr-2 mt-1">이미지 삭제</span>
										<input class="ckeckbox" type="checkbox" name="deleteFile__member__0__extra__profileImg__1" value="Y" />
									</label>
								</div>
								<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="file-input file-input-bordered border-gray-400" type="file" name="file__member__0__extra__profileImg__1" />
							</td>
						</tr>
						<tr>
							<th>이메일</th>
							<td><input onblur="return errorMsg(this);" class="bg-white input input-ghost w-full text-lg border-gray-400" type="email" name="email"
								placeholder="이메일을 입력해주세요." value="${rq.loginedMember.email }" />
								<div class="errorMsg mt-2 font-bold text-red-500 text-sm"></div></td>
						</tr>
						<tr>
							<th>전화번호</th>
							<td><input onblur="return errorMsg(this);" class="bg-white input input-ghost w-full text-lg border-gray-400" type="tel" name=cellphoneNum
								placeholder="전화번호를 입력해주세요." value="${rq.loginedMember.cellphoneNum }" />
								<div class="errorMsg mt-2 font-bold text-red-500 text-sm"></div></td>
						</tr>
						<tr>
							<th>회원 등급</th>
							<td>
								<c:if test="${rq.loginedMember.memberType == 3}">일반 회원</c:if>
								<c:if test="${rq.loginedMember.memberType == 6}">판매자</c:if>
								<c:if test="${rq.loginedMember.memberType == 9}">관리자</c:if>
							</td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="btns flex justify-between mt-5">
				<button class="btn btn-primary" onclick="history.back();"><i class="fa-solid fa-right-from-bracket"></i>뒤로가기</button>
				<div>
					<a class="btn btn-outline btn-info" href="passwordModify?memberModifyAuthKey=${param.memberModifyAuthKey }" >비밀번호 변경</a>
					<button class="btn btn-outline btn-success">회원정보 수정</button>
				</div>
			</div>
		</form>

	</div>
</section>
<%@ include file="../common/foot.jsp"%>