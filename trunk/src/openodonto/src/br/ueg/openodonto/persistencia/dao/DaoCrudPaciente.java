package br.ueg.openodonto.persistencia.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.constante.TiposUF;
import br.ueg.openodonto.persistencia.EntityManagerIF;

@SuppressWarnings("serial")
public class DaoCrudPaciente extends BaseDAO<Paciente> implements EntityManagerIF<Paciente> {

	private static Map<String , String>     storedQuerysMap;
	
	private Map<Paciente, Paciente> cachedSession;
	
	public DaoCrudPaciente() {
		cachedSession = new HashMap<Paciente, Paciente>();
	}
	
	static{
		storedQuerysMap = new HashMap<String, String>();
		initQueryMap();
	}
	
	private static void initQueryMap(){
		storedQuerysMap.put("findByKey","SELECT * FROM pacientes pc LEFT JOIN pessoas ps ON pc.id_pessoa = ps.id AND ps.`DTYPE` = 'PACIENTES' WHERE ps.`id` = ?;");
		storedQuerysMap.put("listAll","SELECT * FROM pacientes pc LEFT JOIN pessoas ps ON pc.id_pessoa = ps.id AND ps.`DTYPE` = 'PACIENTES'");
	}
	
	@Override
	protected String getTableName() {
		return "pacientes";
	}
	
	private Paciente parseEntry(ResultSet rs) throws SQLException{
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
		format.put("estado", TiposUF.format(paciente.getEstado()));
		format.put("nome", paciente.getNome());
		format.put("observacao", paciente.getObservacao());
		format.put("referencia", paciente.getReferencia());
		format.put("responsavel", paciente.getResponsavel());
		return format;
	}
	
	@Override
	public void alterar(Paciente o) throws Exception {
		Map<String , Object> params = new LinkedHashMap<String, Object>();
		if(contem(o)){
			Paciente cached = this.cachedSession.get(o);
			params.put("id", cached.getCodigo());
			super.executeUpdate(o, params);
		}else{
			inserir(o);
		}
	}

	@Override
	public boolean contem(Paciente entity) {
		return this.cachedSession.get(entity) != null;
	}

	@Override
	public List<Paciente> executarQuery(String nomeQuery,
			String nomeParametrro, Object valorParametro) throws Exception {
		return null;
	}

	@Override
	public List<Paciente> executarQuery(String nomeQuery,
			String nomeParametrro, Object valorParametro, Integer quant)
			throws Exception {
		return null;
	}

	@Override
	public List<Paciente> executarQuery(String nomeQuery,
			Map<String, Object> params) {
		return null;
	}

	@Override
	public List<Paciente> executarQuery(String nomeQuery,
			Map<String, Object> params, Integer quant) {
		return null;
	}

	@Override
	public Paciente getEntityBean() {
		return null;
	}

	@Override
	public void inserir(Paciente o) throws Exception {
		
	}

	@Override
	public List<Paciente> listar(Object key) throws Exception {		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Paciente> listar() {
		List<Paciente> pList = new ArrayList<Paciente>();
		try{
			ResultSet rs = super.executeQuery(
					DaoCrudPaciente.storedQuerysMap.get("findByKey"),
					Collections.EMPTY_LIST);
			while(rs.next()) {
				Paciente paciente = this.parseEntry(rs);
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
				List<Object> params = new ArrayList<Object>();
				params.add(key);
				ResultSet rs = super.executeQuery(
						DaoCrudPaciente.storedQuerysMap.get("findByKey"),
						params);
				if (rs.next()) {
					paciente = this.parseEntry(rs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paciente;
	}

	@Override
	public void remover(Paciente o) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
