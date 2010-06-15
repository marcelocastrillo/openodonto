package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.dao.sql.QueryExecutor;
import br.ueg.openodonto.persistencia.dao.sql.SqlExecutor;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

public class DaoCrudTelefone extends BaseDAO<Telefone>{

	private static final long serialVersionUID = -8028356632411640718L;

	private SqlExecutor<Telefone> sqlExecutor;
		
	public DaoCrudTelefone() {
		super(Telefone.class);
		sqlExecutor = new QueryExecutor<Telefone>(this);
	}
	
	static{
		initQuerymap();
	}
	
	private static void initQuerymap(){
		BaseDAO.getStoredQuerysMap().put("findByKey","WHERE id = ?");
		BaseDAO.getStoredQuerysMap().put("findByPessoa","WHERE id_pessoa = ?");
	}
	
	@Override
	public void alterar(Telefone o) throws Exception {		
		if(o != null && o.getCodigo() != null &&  pesquisar(o.getCodigo()) != null){
			Savepoint save = null;
			try{
				if(o == null){
					return;
				}
				getConnection().setAutoCommit(false);
				save = getConnection().setSavepoint("Before Update Telefoe - Savepoint");
				OrmFormat orm = new OrmFormat(o);
				update(o, orm.formatKey());
			}catch(Exception ex){
				ex.printStackTrace();
				if(save != null){
					getConnection().rollback(save);
				}
				throw ex;
			}
			getConnection().setAutoCommit(true);
		}else if(o != null){
			inserir(o);
		}
	}

	@Override
	public void inserir(Telefone o) throws Exception {
		if (o != null && o.getCodigo() != null && pesquisar(o.getCodigo()) != null) {
			alterar(o);
		} else if (o != null) {
			Savepoint save = null;
			try {
				if (o == null) {
					return;
				}
				getConnection().setAutoCommit(false);
				save = getConnection().setSavepoint("Before Insert Telefone - Savepoint");
				insert(o);
			} catch (Exception ex) {
				ex.printStackTrace();
				if (save != null) {
					getConnection().rollback(save);
				}
				throw ex;
			}
			getConnection().setAutoCommit(true);
		}
	}


	@Override
	public List<Telefone> listar() {
		List<Telefone> telefone = null;
		try{
			getConnection().setReadOnly(true);
			telefone = listar("*");
			getConnection().setReadOnly(false);
		}catch (Exception e) {
            e.printStackTrace();
		}
		return telefone;
	}

	@Override
	public Telefone pesquisar(Object key) {
		Long id = Long.parseLong(String.valueOf(key));
		Telefone find = new Telefone();
		find.setCodigo(id);
		OrmFormat orm = new OrmFormat(find);
		IQuery query = CrudQuery.getSelectQuery(Paciente.class, orm.formatNotNull(), "*");
		List<Telefone> lista;
		try {
			lista = getSqlExecutor().executarQuery(query.getQuery(), query.getParams(), 1);
			if(lista.size() == 1){
				return lista.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void remover(Telefone o) throws Exception {
		Savepoint save = null;
		try{
			save = getConnection().setSavepoint("Before Remove Telefone - Savepoint");
			OrmFormat orm = new OrmFormat(o);
			Map<String , Object> params = orm.formatKey();
			remove(params, false);
		}catch(Exception ex){
			ex.printStackTrace();
			if(save != null){
				getConnection().rollback(save);
			}
			throw ex;
		}
		getConnection().setAutoCommit(true);
	}

	@Override
	public Telefone getNewEntity() {
		return new Telefone();
	}

	@Override
	public SqlExecutor<Telefone> getSqlExecutor() {
		return sqlExecutor;
	}
	

}
