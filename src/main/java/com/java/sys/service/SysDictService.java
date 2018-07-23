package com.java.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.sys.common.basic.service.BaseService;
import com.java.sys.dao.SysDictDao;
import com.java.sys.entity.SysDict;


@Service
@Transactional(readOnly = true)
public class SysDictService extends BaseService<SysDictDao, SysDict> {

	
}