package br.ueg.openodonto.persistencia.dao;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.dao.sql.Query;
import br.ueg.openodonto.persistencia.dao.sql.QueryExecutor;
import br.ueg.openodonto.persistencia.dao.sql.SqlExecutor;
import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.ForwardKey;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.persistencia.orm.OrmResolver;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;

public abstract class DaoCrud<T extends Entity> extends DaoBase<T> {

	private static final long serialVersionUID = 7282308921887713737L;

	private SqlExecutor<T> sqlExecutor;
	protected List<Field> fields;

	public DaoCrud(Class<T> classe) {
		super(classe);
		sqlExecutor = new QueryExecutor<T>(this);
		fields = OrmResolver.getAllFields(new LinkedList<Field>(), getClasse(), true);
	}

	public boolean exists(T o) throws SQLException {
		OrmFormat orm = new OrmFormat(o);
		Map<String, Object> keyMap = orm.formatKey();
		
		if (keyMap == null || keyMap.size() == 0) {
			return false;
		}else{
			Iterator<Map.Entry<String,Object>> iterator = keyMap.entrySet().iterator();
			while(iterator.hasNext()){
				Map.Entry<String,Object> key = iterator.next();
				if(key.getValue() == null){
					return false;
				}else if(key.getValue() instanceof String){
					if(key.getValue().toString().isEmpty()){
						return false;
					}
				}else if(key.getValue() instanceof Number){
					if(key.getValue().toString().equals("0")){
						return false;
					}
				}
			}			
			return true;
		}
		
		
		/*
		IQuery query = CrudQuery.getSelectQuery(super.getClasse(), keyMap);
		return executeQuery(query.getQuery(), query.getParams().toArray()).next();
		*/
		
	}
	
	protected void beforeUpdate(T o) throws Exception {
	}

	@Override
	public void alterar(T o) throws Exception {
		if (o != null && exists(o)) {
			Savepoint save = null;
			try {
				getConnection().setAutoCommit(false);
				save = getConnection().setSavepoint("Before Update Dentista - Savepoint");
				beforeUpdate(o);
				OrmFormat orm = new OrmFormat(o);
				update(o, orm.formatKey());
				afterUpdate(o);
			} catch (Exception ex) {
				ex.printStackTrace();
				if (save != null) {
					getConnection().rollback(save);
				}
				throw ex;
			}finally{
				getConnection().setAutoCommit(true);
			}			
		} else if (o != null) {
			inserir(o);
		}
	}

	protected void afterUpdate(T o) throws Exception {
	}

	protected void beforeInsert(T o) throws Exception {
	}

	@Override
	public void inserir(T o) throws Exception {
		if (o != null && exists(o)) {
			alterar(o);
		} else if (o != null) {
			Savepoint save = null;
			try {
				getConnection().setAutoCommit(false);
				save = getConnection().setSavepoint(
						"Before Insert Dentista - Savepoint");
				beforeInsert(o);
				insert(o);
				aferInsert(o);
			} catch (Exception ex) {
				ex.printStackTrace();
				if (save != null) {
					getConnection().rollback(save);
				}
				throw ex;
			}finally{
				getConnection().setAutoCommit(true);
			}			
		}

	}

	protected void aferInsert(T o) throws Exception {
	}

	@Override
	public SqlExecutor<T> getSqlExecutor() {
		return sqlExecutor;
	}

	protected void beforeList() throws Exception {
	}

