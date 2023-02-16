package com.kks.work.project.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kks.work.project.vo.Rq;

@Component
public class NeedAdminInterceptor implements HandlerInterceptor {
	
private Rq rq;
	
	@Autowired
	public NeedAdminInterceptor(Rq rq) {
		this.rq = rq;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {	
		if(rq.getLoginedMember().getMemberType() != 9 ) {
			rq.jsPrintHistoryBack("관리자 권한이 필요합니다.");
			
			return false;
		}		
		
		return HandlerInterceptor.super.preHandle(req, res, handler);
	}

}
