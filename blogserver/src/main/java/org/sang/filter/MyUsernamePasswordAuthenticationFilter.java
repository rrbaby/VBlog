package org.sang.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.alibaba.druid.util.StringUtils;

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private boolean isOpenValidateCode = true;
	
	public static final String VALIDATE_CODE = "validateCode";

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if(isOpenValidateCode) {
			checkValidateCode(request);
		}
		return super.attemptAuthentication(request, response);
	}
	
	
	protected void checkValidateCode(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sessionValidateCode = "1234";//假的验证码
		//让上次的额验证码失效
		session.setAttribute(VALIDATE_CODE, null);
		String validateCodeParameter = obtainValidateCodeParameters(request);
		if(StringUtils.isEmpty(validateCodeParameter)||
				sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			throw new AuthenticationServiceException("验证码错误!");
			
		}
	}
	
	protected String obtainSessionValidateCode(HttpSession session) {
		Object object = session.getAttribute(VALIDATE_CODE);
		return object == null?"":object.toString();
	}
	
	private  String obtainValidateCodeParameters(HttpServletRequest request) {
		Object object = request.getParameter(VALIDATE_CODE);
		return object == null?"":object.toString();
	}
	
}
