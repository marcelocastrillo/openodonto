<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login OpenOdonto</title>
<script language=JavaScript>  
function logar(event) {  
         var keynum;           
         if(window.event) {  
             keynum = event.keyCode;  
         }else if(event.which){  
             keynum = event.which;  
         }  
         if( keynum==13 ){  
        	 return true;
         }
         return false;  
      }  
</script> 
</head>
<body style="background-color: #fafbfc !important;">
<div align="center" style="left : 0;top : 0;position: absolute;width: 100%;height: 100% ;z-index: -1;background-image: url('/openodonto/helt/back-home.png');background-repeat: repeat-x"></div>
<f:view>
	<rich:toolBar style="margin-top : 10px;vertical-align: botton;background-image: url('#{facesContext.externalContext.requestContextPath}/helt/menu-fundo.png');border-left-style : none;border-right-style : none;border-top : 15px;height : 64px;background-color : #ACBECE;background-position : center;">
	<f:verbatim>
        <a href="/openodonto/faces/index.jsp"><h:panelGroup layout="block" style="width: 320px ;height:130px;vertical-align: middle;position: absolute;margin-left: -250.5px;left : 94.7%; margin-top: -44px;background-image: url('/openodonto/helt/loago-b.png');background-repeat : no-repeat;"/></a>
    </f:verbatim>
	</rich:toolBar>

	<jsp:include page="/Loading.jsp" />
		<center>
		
		
			<div id="LoginMargin" style="margin-top : 2%;font-weight: bold; left: 50%;position :static; top: 130px;color: #154876; width: 640px;height : 480px;font-size: 12px; ">
			<table border='3' width='100%' id="table1" cellspacing='0' height='100%' bordercolorlight='#ACBECE' cellpadding='0' bordercolordark='#ACBECE' style=" border: 2px solid #ACBECE; border-collapse: collapse;bo	">
				<tr>
					<td>
						<div align="center">
						<table border="0" width="70%" height='100%' id="table2" cellspacing="0" cellpadding="0">
							<tr>
								<td align="center">

			<h:graphicImage value="/helt/Stomatologist-icon.png" style="padding-bottom: 20px" />
			<rich:spacer height="32" />
			<div id="LoginInput" style="font-weight: bold; left: 50%;position :static; bottom: 130px;background-image: url('helt/transparent-back-menu.png');color: #154876; width: 280px;font-size: 12px; ">
			<table border='3' width='100%' id="table1" cellspacing='0' height='100%' bordercolorlight='#ACBECE' cellpadding='0' bordercolordark='#ACBECE' style=" border: 2px solid #ACBECE; border-collapse: collapse;bo	">
				<tr>
					<td>
						<a4j:form id="LoginForm">
						<a4j:jsFunction name="acaoLogar" action="#{login.acaoAutenticarUsuario}" />
						<div align="center">
						<table border="0" width="70%" height='100%' id="tableInput" cellspacing="0" cellpadding="0">
							<tr>
								<td width="75%"  align="center">
									<rich:spacer height="32"></rich:spacer>
									<rich:message id="messageLogin" for="messageLogin" style="font-weight:bold; font-size:10;color:red"/>
									<h:panelGrid columns="3" style="word-spacing: 0; text-indent: 0;line-height: 0.5px; margin: 0">
										<h:outputLabel value="Usuario" for="username" />
										<h:outputText  value=" : " />
										<h:inputText id="username" value="#{login.usuario.user}" />
										<h:outputLabel value="Senha" for="pwd" />
										<h:outputText value=" : " />
										<h:inputSecret id="pwd" value="#{login.usuario.senha}" onkeypress="if(logar(event)){acaoLogar();}" />
									</h:panelGrid>									
									<rich:spacer height="16"></rich:spacer>
									<div align="right"><a4j:commandButton value="logar" action="#{login.acaoAutenticarUsuario}" style=" width : 96px;" reRender="formShowLogin" /></div>
									<rich:spacer height="16"></rich:spacer>
									</td>
								
							</tr>
						</table>
						</div>
						</a4j:form>
					</td>
				</tr>
			</table>									
			</div>
											</td>
							</tr>
						</table>
						</div>
					</td>
				</tr>
			</table>									
			</div>
			</center>

<jsp:include page="/footer.jsp" />

</f:view>
</body>
</html>