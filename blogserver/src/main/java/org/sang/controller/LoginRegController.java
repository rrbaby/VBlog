package org.sang.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sang.bean.RespBean;
import org.sang.bean.User;
import org.sang.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sang on 2017/12/17.
 */
@RestController
public class LoginRegController {
	private static final Log logger  = LogFactory.getLog(LoginRegController.class);
	
	
    @Autowired
    UserService userService;
    
    
//    @RequestMapping("/login")
    @PostMapping
    public RespBean login() {
    	logger.info("====login=====");
		return new RespBean("success","登录成功!");
	}

    @RequestMapping("/login_error")
    public RespBean loginError() {
        return new RespBean("error", "登录失败!");
        
    }
    @GetMapping("/loginfail")
    public RespBean loginfail() {
		return new RespBean("error","请输入正确的用户");
	}
    

    @RequestMapping("/login_success")
    public RespBean loginSuccess() {
        return new RespBean("success", "登录成功!");
    }

    /**
     * 如果自动跳转到这个页面，说明用户未登录，返回相应的提示即可
     * <p>
     * 如果要支持表单登录，可以在这个方法中判断请求的类型，进而决定返回JSON还是HTML页面
     *
     * @return
     */
    @RequestMapping("/login_page")
    public RespBean loginPage() {
    	logger.info("=====test=======");
        return new RespBean("error", "尚未登录，请登录!");
    }

    @RequestMapping("/reg")
    public RespBean reg(User user) {
        int result = userService.reg(user);
        if (result == 0) {
            //成功
            return new RespBean("success", "注册成功!");
        } else if (result == 1) {
            return new RespBean("error", "用户名重复，注册失败!");
        } else {
            //失败
            return new RespBean("error", "注册失败!");
        }
    }
}
