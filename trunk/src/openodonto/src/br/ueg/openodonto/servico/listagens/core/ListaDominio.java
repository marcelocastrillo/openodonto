package br.ueg.openodonto.servico.listagens.core;

import java.util.ArrayList;
import java.util.List;

import br.ueg.openodonto.persistencia.EntityManagerIF;
import br.ueg.openodonto.persistencia.dao.DaoFactory;


/**
 * @author vinicius.rodrigues
 *
 * @param <T>
 */
public class ListaDominio<T> extends AbstractLista<T>{
	
	public ListaDominio(Class<T> classe){
		super(classe);
	}
	
	public List<T> getDominio(){
		EntityManagerIF<T> daoDominio = DaoFactory.getInstance().getDao(getClasse());
		List<T> lista = new ArrayList<T>();
		try {
			lista = daoDominio.listar();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return lista;
	}

	
}
