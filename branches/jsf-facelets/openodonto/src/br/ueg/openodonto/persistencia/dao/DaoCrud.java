package br.ueg.openodonto.persistencia.dao;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.dao.sql.QueryExecutor;
import br.ueg.openodonto.persistencia.dao.sql.SqlExecutor;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.persistencia.orm.OrmResolver;

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

}
