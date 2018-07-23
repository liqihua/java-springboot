package com.java.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.java.sys.entity.SysColumn;

@Repository
public interface SysColumnDao {
	
	List<String> findTableList();
	
	List<SysColumn> findColumnList(@Param("tableName") String tableName);
	
	
	
}
