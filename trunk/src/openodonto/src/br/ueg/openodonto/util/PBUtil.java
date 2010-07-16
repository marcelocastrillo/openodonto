package br.ueg.openodonto.util;

import org.apache.commons.beanutils.PropertyUtilsBean;

public class PBUtil {

	public static PropertyUtilsBean propertyUtilsBean;
	
	static{
	    propertyUtilsBean = new PropertyUtilsBean();
	}
	
	public static PropertyUtilsBean instance(){
		return propertyUtilsBean;
	}
	
}
