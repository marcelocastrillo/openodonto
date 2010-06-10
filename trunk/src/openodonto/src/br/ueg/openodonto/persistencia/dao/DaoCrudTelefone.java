package br.ueg.openodonto.persistencia.dao;

import java.sql.ResultSet;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.QueryExecutor;
import br.ueg.openodonto.persistencia.dao.sql.SqlExecutor;

public class DaoCrudTelefone extends BaseDAO<Telefone> implements EntityManager<Telefone> {

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
		BaseDAO.getStoredQuerysMap().put("findByKey","SELECT * FROM telefones WHERE id = ?");
		BaseDAO.getStoredQuerysMap().put("findByPessoa","SELECT * FROM telefones WHERE id_pessoa = ?");
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
				Map<String , Object> params = new LinkedHashMap<String, Object>();
				params.put("id", o.getCodigo());
				//super.executeUpdate(o, params);
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
				Map<String, Object> generated = null;//super.executeInsert(o);		// TODO Auto-generated method stub
				o.setCodigo((Long) generated.values().iterator().next());
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
		List<Telefone> telefone = new ArrayList<Telefone>();
		try{
			getConnection().setReadOnly(true);
			telefone = super.listar();
			getConnection().setReadOnly(false);
		}catch (Exception e) {
            e.printStackTrace();
		}
		return telefone;
	}

	@Override
	public Telefone pesquisar(Object key) {
		Telefone telefone = null;
		try {
			if (key != null) {
				getConnection().setReadOnly(true);
				List<Object> params = new ArrayList<Object>();
				params.add(key);
				ResultSet rs = super.executeQuery(
						BaseDAO.getStoredQuerysMap().get("findByKey"),
						params);
				getConnection().setReadOnly(false);
				if (rs.next()) {
					telefone = this.parseEntry(rs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return telefone;
	}

	@Override
	public void remover(Telefone o) throws Exception {
		Savepoint save = null;
		try{
			Map<String , Object> params = null;
			if(o != null && o.getCodigo() != null && o.getCodigo() > 0){
				params = new HashMap<String, Object>();
				params.put("id", o.getCodigo());
			}else{
				return;
			}
			getConnection().setAutoCommit(false);
			save = getConnection().setSavepoint("Before Remove Telefone - Savepoint");
			//super.executeRemove(params); 		// TODO Auto-generated method stub
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
		return null;
	}

	@Override
	public SqlExecutor<Telefone> getSqlExecutor() {
		return sqlExecutor;
	}
	

}
