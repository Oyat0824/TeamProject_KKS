<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<section>
	<div class="container flex justify-center mx-auto my-10">
		<div class="logo text-6xl select-none">
			<a href="/usr/store/view?id=${store.id}">
				<c:choose>
					<c:when test="${rq.getImgUri('store', store.id, 'storeLogo') == rq.getProfileFallbackImgUri()}">
						<c:out value="${store.storeName}" />
					</c:when>
					<c:otherwise>
						<img class="max-w-sm max-h-20" src="${rq.getImgUri('store', store.id, 'storeLogo')}" alt="${store.storeName}" />
					</c:otherwise>
				</c:choose>
			</a>
		</div>
	</div>
	
	<div class="w-full h-20 border-t border-b border-gray-300">
		<div class="container flex items-center mx-auto h-full">
			<div class="mr-10">
				<a class="font-bold" href="/usr/store/productList?id=${store.id }">전체상품</a>
			</div>
			<c:forEach var="category" items="${categorys}" varStatus="status">
				<div class="mr-10">
					<a class="${param.cate == category.id ? 'text-indigo-600' : ''}" href="/usr/store/productList?id=${store.id}&cate=${category.id}" data-form="cate" data-type="${category.id}" onclick="return chgForm(this); ">${category.name}</a>
				</div>
			</c:forEach>
		</div>
	</div>
</section>
