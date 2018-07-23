package com.java.sys.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.java.sys.common.basic.service.BaseService;
import com.java.sys.common.utils.Tool;
import com.java.sys.dao.SysPermDao;
import com.java.sys.dao.SysRolePermDao;
import com.java.sys.entity.SysPerm;
import com.java.sys.entity.SysRolePerm;
@Service
public class SysRolePermService extends BaseService<SysRolePermDao, SysRolePerm> {
	@Resource
	private SysRolePermDao sysRolePermDao;
	@Resource
	private SysPermDao sysPermDao;
	
	public String roleToPermStr(String roleId){
		StringBuilder sb = new StringBuilder();
		if(Tool.isNotBlank(roleId)){
			SysRolePerm _rp = new SysRolePerm(roleId, null);
			List<SysRolePerm> rpList = sysRolePermDao.findList(_rp);
			if(rpList != null && rpList.size()>0){
				for(SysRolePerm rp : rpList){
					SysPerm perm = sysPermDao.get(rp.getPermId());
					if(perm != null){
						sb.append(perm.getPermission()).append(",");
					}
				}
			}
			
		}
		return sb.toString();
	}
	
}
