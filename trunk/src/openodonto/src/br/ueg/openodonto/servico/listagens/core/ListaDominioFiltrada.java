package br.ueg.openodonto.servico.listagens.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.DaoFactory;


/**
 * @author vinicius.rodrigues
 *
 * @param <T>
 */
public class ListaDominioFiltrada<T> extends AbstractLista<T>{

	private Map<String , Object> params;
	private String query;
	
	public ListaDominioFiltrada(Class<T> classe , String query , Map<String,Object> params){
		super(classe);
		this.params = params;
		this.query = query;
	}

	public ListaDominioFiltrada(Class<T> classe){
		this(classe,null,null);
	}
	
	public List<T> getDominio(){
		if(params == null || query == null)
			return new ArrayList<T>();
		EntityManager<T> daoDominio = DaoFactory.getInstance().getDao(getClasse());
		List<T> lista = new ArrayList<T>();
		lista = daoDominio.executarQuery(query, params);
		return lista;
	}

	
	public Map<String, Object> getParams() {	
		return this.params;
	}

	
	public void setParams(Map<String, Object> params) {	
		this.params = params;
	}	

	
}
