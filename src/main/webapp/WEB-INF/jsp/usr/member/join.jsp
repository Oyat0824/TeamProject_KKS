<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Member Join" />
<%@ include file="../common/head.jsp"%>

<script>
	// 정규식
	const reg_num = /^01([0|1|6|7|8|9])-([0-9]{3,4})-([0-9]{4})$/; // 휴대폰 번호
	const reg_email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/; // 길이까지 확실한 검증
	const reg_id = /^[A-Za-z]{1}[A-Za-z0-9_-]{3,19}$/; // 반드시 영문으로 시작 숫자+언더바/하이픈 허용 4~20자리
	
	let validLoginId = null;
	
	// Submit 전 검증
	const MemberJoin__submit = function(form) {
		// 아이디 검증
		form.loginId.value = form.loginId.value.trim();
		if(form.loginId.value.length == 0) {
			alert("아이디를 입력해주세요.");
			form.loginId.focus();
			
			return false;
		}
		if(form.loginId.value == validLoginId) {
			alert("이미 사용중인 아이디입니다.");
			form.loginId.focus();
			
			return false;
		}
		if(!reg_id.test(form.loginId.value)) {
			alert("4글자 이상으로 영문 + 숫자 조합으로 입력해주세요.\n"
				+ "언더바(_), 하이픈(-)도 조합으로 사용가능합니다.")
			form.loginId.focus();
			
			return false;
		}
		
		// 비밀번호 검증
		form.loginPw.value = form.loginPw.value.trim();
		if(form.loginPw.value.length == 0) {
			alert("비밀번호를 입력해주세요.");
			form.loginPw.focus();
			
			return false;
		}
		form.loginPwChk.value = form.loginPwChk.value.trim();
		if(form.loginPwChk.value.length == 0) {
			alert("비밀번호 확인을 입력해주세요.");
			form.loginPwChk.focus();
			
			return false;
		}
		if (form.loginPw.value != form.loginPwChk.value) {
			alert('비밀번호가 일치하지 않습니다');
			form.loginPw.focus();
		
			return false;
		}
		
		// 이름 검증
		form.name.value = form.name.value.trim();
		if(form.name.value.length == 0) {
			alert("이름을 입력해주세요.");
			form.name.focus();
			
			return false;
		}
		
		// 성별 검증
		form.gender.value = form.gender.value.trim();
		if(form.gender.value.length == 0  || (!form.gender.value == "male" && !form.gender.value == "female")) {
			alert("성별을 선택해주세요.");
			form.gender.focus();
			
			return false;
		}
		
		// 생년월일 검증
		form.birthday.value = form.birthday.value.trim();
		if(form.birthday.value.length == 0) {
			alert("생년월일을 선택해주세요.");
			form.birthday.focus();
			
			return false;
		}
		
		// 파일 검증
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
		
		if(el[0].name == "loginId") {
			if(!reg_id.test(el.val())) {
				el.next(".errorMsg")
				.addClass("text-red-500")
				.removeClass("text-green-400")
				.html("4글자 이상으로 영문 + 숫자 조합으로 입력해주세요.<br />"
						+ "언더바( _ ), 하이픈( - )도 조합으로 사용가능합니다.");
				
				return false;
			}
			
			$.get('getLoginIdDup', {
				loginId : el.val(),
				ajaxMode : 'Y'
			}, function(data){
				if(data.fail) {
					validLoginId = data.data1;
					el.next(".errorMsg").addClass("text-red-500").removeClass("text-green-400").html(`\${data.data1}은(는) \${data.msg}`);
				} else {
					validLoginId = null;
					el.next(".errorMsg").addClass("text-green-400").removeClass("text-red-500").html(`\${data.msg}`);
				}
			}, 'json');
			
			return false;
		}
		
		if(el.val().length == 0) {
			el.next(".errorMsg").addClass("text-red-500").removeClass("text-green-400").html("필수 정보입니다.");
			return false;
		}
		
		if(el[0].name == "loginPwChk") {
			if(form.loginPw.value != el.val()) {
				el.next(".errorMsg").html("비밀번호가 일치하지 않습니다.");
			}
		}
	}
	
	// 이미지 미리보기 기능
	const imgChg = function(e) {
		const selectedFile = e.files[0];
		const fileReader = new FileReader();
		
		fileReader.readAsDataURL(selectedFile);
		fileReader.onload = function () {
			$(e).prev().children("img").attr("src", fileReader.result);
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
		
		// 생년월일 달력 표기
		$("#birthday").datepicker({
			// showOn: "both", // 버튼과 텍스트 필드 모두 캘린더를 보여준다.
			// buttonImage: "", // 버튼 이미지
			// buttonImageOnly: false, // 버튼에 있는 이미지만 표시한다.
			changeMonth: true, // 월을 바꿀수 있는 셀렉트 박스를 표시한다.
			changeYear: true, // 년을 바꿀 수 있는 셀렉트 박스를 표시한다.
			minDate: '-100y', // 현재날짜로부터 100년이전까지 년을 표시한다.
			maxDate: '0', // 현재날짜 이후는 disabled
			nextText: '다음 달', // next 아이콘의 툴팁.
			prevText: '이전 달', // prev 아이콘의 툴팁.
			numberOfMonths: [1,1], // 한번에 얼마나 많은 월을 표시할것인가. [2,3] 일 경우, 2(행) x 3(열) = 6개의 월을 표시한다.
			stepMonths: 1, // next, prev 버튼을 클릭했을때 얼마나 많은 월을 이동하여 표시하는가. 
			yearRange: 'c-100:c+10', // 년도 선택 셀렉트박스를 현재 년도에서 이전, 이후로 얼마의 범위를 표시할것인가.
			showButtonPanel: false, // 캘린더 하단에 버튼 패널을 표시한다. ( ...으로 표시되는부분이다.) 
			dateFormat: "yy-mm-dd", // 텍스트 필드에 입력되는 날짜 형식.
			showMonthAfterYear: true , // 월, 년순의 셀렉트 박스를 년,월 순으로 바꿔준다. 
			dayNamesMin: ['월', '화', '수', '목', '금', '토', '일'], // 요일의 한글 형식.
			monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] // 월의 한글 형식.
		});
	})
