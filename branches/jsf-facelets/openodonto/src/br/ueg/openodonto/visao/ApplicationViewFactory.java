package br.ueg.openodonto.visao;

import java.util.Map;

public class ApplicationViewFactory {

    public enum ViewHandler {
	JSF, SWING, TEST;
    }

    public static ApplicationView getViewInstance(ViewHandler handler,
	    Map<String,String> params) {
	switch (handler) {
	case JSF:
	    return new JSFView(params);
	case SWING:
	    return null;
	default:
	    return null;
	}
    }

}
