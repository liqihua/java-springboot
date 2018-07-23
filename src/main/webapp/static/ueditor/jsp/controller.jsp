<%@ page language="java" contentType="text/html; charset=UTF-8" import="com.java.sys.common.ueditor.ActionEnter,com.java.sys.common.utils.Tool" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );
	//String rootPath = Tool.getProjectPath();
	System.out.println("--- controller.jsp rootPath : "+rootPath);
	String exec = new ActionEnter( request, rootPath ).exec();
	
	System.out.println("--- controller.jsp exec : "+exec);
	out.write( exec );
	
%>