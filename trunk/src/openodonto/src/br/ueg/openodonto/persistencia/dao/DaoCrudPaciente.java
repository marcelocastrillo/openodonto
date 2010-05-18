package br.ueg.openodonto.persistencia.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.dominio.constante.TiposUF;
import br.ueg.openodonto.persistencia.EntityManagerIF;
import br.ueg.openodonto.util.Memento;

@SuppressWarnings("serial")
public class DaoCrudPaciente extends BaseDAO<Paciente> implements EntityManagerIF<Paciente> {

	private static Map<String , String>      storedQuerysMap;
	
	private static Map<Paciente, Paciente>   cachedSession;
	
	private Paciente                         managed;

	static{
		storedQuerysMap = new HashMap<String, String>();
		cachedSession = new HashMap<Paciente, Paciente>();
		initQueryMap();
	}
	
	private static void initQueryMap(){
		storedQuerysMap.put("findByKey","SELECT * FROM pacientes pc LEFT JOIN pessoas ps ON pc.id_pessoa = ps.id WHERE ps.`id` = ?");
		storedQuerysMap.put("removePessoa","DELETE FROM pessoas WHERE id = ?");
		storedQuerysMap.put("removePaciente","DELETE FROM pacientes WHERE id_pessoa =  ?");
		storedQuerysMap.put("insertPessoa","INSERT INTO pessoas (email, nome, endereco , estado, cidade) VALUES (? , ?, ?, ? , ? )");
		storedQuerysMap.put("insertPaciente","INSERT INTO pacientes (id_pessoa,cpf ,data_inicio_tratamento ,data_termino_tratamento,data_retorno, data_nascimento, responsavel, referencia, observacao ) VALUES (? , ?, ?, ?, ?, ?, ?, ? , ?)");
		storedQuerysMap.put("updatePessoa","UPDATE pessoas SET email = ? , nome = ? , endereco = ? , estado = ? , cidade = ? WHERE id = ?");
		storedQuerysMap.put("updatePaciente","UPDATE pacientes SET cpf = ? ,data_inicio_tratamento = ? ,data_termino_tratamento = ?,data_retorno = ?, data_nascimento = ?, responsavel = ?, referencia = ?, observacao = ? WHERE id_pessoa = ?");
		storedQuerysMap.put("listAll","SELECT * FROM pacientes pc LEFT JOIN pessoas ps ON pc.id_pessoa = ps.id");
		
		storedQuerysMap.put("Paciente.BuscaByNome","SELECT * FROM pacientes pc LEFT JOIN pessoas ps ON pc.id_pessoa = ps.id WHERE ps.nome LIKE ?");
		storedQuerysMap.put("Paciente.BuscaByCodigo","SELECT * FROM pacientes pc LEFT JOIN pessoas ps ON pc.id_pessoa = ps.id WHERE ps.id = ?");
		storedQuerysMap.put("Paciente.BuscaByCPF","SELECT * FROM pacientes pc LEFT JOIN pessoas ps ON pc.id_pessoa = ps.id WHERE pc.cpf = ?");
		storedQuerysMap.put("Paciente.BuscaByEmail","SELECT * FROM pacientes pc LEFT JOIN pessoas ps ON pc.id_pessoa = ps.id WHERE ps.email = ?");
	}
	
	@Override
	protected String getTableName() {
		return "pacientes";
	}
	
	protected Paciente parseEntry(ResultSet rs) throws SQLException{
		Paciente paciente = new Paciente();
		paciente.setCidade(rs.getString("cidade"));
		paciente.setCodigo(rs.getLong("id"));
		paciente.setCpf(rs.getString("cpf"));
		paciente.setDataInicioTratamento(rs.getDate("data_inicio_tratamento"));
		paciente.setDataNascimento(rs.getDate("data_nascimento"));
		paciente.setDataRetorno(rs.getDate("data_retorno"));
		paciente.setDataTerminoTratamento(rs.getDate("data_termino_tratamento"));
		paciente.setEmail(rs.getString("email"));
		paciente.setEndereco(rs.getString("endereco"));
		paciente.setEstado(TiposUF.parse(rs.getInt("estado")));
		paciente.setNome(rs.getString("nome"));
		paciente.setObservacao(rs.getString("observacao"));
		paciente.setReferencia(rs.getString("referencia"));
		paciente.setResponsavel(rs.getString("responsavel"));
		return paciente; 
	}
	