</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<form action="doJoin" method="POST" enctype="multipart/form-data" onsubmit="return MemberJoin__submit(this);">
			<div class="table-box-type-1">
				<table class="table table-zebra w-full">
					<colgroup>
						<col width="200" />
					</colgroup>

					<tbody>
						<tr>
							<th>아이디</th>
							<td><input onblur="return errorMsg(this);" class="input input-ghost w-full text-lg border-gray-400"
								type="text" name="loginId" placeholder="아이디를 입력해주세요." />
								<div class="errorMsg mt-2 font-bold text-red-500 text-sm"></div></td>
						</tr>
						<tr>
							<th>비밀번호</th>
							<td><input onblur="return errorMsg(this);" class="input input-ghost w-full text-lg border-gray-400"
								type="password" name="loginPw" placeholder="비밀번호를 입력해주세요." />
								<div class="errorMsg mt-2 font-bold text-red-500 text-sm"></div></td>
						</tr>
						<tr>
							<th>비밀번호 확인</th>
							<td><input onblur="return errorMsg(this);" class="bg-white input input-ghost w-full text-lg border-gray-400"
								type="password" name="loginPwChk" placeholder="비밀번호 확인을 위해 입력해주세요." value="" />
								<div class="errorMsg mt-2 font-bold text-red-500 text-sm"></div></td>
						</tr>
						<tr>
							<th>이름</th>
							<td><input onblur="return errorMsg(this);" class="input input-ghost w-full text-lg border-gray-400"
								type="text" name="name" placeholder="이름을 입력해주세요." />
								<div class="errorMsg mt-2 font-bold text-red-500 text-sm"></div></td>
						</tr>
						<tr>
							<th>성별</th>
							<td><label class="label cursor-pointer" for="male"> <span class="label-text">남성</span> <input
									id="male" type="radio" name="gender" class="radio checked:bg-blue-500" value="male" checked />
							</label> <label class="label cursor-pointer" for="female"> <span class="label-text">여성</span> <input id="female"
									type="radio" name="gender" class="radio checked:bg-red-500" value="female" />
							</label></td>
						</tr>
						<tr>
							<th>생년월일</th>
							<td><input onblur="return errorMsg(this);" class="bg-white input input-ghost w-full text-lg border-gray-400"
								id="birthday" name="birthday" placeholder="생년월일을 입력해주세요." readonly />
								<div class="errorMsg mt-2 font-bold text-red-500 text-sm"></div></td>
						</tr>
						<tr>
							<th>프로필 이미지</th>
							<td>
								<div><img id="profileImg" src="" alt="" /></div>
								<input onchange="return imgChg(this);" accept="image/gif, image/jpeg, image/png" class="file-input file-input-bordered border-gray-400" type="file" name="file__member__0__extra__profileImg__1" />
							</td>
						</tr>
						<tr>
							<th>이메일</th>
							<td><input onblur="return errorMsg(this);" class="bg-white input input-ghost w-full text-lg border-gray-400"
								type="email" name="email" placeholder="이메일을 입력해주세요." />
								<div class="errorMsg mt-2 font-bold text-red-500 text-sm"></div></td>
						</tr>
						<tr>
							<th>전화번호</th>
							<td><input onblur="return errorMsg(this);" maxlength="13"
								class="bg-white input input-ghost w-full text-lg border-gray-400" type="tel" name=cellphoneNum
								placeholder="전화번호를 입력해주세요." />
								<div class="errorMsg mt-2 font-bold text-red-500 text-sm"></div></td>
						</tr>

						<tr>
							<td colspan="2"><button class="btn btn-outline btn-accent w-full">회원가입</button></td>
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