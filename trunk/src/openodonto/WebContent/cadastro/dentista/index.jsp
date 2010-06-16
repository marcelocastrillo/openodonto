<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OpenOdonto - Cadastro de Dentistas por Especialidade</title>
<script type="text/javascript" src="/openodonto/js/jquery.maskedinput-1.1.4.pack.js"></script>
<script type="text/javascript" src="/openodonto/js/scriptValidaNumeros.js"></script>
<script type="text/javascript">
function exibirMensagem(msg){
	alert(msg);
}
</script>
</head>
<body style="background-color: #fafbfc !important;">
<f:view>
<a4j:keepAlive beanName="manterDentista"/>
<a4j:region id="rb">
<jsp:include page="/cabecalho.jsp" />
<jsp:include page="/Loading.jsp" />
<a4j:form ajaxSubmit="true" id="formDentista">
<a4j:jsFunction name="popUp" action="#{manterDentista.acaoShowed}" oncomplete="exibirMensagem('#{manterDentista.view.popUpMsg}')"/>
	<h:panelGrid>
		<rich:message id="output" for="output" style=" width : 100%;"/>
	</h:panelGrid>	
	<rich:panel style="background-color : transparent">
			<f:facet name="header">
				<h:outputText value="Cadastro de Dentistas" style="COLOR : white" />
			</f:facet>
							
			<center>
			<rich:panel style="width : 90%; text-align:left;" >				
				<center>
				<h:panelGrid columns="1">			
			
			<h:panelGrid columns="2">
				<h:panelGrid columns="1">
					<h:outputLabel value="Codigo" for="codigoDentista" />
					<h:inputText id="codigoDentista" readonly="true" disabled="true" value="#{manterDentista.dentista.codigo}" style=" width : 160px;" />
				</h:panelGrid>

				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="Nome" for="entradaNome"  style="color : red" />
	                    <rich:spacer width="8" />
	                    <rich:message id="messageEntradaNome" for="entradaNome" style="color : red ;FONT-WEIGHT : bold" />
	                </h:panelGrid>
					<h:inputText id="entradaNome" value="#{manterDentista.dentista.nome}" style="width : 310px;"	/>
				</h:panelGrid>

           
				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="CRO" for="entradaCro" style="color : red"/>
	                    <rich:spacer width="8" />
	                    <rich:message id="messageEntradaCro" for="entradaCro" style="color : red ;FONT-WEIGHT : bold" />
	                </h:panelGrid>
					<h:inputText value="#{manterDentista.dentista.cro}" onkeypress="return digitarNumero(this, event)" id="entradaCro" style="width : 160px;"/>
				</h:panelGrid>
				
				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="Especialidade" style="color : red" for="entradaEspecialidade" />
	                    <rich:spacer width="8" />
	                    <rich:message id="messageEntradaEspecialidade" for="entradaEspecialidade" style="color : red ;FONT-WEIGHT : bold" />
	                </h:panelGrid>
					<h:inputText id="entradaEspecialidade" value="#{manterDentista.dentista.especialidade}" style=" width : 310px;" />
				</h:panelGrid>
				
			</h:panelGrid>
			
			
			</h:panelGrid>
				</center>
			</rich:panel>
			</center>			
		</rich:panel>

			<rich:tabPanel switchType="client">
				<rich:tab label="Principal" switchType="client" style="background-color : transparent">
					<jsp:include page="dentistaMenuPrincipal.jsp" />
				</rich:tab>
				<rich:tab label="Fone/Contato" switchType="client" style="background-color : transparent">
					<jsp:include page="dentistaMenuContato.jsp" />
				</rich:tab>
			</rich:tabPanel>
			
			<rich:spacer height="16"/>	
			<center>			
            <h:panelGroup>
				<a4j:commandButton id="botaoSalvar" image="/helt/salvar.png" oncomplete="if(#{manterDentista.view.displayPopUp}){popUp()}" action="#{manterDentista.acaoAlterar}" value="salvar" reRender="formDentista" focus="output" />
				<rich:spacer width="16"/>
				<a4j:commandButton image="/helt/pesquisar.png" id="botaoBuscar" onclick="#{rich:component('painelBuscaDentista')}.show()" reRender="formModalDentista"/>
				<rich:spacer width="16"/>
				<a4j:commandButton image="/helt/excluir.png" id="botaoExcluir"  action="#{manterDentista.acaoRemoverSim}" onclick="if(!confirm('Deseja realmente excluir o registro ?')){return false}" reRender="formDentista" oncomplete="if(#{manterDentista.view.displayPopUp}){popUp()}" />
				<rich:spacer width="16"/>
				<a4j:commandButton image="/helt/cancelar.png"  action="#{manterDentista.acaoAtualizar}" reRender="formDentista" />												
            </h:panelGroup>			
			</center>

	</a4j:form>
	
	<jsp:include page="/footer.jsp" />
   <jsp:include page="modalAlterarContato.jsp" />
   <jsp:include page="modalBuscarDentista.jsp" />
	
	</a4j:region>
	

</f:view>
</body>
</html>