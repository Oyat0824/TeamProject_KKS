<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="pageTitle" value="Member Login" />
<%@ include file="../common/head.jsp"%>

<script>
function delCart(cartId) {
	
	$.get('delCart', {
		cartId : cartId,
		ajaxMode : 'Y'
	}, function(data){
		let addHtml = '';
		showCart = data.data1;
		totalPriceSum = data.totalPriceSum;
		
		for (let i = 0; i < showCart.length; i++) {
			addHtml += `
						<tr>
							<td>\${showCart[i].productCategory}</td>
							<td>\${showCart[i].productName}</td>
							<td>\${showCart[i].productPrice}</td>
							<td>
								<div class="quant sm:min-w-0">
									<input  type="hidden" value="\${showCart[i].cartId}"   style="text-align:center; width:50px; background-color: white;"/>
									<input class = "\${showCart[i].cartId}" type="text" value="\${showCart[i].productCount}"   style="text-align:center; width:50px; background-color: white;"/>
									<button type="button" class = 'quantityBtn plusBtn' onclick= "updateQuantity('incr',\${showCart[i].cartId});" style = 'vertical-align: middle'>+</button>
									<button type="button" class = 'quantityBtn minusBtn' onclick= "updateQuantity('decr',\${showCart[i].cartId});" style = 'vertical-align: middle' >-</button>
								</div>
							</td>
							<td><a href="#" id ="del" onclick ="delCart(\${showCart[i].cartId});">X</a></td>
						</tr>`
		}
		
		addHtml += `<tr style = "border-top : 1px solid  black">
						<th>총 결제금액</th>						
						<td>\${totalPriceSum}</td>
					</tr>`
		$('.renewTable').empty().html(addHtml);
	}, 'json');
}

function updateQuantity(str, cartId) {
	$.get('updateQuantity', {
		str : str,
		cartId : cartId,
		ajaxMode : 'Y'
	}, function(data){
// 	let quantity = data.data1.productCount;
		//console.log(data);
		let addHtml = '';
		$('.' + data.data1.cartId).val(data.data1.productCount);
// 	$('${showCart.cartId}'').next().empty().html(quantity);
	/*	addHtml += `<tr style = "border-top : 1px solid  black">
						<th>총 결제금액</th>						
						<td>\${data.totalPriceSum}</td>
					</tr>`
		
		$(.totalPrice).empty().html(addHtml);	*/
	}, 'json');
}

	
	




</script>


	

<div class="basketHead text-center" >
	<h1 class = "text-4xl text-gray-800" >장바구니</h1>
</div>
	
	
	
<div class = "shopWrap">
	<div class="table-box-shopCart">
		<table  class = "cartTable lg:w-9/12">
			<thead>
				<tr>
					
					<th>제품 카테고리</th> 
					<th>제품명</th>
					<th>가격</th>
					<th rowspan="2">수량</th>
				</tr>
			</thead>
			<tbody class="renewTable">
				<c:forEach var="showCart" items="${showCarts}">
				
						<tr>
							<td>${showCart.productCategory}</td>
 							<td>${showCart.productName}</td> 
							<td>${showCart.productPrice}</td>
							<td>
								<div class="quant sm:min-w-0">
									<input  type="hidden" value="${showCart.cartId}"   style="text-align:center; width:50px; background-color: white;"/>
									<input class = "${showCart.cartId}" type="text" value="${showCart.productCount}"   style="text-align:center; width:50px; background-color: white;"/>
									<button type="button" class = 'quantityBtn plusBtn' onclick= "updateQuantity('incr',${showCart.cartId});" style = 'vertical-align: middle'>+</button>
									<button type="button" class = 'quantityBtn minusBtn' onclick= "updateQuantity('decr',${showCart.cartId});" style = 'vertical-align: middle' >-</button>
								</div>
							</td>
							<td><a href="#" id ="del" onclick ="delCart(${showCart.cartId});">X</a></td>
						</tr>
					
 				
				</c:forEach>
					<tr class="totalPrice" style = "border-top : 1px solid  black">
						<th>총 결제금액</th>						
						<td>${totalPriceSum}</td>
					</tr>	
			</tbody>
		</table>
	</div>
</div>




<%@ include file="../common/foot.jsp"%>