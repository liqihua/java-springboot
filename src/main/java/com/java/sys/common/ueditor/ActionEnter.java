package com.java.sys.common.ueditor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.java.sys.common.ueditor.config.UEditorConfig;
import com.java.sys.common.ueditor.config.UEditorConfigBuilder;
import com.java.sys.common.ueditor.define.ActionMap;
import com.java.sys.common.ueditor.define.AppInfo;
import com.java.sys.common.ueditor.define.BaseState;
import com.java.sys.common.ueditor.define.State;
import com.java.sys.common.ueditor.hunter.FileManager;
import com.java.sys.common.ueditor.hunter.ImageHunter;
import com.java.sys.common.ueditor.upload.Uploader;
import com.java.sys.common.utils.Tool;


public class ActionEnter {
	
	private HttpServletRequest request = null;
	
	private String rootPath = null;
	private String contextPath = null;
	
	private String actionType = null;
	
	private ConfigManager configManager = null;

	public ActionEnter ( HttpServletRequest request, String rootPath ) {
		
		this.request = request;
		this.rootPath = rootPath;
		this.actionType = request.getParameter( "action" );
		this.contextPath = request.getContextPath();
		this.configManager = ConfigManager.getInstance( this.rootPath, this.contextPath, request.getRequestURI() );
		Tool.info("--- ActionEnter action : "+this.actionType+",this.actionType : "+this.actionType);
		Tool.info("--- ActionEnter this.contextPath : "+this.contextPath);
	}
	
	public String exec () {
		
		String callbackName = this.request.getParameter("callback");
		Tool.info("--- ActionEnter exec() callbackName : "+callbackName);
		if ( callbackName != null ) {
			Tool.info("--- ActionEnter exec() callbackName != null ");
			if ( !validCallbackName( callbackName ) ) {
				Tool.info("--- ActionEnter exec() !validCallbackName( callbackName ) ");
				return new BaseState( false, AppInfo.ILLEGAL ).toJSONString();
			}
			
			return callbackName+"("+this.invoke()+");";
			
		} else {
			return this.invoke();
		}

	}
	
	public String invoke() {
		Tool.info("--- ActionEnter invoke() come !");
		if ( actionType == null || !ActionMap.mapping.containsKey( actionType ) ) {
			return new BaseState( false, AppInfo.INVALID_ACTION ).toJSONString();
		}
		
		if ( this.configManager == null || !this.configManager.valid() ) {
			if(this.configManager == null) {
				Tool.info("--- ActionEnter invoke() : this.configManager == null");
			}else {
				Tool.info("--- ActionEnter invoke() : this.configManager.valid() : "+this.configManager.valid());
			}
			return new BaseState( false, AppInfo.CONFIG_ERROR ).toJSONString();
		}
		
		State state = null;
		
		int actionCode = ActionMap.getType( this.actionType );
		
		Map<String, Object> conf = null;
		
		switch ( actionCode ) {
		
			case ActionMap.CONFIG:
				UEditorConfig config = UEditorConfigBuilder.build();
				JSONObject jsonConfig = new JSONObject(config);
				return jsonConfig.toString();
				//return this.configManager.getAllConfig().toString();
				
			case ActionMap.UPLOAD_IMAGE:
			case ActionMap.UPLOAD_SCRAWL:
			case ActionMap.UPLOAD_VIDEO:
			case ActionMap.UPLOAD_FILE:
				conf = this.configManager.getConfig( actionCode );
				state = new Uploader( request, conf ).doExec();
				Tool.info("--- ActionEnter invoke() case ActionMap.UPLOAD_IMAGE  state.toJSONString() : "+state.toJSONString());
				break;
				
			case ActionMap.CATCH_IMAGE:
				conf = configManager.getConfig( actionCode );
				String[] list = this.request.getParameterValues( (String)conf.get( "fieldName" ) );
				state = new ImageHunter( conf ).capture( list );
				break;
				
			case ActionMap.LIST_IMAGE:
			case ActionMap.LIST_FILE:
				conf = configManager.getConfig( actionCode );
				int start = this.getStartIndex();
				state = new FileManager( conf ).listFile( start );
				break;
				
		}
		
		return state.toJSONString();
		
	}
	
	public int getStartIndex () {
		
		String start = this.request.getParameter( "start" );
		
		try {
			return Integer.parseInt( start );
		} catch ( Exception e ) {
			return 0;
		}
		
	}
	
	/**
	 * callback参数验证
	 */
	public boolean validCallbackName ( String name ) {
		
		if ( name.matches( "^[a-zA-Z_]+[\\w0-9_]*$" ) ) {
			return true;
		}
		
		return false;
		
	}
	
}