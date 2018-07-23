package com.java.sys.common.tag;

import com.java.sys.common.utils.Tool;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class SysTreeTag extends TagSupport{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String value;
	private String required;
	private String url;

	
	/*
	 * <div class="input-append">
			<input id="inputId" name="inputName" type="hidden" value=""/>
			<input title="name" class="span3 sys-tree-display required" type="text" readOnly="readOnly" value="">
			<a id="a-sys-tree" href="#sys-tree-model" class="btn a-sys-tree" data-toggle="modal" onclick="sysTreeClick('/java/api/test/getTreeList')">
				&nbsp;&nbsp;&nbsp;<i class="icon-search"></i>&nbsp;&nbsp;&nbsp;
			</a>
			<div id="sys-tree-model" class="modal hide fade sys-tree-model" tabindex="-1" style="width:350px;">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">×</button>
					<h4>选择列表</h4>
				</div>
				<div class="modal-body">
					<ul id="ul-sys-tree" class="ztree"></ul>
				</div>
			</div>
		</div>
	 */
	public int doStartTag() throws JspException {
		if(Tool.isNotBlank(required) && ("true".equals(required) || "required".equals(required))){
			required = "required";
		}else{
			required = "";
		}
		
		JspWriter out = this.pageContext.getOut();
		try{
			
			if(Tool.isBlank(name,url)){
				out.println("");
				return SKIP_BODY;
			}
			String oldValue = "";
			if(Tool.isNotBlank(value)){
				oldValue = value;
			}
			out.println("<div class='input-append'>");
			out.println("<input id='"+name+"' name='"+name+"' type='hidden' value='"+oldValue+"' url='"+url+"' class='sys-hidden-input'/>");
			out.println("<input title='"+name+"' class='span3 sys-tree-display "+required+"' type='text' readOnly='readOnly' value=''>");
			out.println("<a id='a-sys-tree' href='#sys-tree-model' class='btn a-sys-tree' data-toggle='modal' onclick=\"sysTreeClick('"+url+"','"+name+"')\">");
			out.println("&nbsp;&nbsp;&nbsp;<i class='icon-search'></i>&nbsp;&nbsp;&nbsp;");
			out.println("</a>");
			out.println("</div>");
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
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	
}
