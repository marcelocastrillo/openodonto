<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="rich" uri="http://richfaces.org/rich"%>
<%@ taglib prefix="a4j" uri="http://richfaces.org/a4j"%>

	<a4j:status>
			<f:facet name="start">
				<f:verbatim>
					<div id="divStatusMessage" style="vertical-align: middle; font-weight: bold; position: fixed; margin-left: -90px; left: 50%; margin-top: -16px; top: 50%; background-image: url('/openodonto/helt/transparent-back-menu.png'); z-index: 5; color: white; height: 32px; width: 180px;">
						<h:panelGrid columns="4">
							<center>
							<h:panelGrid columns="1">
								<h:outputText id="statusMessage" value="Carregando" style="text-align: center; width : 140px;"/>
							</h:panelGrid>
							<rich:spacer width="4px"></rich:spacer>
							<h:panelGrid columns="1">
								<h:graphicImage value="/imagens/loading.gif"  width="24" height="24" style="width : 24px ; height : 24px "/>
								<rich:spacer width="16"></rich:spacer>
							</h:panelGrid>							
							</center>
						</h:panelGrid>
					</div>
					
				</f:verbatim>
			</f:facet>
		</a4j:status>