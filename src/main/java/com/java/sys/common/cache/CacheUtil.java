package com.java.sys.common.cache;

import java.util.*;

import com.java.sys.common.springholder.SpringContextHolder;
import com.java.sys.common.tag.SysTree;
import com.java.sys.common.utils.SysConfig;
import com.java.sys.common.utils.Tool;

import com.java.sys.entity.*;
import com.java.sys.service.*;


public class CacheUtil {
	/**
	 * shiro角色map键值
	 */
	public static final String KEY_SHIRO_ROLES_MAP = "key_shiro_roles_map";
	/**
	 * shiro权限map键值
	 */
	public static final String KEY_SHIRO_PERMS_MAP = "key_shiro_perms_map";
	/**
	 * 系统菜单
	 */
	public static final String KEY_MENU_LIST = "key_menu_list";
	/**
	 * 系统菜单树
	 */
	public static final String KEY_MENU_TREE = "key_menu_tree";

	/*
	 * 业务层注入
	 */
	protected static SysRolePermService rolePermService = SpringContextHolder.getBean(SysRolePermService.class);
	protected static SysRoleUserService roleUserService = SpringContextHolder.getBean(SysRoleUserService.class);
	protected static SysRoleService roleService = SpringContextHolder.getBean(SysRoleService.class);
	protected static SysPermService permService = SpringContextHolder.getBean(SysPermService.class);
	protected static SysUserService userService = SpringContextHolder.getBean(SysUserService.class);
	protected static SysMenuService menuService = SpringContextHolder.getBean(SysMenuService.class);
	
	protected static RedisUtil redisUtil = SpringContextHolder.getBean(RedisUtil.class);


	
	/**
	 * 方法名：init
	 * 详述：初始化
	 */
	public static void init(){
		Tool.info("--- CacheUtil : init()  ... ",CacheUtil.class);
		//连接地址
		String ip = SysConfig.getConfig("spring.redis.host");
		//shiro、系统菜单
    	if(ip.contains("localhost") || ip.contains("127.0.0.1")) {
    		refreshRealm();
    		refreshMenu();
    	}else {
    		Map<String,Object> rolesMap = getShiroRolesMap();
		    Map<String,Object> permsMap = getShiroPermsMap();
		    List<SysMenu> sysList = CacheUtil.getMenuList();
		    List<SysTree> sysTree = CacheUtil.getMenuTree();
		    if(rolesMap == null || permsMap == null){
				refreshRealm();
            }
		    if(sysList == null || sysTree == null) {
		    	refreshMenu();
		    }
    	}
		Tool.info("--- CacheUtil : init() finished. ",CacheUtil.class);
	}
	
	


    /**
     * 刷新缓存中的角色和权限
     */
    public static void refreshRealm(){
        Tool.info("----- CacheUtil -> refreshRealm() ... ",CacheUtil.class);
        Map<String,Object> rolesMap = new HashMap<String,Object>();
        Map<String,Object> permsMap = new HashMap<String,Object>();
        List<SysUser> userList = userService.findList(null);
        if(userList != null && userList.size()>0){
            for(SysUser user : userList){
                Set<String> roles = new HashSet<String>();
                Set<String> perms = new HashSet<String>();
                //查询用户拥有的角色
                SysRoleUser _ru = new SysRoleUser(null, user.getId());
                List<SysRoleUser> ruList = roleUserService.findList(_ru);
                if(ruList != null && ruList.size()>0){
                    for(SysRoleUser ru : ruList){
                        SysRole role = roleService.get(ru.getRoleId());
                        if(role != null){
                            roles.add(role.getName());
                            // 查询角色拥有的权限
                            SysRolePerm _rp = new SysRolePerm(role.getId(), null);
                            List<SysRolePerm> rolePermList = rolePermService.findList(_rp);
                            if(rolePermList != null && rolePermList.size()>0){
                                for(SysRolePerm rolePerm : rolePermList){
                                    SysPerm perm = permService.get(rolePerm.getPermId());
                                    if(perm != null){
                                        perms.add(perm.getPermission());
                                    }
                                }
                            }
                        }
                    }
                }
                rolesMap.put(user.getId(), roles);
                permsMap.put(user.getId(), perms);
            }
        }
        set(KEY_SHIRO_ROLES_MAP, rolesMap);
        set(KEY_SHIRO_PERMS_MAP, permsMap);
		Tool.info("----- CacheUtil -> refreshRealm() finished. ",CacheUtil.class);
    }





