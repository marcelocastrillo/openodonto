package br.ueg.openodonto.controle.servico;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.servico.listagens.core.AbstractLista;
import br.ueg.openodonto.servico.listagens.core.ListaDominio;
import br.ueg.openodonto.servico.listagens.core.ListaTipo;

public class ManageListagem implements Serializable {

	private static final long serialVersionUID = 7484584628436233933L;
	private static Map<Class<?>, AbstractLista<?>> cache;
	private static Map<String,String>              aliasMap;

	public static final String[][] ALIAS = {
	    {"ALIAS_DENTE" , "br.ueg.openodonto.dominio.constante.Dente"},
	    {"ALIAS_FACE" , "br.ueg.openodonto.dominio.constante.Face"},
	    {"ALIAS_UF" , "br.ueg.openodonto.dominio.constante.TiposUF"},
	    {"ALIAS_TIPO_TEL","br.ueg.openodonto.dominio.constante.TiposTelefone"},
	    {"ALIAS_PROC","br.ueg.openodonto.dominio.Procedimento"},
	    {"ALIAS_STATUS_PROC","br.ueg.openodonto.dominio.constante.TipoStatusProcedimento"}};
	
	static{
	    aliasMap = new HashMap<String, String>();
		cache = new HashMap<Class<?>, AbstractLista<?>>() {
			private static final long serialVersionUID = 4625686751396609699L;

			@Override
			public AbstractLista<?> get(Object arg) {
				if (arg instanceof String)
					return getLista((String) arg);
				else
					return super.get(arg);
			}

		};
		configureAlias();
	}	

	private static void configureAlias(){
	    for(String[] alias : ALIAS){
	        aliasMap.put(alias[0], alias[1]);
	    }
	}
	
	private static <T> AbstractLista<T> getListaTipo(Class<T> classe) {
		return new ListaTipo<T>(classe);
	}

	private static <T extends Entity> AbstractLista<T> getListaDominio(Class<T> classe) {
		return new ListaDominio<T>(classe);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Entity> AbstractLista<T> getLista(Class<T> classe) {
		AbstractLista<T> lista = null;
		if (cache.get(classe) == null) {
			if (classe.isEnum())
				lista = getListaTipo(classe);
			else
				lista = getListaDominio(classe);
			cache.put(classe, lista);
		} else {
			lista = (AbstractLista<T>) cache.get(classe);
		}
		return lista;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Entity> AbstractLista<?> getLista(String className) {
		Class<T> classe = (Class<T>) resolveClass(className);
		return getLista(classe);
	}

	private static Class<?> resolveClass(String className) {
		Class<?> classe;
		try {
			classe = Class.forName(className);
		} catch (ClassNotFoundException e) {			
			if(className.startsWith("ALIAS") && aliasMap.containsKey(className)){
			    try {
                    classe = Class.forName(aliasMap.get(className));
                } catch (ClassNotFoundException e1) {
                    throw new RuntimeException("Classe não encontrada", e);
                }
			}else{
			    throw new RuntimeException("Classe não encontrada", e);
			}
		}
		return classe;
	}

	public Map<Class<?>, AbstractLista<?>> getCache() {
		return cache;
	}

}
