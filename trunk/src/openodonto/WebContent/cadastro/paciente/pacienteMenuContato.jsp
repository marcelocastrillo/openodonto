<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<%@ taglib prefix="t"  uri="http://myfaces.apache.org/tomahawk"%>

	<center>
	<rich:panel style="background-color : white;width : 90%">
		<center>
		<h:panelGrid columns="4" id="gridAddTel">
			<h:panelGrid columns="1">
				<h:outputLabel value="Tipo telefone" for="tipoTelefoneContato" />
				<h:selectOneMenu id="tipoTelefoneContato" value="#{manterPaciente.manageTelefone.telefone.tipoTelefone}" converter="simpleEntityConverter">
					<t:selectItems value="#{manterListagem.cache['br.ueg.openodonto.dominio.constante.TiposTelefone'].dominio}" var="tipo" itemLabel="#{tipo.descricao}" itemValue="#{tipo}" />
				</h:selectOneMenu>
			</h:panelGrid>
			<rich:spacer width="16"/>
			<h:panelGrid columns="1">
				<h:outputLabel value="Telefone" for="telefone" />
				<h:inputText id="telefone" value="#{manterPaciente.manageTelefone.telefone.numero}" />
			</h:panelGrid>
			<h:panelGrid columns="1">
				<rich:spacer height="8"></rich:spacer>
				<h:panelGrid columns="3">
					<a4j:commandButton actionListener="#{manterPaciente.manageTelefone.acaoInserirTelefone}" image="/helt/add.jpg" reRender="listaTelefones,gridAddTel" value="Inserir" />
					<rich:spacer width="16"/>
					<rich:message id="messageTelefone" for="messageTelefone" style=" width : 120px;font-weight:bold; font-size:10;color:red"/>
				</h:panelGrid>
			</h:panelGrid>
		</h:panelGrid>
	</center>
	</rich:panel>
	<rich:panel style="background-color : white;width : 90%;border-top: none;">
		<h:panelGrid columns="3">
			<h:panelGrid columns="1">
				<h:outputText value="Contato" />
				<rich:scrollableDataTable width="500px" height="300px" var="telefone" value="#{manterPaciente.paciente.telefone}" id="listaTelefones" rowKeyVar="index" rows="10" >
					<rich:column width="200px" style="margin-left: 8px">
						<f:facet name="header">
							<h:outputText value="Numero" />
						</f:facet>
						<center>
							<h:outputText value="#{telefone.numero}" />
						</center>
					</rich:column>
					<rich:column width="100px" style="margin-left: 8px">
						<f:facet name="header">
							<h:outputText value="tipo" />
						</f:facet>
						<center>
							<h:outputText value="#{telefone.tipoTelefone}" />
						</center>
					</rich:column>
					<rich:column width="100px">
						<f:facet name="header">
							<h:outputText value="Alterar" />
						</f:facet>
						<center>
						<a4j:commandButton value="Alterar" id="alterarTelefone" actionListener="#{manterPaciente.manageTelefone.acaoCarregarTelefone}"  oncomplete="#{rich:component('painelAlterarTelefone')}.show();" reRender="formAlterarTelefone" image="/helt/edit-19x19.jpg">
							<f:param name="index" id="indexAlterar" value="#{index}"/>
						</a4j:commandButton>
						</center>						
					</rich:column>
					<rich:column width="100px">
						<f:facet name="header">
							<h:outputText value="Remover" />
						</f:facet>
						<center>
							<a4j:commandButton value="Excluir" actionListener="#{manterPaciente.manageTelefone.acaoRemoverTelefone}" reRender="listaTelefones" image="/helt/minus.jpg">
								<f:param name="index" id="indexRemover" value="#{index}"/>
							</a4j:commandButton>
						</center>
					</rich:column>
					<f:facet name="footer">
						<rich:datascroller for="listaTelefones" maxPages="4"></rich:datascroller>
					</f:facet>
				</rich:scrollableDataTable>

			</h:panelGrid>
		</h:panelGrid>
	</rich:panel></center>	
	