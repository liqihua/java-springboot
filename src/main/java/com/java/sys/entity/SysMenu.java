package com.java.sys.entity;

import java.util.List;

import com.java.sys.common.basic.entity.BaseEntity;


public class SysMenu extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String parentId;
	private String parentName;
	private String name;
	private String rank;
	private String href;
	private String target;
	private String icon;
	private String hide;
	private String level;
	
	private List<SysMenu> childList;
	
	public SysMenu() {
		super();
	}
	
	public SysMenu(String parentId, String parentName, String name,
			String rank, String href, String target, String icon, String hide,
			String level, List<SysMenu> childList) {
		super();
		this.parentId = parentId;
		this.parentName = parentName;
		this.name = name;
		this.rank = rank;
		this.href = href;
		this.target = target;
		this.icon = icon;
		this.hide = hide;
		this.level = level;
		this.childList = childList;
	}


	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getHide() {
		return hide;
	}
	public void setHide(String hide) {
		this.hide = hide;
	}



	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}

	public List<SysMenu> getChildList() {
		return childList;
	}

	public void setChildList(List<SysMenu> childList) {
		this.childList = childList;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	@Override
	public String toString() {
		return "SysMenu [parentId=" + parentId + ", parentName=" + parentName
				+ ", name=" + name + ", rank=" + rank + ", href=" + href
				+ ", target=" + target + ", icon=" + icon + ", hide=" + hide
				+ ", level=" + level + "]";
	}
}
