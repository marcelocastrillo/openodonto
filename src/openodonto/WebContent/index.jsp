<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>:: OpenOdonto ::</title>
</head>
<body style="background-color: #fafbfc !important;">
<f:view>
<a4j:region id="rb">
	<jsp:include page="/cabecalho.jsp" />
	<jsp:include page="Loading.jsp" />
	<a4j:form id="formHome" ajaxSubmit="true">
	<h:panelGrid width="100%">
	<rich:message id="output" for="output" style=" width : 100%;"/>
	<rich:panel style=" width : 100%;background-color : transparent">
	    <f:facet name="header">
	        <h:outputText value="Home" />
	    </f:facet>
	    <center>
	         <h:panelGrid columns="1" style="aligin : cenetr">
	            <h:graphicImage value="/helt/Home2-256x256.png" />
			    <h:outputText value="Bem vindo !" style="FONT-WEIGHT: bold;line-height: 30px ; font-size : 24px ; padding-left : 48px"/>									
	        </h:panelGrid>
	    </center>
	</rich:panel>
	</h:panelGrid>
	</a4j:form>
	<jsp:include page="/footer.jsp" />
</a4j:region>
</f:view>
</body>
</html>