	public List<T> listar(boolean lazy, String... fields) {
		List<T> tList = null;
		try {
			getConnection().setReadOnly(true);
			beforeList();
			tList = listar(fields);
			afterList();
			getConnection().setReadOnly(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tList;
	}
	
	@Override
	public List<T> listar() {
		return listar(true, "*");
	}

	protected void afterList() throws Exception {
	}

	protected void beforeLoad(T o) throws Exception {
	}

	public void load(T o) throws Exception {
		if (o == null) {
			throw new RuntimeException("Registro Inválido");
		}
		OrmFormat orm = new OrmFormat(o);
		Map<String, Object> key = orm.formatKey();
		if (key == null || key.size() == 0) {
			throw new RuntimeException("Registro Inválido");
		}
		IQuery query = CrudQuery.getSelectQuery(super.getClasse(), key, "*");
		List<T> lista = getSqlExecutor().executarQuery(query.getQuery(),
				query.getParams());
		if (lista.size() == 1) {
			beforeLoad(o);
			T loaded = lista.get(0);
			OrmFormat ormLoaded = new OrmFormat(loaded);
			orm.parse(ormLoaded.format());
			afterLoad(o);
		}
	}

	protected void afterLoad(T o) throws Exception {
	}

	protected boolean beforeRemove(T o, Map<String, Object> params)	throws Exception {
		return false;
	}

	@Override
	public void remover(T o) throws Exception {
		Savepoint save = null;
		try {
			save = getConnection().setSavepoint(
					"Before Remove Telefone - Savepoint");
			OrmFormat orm = new OrmFormat(o);
			Map<String, Object> params = orm.formatKey();
			boolean tolerance = beforeRemove(o, params);
			remove(params, tolerance);
			afterRemove(o);
		} catch (Exception ex) {
			ex.printStackTrace();
			if (save != null) {
				getConnection().rollback(save);
			}
			throw ex;
		}
		getConnection().setAutoCommit(true);

	}

	protected void afterRemove(T o) throws Exception {
	}

	/**
	 * Faz uma select na classe recebida em 'relacao' com join para classe atual do DAO .
	 * Para filtrar o join ( clausula 'ON') usa a declaracao de foregin key presente no field 'name'.
	 * @param <R> O Tipo de retorno
	 * @param relacao
	 * @param name
	 * @param whereParams
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public <R extends Entity> void getRelacionamento(Class<R> relacao,String name,Map<String,Object> whereParams,Set lista,boolean formated) throws SQLException{
		StringBuilder sql = new StringBuilder(CrudQuery.getSelectRoot(relacao, "*"));
		OrmTranslator translator = new OrmTranslator(fields);
		sql.append(CrudQuery.buildJoin(translator,name,relacao));
		List<Object> params = new ArrayList<Object>();
		CrudQuery.makeWhereOfQuery(whereParams, params, sql);
		EntityManager<R> dao = DaoFactory.getInstance().getDao(relacao);
		IQuery query = new Query(sql.toString(),params,CrudQuery.getTableName(relacao));
		if(formated){
			lista.addAll(dao.getSqlExecutor().executarQuery(query));
		}else{
			lista.addAll(dao.getSqlExecutor().executarUntypedQuery(query));
		}	
	}	
	
	/**
	 * Inicialmente faz uma consulta filtrando pelo exemplo de <E>;
	 * Em seguida pega os resultados da consulta anterior e tenta relacionar cada resultado com <T>.
	 * Usa o nome do field 'fname' para recuperar a foregin key da classe atual com a classe <E>
	 * fornecendo assim a juncao inicial para a filtragem pelo exemplo de 'relacao'.
	 * @param <R>
	 * @param <E>
	 * @param example
	 * @param relacao
	 * @param fname
	 * @param rname
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public <R extends Entity,E extends Entity>void getRelacionamento(E example,R relacao,String fname,String rname,Set lista,boolean formated) throws SQLException{
		OrmTranslator translator = new OrmTranslator(fields);
		OrmTranslator exampleTranslator = new OrmTranslator(OrmResolver.getAllFields(new LinkedList<Field>(), example.getClass(), true));
		//Recupera o nome da coluna referenciada pela FK de 'fname' e traduz para o nome do field associado.
		ForwardKey[] fks = translator.getFieldByFieldName(fname).getAnnotation(Column.class).joinFields();
		String[] crossFkField = new String[fks.length];
		for(int i = 0 ; i < fks.length;i++){
			crossFkField[i] = exampleTranslator.getFieldName(fks[i].tableField());
		}		
		OrmFormat format = new OrmFormat(example);
		OrmFormat formatRelacao = new OrmFormat(relacao);
		IQuery query = CrudQuery.getSelectQuery(example.getClass(), format.formatNotNull(), crossFkField);		
		EntityManager<E> dao = DaoFactory.getInstance().getDao((Class<E>)example.getClass());
		List<Map<String, Object>> results = dao.getSqlExecutor().executarUntypedQuery(query.getQuery(), query.getParams(), 1000);		

		for(Map<String, Object> result : results){
			Map<String, Object> whereParams = new HashMap<String, Object>();
			whereParams.put(translator.getColumn(fname), result.get(exampleTranslator.getColumn(crossFkField[0])));
			whereParams.putAll(formatRelacao.formatNotNull());
			getRelacionamento((Class<R>)relacao.getClass(),rname,whereParams,lista,formated);
		}
	}
	
	public <R extends Entity> List<R> getRelacionamento(Class<R> relacao,String name,Map<String,Object> whereParams) throws SQLException{
		Set<R> lista = new HashSet<R>();
		getRelacionamento(relacao,name,whereParams,lista,true);
		return new ArrayList<R>(lista);

	}
	
	public <R extends Entity,E extends Entity>List<R> getRelacionamento(E example,R relacao,String fname,String rname) throws SQLException{
		Set<R> lista = new HashSet<R>();
		getRelacionamento(example,relacao,fname,rname,lista,true);
		return new ArrayList<R>(lista);
	}
	
	public <R extends Entity> List<Map<String,Object>> getAtypeRelacionamento(Class<R> relacao,String name,Map<String,Object> whereParams) throws SQLException{
		Set<Map<String,Object>> lista = new HashSet<Map<String,Object>>();
		getRelacionamento(relacao,name,whereParams,lista,false);
		return new ArrayList<Map<String,Object>>(lista);

	}
	
	public <R extends Entity,E extends Entity>List<Map<String,Object>> getAtypeRelacionamento(E example,R relacao,String fname,String rname) throws SQLException{
		Set<Map<String,Object>> lista = new HashSet<Map<String,Object>>();
		getRelacionamento(example,relacao,fname,rname,lista,false);
		return new ArrayList<Map<String,Object>>(lista);
	}

	
}
