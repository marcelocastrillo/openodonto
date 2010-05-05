package br.ueg.openodonto.persistencia;

import java.util.List;
import java.util.Map;

/**
 * @author vinicius.rodrigues
 *
 * @param <T>
 */
public interface EntityManagerIF<T> {

	void inserir(T o) throws Exception;

	void alterar(T o) throws Exception;

	List<T> executarQuery(String nomeQuery, String nomeParametrro,
			Object valorParametro) throws Exception;

	List<T> executarQuery(String nomeQuery, String nomeParametrro,
			Object valorParametro, Integer quant) throws Exception;

	public List<T> executarQuery(String nomeQuery, Map<String, Object> params);

	public List<T> executarQuery(String nomeQuery, Map<String, Object> params,
			Integer quant);

	void remover(T o) throws Exception;

	List<T> listar(Object o) throws Exception;
	
	List<T> listar();
	
	T pesquisar(Object key);
	
	T getEntityBean();
	
	boolean contem(T entity);
	
	void closeConnection();

}
