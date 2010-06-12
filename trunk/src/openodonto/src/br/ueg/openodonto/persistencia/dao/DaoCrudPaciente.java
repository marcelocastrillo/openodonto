package br.ueg.openodonto.persistencia.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.dominio.constante.TiposUF;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.QueryExecutor;
import br.ueg.openodonto.persistencia.dao.sql.SqlExecutor;

@SuppressWarnings("serial")
public class DaoCrudPaciente extends BaseDAO<Paciente> implements EntityManager<Paciente> {

	private SqlExecutor<Paciente>     sqlExecutor;
	
	static{		
		initQueryMap();
	}
	
	public static void main(String[] args) {
		
		/*
		DaoCrudTelefone daoCrudTelefone = new DaoCrudTelefone();
		Telefone telefnoe = new Telefone();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", 1L);
		try {
			daoCrudTelefone.update(telefnoe, params);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		*/
		
		long start = System.currentTimeMillis();
		DaoCrudPaciente dao = new DaoCrudPaciente();
		/*
		Paciente paciente = new Paciente();
		paciente.setCidade("Goiania");
		paciente.setCpf("02549287142");
		paciente.setDataInicioTratamento(new java.sql.Date(System.currentTimeMillis()));
		paciente.setEmail("viiniiciius@gmail.com");
		paciente.setEndereco("Jardin America");
		paciente.setEstado(TiposUF.GO);
		paciente.setNome("Vinicius Gardenio Guimaraes Rodrigues");
		paciente.setTelefone(new ArrayList<Telefone>());
        */
		try {
			System.out.println(dao.listar("*"));
			//dao.insert(paciente);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis() - start);
	}
	
	public static void initQueryMap(){
		BaseDAO.getStoredQuerysMap().put("Paciente.findByKey","WHERE id = ?");
		BaseDAO.getStoredQuerysMap().put("Paciente.removePessoa","DELETE FROM pessoas WHERE id = ?");
		BaseDAO.getStoredQuerysMap().put("Paciente.removePaciente","DELETE FROM pacientes WHERE id_pessoa =  ?");
		BaseDAO.getStoredQuerysMap().put("Paciente.insertPessoa","INSERT INTO pessoas (email, nome, endereco , estado, cidade) VALUES (? , ?, ?, ? , ? )");
		BaseDAO.getStoredQuerysMap().put("Paciente.insertPaciente","INSERT INTO pacientes (id_pessoa,cpf ,data_inicio_tratamento ,data_termino_tratamento,data_retorno, data_nascimento, responsavel, referencia, observacao ) VALUES (? , ?, ?, ?, ?, ?, ?, ? , ?)");
		BaseDAO.getStoredQuerysMap().put("Paciente.listAll","SELECT * FROM pacientes pc LEFT JOIN pessoas ps ON pc.id_pessoa = ps.id");
		
		BaseDAO.getStoredQuerysMap().put("Paciente.BuscaByNome","WHERE ps.nome LIKE ?");
		BaseDAO.getStoredQuerysMap().put("Paciente.BuscaByCodigo","WHERE ps.id = ?");
		BaseDAO.getStoredQuerysMap().put("Paciente.BuscaByCPF","WHERE pc.cpf = ?");
		BaseDAO.getStoredQuerysMap().put("Paciente.BuscaByEmail","WHERE ps.email = ?");
	}

	public DaoCrudPaciente() {
		super(Paciente.class);
		sqlExecutor = new QueryExecutor<Paciente>(this);
	}
	
	@Override
    public Paciente getNewEntity(){
    	return new Paciente();
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
				Map<String , Object> paramsMap = null;//format(o); TODO Arrumar aqui
				Object[] pessoaParams = {paramsMap.get("email"),
						paramsMap.get("nome"),
						paramsMap.get("endereco"),
						paramsMap.get("estado"),
						paramsMap.get("cidade"),
						paramsMap.get("id"),};
				//super.execute(BaseDAO.getStoredQuerysMap().get("updatePessoa"), pessoaParams);
				Object[] pacienteParams = {
						paramsMap.get("cpf"),
						paramsMap.get("data_inicio_tratamento"),
						paramsMap.get("data_termino_tratamento"),
						paramsMap.get("data_retorno"),
						paramsMap.get("data_nascimento"),
						paramsMap.get("responsavel"),
						paramsMap.get("referencia"),
						paramsMap.get("observacao"),
						paramsMap.get("id")};				
				//super.execute(BaseDAO.getStoredQuerysMap().get("updatePaciente"), pacienteParams);
				if(o.getTelefone() != null){
					EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
					List<Telefone> todos = getTelefonesFromCliente(o.getCodigo());
					for(Telefone telefone : todos){
						if(!o.getTelefone().contains(telefone)){
							entityManagerTelefone.remover(telefone);
						}
					}
					for(Telefone telefone : o.getTelefone()){
						telefone.setId_pessoa(o.getCodigo());
						entityManagerTelefone.alterar(telefone);
						getConnection().setAutoCommit(false);
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
				if(save != null){
					getConnection().setAutoCommit(false);
					getConnection().rollback(save);
				}
				throw ex;
			}
			getConnection().setAutoCommit(true);
		}else if(o != null){
			inserir(o);
		}
	}

	private List<Telefone> getTelefonesFromCliente(Long id){
		List<Telefone> telefones = new ArrayList<Telefone>();
		Map<String , Object> paramsTel = new HashMap<String, Object>();
		paramsTel.put("id_pessoa", id);
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		//telefones.addAll(entityManagerTelefone.executarQuery("findByPessoa", paramsTel));TODO Arrumar aqui
		return telefones;
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
			Map<String , Object> paramsMap = null;//format(o);TODO Arrumar aqui
			Object[] pessoaParams = {paramsMap.get("email"),
					paramsMap.get("nome"),
					paramsMap.get("endereco"),
					paramsMap.get("estado"),
					paramsMap.get("cidade")};
			Map<String, Object> generated = null;//super.execute(BaseDAO.getStoredQuerysMap().get("insertPessoa"), pessoaParams);TODO Arrumar aqui
			o.setCodigo((Long)generated.values().iterator().next());
			Object[] pacienteParams = {o.getCodigo(),
					paramsMap.get("cpf"),
					paramsMap.get("data_inicio_tratamento"),
					paramsMap.get("data_termino_tratamento"),
					paramsMap.get("data_retorno"),
					paramsMap.get("data_nascimento"),
					paramsMap.get("responsavel"),
					paramsMap.get("referencia"),
					paramsMap.get("observacao")};			
			//super.execute(BaseDAO.getStoredQuerysMap().get("insertPaciente"), pacienteParams);
			if(o.getTelefone() != null){
				EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
				for(Telefone telefone : o.getTelefone()){
					telefone.setId_pessoa(o.getCodigo());
					entityManagerTelefone.alterar(telefone);
				}
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Paciente> listar() {
		List<Paciente> pList = new ArrayList<Paciente>();
		try{
			getConnection().setReadOnly(true);
			ResultSet rs = super.executeQuery(
					BaseDAO.getStoredQuerysMap().get("listAll"),
					Collections.EMPTY_LIST);
			getConnection().setReadOnly(false);
			while(rs.next()) {
				Paciente paciente = this.parseEntity(rs);
				paciente.setTelefone(getTelefonesFromCliente(paciente.getCodigo()));
				pList.add(paciente);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pList;
	}
	
	@Override
	public Paciente pesquisar(Object key) {
		Paciente paciente = null;
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
					paciente = this.parseEntity(rs);
					paciente.setTelefone(getTelefonesFromCliente(paciente.getCodigo()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paciente;
	}

	@Override
	public void remover(Paciente o) throws Exception {
		Savepoint save = null;
		try{
			Object[] params = null;
			if(o != null && o.getCodigo() != null && o.getCodigo() > 0){
				params = new Object[]{o.getCodigo()};
			}else{
				return;
			}
			getConnection().setAutoCommit(false);
			save = getConnection().setSavepoint("Before Remove Paciente - Savepoint");
			EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
			for(Telefone telefone : o.getTelefone()){
				entityManagerTelefone.remover(telefone);
			}		
			//super.execute(BaseDAO.getStoredQuerysMap().get("removePaciente"), params);
			//super.execute(BaseDAO.getStoredQuerysMap().get("removePessoa"), params);
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

	public void setSqlExecutor(SqlExecutor<Paciente> sqlExecutor) {
		this.sqlExecutor = sqlExecutor;
	}	

	
	
}
