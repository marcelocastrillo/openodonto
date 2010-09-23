package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchablePaciente;
import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.validator.ValidatorFactory;

public class ManterPaciente extends ManageBeanGeral<Paciente> {
	
	private static final long serialVersionUID = -2146469226044009908L;
	
	private ManageTelefone                manageTelefone;
	private static Map<String, String>    params;
	private Search<Paciente>              search;
	private MessageDisplayer              displayer;
	
	static{
		params = new HashMap<String, String>();
		params.put("managebeanName", "manterPaciente");
		params.put("formularioSaida", "formPaciente");
		params.put("formModalSearch", "formSearch");
		params.put("nameModalSearch", "painelBusca");
		params.put("saidaPadrao", "formPaciente:output");
	}

	public ManterPaciente() {
		super(Paciente.class);
	}

	protected void initExtra() {
		this.displayer = new ViewDisplayer("searchDefaultOutput");
		this.manageTelefone = new ManageTelefone(getPaciente().getTelefone(), this);
		this.search = new SearchBase<Paciente>(new SearchablePaciente(this.displayer),"Buscar Paciente");
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

	protected List<ValidationRequest> getCamposObrigatorios() {
		List<ValidationRequest> obrigatorios = new ArrayList<ValidationRequest>();
		obrigatorios.add((new ValidationRequest("nome", ValidatorFactory.newStrRangeLen(100, 5,true),"formPaciente:entradaNome")));
		obrigatorios.add(new ValidationRequest("cpf",ValidatorFactory.newCpf(),"formPaciente:entradaCpf"));
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

	protected class SearchPacienteHandler extends SearchBeanHandler<Paciente>{
		private String[] showColumns = {"codigo", "nome", "email", "cpf"};
		@Override
		public String[] getShowColumns() {
			return showColumns;
		}
	}
	
}
