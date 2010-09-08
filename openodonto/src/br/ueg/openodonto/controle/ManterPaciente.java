package br.ueg.openodonto.controle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.validador.AbstractValidator;
import br.ueg.openodonto.controle.validador.ValidadorPadrao;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.event.AbstractSearchListener;
import br.ueg.openodonto.servico.busca.event.SearchEvent;

public class ManterPaciente extends ManageBeanGeral<Paciente> {
	
	private static final long serialVersionUID = -2146469226044009908L;
	
	private ManageTelefone                manageTelefone;
	private static Map<String, String>    params;
	private Search<Paciente>              search;
	
	static{
		params = new HashMap<String, String>();
		params.put("managebeanName", "manterPaciente");
		params.put("formularioSaida", "formPaciente");
		params.put("formModalSearch", "formSearch");
		params.put("nameModalSearch", "painelBusca");
		params.put("saidaPadrao", "formPaciente:output");
		params.put("saidaContato", "messageTelefone");
	}

	public ManterPaciente() {
		super(Paciente.class);
	}

	protected void initExtra() {
		this.manageTelefone = new ManageTelefone(getPaciente().getTelefone(), this);
		this.search = new SearchBase<Paciente>(new SearchablePaciente("painelBusca:busca"),"Buscar Paciente");
		this.search.addSearchListener(new SearchPacienteHandler());
		this.search.addSearchListener(new SearchSelectedHandler());
		makeView(params);
	}

	protected void carregarExtra() {
		manageTelefone.setTelefones(getPaciente().getTelefone());
	}

	protected List<String> getCamposFormatados() {
		List<String> formatados = new ArrayList<String>();
		formatados.add("nome");
		formatados.add("cpf");
		formatados.add("cidade");
		formatados.add("endereco");
		formatados.add("responsavel");
		formatados.add("referencia");
		return formatados;
	}

	protected List<AbstractValidator> getCamposObrigatorios() {
		List<AbstractValidator> obrigatorios = new ArrayList<AbstractValidator>();
		obrigatorios.add(new ValidadorPadrao("nome", "formPaciente:entradaNome"));
		obrigatorios.add(new ValidadorPadrao("cpf", "formPaciente:entradaCpf"));
		return obrigatorios;
	}
	
	public Search<Paciente> getSearch() {
		return search;
	}

	public Paciente getPaciente() {
		return getBackBean();
	}

	public void setPaciente(Paciente paciente) {
		setBackBean(paciente);
	}

	public ManageTelefone getManageTelefone() {
		return this.manageTelefone;
	}

	public void setManageTelefone(ManageTelefone manageTelefone) {
		this.manageTelefone = manageTelefone;
	}

	protected class SearchPacienteHandler extends AbstractSearchListener{
		@Override
		public void searchPerfomed(SearchEvent event) {
			long time = System.currentTimeMillis();
			IQuery query = CrudQuery.getListQuery(Paciente.class, "codigo", "nome", "email", "cpf");
			try {
				List<Map<String,Object>> result = dao.getSqlExecutor().executarUntypedQuery(query.getQuery(), query.getParams(), 1000);
				search.getResults().clear();
				search.getResults().addAll(wrapResult(result));
				time = System.currentTimeMillis() - time;
				System.out.println(time);
				showTimeQuery(params.get("formModalSearch"), result.size(), time);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	
}
