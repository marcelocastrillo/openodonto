<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>


	<rich:modalPanel id="painelAlterarTelefone" autosized="true" width="300" height="100" >
	
			<f:facet name="header">
				<h:outputText value="Alterar Telefone" />
			</f:facet>
	
		<a4j:form  id="formAlterarTelefone">
			<rich:panel>
				<h:panelGrid columns="3">
					<h:panelGrid columns="1">
						<h:outputLabel value="Tipo telefone" for="tipoTelefonePainel" />
						 <h:selectOneMenu id="tipoTelefonePainel" value="#{manterPaciente.manageTelefone.telefone.tipoTelefone}"  converter="simpleEntityConverter">
							<f:selectItems value="#{manterListagem.cache['br.ueg.openodonto.dominio.constante.TiposTelefone'].dominio}" />
						</h:selectOneMenu>
					</h:panelGrid>
					<h:panelGrid columns="1">
						<h:outputLabel value="Telefone" for="numeroTelefonePainel" />
						<h:inputText id="numeroTelefonePainel" value="#{manterPaciente.manageTelefone.telefone.numero}" />
					</h:panelGrid>
					<h:panelGrid columns="3">
						<a4j:commandButton id="btnAlterar" actionListener="#{manterPaciente.manageTelefone.acaoAlterarTelefone}" reRender="listaTelefones" value="Alterar" oncomplete="#{rich:component('painelAlterarTelefone')}.hide();return false" />
						<a4j:commandButton id="btnCancelar" actionListener="#{manterPaciente.manageTelefone.acaoAlterarTelefone}"  value="Cancelar"></a4j:commandButton>
					</h:panelGrid>
				</h:panelGrid>
				<rich:componentControl for="painelAlterarTelefone" attachTo="btnCancelar" event="onclick" operation="hide" />
			</rich:panel>
			</a4j:form>
			
	</rich:modalPanel>
