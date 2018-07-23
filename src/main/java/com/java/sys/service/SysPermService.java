package com.java.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.sys.common.basic.service.BaseService;
import com.java.sys.dao.SysPermDao;
import com.java.sys.entity.SysPerm;


@Service
@Transactional(readOnly = true)
public class SysPermService extends BaseService<SysPermDao, SysPerm> {

	
}