	protected Map<String , Object> format(Paciente paciente){
		Map<String, Object> format = new LinkedHashMap<String, Object>();  // TEM QUE SER UM LINKEDHASHMAP pois a ordem importa
		format.put("cidade", paciente.getCidade());
		format.put("id", paciente.getCodigo());
		format.put("cpf", paciente.getCpf());
		format.put("data_inicio_tratamento", paciente.getDataInicioTratamento());
		format.put("data_nascimento", paciente.getDataNascimento());
		format.put("data_retorno", paciente.getDataRetorno());
		format.put("data_termino_tratamento", paciente.getDataTerminoTratamento());
		format.put("email", paciente.getEmail());
		format.put("endereco", paciente.getEndereco());
		format.put("estado",paciente.getEstado() != null ? TiposUF.format(paciente.getEstado()) : null);
		format.put("nome", paciente.getNome());
		format.put("observacao", paciente.getObservacao());
		format.put("referencia", paciente.getReferencia());
		format.put("responsavel", paciente.getResponsavel());
		return format;
	}
	
	@Override
	public void alterar(Paciente o) throws Exception {
		if(o != null && o.getCodigo() != null &&  pesquisar(o.getCodigo()) != null){
			Savepoint save = null;
			try{
				if(o == null){
					managed = null;
					return;
				}
				getConnection().setAutoCommit(false);
				save = getConnection().setSavepoint("Before Update Paciente - Savepoint"); 
				Map<String , Object> paramsMap = format(o);
				Object[] pessoaParams = {paramsMap.get("email"),
						paramsMap.get("nome"),
						paramsMap.get("endereco"),
						paramsMap.get("estado"),
						paramsMap.get("cidade"),
						paramsMap.get("id"),};
				super.execute(DaoCrudPaciente.storedQuerysMap.get("updatePessoa"), pessoaParams);
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
				super.execute(DaoCrudPaciente.storedQuerysMap.get("updatePaciente"), pacienteParams);
				if(o.getTelefone() != null){
					EntityManagerIF<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
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
				managed = o;
				cachedSession.put(managed , Memento.deepClone(managed));
			}catch(Exception ex){
				ex.printStackTrace();
				if(save != null){
					getConnection().rollback(save);
				}
			}finally{
				getConnection().setAutoCommit(true);
			}
		}else if(o != null){
			inserir(o);
		}
	}

	@Override
	public boolean contem(Paciente entity) {
		return entity != null && DaoCrudPaciente.cachedSession.get(entity) != null;
	}

	@Override
	public List<Paciente> executarQuery(String nomeQuery,String nomeParametrro, Object valorParametro) throws Exception {
		return executarQuery(nomeQuery , nomeParametrro , valorParametro , null);
	}

	@Override
	public List<Paciente> executarQuery(String nomeQuery, String nomeParametrro, Object valorParametro, Integer quant)throws Exception {
		Map<String , Object> params = new LinkedHashMap<String, Object>();
		params.put(nomeParametrro, valorParametro);
		return executarQuery(nomeQuery, params, quant);
	}

	@Override
	public List<Paciente> executarQuery(String nomeQuery, Map<String, Object> params) {
		return executarQuery(nomeQuery, params, null);
	}
	
	private List<Telefone> getTelefonesFromCliente(Long id){
		List<Telefone> telefones = new ArrayList<Telefone>();
		Map<String , Object> paramsTel = new HashMap<String, Object>();
		paramsTel.put("id_pessoa", id);
		EntityManagerIF<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		telefones.addAll(entityManagerTelefone.executarQuery("findByPessoa", paramsTel));
		return telefones;
	}

	@Override
	public List<Paciente> executarQuery(String nomeQuery,Map<String, Object> params, Integer quant) {
		List<Paciente> pList = new ArrayList<Paciente>();
		if(params == null){
			return pList;
		}
		try{
			getConnection().setReadOnly(true);
			ResultSet rs = super.executeQuery(
					DaoCrudPaciente.storedQuerysMap.get(nomeQuery),
					new ArrayList<Object>(params.values()) , quant);
			getConnection().setReadOnly(false);
			while(rs.next()) {
				Paciente paciente = this.parseEntry(rs);
				paciente.setTelefone(getTelefonesFromCliente(paciente.getCodigo()));
				pList.add(paciente);
			}
			getConnection().setReadOnly(false);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pList;
	}

	@Override
	public Paciente getEntityBean() {
		return cachedSession.get(managed);
	}

	@Override
	public void inserir(Paciente o) throws Exception {
		Savepoint save = null;
		try{
			if(o == null){
				managed = null;
				return;
			}
			getConnection().setAutoCommit(false);
			save = getConnection().setSavepoint("Before Insert Paciente - Savepoint"); 
			Map<String , Object> paramsMap = format(o);
			Object[] pessoaParams = {paramsMap.get("email"),
					paramsMap.get("nome"),
					paramsMap.get("endereco"),
					paramsMap.get("estado"),
					paramsMap.get("cidade")};
			Map<String, Object> generated = super.execute(DaoCrudPaciente.storedQuerysMap.get("insertPessoa"), pessoaParams);
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
			super.execute(DaoCrudPaciente.storedQuerysMap.get("insertPaciente"), pacienteParams);
			if(o.getTelefone() != null){
				EntityManagerIF<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
				for(Telefone telefone : o.getTelefone()){
					telefone.setId_pessoa(o.getCodigo());
					entityManagerTelefone.alterar(telefone);
				}
			}
			managed = o;
			cachedSession.put(managed , Memento.deepClone(managed));
		}catch(Exception ex){
			ex.printStackTrace();
			if(save != null){
				getConnection().rollback(save);
			}
		}finally{
			getConnection().setAutoCommit(true);
		}
	}

	@Override
	public List<Paciente> listar(Object key) throws Exception {		
		List<Paciente> pList = new ArrayList<Paciente>();
		if(key == null){
			return pList;
		}
		try{
			getConnection().setReadOnly(true);
			ResultSet rs = super.executeQuery(
					DaoCrudPaciente.storedQuerysMap.get("findByKey"),
					new Object[]{key});
			getConnection().setReadOnly(false);
			while(rs.next()) {
				Paciente paciente = this.parseEntry(rs);
				paciente.setTelefone(getTelefonesFromCliente(paciente.getCodigo()));
				pList.add(paciente);
			}			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Paciente> listar() {
		List<Paciente> pList = new ArrayList<Paciente>();
		try{
			getConnection().setReadOnly(true);
			ResultSet rs = super.executeQuery(
					DaoCrudPaciente.storedQuerysMap.get("listAll"),
					Collections.EMPTY_LIST);
			getConnection().setReadOnly(false);
			while(rs.next()) {
				Paciente paciente = this.parseEntry(rs);
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
						DaoCrudPaciente.storedQuerysMap.get("findByKey"),
						params);
				getConnection().setReadOnly(false);
				if (rs.next()) {
					paciente = this.parseEntry(rs);
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
			EntityManagerIF<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
			for(Telefone telefone : o.getTelefone()){
				entityManagerTelefone.remover(telefone);
			}		
			super.execute(DaoCrudPaciente.storedQuerysMap.get("removePaciente"), params);
			super.execute(DaoCrudPaciente.storedQuerysMap.get("removePessoa"), params);
		}catch(Exception ex){
			ex.printStackTrace();
			if(save != null){
				getConnection().rollback(save);
			}
		}finally{
			getConnection().setAutoCommit(true);
		}
	}	

}
