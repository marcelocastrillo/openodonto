<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>

<rich:modalPanel id="painelBuscaDentista"  width="640" height="490" >
        <f:facet name="header">
            <h:panelGroup>
                <h:outputText value="Buscar Dentista"></h:outputText>
            </h:panelGroup>
        </f:facet>
        <f:facet name="controls">
            <h:panelGroup>
				<a4j:commandButton image="/imagens/close.png" reRender="formDentista" id="hidelink3" styleClass="hidelink">
					<rich:componentControl for="painelBuscaDentistas" attachTo="hidelink3" operation="hide" event="onclick"/>
				</a4j:commandButton>
            </h:panelGroup>
        </f:facet>
		<rich:panel style="height : 448px">
		<a4j:form id="formModalDentista">
			<h:selectOneRadio id="opcao" value="#{manterDentista.opcao}" >
				<f:selectItem itemLabel="Código" itemValue="codigo"/>
				<f:selectItem itemLabel="Nome" itemValue="nome" />
				<f:selectItem itemLabel="CRO" itemValue="cro" />
				<f:selectItem itemLabel="Especialidade" itemValue="especialidade" />
			</h:selectOneRadio>
			<rich:spacer width="32" />
			<h:panelGrid columns="1">
				<h:panelGrid columns="3">
					<h:outputLabel value="Parametro de busca : " for="paramBusca" />
					<rich:spacer width="180"></rich:spacer>
					<rich:message id="buscar" for="buscar" style=" width : 280px;"/>
				</h:panelGrid>
				<h:inputText id="paramBusca" value="#{manterDentista.paramBusca}"/>
			</h:panelGrid>
			<rich:spacer width="32"></rich:spacer>
			<rich:panel>
				<f:facet name="header">
					<h:outputText value="Resultado: " />
				</f:facet>
				<rich:scrollableDataTable id="listaContat" width="580px" height="280px" value="#{manterDentista.procurado}" var="dentista" rowKeyVar="index" rows="10">
					<rich:column width="100">
						<f:facet name="header"><h:outputText value="Codigo" styleClass="headerText"/></f:facet>
						<center>
						<a4j:commandLink value="#{dentista.codigo}" reRender="formDentista" action="#{manterDentista.acaoCarregarBean}" id="idDentista"  oncomplete="#{rich:component('painelBuscaDentista')}.hide();if(#{manterDentista.view.displayPopUp}){popUp()}" >
							<f:param name="index" id="codigoDentista" value="#{index}"/>
						</a4j:commandLink>							
						</center>
					</rich:column>
					<rich:column width="250">
						<f:facet name="header"><h:outputText value="Nome" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{dentista.nome}"/></center>
					</rich:column>					
					<rich:column width="120">
						<f:facet name="header"><h:outputText value="CRO" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{dentista.cro}"/></center>
					</rich:column>					
                    <rich:column width="120">
						<f:facet name="header"><h:outputText value="Especialidade" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{dentista.especialidade}"/></center>
					</rich:column>
					
				</rich:scrollableDataTable>
			</rich:panel>
			<rich:spacer height="8" />
			<h:panelGrid columns="2">
				<a4j:commandButton value="Buscar" reRender="listaContat" action="#{manterDentista.acaoPesquisar}" />
				<a4j:commandButton id="limpar" value="Limpar" action="#{manterDentista.acaoLimpar}" reRender="formModalDentista" />
			</h:panelGrid>			
		</a4j:form>

		</rich:panel>	
	</rich:modalPanel>