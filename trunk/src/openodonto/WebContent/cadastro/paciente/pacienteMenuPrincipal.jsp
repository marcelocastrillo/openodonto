<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>
<%@ taglib prefix="t"  uri="http://myfaces.apache.org/tomahawk"%>


<center>
<rich:panel style="background-color : white;width : 90%;text-align: left">
<center>
<h:panelGrid id="gridPrincipal">

		<h:panelGrid columns="3">
			<h:panelGrid columns="1">
				<h:outputLabel value="Estado" for="selectMenuEstado" />
				<h:selectOneMenu immediate="true" id="selectMenuEstado" value="#{manterPaciente.paciente.estado}" converter="simpleEntityConverter" style="width : 200px">
					<f:selectItem itemLabel="-- Selecione --" itemValue="" />
					<t:selectItems id="itemsEstado" value="#{manterListagem.cache['br.ueg.openodonto.dominio.constante.TiposUF'].dominio}" var="estado" itemLabel="#{estado.descricao}" itemValue="#{estado}" />
				</h:selectOneMenu>
			</h:panelGrid>
			<h:panelGrid columns="1">
				<h:outputLabel value="Cidade" for="entradaCidade" />
				<h:inputText id="entradaCidade"	value="#{manterPaciente.paciente.cidade}" style="width : 180px"/>
			</h:panelGrid>
			<h:panelGrid columns="1">
				<h:outputLabel value="Endereco" for="entradaEndereco" />
				<h:inputText id="entradaEndereco" value="#{manterPaciente.paciente.endereco}"
					style=" width : 300px;" />
			</h:panelGrid>


	<h:panelGrid columns="1">
		<h:outputLabel value="Responsável" for="entradaResponavel" />
		<h:inputText id="entradaResponavel"	value="#{manterPaciente.paciente.responsavel}"  style=" width : 200px;"/>
	</h:panelGrid>
	
	<h:panelGrid columns="1">
		<h:outputLabel value="E-mail" for="entradaEmail" />
		<h:inputText id="entradaEmail" value="#{manterPaciente.paciente.email}" style=" width : 180px;" />
	</h:panelGrid>
	
	<h:panelGrid columns="1">
		<h:outputLabel value="Referencia ( Quem indicou )" for="entradaReferencia" />
		<h:inputText id="entradaReferencia" value="#{manterPaciente.paciente.referencia}" style="width : 300px;"/>
	</h:panelGrid>



				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="Data Termino Tratamento" for="dataInicioTratamento" />
	                    <rich:spacer width="8" />
	                    <rich:message id="messageDataTerminoTratamento" for="dataTerminoTratamento" style="color : red ;FONT-WEIGHT : bold;font-size : 12px" />
	                </h:panelGrid>
					<rich:calendar id="dataTerminoTratamento" datePattern="dd/MM/yyyy" enableManualInput="false" value="#{manterPaciente.paciente.dataTerminoTratamento}" inputStyle="width : 180px;" >
						<f:converter converterId="SqlDateConverter" />
					</rich:calendar>
				</h:panelGrid>

				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="Data Retorno" for="dataInicioTratamento" />
	                    <rich:spacer width="8" />
	                    <rich:message id="messageDataRetorno" for="dataRetorno" style="color : red ;FONT-WEIGHT : bold;font-size : 12px" />
	                </h:panelGrid>
					<rich:calendar id="dataRetorno" datePattern="dd/MM/yyyy" enableManualInput="false" value="#{manterPaciente.paciente.dataRetorno}" inputStyle="width : 160px;" >
						<f:converter converterId="SqlDateConverter" />
					</rich:calendar>
				</h:panelGrid>

				<h:panelGrid columns="1">
				    <h:panelGrid columns="3">				
					    <h:outputLabel value="Data Nascimento" for="dataInicioTratamento" />
	                    <rich:spacer width="8" />
	                    <rich:message id="messageDataNascimento" for="dataNascimento" style="color : red ;FONT-WEIGHT : bold;font-size : 12px" />
	                </h:panelGrid>
					<rich:calendar id="dataNascimento" datePattern="dd/MM/yyyy" enableManualInput="false" value="#{manterPaciente.paciente.dataNascimento}" inputStyle="width : 280px;" >
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