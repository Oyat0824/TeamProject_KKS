<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="ADMIN PAGE - MEMBER LIST" />
<c:set var="menuName" value="memberList" />
<%@ include file="../../usr/common/head.jsp"%>

<section class="mt-8 text-xl">
	<div class="container mx-auto px-3">
		<div class="mb-2 flex justify-between items-center">
			<div>
				<span>회원수 : ${membersCount }</span>
			</div>
			<form>
				<select data-value="${memberType }" class="select select-bordered" name="memberType">
					<option value="0">전체</option>
					<option value="3">일반</option>
					<option value="6">판매자</option>
					<option value="9">관리자</option>
				</select>

				<select data-value="${searchKeywordTypeCode }" class="select select-bordered" name="searchKeywordTypeCode">
					<option value="loginId,name">전체</option>
					<option value="loginId">아이디</option>
					<option value="name">이름</option>
				</select>

				<input class="ml-2 w-84 input input-bordered" type="text" name="searchKeyword" placeholder="검색어를 입력해주세요" maxlength="20" value="${searchKeyword }" />

				<button class="ml-2 btn btn-active btn-ghost">검색</button>
			</form>
		</div>
		<c:choose>
			<c:when test="${membersCount == 0}">
				<div class="text-center py-2">조건에 일치하는 검색결과가 없습니다</div>
			</c:when>
			<c:otherwise>
				<div class="table-box-type-1">
					<table class="table w-full">
						<thead>
							<tr>
								<th><input type="checkbox" class="checkbox-all-member-id" /></th>
								<th>번호</th>
								<th>가입날짜</th>
								<th>수정날짜</th>
								<th>아이디</th>
								<th>이름</th>
								<th>스토어 등록</th>
								<th>이메일</th>
								<th>삭제여부</th>
								<th>삭제날짜</th>
							</tr>
						</thead>

						<tbody>
							<c:forEach var="member" items="${members}">
								<c:if test="${member.memberType != 9 }">
									<tr class="hover">
										<c:choose>
											<c:when test="${member.delStatus != true }">
												<td><input type="checkbox" class="checkbox-member-id" value="${member.id }" /></td>
											</c:when>
											<c:otherwise>
												<td><input type="checkbox" class="checkbox-member-id" value="${member.id }" disabled/></td>
											</c:otherwise>
										</c:choose>
										<td>${member.id}</td>
										<td>${member.regDate.substring(2,16)}</td>
										<td>${member.updateDate.substring(2,16)}</td>
										<td>${member.loginId}</td>
										<td>${member.name}</td>
										<td>${member.storeState}</td>
										<td>${member.email}</td>
										<td>${member.delStatusStr()}</td>
										<td>${member.delDateStr()}</td>
									</tr>
								</c:if>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:otherwise>
		</c:choose>

		<script>
			$('.checkbox-all-member-id').change(function() {
				const allCheck = $(this);
				const allChecked = allCheck.prop('checked');
				$('.checkbox-member-id').prop('checked', allChecked);
				$('.checkbox-member-id:is(:disabled)').prop('checked', false)
			})
			
			$('.checkbox-member-id').change(function() {
				const checkboxMemberIdCount = $('.checkbox-member-id').length;
				const checkboxMemberIdCheckedCount = $('.checkbox-member-id:checked').length;
				const checkboxDisabledCount = $('.checkbox-member-id:is(:disabled)').length;
				const allChecked = (checkboxMemberIdCount - checkboxDisabledCount) == checkboxMemberIdCheckedCount;
				$('.checkbox-all-member-id').prop('checked', allChecked);
			})
		</script>

		<div class="mt-2 flex justify-between ">
			<button class="btn btn-primary" onclick="history.back();"><i class="fa-solid fa-right-from-bracket"></i>뒤로가기</button>
			<button class="btn-text-link btn btn-active btn-ghost btn-delete-selected-members">회원 삭제</button>
		</div>

		<form method="POST" name="do-delete-members-form" action="doDeleteMembers">
			<input type="hidden" name="ids" value="" />
		</form>

		<script>
			$('.btn-delete-selected-members').click(function() {
				const values = $('.checkbox-member-id:checked').map((index, el) => el.value).toArray();
				if (values.length == 0) {
					alert('선택한 회원이 없습니다');
					return;
				}
				if (confirm('선택한 회원을 삭제하시겠습니까?') == false) {
					return;
				}
				$('input[name=ids]').val(values.join(','));
				$('form[name=do-delete-members-form]').submit();
			})
		</script>
		
		<div class="page-menu mt-2 flex justify-center">
			<div class="btn-group">
				<c:set var="pageMenuLen" value="5" />
				<c:set var="startPage" value="${page - pageMenuLen >= 1 ? page - pageMenuLen : 1}" />
				<c:set var="endPage" value="${page + pageMenuLen <= pagesCount ? page + pageMenuLen : pagesCount}" />

				<c:set var="pageBaseUri" value="?searchKeywordTypeCode=${searchKeywordTypeCode }&searchKeyword=${searchKeyword }" />

				<c:if test="${membersCount != 0 }">
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