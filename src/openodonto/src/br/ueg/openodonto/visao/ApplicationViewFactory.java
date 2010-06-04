package br.ueg.openodonto.visao;

import java.util.Properties;


public class ApplicationViewFactory {

	public enum ViewHandler{
		JSF,SWING;
	}
	
	public static ApplicationView getViewInstance(ViewHandler handler,Properties params){
		switch(handler){
		case JSF:
			return new JSFView(params);
		case SWING:
			return null;
		default:
			return null;
		}
	}
	
}
