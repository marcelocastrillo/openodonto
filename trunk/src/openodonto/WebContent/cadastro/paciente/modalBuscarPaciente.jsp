<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>

<rich:modalPanel id="painelBuscaPaciente" autosized="true" resizeable="false" onshow="listar()">
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
		<rich:panel id="searchMasterPainel" >
		<a4j:form id="formModalPaciente">
		    <a4j:jsFunction name="listar" reRender="listaPaciente,dataScroller,buscar" action="#{manterPaciente.acaoListar}"  />
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
					<rich:spacer width="180"/>					
					<h:panelGrid style="text-align : center">					    
					    <rich:message id="buscar" for="buscar" style=" width : 280px;"/>
					    <rich:message id="queryTime" for="queryTime" style=" width : 120px;font-size : 9px"/>
					</h:panelGrid>
				</h:panelGrid>
				<h:inputText style="width : 220px" id="paramBusca" value="#{manterPaciente.paramBusca}"/>
			</h:panelGrid>
			<rich:spacer width="32"></rich:spacer>
			<rich:panel>
				<f:facet name="header">
					<h:outputText value="Resultado: " />
				</f:facet>
				<rich:dataTable onRowMouseOver="this.style.backgroundColor='#F1F1F1';this.style.cursor='pointer'" onRowMouseOut="this.style.backgroundColor='#{a4jSkin.tableBackgroundColor}';this.style.cursor='default'" id="listaPaciente" width="590px" value="#{manterPaciente.procurado}" var="paciente" rowKeyVar="index" rows="10">
                    <a4j:support event="onRowClick" reRender="formPaciente" action="#{manterPaciente.acaoCarregarBean}" oncomplete="#{rich:component('painelBuscaPaciente')}.hide();if(#{manterPaciente.view.displayPopUp}){popUp()}">
				        <f:param name="index" id="codigoPaciente" value="#{index}"/>
				    </a4j:support>
					<rich:column width="100px">
						<f:facet name="header"><h:outputText value="Codigo" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{paciente.codigo}"/></center>
					</rich:column>
					<rich:column width="270px">
						<f:facet name="header"><h:outputText value="Nome" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{paciente.nome}"/></center>
					</rich:column>					
					<rich:column width="120px">
						<f:facet name="header"><h:outputText value="CPF" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{paciente.cpf}"/></center>
					</rich:column>					
                    <rich:column width="120px">
						<f:facet name="header"><h:outputText value="E-mail" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{paciente.email}"/></center>
					</rich:column>										
				</rich:dataTable>
				<rich:spacer height="8" />
				<rich:datascroller for="listaPaciente" maxPages="20" align="center" id="dataScroller"  />
			</rich:panel>
			<rich:spacer height="4" />
			<h:panelGrid columns="3">
				<a4j:commandButton value="Buscar" reRender="listaPaciente,dataScroller" action="#{manterPaciente.acaoPesquisar}" />
    			<a4j:commandButton value="Listar" onclick="listar()" />
				<a4j:commandButton id="limpar" value="Limpar" action="#{manterPaciente.acaoLimpar}" reRender="formModalPaciente" />
			</h:panelGrid>			
		</a4j:form>

		</rich:panel>	
	</rich:modalPanel>