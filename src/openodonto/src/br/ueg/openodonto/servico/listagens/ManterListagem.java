package br.ueg.openodonto.servico.listagens;

import java.util.HashMap;
import java.util.Map;

import br.ueg.openodonto.servico.listagens.core.AbstractLista;
import br.ueg.openodonto.servico.listagens.core.ListaDominio;
import br.ueg.openodonto.servico.listagens.core.ListaTipo;


public class ManterListagem{
	
	private static Map<Class<?>, AbstractLista<?>> cache;	
	
	@SuppressWarnings("serial")
	public ManterListagem(){
		cache = new HashMap<Class<?>, AbstractLista<?>>(){
			@Override
			public AbstractLista<?> get(Object arg) {
				if(arg instanceof String)
					return getLista((String)arg);
				else
					return super.get(arg);				
			}

		};
	}
	
	private static <T> AbstractLista<T> getListaTipo(Class<T> classe){
		return new ListaTipo<T>(classe);
	}
	
	private static <T> AbstractLista<T> getListaDominio(Class<T> classe){		
		return new ListaDominio<T>(classe);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> AbstractLista<T> getLista(Class<T> classe){
		AbstractLista<T> lista = null;
		if(cache.get(classe) == null){
			if(classe.isEnum())
				lista = getListaTipo(classe);
			else
				lista = getListaDominio(classe);
			cache.put(classe, lista);
		}else
			lista = (AbstractLista<T>)cache.get(classe);
		if(lista.isOld())
			lista.refreshDominio(null);
		return lista;
	}
	
	
	public static AbstractLista<?> getLista(String className){
		Class<?> classe = resolveClass(className);
		return getLista(classe);
	}
	
	private static Class<?> resolveClass(String className){
		Class<?> classe;
		try {
			classe = Class.forName(className);
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
			throw new RuntimeException("Classe não encontrada",e);
		}
		return classe;
	}	
	
	public Map<Class<?>, AbstractLista<?>> getCache() {	
		return cache;
	}	

}
