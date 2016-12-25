package com.atguigu.crm.handler;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atguigu.crm.entity.User;
import com.atguigu.crm.service.UserService;

@RequestMapping("/user")
@Controller
public class UserHandler {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ResourceBundleMessageSource messagSource;
	
	@RequestMapping("/shiro-login")
	public String shiroLogin(@RequestParam(value="username",required=false) 
			String name, @RequestParam(value="password", required=false) String password,
			RedirectAttributes attributes,
			Locale locale,
			HttpSession session){
		
		Subject currentUser = SecurityUtils.getSubject();

        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(name, password);
            token.setRememberMe(true);
            try {
            	//这一步将会调用 ShiroRealm中的doGetAuthenticationInfo()
                currentUser.login(token);
                //如何能够访问到已经在 Realm 中获取到的 User 的实例.
                Object principal = SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
                session.setAttribute("user", principal);
            } catch (AuthenticationException ae) {
            	attributes.addFlashAttribute("message", messagSource.getMessage("error.crm.user.login", null, locale));
    			return "redirect:/index";
            }
        }
        
		return "home/success";
	}
	
	@RequestMapping("/login")
	public String login(@RequestParam(value="username",required=false) 
			String name, @RequestParam(value="password", required=false) String password,
			RedirectAttributes attributes,
			Locale locale,
			HttpSession session){
		User user = userService.login(name, password);
		
		if(user == null){
			attributes.addFlashAttribute("message", messagSource.getMessage("error.crm.user.login", null, locale));
			return "redirect:/index";
//			return "redirect:/index.jsp";
//			return "forward:/index.jsp";
		}
		
		//把 User 放入到 Session 中. 
		session.setAttribute("user", user);
		
		return "home/success";
	}
	
}
