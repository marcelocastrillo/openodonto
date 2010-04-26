package br.ueg.openodonto.servico.listagens.core.codec;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import br.ueg.openodonto.servico.listagens.ManterListagem;
import br.ueg.openodonto.servico.listagens.core.AbstractLista;


public class Encoder {

	public static <T> List<Long> encode(Class<T> classe,List<T> values){
		if(values == null || classe == null)
			return new ArrayList<Long>();
		List<Long> lista =  new ArrayList<Long>();
		for(T value : values)
			lista.add(encode(classe,value));
		return lista;
	}
	
	public static <T> Long encode(Class<T> classe,T value){
		if(value == null || classe == null)
			return 0L;
		if(classe.isEnum())
			return (long)((Enum<?>)value).ordinal();
		else
			try {
				Method metodo =  classe.getMethod("getCodigo", new Class<?>[0]);
				return (Long)metodo.invoke(value, new Object[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
			
	public static <T> T decode(Class<T> classe,Long key){
		AbstractLista<T> lista = ManterListagem.getLista(classe);;
		if(lista.getDominioMap().size() == 0)
			lista.refreshDominio(null);
		return lista.getDominioMap().get(key);
	}
	
	public static <T> T decode(Class<T> classe,Long key , AbstractLista<T> contraDominio){
		if(contraDominio.getDominioMap().size() == 0)
			contraDominio.refreshDominio(null);
		return contraDominio.getDominioMap().get(key);
	}
	
	public static <T> List<T> decode(Class<T> classe,List<Long> keys){
		List<T> lista = new ArrayList<T>();
		for(Long key : keys)
			lista.add(decode(classe,key));
		return lista;
	}
	
	public static <T> List<T> decode(Class<T> classe,List<Long> keys , AbstractLista<T> contraDominio){
		List<T> lista = new ArrayList<T>();
		for(Long key : keys)
			lista.add(decode(classe,key,contraDominio));
		return lista;
	}
	
}
