package br.ueg.openodonto.servico.listagens.core.codec;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author vinicius.rodrigues
 *
 */
public class PropriedadeRefletida {
	
	public static enum Way{
		GET,SET
	}
	
	public static <T> Map<Way,Method> getEngine(Class<T> classe ,String atributo){
		Map<Way,Method> mapEngine = new LinkedHashMap<Way, Method>();
		mapEngine.put(Way.GET, pojoGetterMaker(classe, atributo));
		mapEngine.put(Way.SET, pojoSetterMaker(classe, atributo));
		return mapEngine;		
	}
	
	public static Method pojoGetterMaker(Class<?> classe , String name){
		Method get = null;
		String methodName = resolveMethodName(Way.GET, name);
		try {
			get = classe.getMethod(methodName, new Class<?>[0]);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return get;
	}
	
	public static Method pojoSetterMaker(Class<?> classe , String name){
		Method set = null;
		String methodName = resolveMethodName(Way.SET, name);
		try {
			set = classe.getMethod(methodName, pojoGetterMaker(classe, name).getReturnType());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return set;
	}
	
	public static String resolveMethodName(Way modo,String atributo){
		String nomeMetodo = new String();
		if(modo == Way.GET)
			nomeMetodo += "get";
		else if(modo == Way.SET)
			nomeMetodo += "set";
		nomeMetodo += getRadical(atributo);
		return nomeMetodo;
	}
	
	private static String getRadical(String atributo){
		if(atributo.length() <= 0)
			return "";
		char firstUpper = Character.toUpperCase(atributo.charAt(0));
		return firstUpper + atributo.substring(1);
	}
		
	

}
