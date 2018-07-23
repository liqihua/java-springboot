package com.java.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.sys.common.basic.service.BaseService;
import com.java.sys.dao.SysRoleDao;
import com.java.sys.entity.SysRole;


@Service
@Transactional(readOnly = true)
public class SysRoleService extends BaseService<SysRoleDao, SysRole> {

	
}