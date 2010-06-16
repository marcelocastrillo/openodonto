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
				<h:selectOneMenu immediate="true" id="selectMenuEstado" value="#{manterDentista.dentista.estado}" converter="simpleEntityConverter" style="width : 200px">
					<f:selectItem itemLabel="-- Selecione --" itemValue="" />
					<t:selectItems id="itemsEstado" value="#{manterListagem.cache['br.ueg.openodonto.dominio.constante.TiposUF'].dominio}" var="estado" itemLabel="#{estado.descricao}" itemValue="#{estado}" />
				</h:selectOneMenu>
			</h:panelGrid>
			<h:panelGrid columns="1">
				<h:outputLabel value="Cidade" for="entradaCidade" />
				<h:inputText id="entradaCidade"	value="#{manterDentista.dentista.cidade}" style="width : 180px"/>
			</h:panelGrid>
			<h:panelGrid columns="1">
				<h:outputLabel value="Endereco" for="entradaEndereco" />
				<h:inputText id="entradaEndereco" value="#{manterDentista.dentista.endereco}"
					style=" width : 300px;" />
			</h:panelGrid>



	<h:panelGrid columns="1">
		<h:outputLabel value="E-mail" for="entradaEmail" />
		<h:inputText id="entradaEmail" value="#{manterDentista.dentista.email}" style=" width : 180px;" />
	</h:panelGrid>
				
</h:panelGrid>

</h:panelGrid>
    <h:panelGrid columns="2">
       
        <h:panelGrid columns="1">
           <h:outputLabel value="Observação : " for="" />   
           <h:inputTextarea value="#{manterDentista.dentista.observacao}" style="width : 480px; height : 96px;" />
        </h:panelGrid>
    </h:panelGrid>
</center>
</rich:panel></center>