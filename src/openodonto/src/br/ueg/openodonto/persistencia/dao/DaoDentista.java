package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.dao.sql.QueryExecutor;
import br.ueg.openodonto.persistencia.dao.sql.SqlExecutor;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

public class DaoDentista extends DaoBase<Dentista> {

	private static final long serialVersionUID = 7872660758710684668L;
	
	private SqlExecutor<Dentista> sqlExecutor;
	
	public DaoDentista() {
		super(Dentista.class);
		this.sqlExecutor = new QueryExecutor<Dentista>(this);
	}

	static{
		initQuerymap();
	}
	
	private static void initQuerymap(){
		DaoBase.getStoredQuerysMap().put("Dentista.findByNome","WHERE nome LIKE ?");
		DaoBase.getStoredQuerysMap().put("Dentista.findByCro","WHERE cro = ?");
		DaoBase.getStoredQuerysMap().put("Dentista.findEspecialidade","WHERE especialidade like ?");
	}	

	@Override
	public Dentista getNewEntity() {
		return new Dentista();
	}

	private void updateRelationshipTelefone(Dentista o) throws Exception{
		if(o.getTelefone() != null){
			EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
			List<Telefone> todos = getTelefonesFromDentista(o.getCodigo());
			for(Telefone telefone : todos){
				if(!o.getTelefone().contains(telefone)){
					entityManagerTelefone.remover(telefone);
				}
			}
			for(Telefone telefone : o.getTelefone()){
				telefone.setIdPessoa(o.getCodigo());
				entityManagerTelefone.alterar(telefone);
				getConnection().setAutoCommit(false);
			}
		}
	}
	
	private List<Telefone> getTelefonesFromDentista(Long id) throws SQLException{		
		EntityManager<Telefone> emTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		OrmFormat orm = new OrmFormat(new Telefone(id));
		IQuery query = CrudQuery.getSelectQuery(Telefone.class, orm.formatNotNull() , "*");		
		return emTelefone.getSqlExecutor().executarQuery(query);
	}
	
	@Override
	public void alterar(Dentista o) throws Exception {
		if(o != null && o.getCodigo() != null &&  pesquisar(o.getCodigo()) != null){
			Savepoint save = null;
			try{
				if(o == null){
					return;
				}
				getConnection().setAutoCommit(false);
				save = getConnection().setSavepoint("Before Update Dentista - Savepoint");
				OrmFormat orm = new OrmFormat(o);
				update(o, orm.formatKey());
				updateRelationshipTelefone(o);
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
	public void inserir(Dentista o) throws Exception {
		if (o != null && o.getCodigo() != null && pesquisar(o.getCodigo()) != null) {
			alterar(o);
		} else if (o != null) {
			Savepoint save = null;
			try {
				if (o == null) {
					return;
				}
				getConnection().setAutoCommit(false);
				save = getConnection().setSavepoint("Before Insert Dentista - Savepoint");
				insert(o);
				updateRelationshipTelefone(o);
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
	public List<Dentista> listar() {
		try{
			List<Dentista> lista = listar("*");
			for(Dentista dentista : lista){
				dentista.setTelefone(getTelefonesFromDentista(dentista.getCodigo()));
			}
			return lista;
		}catch (Exception e) {
            e.printStackTrace();
		}
		return null;
	}

	@Override
	public void load(Dentista o) throws Exception {
		if(o == null || o.getCodigo() == null){
			throw new RuntimeException("Dentista Inválido");
		}
		OrmFormat orm = new OrmFormat(o);
		List<Telefone> telefones = getTelefonesFromDentista(o.getCodigo());
		Dentista loaded = pesquisar(o.getCodigo());
		OrmFormat ormLoaded = new OrmFormat(loaded);
		orm.parse(ormLoaded.format());
		o.setTelefone(telefones);
	}

	@Override
	public Dentista pesquisar(Object key) {
		if(key == null){
			return null;
		}
		List<Dentista> lista;
		try {
			Long id = Long.parseLong(String.valueOf(key));
			Dentista find = new Dentista();
			find.setCodigo(id);
			OrmFormat orm = new OrmFormat(find);
			IQuery query = CrudQuery.getSelectQuery(Dentista.class, orm.formatNotNull(), "*");
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
	public void remover(Dentista o) throws Exception {
		Savepoint save = null;
		try{
			save = getConnection().setSavepoint("Before Remove Dentista - Savepoint");
			OrmFormat orm = new OrmFormat(o);
			Map<String , Object> params = orm.formatKey();
			List<String> referencias = referencedConstraint(Pessoa.class, params);
			if(referencias.contains(CrudQuery.getTableName(Dentista.class)) &&
					referencias.contains(CrudQuery.getTableName(Telefone.class)) &&
					referencias.size() == 2){
				EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
				for(Telefone telefone : o.getTelefone()){
					entityManagerTelefone.remover(telefone);
				}
				remove(params, false);
			}else{
				remove(params, true);
			}
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
	public SqlExecutor<Dentista> getSqlExecutor() {
		return sqlExecutor;
	}

	
	
}
