<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>

<rich:modalPanel id="painelBuscaUsuario"  width="640" height="490" >
        <f:facet name="header">
            <h:panelGroup>
                <h:outputText value="Buscar Usuario"></h:outputText>
            </h:panelGroup>
        </f:facet>
        <f:facet name="controls">
            <h:panelGroup>
				<a4j:commandButton image="/imagens/close.png" reRender="formUsuario" id="hidelink3" styleClass="hidelink">
					<rich:componentControl for="painelBuscaUsuario" attachTo="hidelink3" operation="hide" event="onclick"/>
				</a4j:commandButton>
            </h:panelGroup>
        </f:facet>
		<rich:panel style="height : 448px">
		<a4j:form id="formModalUsuario">
			<h:selectOneRadio id="opcao" value="#{manterUsuario.opcao}" >
				<f:selectItem itemLabel="Código" itemValue="codigo"/>
				<f:selectItem itemLabel="Nome" itemValue="nome" />
				<f:selectItem itemLabel="Usuario" itemValue="user" />
			</h:selectOneRadio>
			<rich:spacer width="32" />
			<h:panelGrid columns="1">
				<h:panelGrid columns="3">
					<h:outputLabel value="Parametro de busca : " for="paramBusca" />
					<rich:spacer width="180"></rich:spacer>
					<rich:message id="buscar" for="buscar" style=" width : 280px;"/>
				</h:panelGrid>
				<h:inputText id="paramBusca" value="#{manterUsuario.paramBusca}"/>
			</h:panelGrid>
			<rich:spacer width="32"></rich:spacer>
			<rich:panel>
				<f:facet name="header">
					<h:outputText value="Resultado: " />
				</f:facet>
				<rich:scrollableDataTable id="listaContat" width="580px" height="280px" value="#{manterUsuario.procurado}" var="usuario" rowKeyVar="index" rows="10">
					<rich:column width="100">
						<f:facet name="header"><h:outputText value="Codigo" styleClass="headerText"/></f:facet>
						<center>
						<a4j:commandLink value="#{usuario.codigo}" reRender="formUsuario" action="#{manterUsuario.acaoCarregarBean}" id="idUsuario"  oncomplete="#{rich:component('painelBuscaUsuario')}.hide();if(#{manterUsuario.view.displayPopUp}){popUp()}" >
							<f:param name="index" id="codigoUsuario" value="#{index}"/>
						</a4j:commandLink>							
						</center>
					</rich:column>
					<rich:column width="250">
						<f:facet name="header"><h:outputText value="Nome" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{usuario.nome}"/></center>
					</rich:column>					
					<rich:column width="120">
						<f:facet name="header"><h:outputText value="Usuário" styleClass="headerText"/></f:facet>
						<center><h:outputText value="#{usuario.user}"/></center>
					</rich:column>					
				</rich:scrollableDataTable>
			</rich:panel>
			<rich:spacer height="8" />
			<h:panelGrid columns="2">
				<a4j:commandButton value="Buscar" reRender="listaContat" action="#{manterUsuario.acaoPesquisar}" />
				<a4j:commandButton id="limpar" value="Limpar" action="#{manterUsuario.acaoLimpar}" reRender="formModalUsuario" />
			</h:panelGrid>			
		</a4j:form>

		</rich:panel>	
	</rich:modalPanel>