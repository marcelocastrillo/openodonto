package br.ueg.openodonto.controle.servico;

import java.util.HashMap;
import java.util.Map;

import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.servico.listagens.core.AbstractLista;
import br.ueg.openodonto.servico.listagens.core.ListaDominio;
import br.ueg.openodonto.servico.listagens.core.ListaTipo;

public class ManageListagem {

    private static Map<Class<?>, AbstractLista<?>> cache;

    public ManageListagem() {
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
    }

    private static <T> AbstractLista<T> getListaTipo(Class<T> classe) {
	return new ListaTipo<T>(classe);
    }

    private static <T extends Entity> AbstractLista<T> getListaDominio(
	    Class<T> classe) {
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
	if (lista.isOld()) {
	    lista.refreshDominio(null);
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
	    e.printStackTrace();
	    throw new RuntimeException("Classe não encontrada", e);
	}
	return classe;
    }

    public Map<Class<?>, AbstractLista<?>> getCache() {
	return cache;
    }

}
