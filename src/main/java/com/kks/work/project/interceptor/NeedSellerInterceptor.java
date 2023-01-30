package com.kks.work.project.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kks.work.project.vo.Rq;

@Component
public class NeedSellerInterceptor implements HandlerInterceptor {
	
	private Rq rq;
	
	@Autowired
	public NeedSellerInterceptor(Rq rq) {
		this.rq = rq;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {	
		if(rq.getLoginedMember().getMemberType() != 6 && rq.getLoginedMember().getMemberType() != 9) {
			rq.jsPrintHistoryBack("판매자 전용 메뉴입니다.");
			
			return false;
		}
		
		
		
		return HandlerInterceptor.super.preHandle(req, res, handler);
	}

}
