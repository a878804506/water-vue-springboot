package cn.enilu.flash.api.utils;


import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 对Session的管理
 */
public class SessionUtils {

	public static HttpSession getSession() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return null;
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getSession();
	}

	public static HttpServletRequest getRequest() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			throw new RuntimeException("request为空");
		}
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	public static ServletContext getServletContext() {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		return context.getServletContext();
	}

	public static HttpServletResponse getResponse() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			throw new RuntimeException("response为空");
		}
		return requestAttributes.getResponse();
	}

}