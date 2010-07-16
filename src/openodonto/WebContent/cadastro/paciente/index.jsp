<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OpenOdonto - Cadastro de Pacientes</title>
<script type="text/javascript" src="/openodonto/js/jquery.maskedinput-1.1.4.pack.js"></script>
<script type="text/javascript">
function exibirMensagem(msg){
	alert(msg);
}
function limparCPF(cpf){
	cpf = cpf.replace(/-/g,"");
	cpf = cpf.replace(/[.]/g,"");
	cpf = cpf.replace(/_/g,"");
	cpf = cpf.replace(/[ ]/g,"");
	return cpf;
}
</script>
</head>
<body style="background-color: #fafbfc !important;">
<f:view>
<a4j:keepAlive beanName="manterPaciente"/>
<a4j:region id="rb">
<jsp:include page="/cabecalho.jsp" />
<jsp:include page="/Loading.jsp" />
<a4j:form ajaxSubmit="true" id="formPaciente">
<a4j:jsFunction name="popUp" action="#{manterPaciente.acaoShowed}" oncomplete="exibirMensagem('#{manterPaciente.view.popUpMsg}')"/>
	<h:panelGrid>
		<rich:message id="output" for="output" style=" width : 100%;"/>
	</h:panelGrid>	
	<rich:panel style="background-color : transparent">
			<f:facet name="header">
				<h:outputText value="Cadastro de Pacientes" style="COLOR : white" />
			</f:facet>
							
			<center>
			<rich:panel style="width : 90%; text-align:left;" >				
				<center>
				<h:panelGrid columns="1">			
			
			<h:panelGrid columns="2">
				<h:panelGrid columns="1">
					<h:outputLabel value="Codigo" for="codigoPaciente" />
					<h:inputText id="codigoPaciente" readonly="true" disabled="true" value="#{manterPaciente.paciente.codigo}" style=" width : 160px;" />
				</h:panelGrid>

				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="Nome" for="entradaNome"  style="color : red" />
	                    <rich:spacer width="8" />
	                    <rich:message id="messageEntradaNome" for="entradaNome" style="color : red ;FONT-WEIGHT : bold" />
	                </h:panelGrid>
					<h:inputText id="entradaNome" value="#{manterPaciente.paciente.nome}" style="width : 310px;"	/>
				</h:panelGrid>

           
				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="CPF" for="entradaCpf" style="color : red"/>
	                    <rich:spacer width="8" />
	                    <rich:message id="messageEntradaCpf" for="entradaCpf" style="color : red ;FONT-WEIGHT : bold" />
	                </h:panelGrid>
					<h:inputText value="#{manterPaciente.paciente.cpf}" id="entradaCpf" style="width : 160px;">
						<rich:jQuery selector="#entradaCpf" query="mask('999.999.999-99')" timing="onload" />
					</h:inputText>
				</h:panelGrid>
				
				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="Data Inicio Tratamento" for="dataInicioTratamento" />
	                    <rich:spacer width="8" />
	                    <rich:message id="messageDataInicioTratamento" for="dataInicioTratamento" style="color : red ;FONT-WEIGHT : bold" />
	                </h:panelGrid>
					<rich:calendar id="dataInicioTratamento" datePattern="dd/MM/yyyy" enableManualInput="false" value="#{manterPaciente.paciente.dataInicioTratamento}" inputStyle="width : 290px;" >
						<f:converter converterId="SqlDateConverter" />
					</rich:calendar>
				</h:panelGrid>
				
			</h:panelGrid>
			
			
			</h:panelGrid>
				</center>
			</rich:panel>
			</center>			
		</rich:panel>

			<rich:tabPanel switchType="client">
				<rich:tab label="Principal" switchType="client" style="background-color : transparent">
					<jsp:include page="pacienteMenuPrincipal.jsp" />
				</rich:tab>
				<rich:tab label="Fone/Contato" switchType="client" style="background-color : transparent">
					<jsp:include page="pacienteMenuContato.jsp" />
				</rich:tab>
				<rich:tab label="Odontograma" switchType="client" disabled="true" style="background-color : transparent">
				</rich:tab>
				<rich:tab label="Ficha anaminese" switchType="client" disabled="true" style="background-color : transparent">
				</rich:tab>
				<rich:tab label="Planejamento" switchType="client" disabled="true" style="background-color : transparent">
				</rich:tab>
			</rich:tabPanel>
			
			<rich:spacer height="16"/>	
			<center>			
            <h:panelGroup>
				<a4j:commandButton id="botaoSalvar" image="/helt/salvar.png" oncomplete="if(#{manterPaciente.view.displayPopUp}){popUp()}" action="#{manterPaciente.acaoAlterar}" value="salvar" reRender="formPaciente" focus="output" />
				<rich:spacer width="16"/>
				<a4j:commandButton image="/helt/pesquisar.png" id="botaoBuscar" onclick="Richfaces.showModalPanel('painelBuscaPaciente', {top:'80px'})" reRender="formModalPaciente"/>
				<rich:spacer width="16"/>
				<a4j:commandButton image="/helt/excluir.png" id="botaoExcluir"  action="#{manterPaciente.acaoRemoverSim}" onclick="if(!confirm('Deseja realmente excluir o registro ?')){return false}" reRender="formPaciente" oncomplete="if(#{manterPaciente.view.displayPopUp}){popUp()}" />
				<rich:spacer width="16"/>
				<a4j:commandButton image="/helt/cancelar.png"  action="#{manterPaciente.acaoAtualizar}" reRender="formPaciente" />												
            </h:panelGroup>			
			</center>

	</a4j:form>
	
	<jsp:include page="/footer.jsp" />
   <jsp:include page="modalAlterarContato.jsp" />
   <jsp:include page="modalBuscarPaciente.jsp" />
	
	</a4j:region>
	

</f:view>
</body>
</html>