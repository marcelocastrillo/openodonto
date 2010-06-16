package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.dao.sql.QueryExecutor;
import br.ueg.openodonto.persistencia.dao.sql.SqlExecutor;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@SuppressWarnings("serial")
public class DaoCrudPaciente extends BaseDAO<Paciente>{

	private SqlExecutor<Paciente>     sqlExecutor;
	
	static{		
		initQueryMap();
	}

	public static void initQueryMap(){
		BaseDAO.getStoredQuerysMap().put("Paciente.BuscaByNome","WHERE nome LIKE ?");
		BaseDAO.getStoredQuerysMap().put("Paciente.BuscaByCPF","WHERE cpf = ?");
		BaseDAO.getStoredQuerysMap().put("Paciente.BuscaByEmail","WHERE email = ?");
	}

	public DaoCrudPaciente() {
		super(Paciente.class);
		sqlExecutor = new QueryExecutor<Paciente>(this);
	}
	
	@Override
    public Paciente getNewEntity(){
    	return new Paciente();
    }
	
	private void updateRelationshipTelefone(Paciente o) throws Exception{
		if(o.getTelefone() != null){
			EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
			List<Telefone> todos = getTelefonesFromPaciente(o.getCodigo());
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

	@Override
	public void alterar(Paciente o) throws Exception {
		if(o != null && o.getCodigo() != null &&  pesquisar(o.getCodigo()) != null){
			Savepoint save = null;
			try{
				if(o == null){
					return;
				}
				getConnection().setAutoCommit(false);
				save = getConnection().setSavepoint("Before Update Paciente - Savepoint");
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

	private List<Telefone> getTelefonesFromPaciente(Long id) throws SQLException{		
		EntityManager<Telefone> emTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		OrmFormat orm = new OrmFormat(new Telefone(id));
		IQuery query = CrudQuery.getSelectQuery(Telefone.class, orm.formatNotNull() , "*");		
		return emTelefone.getSqlExecutor().executarQuery(query);
	}


	@Override
	public void inserir(Paciente o) throws Exception {
		Savepoint save = null;
		try{
			if(o == null){
				return;
			}
			getConnection().setAutoCommit(false);
			save = getConnection().setSavepoint("Before Insert Paciente - Savepoint");
			insert(o);
			updateRelationshipTelefone(o);
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
	public List<Paciente> listar() {
		try{
			List<Paciente> lista = listar("*");
			for(Paciente paciente : lista){
				paciente.setTelefone(getTelefonesFromPaciente(paciente.getCodigo()));
			}
			return lista;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Paciente pesquisar(Object key) {
		if(key == null){
			return null;
		}
		List<Paciente> lista;
		try {
			Long id = Long.parseLong(String.valueOf(key));
			OrmFormat orm = new OrmFormat(new Paciente(id));
			IQuery query = CrudQuery.getSelectQuery(Paciente.class, orm.formatNotNull(), "*");
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
	public void remover(Paciente o) throws Exception {
		Savepoint save = null;
		try{
			getConnection().setAutoCommit(false);
			save = getConnection().setSavepoint("Before Remove Paciente - Savepoint");
			OrmFormat orm = new OrmFormat(o);
			Map<String , Object> params = orm.formatKey();
			List<String> referencias = referencedConstraint(Pessoa.class, params);
			if(referencias.contains(CrudQuery.getTableName(Paciente.class)) &&
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
	public SqlExecutor<Paciente> getSqlExecutor() {
		return sqlExecutor;
	}

	@Override
	public void load(Paciente o) throws SQLException {
		if(o == null || o.getCodigo() == null){
			throw new RuntimeException("Paciente Inválido");
		}
		OrmFormat orm = new OrmFormat(o);
		List<Telefone> telefones = getTelefonesFromPaciente(o.getCodigo());
		Paciente loaded = pesquisar(o.getCodigo());
		OrmFormat ormLoaded = new OrmFormat(loaded);
		orm.parse(ormLoaded.format());
		o.setTelefone(telefones);
	}
	
	
}
