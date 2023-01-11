package com.kks.work.project.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kks.work.project.vo.Rq;

@Component
public class BeforeActionInterceptor implements HandlerInterceptor {
	private Rq rq;

	@Autowired
	public BeforeActionInterceptor(Rq rq) {
		this.rq = rq;
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		req.setAttribute("rq", rq);
		
		return HandlerInterceptor.super.preHandle(req, res, handler);
	}

}
