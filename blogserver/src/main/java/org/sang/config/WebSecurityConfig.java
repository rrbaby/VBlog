package org.sang.config;

import org.sang.filter.MyUsernamePasswordAuthenticationFilter;
import org.sang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

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
		http.authorizeRequests().antMatchers("/","/login/**").permitAll()
		.antMatchers("/admin/**/","/reg").hasRole("超级管理员")//超级管理员权限可以访问
		.anyRequest().authenticated().and()//其他地址的访问权限均需验证登录
		.addFilterAt(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
		.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login_page"))
		.and().formLogin().loginPage("/login_page")//制定登录页面的请求路径
		.loginProcessingUrl("/login").permitAll()
		.and().logout().logoutUrl("/siginout").logoutSuccessUrl("/login_page").permitAll()
		.and().rememberMe().key("testallKey")
		.and().csrf().disable();
	}
	


	/**
	 * 忽略静态文件
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/blogimg/**", "/index.html", "/static/**");
	}



	@Bean
	AccessDeniedHandler getAccessDeniedHandler() {
		return new AuthenticationAccessDeniedHandler();
	}
	
	@Bean
	public MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter myFilter = new MyUsernamePasswordAuthenticationFilter();
        myFilter.setAuthenticationManager(authenticationManagerBean());
        myFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        myFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        myFilter.setRememberMeServices(tokenBasedRememberMeServices());
        return myFilter;
	}
	
	
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler("/login/success");
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler("/login/failure");
    }

    @Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
    
    @Bean
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        TokenBasedRememberMeServices tbrms = new TokenBasedRememberMeServices("testallKey",userDetailsService());
        // 设置cookie过期时间为2天
        tbrms.setTokenValiditySeconds(60 * 60 * 24 * 2);
        // 设置checkbox的参数名为rememberMe（默认为remember-me），注意如果是ajax请求，参数名不是checkbox的name而是在ajax的data里
        tbrms.setParameter("rememberMe");
        return tbrms;
    }
    
}