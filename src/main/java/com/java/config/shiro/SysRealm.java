package com.java.config.shiro;

import com.java.sys.common.cache.CacheUtil;
import com.java.sys.entity.SysUser;
import com.java.sys.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysRealm extends AuthorizingRealm {
	@Resource
	private SysUserService sysUserService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUser sysUser = (SysUser)principals.getPrimaryPrincipal();
		if(sysUser == null){
			return null;
		}
		Map<String,Object> rolesMap = (Map<String, Object>) CacheUtil.get(CacheUtil.KEY_SHIRO_ROLES_MAP);
		Map<String,Object> permsMap = (Map<String, Object>) CacheUtil.get(CacheUtil.KEY_SHIRO_PERMS_MAP);
		Set<String> roles = (Set<String>) rolesMap.get(sysUser.getId());
		Set<String> perms = (Set<String>) permsMap.get(sysUser.getId());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setRoles(roles);
		info.setStringPermissions(perms);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		String username = token.getPrincipal().toString();
		String password = new String((char[])token.getCredentials());
		
		SysUser _u = new SysUser(username, password, null, null, null);
		List<SysUser> list = sysUserService.findList(_u);
		if(list == null || list.size()<1){
			throw new UnknownAccountException("用户名或密码错误");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(list.get(0), password, this.getName());
		return info;
	}

}
