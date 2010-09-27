package br.ueg.openodonto.controle.busca;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.servico.busca.ResultFacade;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.servico.busca.event.AbstractSearchListener;
import br.ueg.openodonto.servico.busca.event.SearchEvent;

public abstract class CommoSearchBeanHandler<E extends Entity> extends AbstractSearchListener{
	
	private Class<E>          classe;
	private EntityManager<E>  dao;
	
	public CommoSearchBeanHandler(Class<E> classe,EntityManager<E> dao) {
		this.dao = dao;
		this.classe = classe;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void searchPerformed(SearchEvent event) {
		try {				
			long time = System.currentTimeMillis();
			Search<E> search = (Search<E>)event.getSource();
			List<Map<String,Object>> result = evaluateResult(search);
			addResults(search,result);
			time = System.currentTimeMillis() - time;
			showTimeQuery(search.getName(), result.size(), time);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void addResults(Search<E> search,List<Map<String,Object>> result){
		search.getResults().clear();
		search.getResults().addAll(wrapResult(result));
	}
	
	protected List<ResultFacade> wrapResult(List<Map<String, Object>> result){
		List<ResultFacade> resultWrap = new ArrayList<ResultFacade>(result.size());
		Iterator<Map<String, Object>> iterator = result.iterator();
		while(iterator.hasNext()){
			resultWrap.add(buildWrapBean(iterator.next()));
		}
		return resultWrap;
	}
	
	protected ResultFacade buildWrapBean(Map<String,Object> value){
		return new ResultFacadeBean(value);
	}
	
	public List<Map<String,Object>> evaluateResult(Search<E> search) throws SQLException{
		E target = buildExample(search.getSearchable());
		IQuery query = getQuery(target);				
		List<Map<String,Object>> result = dao.getSqlExecutor().executarUntypedQuery(query);
		return result;
	}
	
	public IQuery getQuery(E example){
		OrmFormat format = new OrmFormat(example);
		return CrudQuery.getSelectQuery(classe, format.formatNotNull(),  getShowColumns());
	}
	
	public E buildExample(Searchable<E> searchable){
		return ((AbstractSearchable<E>)searchable).buildExample();
	}
	
	public EntityManager<E> getDao() {
		return dao;
	}
	
	public Class<E> getClasse() {
		return classe;
	}
	
	public abstract String[] getShowColumns();
	public abstract void showTimeQuery(String name, int size, long time);
}
