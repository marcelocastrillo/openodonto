package br.ueg.openodonto.servico.listagens.core;

import java.util.ArrayList;
import java.util.List;

import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.persistencia.orm.Entity;


/**
 * @author vinicius.rodrigues
 *
 * @param <T>
 */
public class ListaDominioFiltrada<T extends Entity> extends AbstractLista<T>{

	private List<Object> params;
	private String query;
	
	public ListaDominioFiltrada(Class<T> classe , String query , List<Object> params){
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
		try{
		    return daoDominio.getSqlExecutor().executarNamedQuery(query, params, "*");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}
	
}