	/**
	 * 返回缓存中的角色Map
	 * @return
	 */
    public static Map<String,Object> getShiroRolesMap(){
        Map<String,Object> rolesMap = (Map<String, Object>) get(KEY_SHIRO_ROLES_MAP);
        return rolesMap;
    }

	/**
	 * 返回缓存中的权限Map
	 * @return
	 */
	public static Map<String,Object> getShiroPermsMap(){
		Map<String,Object> permsMap = (Map<String, Object>) get(KEY_SHIRO_PERMS_MAP);
		return permsMap;
	}
	
	/**
	 * 方法名：refreshMenu
	 * 详述：刷新系统菜单
	 */
	public static void refreshMenu() {
		Tool.info("----- CacheUtil -> refreshMenu() ... ",CacheUtil.class);
		refreshMenuList();
		refreshMenuTree();
		Tool.info("----- CacheUtil -> refreshMenu() finished. ",CacheUtil.class);
	}
	
	
	/**
	 * 方法名：refreshMenuList
	 * 详述：刷新系统菜单列表
	 */
	public static void refreshMenuList() {
		SysMenu _m1 = new SysMenu();
		_m1.setOrderBy("a.rank ASC");
		_m1.setLevel("1");
		_m1.setHide("0");
		List<SysMenu> list = menuService.findList(_m1);
		if(list != null && list.size()>0){
			for(SysMenu menuOne : list){
				SysMenu _m2 = new SysMenu();
				_m2.setOrderBy("a.rank ASC");
				_m2.setLevel("2");
				_m2.setHide("0");
				_m2.setParentId(menuOne.getId());
				List<SysMenu> menuTwoList = menuService.findList(_m2);
				if(menuTwoList != null && menuTwoList.size()>0){
					for(SysMenu menuTwo :menuTwoList){
						SysMenu _m3 = new SysMenu();
						_m3.setOrderBy("a.rank ASC");
						_m3.setLevel("3");
						_m3.setHide("0");
						_m3.setParentId(menuTwo.getId());
						List<SysMenu> menuThreeList = menuService.findList(_m3);
						if(menuThreeList != null && menuThreeList.size()>0){
							menuTwo.setChildList(menuThreeList);
						}
					}
					menuOne.setChildList(menuTwoList);
				}
			}
			set(KEY_MENU_LIST, list);
		}
	}
	
	/**
	 * 方法名：refreshMenuTree
	 * 详述：刷新系统菜单树
	 */
	public static void refreshMenuTree() {
		List<SysTree> treeList = new ArrayList<SysTree>();
		SysMenu _m = new SysMenu();
		_m.setOrderBy("a.rank ASC");
		_m.setLevel("1");
		_m.setHide("0");
		List<SysMenu> list = menuService.findList(_m);
		if(list != null && list.size()>0){
			for(SysMenu menuOne : list){
				SysTree tree = new SysTree(menuOne.getId(), menuOne.getName());
				_m.setLevel("2");
				_m.setParentId(menuOne.getId());
				List<SysMenu> menuTwoList = menuService.findList(_m);
				if(menuTwoList != null && menuTwoList.size()>0){
					List<SysTree> treeList2 = new ArrayList<SysTree>();
					for(SysMenu menuTwo :menuTwoList){
						SysTree tree2 = new SysTree(menuTwo.getId(), menuTwo.getName());
						treeList2.add(tree2);
						_m.setLevel("3");
						_m.setParentId(menuTwo.getId());
						List<SysMenu> menuThreeList = menuService.findList(_m);
						if(menuThreeList != null && menuThreeList.size()>0){
							List<SysTree> treeList3 = new ArrayList<SysTree>();
							for(SysMenu menuThree : menuThreeList){
								SysTree tree3 = new SysTree(menuThree.getId(), menuThree.getName());
								treeList3.add(tree3);
							}
							tree2.setChildren(treeList3);
						}
					}
					tree.setChildren(treeList2);
				}
				treeList.add(tree);
			}
			set(KEY_MENU_TREE, treeList);
		}
	}
	
	/**
	 * 方法名：getMenuList
	 * 详述：获取缓存菜单列表
	 * @return
	 */
	public static List<SysMenu> getMenuList() {
		return  (List<SysMenu>) get(KEY_MENU_LIST);
	}

