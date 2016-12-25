package com.atguigu.crm.security;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.atguigu.crm.entity.Authority;
import com.atguigu.crm.entity.User;
import com.atguigu.crm.service.UserService;

@Component("realm")
public class ShiroRealm extends AuthorizingRealm{

	@Autowired
	private UserService userService;
	
	/**
	 * 验证用户是否具有某一项操作的权限
	 * @param arg0 用户的登录信息,可以获取到doGetAuthenticationInfo()返回值中的principal
	 * @return SimpleAuthorizationInfo
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		Object principal = arg0.getPrimaryPrincipal();
		User user = (User) principal;
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Set<String> roles = new HashSet<>();
		for(Authority authority: user.getRole().getAuthorities()){
			roles.add(authority.getName());
		}
		info.addRoles(roles);
		
		return info;
	}
	
	/**
	 * 用于验证用户是否登录的方法 
	 * @param arg0 为 UserHandler中传入的UsernamePasswordToken
	 * @return 返回的是一个包含用户登录信息的AuthenticationInfo对象
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken arg0) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) arg0;
		
		//1. 获取登录名 name
		String name = token.getUsername();
		
		//2. 使用 name 从数据库中获取对应的记录
		User user = userService.getByName(name);
		
		//3. 把对应的记录封装为 SimpleAuthenticationInfo 对象并返回
		Object principal = user; //登录信息,对象类型
		Object hashedCredentials = user.getPassword(); //登录凭证,即密码
		ByteSource credentialsSalt = ByteSource.Util.bytes(user.getSalt());//获取盐值
		String realmName = getName(); //当前realm的名字
		
		return new SimpleAuthenticationInfo(principal, hashedCredentials, credentialsSalt, realmName);
	}
	
	//配置使用 MD5 盐值加密
	@PostConstruct //相当于 init-method. 即在构造器执行后执行的方法
	public void initCredentialsMatcher(){
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName("MD5");
		credentialsMatcher.setHashIterations(1024);
		
		//更新当前的 Service 的 CrendialsMatcher 属性
		setCredentialsMatcher(credentialsMatcher);
	}
	
	//MD5 盐值加密后密码的算法  在用户注册保存密码时会用到
	public static void main(String[] args) {
		String algorithmName = "MD5";
		String source = "123456";
		ByteSource salt = ByteSource.Util.bytes("e2b87e6eced06509");
		int iterations = 1024;
		
		SimpleHash result = new SimpleHash(algorithmName, source, salt, iterations);
		System.out.println(result);
	}
}
