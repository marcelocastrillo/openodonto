<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>



<rich:modalPanel binding="#{manterPaciente.dialogModalPanel.modal}" id="modalConfirmDialog" autosized="true" width="320">
	<f:facet name="header">
		<h:outputText id="titleDialog" value="Titulo"></h:outputText>
	</f:facet>
	<f:facet name="controls">
		<h:panelGroup>
			<a4j:commandButton image="/imagens/close.png" reRender="formPaciente" ajaxSingle="true" id="hidelink6" styleClass="hidelink">
				<rich:componentControl for="modalConfirmDialog" attachTo="hidelink6" operation="hide" event="onclick"/>
			</a4j:commandButton>
		</h:panelGroup>
	</f:facet>
	<a4j:form id="formDialog" ajaxSubmit="true">
		<rich:panel>
		<center>
		<h:panelGrid columns="1">
			<h:outputText id="messageDialog" value="mensagem" />
		</h:panelGrid>
		<h:panelGrid columns="3">
			<a4j:commandButton id="yesButton" value="yes" reRender="modalConfirmDialog" oncomplete="if(#{manterPaciente.showPopUp}){popUp()}">
				<f:param id="paramConfirmDialogTo" name="paramConfirmDialogTo" value=""/>
				<f:param id="param1" name="param1" value="" />
				<f:param id="param2" name="param2" value="" />
			</a4j:commandButton>
			<a4j:commandButton id="noButton" value="no" />
			<a4j:commandButton id="cancelButton" value="cancel"/>
		</h:panelGrid>
		<rich:componentControl for="modalConfirmDialog" attachTo="cancelButton" event="onclick" operation="hide" ></rich:componentControl>
		<rich:componentControl for="modalConfirmDialog" attachTo="noButton" event="onclick" operation="hide" ></rich:componentControl>
		</center>
		</rich:panel>
	</a4j:form>
</rich:modalPanel>
