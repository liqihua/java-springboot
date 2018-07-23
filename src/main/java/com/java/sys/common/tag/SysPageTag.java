package com.java.sys.common.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.java.sys.common.page.SysPage;

@SuppressWarnings("rawtypes")
public class SysPageTag extends TagSupport{
	private static final long serialVersionUID = 1L;
	
	
	private SysPage page;
	
	
	/*<div class="pagination">
	<ul>
	    <li><a href="javascript:;" onclick="pageClick(${page.prevPage*page.pageSize})">上一页</a></li>
	    <li class="disabled"><a href="javascript:;">${page.page + 1 }</a></li>
	    <li><a href="javascript:;" onclick="pageClick(${page.nextPage*page.pageSize})">下一页</a></li>
	    <li class="disabled"><a href="javascript:;">共&nbsp;${page.pageTotal }&nbsp;页，&nbsp;${page.count }&nbsp;条</a></li>
	    <li><input type="text" class="input-mini input-pageGo" /></li>
		<li><a href="javascript:;" class="pageGo " pageSize="${page.pageSize }">跳转</a></li>
  	</ul>
  	</div>*/
	
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try{
			if(page == null){
				out.println("");
				return SKIP_BODY;
			}
			out.println("<div class='pagination'>");
			out.println("<ul>");
			out.println("<li><a href='javascript:;' onclick='pageClick("+(page.getPrevPage()*page.getPageSize())+")'>上一页</a></li>");
			out.println("<li class='disabled'><a href='javascript:;'>"+(page.getPage()+1)+"</a></li>");
			out.println("<li><a href='javascript:;' onclick='pageClick("+(page.getNextPage()*page.getPageSize())+")'>下一页</a></li>");
			out.println("<li class='disabled'><a href='javascript:;'>共&nbsp;"+page.getPageTotal()+"&nbsp;页，&nbsp;"+page.getCount()+"&nbsp;条</a></li>");
			out.println("<li><input type='text' class='input-mini input-pageGo' /></li>");
			out.println("<li><a href='javascript:;' class='pageGo' pageSize='"+page.getPageSize()+"'>跳转</a></li>");
			out.println("</ul>");
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

	public SysPage getPage() {
		return page;
	}
	public void setPage(SysPage page) {
		this.page = page;
	}
	
}
