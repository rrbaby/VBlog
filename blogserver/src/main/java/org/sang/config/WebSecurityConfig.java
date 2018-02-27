package org.sang.config;

import org.sang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by sang on 2017/12/17.
 */
@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserService userService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(new Md5PasswordEncoder());
//		auth.userDetailsService(userService).passwordEncoder(new PasswordEncoder() {
//			@Override
//			public String encode(CharSequence charSequence) {
//				return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
//			}
//
//			/**
//			 * @param charSequence
//			 *            明文
//			 * @param s
//			 *            密文
//			 * @return
//			 */
//			@Override
//			public boolean matches(CharSequence charSequence, String s) {
//				return s.equals(DigestUtils.md5DigestAsHex(charSequence.toString().getBytes()));
//			}
//		});
	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers("/admin/category/all").authenticated().antMatchers("/admin/**", "/reg")
//				.hasRole("超级管理员")/// admin/**的URL都需要有超级管理员角色，如果使用.hasAuthority()方法来配置，需要在参数中加上ROLE_,如下.hasAuthority("ROLE_超级管理员")
//				.anyRequest().authenticated()// 其他的路径都是登录后即可访问
//				.and().formLogin().loginPage("/login_page").successHandler(new AuthenticationSuccessHandler() {
//					@Override
//					public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
//							HttpServletResponse httpServletResponse, Authentication authentication)
//							throws IOException, ServletException {
//						httpServletResponse.setContentType("application/json;charset=utf-8");
//						PrintWriter out = httpServletResponse.getWriter();
//						out.write("{\"status\":\"success\",\"msg\":\"登录成功\"}");
//						out.flush();
//						out.close();
//					}
//				}).failureHandler(new AuthenticationFailureHandler() {
//					@Override
//					public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
//							HttpServletResponse httpServletResponse, AuthenticationException e)
//							throws IOException, ServletException {
//						httpServletResponse.setContentType("application/json;charset=utf-8");
//						PrintWriter out = httpServletResponse.getWriter();
//						out.write("{\"status\":\"error\",\"msg\":\"登录失败\"}");
//						out.flush();
//						out.close();
//					}
//				}).loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password").permitAll()
//				.and().logout().permitAll().and().csrf().disable().exceptionHandling()
//				.accessDeniedHandler(getAccessDeniedHandler());
	
	

//	}
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
	    .anyRequest().authenticated()
	    .and().formLogin().loginPage("/login").failureUrl("/loginfail").permitAll().and()
	    .logout().permitAll().and().csrf().disable().antMatcher("/admin/**");
		http.authorizeRequests().anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll();
//		http.authorizeRequests().antMatchers("/login").authenticated().and().formLogin().loginPage("/login").permitAll();
//		http.authorizeRequests().antMatchers("/login/**");
		
		
//		http.authorizeRequests().antMatchers("/admin/category/all").authenticated().antMatchers("/admin/**", "/reg").anonymous()
////		.hasRole("超级管理员")/// admin/**的URL都需要有超级管理员角色，如果使用.hasAuthority()方法来配置，需要在参数中加上ROLE_,如下.hasAuthority("ROLE_超级管理员")
//		.anyRequest().authenticated()// 其他的路径都是登录后即可访问
//		.and().formLogin().loginPage("/login_page").permitAll().and().logout().permitAll();
		
//		
//		http.authorizeRequests().antMatchers("/admin/category/all").authenticated().antMatchers("/admin/**", "/reg")
//				.hasRole("超级管理员")/// admin/**的URL都需要有超级管理员角色，如果使用.hasAuthority()方法来配置，需要在参数中加上ROLE_,如下.hasAuthority("ROLE_超级管理员")
//				.anyRequest().authenticated()// 其他的路径都是登录后即可访问
//				.and().formLogin().loginPage("/login").usernameParameter("username").passwordParameter("password").permitAll();
	}
	

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/blogimg/**", "/index.html", "/static/**");
	}

	@Bean
	AccessDeniedHandler getAccessDeniedHandler() {
		return new AuthenticationAccessDeniedHandler();
	}
}