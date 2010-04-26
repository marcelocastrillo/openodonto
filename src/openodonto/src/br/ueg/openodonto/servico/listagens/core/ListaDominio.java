package br.ueg.openodonto.servico.listagens.core;

import java.util.ArrayList;
import java.util.List;

import br.ueg.openodonto.persistencia.EntityManagerIF;


/**
 * @author vinicius.rodrigues
 *
 * @param <T>
 */
public class ListaDominio<T> extends AbstractLista<T>{
	
	public ListaDominio(Class<T> classe){
		super(classe);
	}
	
	@SuppressWarnings("null")
	public List<T> getDominio(){
		EntityManagerIF<T> daoDominio = null; //TODO definir implementação new DaoIMPL<T>(classe);
		List<T> lista = new ArrayList<T>();
		try {
			lista = daoDominio.listar();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return lista;
	}

	
}
