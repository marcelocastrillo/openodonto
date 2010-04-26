<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>

<rich:modalPanel id="painelBuscaPaciente"  width="640" height="490" >
        <f:facet name="header">
            <h:panelGroup>
                <h:outputText value="Buscar Paciente"></h:outputText>
            </h:panelGroup>
        </f:facet>
        <f:facet name="controls">
            <h:panelGroup>
				<a4j:commandButton image="/imagens/close.png" reRender="formPaciente" id="hidelink3" styleClass="hidelink">
					<rich:componentControl for="painelBuscaPaciente" attachTo="hidelink3" operation="hide" event="onclick"/>
				</a4j:commandButton>
            </h:panelGroup>
        </f:facet>
		<rich:panel style="height : 448px">
		<a4j:form id="formModalPaciente">
			<h:selectOneRadio id="opcao" value="#{manterPaciente.opcao}" >
				<f:selectItem itemLabel="Código" itemValue="codigo"/>
				<f:selectItem itemLabel="Nome" itemValue="nome" />
				<f:selectItem itemLabel="CPF" itemValue="cpf" />
				<f:selectItem itemLabel="E-mail" itemValue="email" />
			</h:selectOneRadio>
			<rich:spacer width="32" />
			<h:panelGrid columns="1">
				<h:panelGrid columns="3">
					<h:outputLabel value="Parametro de busca : " for="paramBusca" />
					<rich:spacer width="180"></rich:spacer>
					<rich:message id="buscar" for="buscar" style=" width : 280px;"/>
				</h:panelGrid>
				<h:inputText id="paramBusca" value="#{manterPaciente.paramBusca}"/>
			</h:panelGrid>
			<rich:spacer width="32"></rich:spacer>
			<rich:panel>
				<f:facet name="header">
					<h:outputText value="Resultado: " />
				</f:facet>
				<rich:scrollableDataTable id="listaContat" width="580px" height="280px" value="#{manterPaciente.procurado}" var="paciente" rowKeyVar="index" rows="10">
					<rich:column width="100">
						<f:facet name="header"><h:outputText value="Codigo" styleClass="headerText"/></f:facet>
						<center>
						<a4j:commandLink value="#{paciente.codigo}" reRender="formPaciente" actionListener="#{manterPaciente.acaoCarregarBean}" id="idPaciente"  oncomplete="#{rich:component('painelBuscaPaciente')}.hide();if(#{manterPaciente.showPopUp}){popUp()}" >
							<f:param name="index" id="codigoPaciente" value="#{index}"/>
						</a4j:commandLink>							
						</center>
					</rich:column>
					<rich:column width="250">
						<f:facet name="header"><h:outputText value="Nome" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{paciente.nome}"/></center>
					</rich:column>					
					<rich:column width="120">
						<f:facet name="header"><h:outputText value="CPF" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{paciente.cpf}"/></center>
					</rich:column>					
                    <rich:column width="120">
						<f:facet name="header"><h:outputText value="E-mail" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{paciente.email}"/></center>
					</rich:column>
					
				</rich:scrollableDataTable>
			</rich:panel>
			<rich:spacer height="8" />
			<h:panelGrid columns="2">
				<a4j:commandButton value="Buscar" reRender="listaContat" actionListener="#{manterPaciente.acaoPesquisar}" />
				<a4j:commandButton id="limpar" value="Limpar" actionListener="#{manterPaciente.acaoLimpar}" reRender="formModalPaciente" />
			</h:panelGrid>			
		</a4j:form>

		</rich:panel>	
	</rich:modalPanel>