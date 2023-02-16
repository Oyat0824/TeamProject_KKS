<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Notice Page"/>    
<%@ include file="../../usr/common/head.jsp" %>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="mb-2 flex justify-between items-center">
			<div><span>${noticesCount } 개</span></div>
			
			<form>
				<select data-value="${searchKeywordTypeCode }" class="select select-bordered" name="searchKeywordTypeCode">
					<option value="title">제목</option>
					<option value="body">내용</option>
					<option value="title,body">제목 + 내용</option>
				</select>

				<input class="ml-2 w-84 input input-bordered" type="text" name="searchKeyword" placeholder="검색어를 입력해주세요" maxlength="20" value="${searchKeyword }" />

				<button class="ml-2 btn btn-active btn-ghost">검색</button>
			</form>
			
		</div>
		
		<c:choose>
			<c:when test="${noticesCount == 0 }">
				<div class="text-center mt-4">조건에 일치하는 검색결과가 없습니다</div>
			</c:when>
			
		</c:choose>
		
		<div class="table-box-type-1">
			<table class="table w-full">
			
				<colgroup>
					<col width="60"/>
					<col width="200"/>
					<col />
					<col width="120"/>
					<col width="50"/>
					<col width="50"/>
				</colgroup>
				
				<thead>
					<tr>
						<th>번호</th>
						<th>날짜</th>
						<th>제목</th>
						<th>작성자</th>
						<th>조회수</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="notice" items="${notices}">
						<tr class="hover">	
							<td>${notice.id}</td>
							<td>${notice.regDate.substring(2, 16)}</td>
							<td><a class="hover:underline" href="../notice/detail?id=${notice.id}">${notice.title}</a></td>
							<td>${notice.writerName}</td>
							<td>${notice.viewCount}</td>
						</tr>
					</c:forEach>
					
				</tbody>
				
			</table>	
			
		</div>
		
		<div class="mt-2 flex justify-between">
			<button class="btn btn-primary" onclick="history.back();"><i class="fa-solid fa-right-from-bracket"></i>뒤로가기</button>
		</div>

		<div class="page-menu mt-2 flex justify-center">
		
			<div class="btn-group">
				<!-- «  -->
				<!-- »  -->
							<!-- 지정한 만큼 페이징 수 보이기  -->
				<c:set var="pageMenuLen" value="10" />
				<c:set var="startPage" value="${page - pageMenuLen >= 1 ? page - pageMenuLen : 1}" />
				<c:set var="endPage" value="${page + pageMenuLen <= pagesCount ? page + pageMenuLen : pagesCount}" />
				
				<!-- 페이지 처음과 끝으로 이동 -->
				<c:if test="${noticesCount != 0 }">
					<c:if test="${page == 1 }">
						<a class="btn btn-sm btn-disabled">«</a>
						<a class="btn btn-sm btn-disabled">&lt;</a>
					</c:if>
					<c:if test="${page > 1 }">
						<a class="btn btn-sm" href="${pageBaseUri }&page=1">«</a>
						<a class="btn btn-sm" href="${pageBaseUri }&page=${page - 1 }">&lt;</a>
					</c:if>
					<c:forEach begin="${startPage }" end="${endPage }" var="i">
						<a class="btn btn-sm ${page == i ? 'btn-active' : ''}" href="${pageBaseUri }&page=${i }">${i }</a>
					</c:forEach>
					<c:if test="${page < pagesCount }">
						<a class="btn btn-sm" href="${pageBaseUri }&page=${page + 1 }">&gt;</a>
						<a class="btn btn-sm" href="${pageBaseUri }&page=${pagesCount }">»</a>
					</c:if>
					<c:if test="${page == pagesCount }">
						<a class="btn btn-sm btn-disabled">&gt;</a>
						<a class="btn btn-sm btn-disabled">»</a>
					</c:if>
				</c:if>
				
			</div>
			
		</div>
		
	</div>
	
</section>

<%@ include file="../../usr/common/foot.jsp"%>	