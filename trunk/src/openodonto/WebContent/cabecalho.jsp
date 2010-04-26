<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<head>
<style type="text/css">

.rich-menu-item{
font-family : Trebuchet MS;
}


.rich-menu-item-hover{
-moz-opacity:0.8;
filter:opacity(alpha=8);
opacity : 0.8; 
background-color : #d8edef;
border-color : #ACBECE;
background-repeat : no-repeat;
}

</style>
</head>
<h:form>
	<rich:toolBar style="margin-top : 10px;vertical-align: botton;background-image: url('#{facesContext.externalContext.requestContextPath}/helt/menu-fundo.png');border-left-style : none;border-right-style : none;border-top : 15px;height : 64px;background-color : #ACBECE;background-position : center;">
		<rich:dropDownMenu >
			<f:facet name="label">
				<h:panelGrid cellpadding="0" cellspacing="0" columns="3" style="vertical-align:middle;">
					<h:graphicImage value="/helt/menu-cadastro-deco-36x36.png"  />
					<rich:spacer width="8"></rich:spacer>
					<h:outputText value="Cadastro" style="font-family : verdana ; font-weigth : bold;COLOR : #f1f2f3;font-size : 20px"/>
				</h:panelGrid>
			</f:facet>
			
			<rich:menuItem submitMode="none" onclick="document.location.href='#{facesContext.externalContext.requestContextPath}/faces/cadastro/paciente/index.jsp'" >
				<h:outputLink value="#{facesContext.externalContext.requestContextPath}/faces/cadastro/paciente/index.jsp">
					<h:outputText value="Paciente" />
				</h:outputLink>
			</rich:menuItem>
			
		</rich:dropDownMenu>
		<f:verbatim><div style="width: 2px ;background-position : top;bottom : 1px;height: 31px;vertical-align: top;position: relative;margin-left: -1px;background-image: url('/openodonto/helt/linhadupla.png');background-repeat : no-repeat;"></div></f:verbatim>
		<rich:dropDownMenu>
			<f:facet name="label">
				<h:panelGrid cellpadding="0" cellspacing="0" columns="3" style="vertical-align:middle">
					<h:graphicImage value="/helt/menu-help-deco-36x36.png" />
					<rich:spacer width="8"></rich:spacer>
					<h:outputText value="Ajuda" style="font-family : verdana ; font-weigth : bold;COLOR : #f1f2f3;font-size : 20px"/>
				</h:panelGrid>
			</f:facet>
			<rich:menuItem submitMode="none" onclick="document.location.href='#{facesContext.externalContext.requestContextPath}/faces/ajuda/sobre.jsp'" >
				<h:outputLink
					value="#{facesContext.externalContext.requestContextPath}/faces/ajuda/sobre.jsp">
					<h:outputText value="Atendimento" />
				</h:outputLink>
			</rich:menuItem>
		</rich:dropDownMenu>
				<f:verbatim><div style="width: 2px ;background-position : top;bottom : 1px;height: 31px;vertical-align: top;position: relative;margin-left: -1px;background-image: url('/openodonto/helt/linhadupla.png');background-repeat : no-repeat;"></div></f:verbatim>
		    <a4j:form ajaxSubmit="true" id="formShowLogin" >		
			<h:panelGrid columns="4">		
				<h:outputLink rendered="#{empty facesContext.externalContext.sessionMap.usuarioSessao }" value="#{facesContext.externalContext.requestContextPath}/faces/login/login.jsp" style="COLOR: #f1f2f3;font-weigth : bold;font-size : 16px">Login</h:outputLink>
				<h:graphicImage rendered="#{not empty facesContext.externalContext.sessionMap.usuarioSessao}" value="/imagens/2820_32x32.png" height="24" />
				<h:outputText rendered="#{not emptyfacesContext.externalContext.sessionMap.usuarioSessao}" value="#{facesContext.externalContext.sessionMap.usuarioSessao.nomeApresentacao}"  style="COLOR: #f1f2f3;font-weigth : bold;font-size : 12px"/>
				<a4j:commandLink rendered="#{not empty facesContext.externalContext.sessionMap.usuarioSessao}" value="(Sair)" actionListener="#{login.acaoLogout}" style="COLOR: #000000;font-size : 8x"/>
			</h:panelGrid>
            </a4j:form>		
		<f:verbatim>
		<a href="/openodonto/faces/index.jsp"><h:panelGroup layout="block" style="width: 320px ;height:130px;vertical-align: middle;position: absolute;margin-left: -250.5px;left : 94.7%; margin-top: -44px;background-image: url('/openodonto/helt/loago-b.png');background-repeat : no-repeat;"/></a>
		</f:verbatim>
	</rich:toolBar>
</h:form>
<div align="center" style="left : 0;top : 0;position: absolute;width: 100%;height: 100% ;z-index: -1;background-image: url('/openodonto/helt/back-home.png');background-repeat: repeat-x"></div>
<rich:spacer height="30px" />


