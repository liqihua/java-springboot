package com.java.sys.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.java.sys.common.basic.service.BaseService;
import com.java.sys.common.utils.Tool;
import com.java.sys.dao.SysMenuDao;
import com.java.sys.dao.SysPermDao;
import com.java.sys.entity.SysMenu;
import com.java.sys.entity.SysPerm;
import com.java.sys.entity.SysUser;
@Service
public class SysMenuService extends BaseService<SysMenuDao, SysMenu>{
	@Resource
	private SysPermDao permDao;
	@Resource
	private SysUserService userService;
	
	/**
	 * 方法名：hasViewPerm
	 * 详述：判断当前系统登陆对菜单有没有view权限，如果有返回true
	 * @param menu
	 * @return
	 */
	public boolean hasViewPerm(SysMenu menu){
		if(menu != null){
			SysUser user = userService.getCurrentUser();
			if(user != null){
				SysPerm _p = new SysPerm(menu.getId(), null, null, null);
				List<SysPerm> permList = permDao.findList(_p);
				if(permList != null && permList.size()>0){
					for(SysPerm p : permList){
						if(p.getPermission().contains("view")){
							if(SecurityUtils.getSubject().isPermitted(p.getPermission())){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断该菜单是否存在子菜单
	 * @param sysMenu
	 * @return
	 */
	public boolean noChild(SysMenu sysMenu){
		if(sysMenu != null && Tool.isNotBlank(sysMenu.getId())){
			if(!"3".equals(sysMenu.getLevel())){
				SysMenu _m = new SysMenu(sysMenu.getId(), null, null, null, null, null, null, null, null, null);
				List<SysMenu> childList = findList(_m);
				if(childList != null && childList.size()>0){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 判断是否存在权限指向该菜单
	 * @param sysMenu
	 * @return
	 */
	public boolean noPerm(SysMenu sysMenu){
		if(sysMenu != null && Tool.isNotBlank(sysMenu.getId())){
			SysPerm _p = new SysPerm(sysMenu.getId(), null, null, null);
			List<SysPerm> permList = permDao.findList(_p);
			if(permList != null && permList.size()>0){
				return false;
			}else{
				return true;
			}
		}
		return true;
	}
	
	/**
	 * 方法名：findListOrderByLevel
	 * 详述：按照排序、菜单等级查出不隐藏的菜单
	 * @return
	 */
	public List<SysMenu> findListOrderByLevel(){
		SysMenu _m1 = new SysMenu();
		_m1.setOrderBy("a.rank ASC");
		_m1.setLevel("1");
		_m1.setHide("0");
		List<SysMenu> list = findList(_m1);
		if(list != null && list.size()>0){
			for(SysMenu menuOne : list){
				SysMenu _m2 = new SysMenu();
				_m2.setOrderBy("a.rank ASC");
				_m2.setLevel("2");
				_m2.setHide("0");
				_m2.setParentId(menuOne.getId());
				List<SysMenu> menuTwoList = findList(_m2);
				if(menuTwoList != null && menuTwoList.size()>0){
					for(SysMenu menuTwo :menuTwoList){
						SysMenu _m3 = new SysMenu();
						_m3.setOrderBy("a.rank ASC");
						_m3.setLevel("3");
						_m3.setHide("0");
						_m3.setParentId(menuTwo.getId());
						List<SysMenu> menuThreeList = findList(_m3);
						if(menuThreeList != null && menuThreeList.size()>0){
							menuTwo.setChildList(menuThreeList);
						}
					}
					menuOne.setChildList(menuTwoList);
				}
			}
		}
		return list;
	}
	
	/**
	 * 方法名：findListWeb
	 * 详述：按照排序、菜单等级查出所有菜单
	 * @return
	 */
	public List<SysMenu> findListWeb(){
		SysMenu _m1 = new SysMenu();
		_m1.setOrderBy("a.rank ASC");
		_m1.setLevel("1");
		List<SysMenu> list = findList(_m1);
		if(list != null && list.size()>0){
			for(SysMenu menuOne : list){
				SysMenu _m2 = new SysMenu();
				_m2.setOrderBy("a.rank ASC");
				_m2.setLevel("2");
				_m2.setParentId(menuOne.getId());
				List<SysMenu> menuTwoList = findList(_m2);
				if(menuTwoList != null && menuTwoList.size()>0){
					for(SysMenu menuTwo :menuTwoList){
						SysMenu _m3 = new SysMenu();
						_m3.setOrderBy("a.rank ASC");
						_m3.setLevel("3");
						_m3.setParentId(menuTwo.getId());
						List<SysMenu> menuThreeList = findList(_m3);
						if(menuThreeList != null && menuThreeList.size()>0){
							menuTwo.setChildList(menuThreeList);
						}
					}
					menuOne.setChildList(menuTwoList);
				}
			}
		}
		return list;
	}
	
	/**
	 * 根据菜单父级关系查询所有菜单，不嵌套childList
	 * @return List<SysMenu>
	 */
	public List<SysMenu> findListOrderByLevelMerge(){
		List<SysMenu> list = new ArrayList<SysMenu>();
		List<SysMenu> listLevel = findListOrderByLevel();
		// 一级菜单
		for(SysMenu menuOne : listLevel){
			list.add(menuOne);
			if(menuOne.getChildList() != null && menuOne.getChildList().size()>0){
				for(SysMenu menuTwo : menuOne.getChildList()){
					list.add(menuTwo);
					if(menuTwo.getChildList() != null && menuTwo.getChildList().size()>0){
						for(SysMenu menuThree : menuTwo.getChildList()){
							list.add(menuThree);
						}
					}
				}
			}
		}
		return list;
	}
	
}
