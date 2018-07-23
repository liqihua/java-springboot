package com.java.sys.common.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.java.sys.common.basic.controller.BaseController;
import com.java.sys.common.utils.Tool;

public class SysMessageTag extends TagSupport{
	private static final long serialVersionUID = 1L;
	
	private SysMessage content;
	
	public SysMessage getContent() {
		return content;
	}
	public void setContent(SysMessage content) {
		this.content = content;
	}

	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try{
			if(content == null || Tool.isBlank(content.getMessage())){
				out.print("");
				return SKIP_BODY;
			}
			String type = Tool.isBlank(content.getType()) ? BaseController.SUCCESS : content.getType();
			out.println("<div id='messageBox' class='alert alert-"+type+"'><button data-dismiss='alert' class='close'>×</button>"+content.getMessage()+"</div>");
		}catch(Exception e){
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public void release() {
		super.release();
		this.content = null;
	}

	
	
	
}
