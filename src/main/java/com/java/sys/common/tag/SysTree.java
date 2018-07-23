package com.java.sys.common.tag;

import java.io.Serializable;
import java.util.List;

public class SysTree implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private List<SysTree> children;
	
	
	public SysTree() {
		super();
	}
	
	public SysTree(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<SysTree> getChildren() {
		return children;
	}

	public void setChildren(List<SysTree> children) {
		this.children = children;
	}
}
