<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Notice Detail"/>    
<%@ include file="../../usr/common/head.jsp" %>
<%@ include file="../../usr/common/toastUiEditorLib.jsp"%>

<script>

 	const params = {};
 	
 	// params.id - 게시글 번호
	params.id = parseInt('${param.id}'); 
	
	function NoticeDetail__increaseViewCount() {
		
		// localStorage에 정보를 담아둘 수 있음. ex) 자동 로그인 기능
		const localStorageKey = 'notice__' + params.id + '__alreadyView';
		
		if(localStorage.getItem(localStorageKey)) {
			return;
		}
		
		localStorage.setItem(localStorageKey, true);
		
		$.get('doIncreaseViewCountRd', {
			id : params.id,
			ajaxMode: 'Y'
		}, function(data){
			$('.notice-detail__view-count').empty().html(data.data1);
		}, 'json');
	}
	
	
	$(function(){
		
		NoticeDetail__increaseViewCount();
		
		// 테스트코드 - 시간이 지나면 조회수 증가
		setTimeout(NoticeDetail__increaseViewCount(), 2000);

	}) 
	
	
</script>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3 ">
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
						<th>작성 날짜</th>
						<td>${notice.regDate}</td>
					</tr>
					<tr>
						<th>수정 날짜</th>
						<td>${notice.updateDate}</td>
					</tr>
					<tr>
						<th>조회 수</th>
						<td><span class="badge notice-detail__view-count">${notice.viewCount}</span></td>
					</tr>
					<tr>
						<th>작성자</th>
						<td>${notice.writerName}</td>
					</tr>
					<tr>
						<th>제목</th>
						<td>${notice.title}</td>
					</tr>
					<tr>
						<th>내용</th>
						<td>
							<div class="toast-ui-viewer" >
								<script type="text/x-template">${notice.getForPrintBody() }</script>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
				
		</div>
		
		<div class="mt-2 flex justify-between">
			<button class="btn btn-primary" onclick="history.back();"><i class="fa-solid fa-right-from-bracket"></i>뒤로가기</button>				
			<c:if test="${notice.actorCanChangeData }">
				<a class="btn-text-link btn btn-active btn-ghost" href="modify?id=${notice.id }">수정</a>
				<a class="btn-text-link btn btn-active btn-ghost"  onclick="if(confirm('정말 삭제하시겠습니까?') == false) return false;" href="doDelete?id=${notice.id }">삭제</a>
			</c:if>			
		</div>
	</div>
	
</section>
	
<%@ include file="../../usr/common/foot.jsp"%>