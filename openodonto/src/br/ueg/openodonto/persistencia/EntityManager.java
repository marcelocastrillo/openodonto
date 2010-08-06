package br.ueg.openodonto.persistencia;

import java.sql.SQLException;
import java.util.List;

import br.ueg.openodonto.persistencia.dao.sql.SqlExecutor;
import br.ueg.openodonto.persistencia.orm.Entity;

/**
 * @author vinicius.rodrigues
 * 
 * @param <T>
 */
public interface EntityManager<T extends Entity> {

    void inserir(T o) throws Exception;

    void alterar(T o) throws Exception;

    void remover(T o) throws Exception;

    void load(T o) throws Exception;

    boolean exists(T o)throws SQLException;    
   
    List<T> listar();
    
    List<T> listar(boolean lazy,String... filds);

    T pesquisar(Object key);    

    SqlExecutor<T> getSqlExecutor();

    void closeConnection();

}
