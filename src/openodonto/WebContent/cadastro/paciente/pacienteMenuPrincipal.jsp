<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>


<center>
<rich:panel style="background-color : white;width : 90%;text-align: left">
<center>
<h:panelGrid id="gridPrincipal">

		<h:panelGrid columns="8">
			<h:panelGrid columns="1">
				<h:outputLabel value="Estado" for="selectMenuEstado" />
				<h:selectOneMenu immediate="true" id="selectMenuEstado" value="#{manterPaciente.jsfEstado.selecaoSimples}" converter="javax.faces.Long" style="width : 128px">
					<f:selectItem itemLabel="-- Selecione --" itemValue="0" />
					<f:selectItems id="itemsEstado" value="#{manterListagem.cache['br.ueg.openodonto.dominio.constante.TiposUF'].labelMap}" />
				</h:selectOneMenu>
			</h:panelGrid>
			<rich:spacer width="8" />
			<h:panelGrid columns="1">
				<h:outputLabel value="Cidade" for="entradaCidade" />
				<h:inputText id="entradaCidade"	value="#{manterPaciente.paciente.cidade}" style="width : 140px"/>
			</h:panelGrid>
			<rich:spacer width="8px" />
			<h:panelGrid columns="1">
				<h:outputLabel value="Endereco" for="entradaEndereco" />
				<h:inputText id="entradaEndereco" value="#{manterPaciente.paciente.endereco}"
					style=" width : 300px;position : relative;z-index : 2" />
			</h:panelGrid>
		</h:panelGrid>


<h:panelGrid columns="5">
	<h:panelGrid columns="1">
		<h:outputLabel value="Responsável" for="entradaResponavel" />
		<h:inputText id="entradaResponavel"	value="#{manterPaciente.paciente.responsavel}"  style=" width : 200px;"/>
	</h:panelGrid>
	<rich:spacer width="8" />
	<h:panelGrid columns="1">
		<h:outputLabel value="E-mail" for="entradaEmail" />
		<h:inputText id="entradaEmail" value="#{manterPaciente.paciente.email}" style=" width : 145px;" />
	</h:panelGrid>
	<rich:spacer width="8" />
	<h:panelGrid columns="1">
		<h:outputLabel value="Referencia ( Quem indicou )" for="entradaReferencia" />
		<h:inputText id="entradaReferencia" value="#{manterPaciente.paciente.referencia}" style="width : 220px;"/>
	</h:panelGrid>
</h:panelGrid>

<h:panelGrid columns="5">
				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="Data Termino Tratamento" for="dataInicioTratamento" />
	                    <rich:spacer width="8" />
	                    <rich:message id="messageDataTerminoTratamento" for="dataTerminoTratamento" style="color : red ;FONT-WEIGHT : bold;font-size : 12px" />
	                </h:panelGrid>
					<rich:calendar id="dataTerminoTratamento" datePattern="dd/MM/yyyy" enableManualInput="false" value="#{manterPaciente.paciente.dataTerminoTratamento}" >
						<f:converter converterId="SqlDateConverter" />
					</rich:calendar>
				</h:panelGrid>
				<rich:spacer width="72" />
				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="Data Retorno" for="dataInicioTratamento" />
	                    <rich:spacer width="8" />
	                    <rich:message id="messageDataRetorno" for="dataRetorno" style="color : red ;FONT-WEIGHT : bold;font-size : 12px" />
	                </h:panelGrid>
					<rich:calendar id="dataRetorno" datePattern="dd/MM/yyyy" enableManualInput="false" value="#{manterPaciente.paciente.dataRetorno}" >
						<f:converter converterId="SqlDateConverter" />
					</rich:calendar>
				</h:panelGrid>
				<rich:spacer width="72" />
				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="Data Nascimento" for="dataInicioTratamento" />
	                    <rich:spacer width="8" />
	                    <rich:message id="messageDataNascimento" for="dataNascimento" style="color : red ;FONT-WEIGHT : bold;font-size : 12px" />
	                </h:panelGrid>
					<rich:calendar id="dataNascimento" datePattern="dd/MM/yyyy" enableManualInput="false" value="#{manterPaciente.paciente.dataNascimento}" >
						<f:converter converterId="SqlDateConverter" />
					</rich:calendar>
				</h:panelGrid>
				
</h:panelGrid>

</h:panelGrid>
    <h:panelGrid columns="2">
       
        <h:panelGrid columns="1">
           <h:outputLabel value="Observação : " for="" />   
           <h:inputTextarea value="#{manterPaciente.paciente.observacao}" style="width : 480px; height : 96px;" />
        </h:panelGrid>
    </h:panelGrid>
</center>
</rich:panel></center>