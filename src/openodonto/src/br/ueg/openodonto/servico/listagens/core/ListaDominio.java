package br.ueg.openodonto.servico.listagens.core;

import java.util.ArrayList;
import java.util.List;

import br.com.simple.jdbc.Entity;
import br.com.simple.jdbc.EntityManager;
import br.com.simple.jdbc.dao.DaoFactory;

/**
 * @author vinicius.rodrigues
 * 
 * @param <T>
 */
public class ListaDominio<T extends Entity> extends AbstractLista<T> {

	public ListaDominio(Class<T> classe) {
		super(classe);
	}

	public List<T> getRefreshDominio() {
		EntityManager<T> daoDominio = DaoFactory.getInstance().getDao(getClasse());
		List<T> lista = new ArrayList<T>();
		try {
			lista = daoDominio.listar();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	@Override
	public boolean isChangeable() {
		return true;
	}
}