	/**
	 * 方法名：getMenuTree
	 * 详述：获取缓存菜单树
	 * @return
	 */
	public static List<SysTree> getMenuTree(){
		return (List<SysTree>) get(KEY_MENU_TREE);
	}


	////////////////////////////////////////// key操作 - start ////////////////////////////////////////////////////////////

	/**
	 * key操作，设置某个key的存活时间
	 * @param key
	 * @param second
	 * @return
	 */
	public static boolean expire(String key,int second){
		return redisUtil.expire(key,second);
	}
	////////////////////////////////////////// key操作 - end ////////////////////////////////////////////////////////////


	
	////////////////////////////////////////// value操作 - start ////////////////////////////////////////////////////////////
	/**
	 * 
	 * 方法名：set
	 * 详述：写操作
	 * @param key
	 * @param obj
	 * @param second 存活秒数
	 * @return
	 */
	public static void set(String key,Object obj,int second) {
		redisUtil.set(key, obj,second);
		Tool.info("--- CacheUtil set() with second : "+second,CacheUtil.class);
	}
	
	/**
	 * 方法名：get
	 * 详述：读操作
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return redisUtil.get(key);
	}
	
	/**
	 * 方法名：del
	 * 详述：删除操作
	 * @param key
	 * @return
	 */
	public static void del(String key) {
		redisUtil.del(key);
	}
	
	/**
	 * 方法名：exists
	 * 详述：判断某个key是否存在
	 * @param key
	 * @return
	 */
	public static boolean hasKey(String key) {
		return redisUtil.hasKey(key);
	}
	
	/**
	 * 方法名：set
	 * 详述：写操作，永久存活
	 * @param key
	 * @param obj
	 * @return
	 */
	public static void set(String key,Object obj) {
		redisUtil.set(key, obj);
		Tool.info("--- CacheUtil set() ",CacheUtil.class);
	}
	
	////////////////////////////////////////// value操作 - end ////////////////////////////////////////////////////////////
	
	/**
	 * 方法名：hset
	 * 详述：hash写操作
	 * @param mapName
	 * @param key
	 * @param obj
	 */
	public static void hset(String mapName,String key,Object obj) {
		redisUtil.hset(mapName, key, obj);
	}
	
	/**
	 * 方法名：hget
	 * 详述：hash读操作
	 * @param mapName
	 * @param key
	 * @return
	 */
	public static Object hget(String mapName,String key) {
		return redisUtil.hget(mapName, key);
	}
	
	/**
	 * 方法名：hHasKey
	 * 详述：判断hash的某个map是否存在某个key
	 * @param mapName
	 * @param key
	 * @return
	 */
	public static boolean hHasKey(String mapName,String key) {
		return redisUtil.hHasKey(mapName, key);
	}
	
	/**
	 * 方法名：hdel
	 * 详述：hash删除操作
	 * @param mapName
	 * @param key
	 */
	public static void hdel(String mapName,String key) {
		redisUtil.hdel(mapName, key);
	}


	/**
	 * 获取Map
	 * @param mapName
	 */
	public static Map<String,Object> entries(String mapName) {
		return redisUtil.entries(mapName);
	}

	/**
	 * 返回Map的所有key
	 * @param mapName
	 * @return
	 */
	public static Set<String> keys(String mapName){
		return redisUtil.keys(mapName);
	}

	/**
	 * 返回Map的所有value
	 * @param mapName
	 * @return
	 */
	public static List<Object> values(String mapName){
		return redisUtil.values(mapName);
	}

	////////////////////////////////////////// hash操作 - start ////////////////////////////////////////////////////////////


	/**
	 * set写操作
	 * @param setName
	 * @param obj
	 */
	public static void sadd(String setName,Object obj) {
		redisUtil.sadd(setName,obj);
	}

	/**
	 * set删除操作
	 * @param setName
	 * @param obj
	 */
	public static void srem(String setName,Object obj){
		redisUtil.srem(setName,obj);
	}


	/**
	 * set读操作
	 * @param setName
	 * @return
	 */
	public static Set<Object> smembers(String setName){
		return redisUtil.smembers(setName);
	}

	////////////////////////////////////////// set操作 - start ////////////////////////////////////////////////////////////
}
