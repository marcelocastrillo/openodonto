package br.ueg.openodonto.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import br.ueg.openodonto.servico.listagens.core.codec.PropriedadeRefletida;
import br.ueg.openodonto.servico.listagens.core.codec.PropriedadeRefletida.Way;


/**
 * @author vinicius.rodrigues
 *
 */
public class ReflexaoUtil {

	
	@SuppressWarnings("unchecked")
	private static <X> X getParent(Object root,String beanPath) {
		X o = (X)root;
		Class<?> classX = o.getClass();
		List<String> parents = Arrays.asList(beanPath.split("[.]"));
		for(int i = 0 ;i < parents.size()-1;i++){
			Method m = PropriedadeRefletida.pojoGetterMaker(classX, parents.get(i));
			try {
				o = (X)m.invoke(o, new Object[0]);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			classX = o.getClass();
		}	
		return o;
	}
	
	public static Method getReflectAcess(Object root , String path , Way way){
		String soon = path.substring(path.lastIndexOf(".")+1);
		if(way.equals(Way.GET))
			return PropriedadeRefletida.pojoGetterMaker(getParent(root , path).getClass(), soon);
		else if(way.equals(Way.SET))
			return PropriedadeRefletida.pojoSetterMaker(getParent(root , path).getClass(), soon);
		else 
			return null;
	}
	
	public static Object getTsimples(Object root,String path){
		Method get = getReflectAcess(root , path , Way.GET);
		Object result = null;
		try {
			result = get.invoke(getParent(root,path), new Object[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void setTsimples(Object root,String path,Object param){
		Method set = getReflectAcess(root , path , Way.SET);
		try {
			set.invoke(getParent(root,path), param);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
}
