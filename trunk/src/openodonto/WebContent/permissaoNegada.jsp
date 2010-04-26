<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Permissao Negada</title>
<script type="text/javascript" src="../../js/jquery.maskedinput-1.1.4.pack.js"></script>
</head>
<body>
<f:view>
<a4j:region id="rb">
	<jsp:include page="/cabecalho.jsp" />
	<jsp:include page="Loading.jsp" />
	<a4j:form id="formAtendimento" ajaxSubmit="true">
	<h:panelGrid>
		<rich:message id="output" for="output" style=" width : 100%;"/>
	</h:panelGrid>
	<center>
		<rich:panel style="background-color : white">
			<f:facet name="header" >
				<h:outputText value="Permissao Negada" />
			</f:facet>				
			
				
			<h:panelGrid columns="3">
			</h:panelGrid>
			<f:verbatim>
			<div id="errorMsg" style="vertical-align: middle; font-weight: bold; left: 50%;position :static; ; top: 50%;background-image: url('imagens/fundo-erro.png'); color: white; height: 90%; width: 80%;font-size: 30px; ">
			<table border='3' width='100%' id="table1" cellspacing='0' height='100%' bordercolorlight='#FF0000' cellpadding='0' bordercolordark='#FF0000' style=" border: 2px solid #FF0000; border-collapse: collapse;	">
				<tr>
					<td>
						<div align="center">
						<table border="0" width="70%" height='100%' id="table2" cellspacing="0" cellpadding="0">
							<tr>
								<td width="25%" align="center">
									<p><img border="0" src="imagens/Symbols-Warning-256x256.png" width="196" height="196"></p>
								</td>
								<td width="75%"  align="center">
									<h:panelGrid columns="1" style="word-spacing: 0; text-indent: 0;line-height: 0.5px; margin: 0">
										<p align="center"><h:outputText value="Acesso não Permitido !" style="FONT-WEIGHT: bold;line-height: 30px"/> <br/>
										<h:outputText value="As suas pemissões não lhe dão acesso a área solicitada." style="font-size: 12px;line-height: 12px"/></p>
									</h:panelGrid>
								</td>
							</tr>
						</table>
						</div>
					</td>
				</tr>
			</table>
					
					
			</div>
			</f:verbatim>
			</rich:panel>

			</center>			

	</a4j:form>
	<jsp:include page="./footer.jsp" />
</a4j:region>
</f:view>
</body>
</html>