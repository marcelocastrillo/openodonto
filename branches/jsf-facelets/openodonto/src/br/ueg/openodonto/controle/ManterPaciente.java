package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchablePaciente;
import br.ueg.openodonto.controle.servico.ExampleRequest;
import br.ueg.openodonto.controle.servico.ManageExample;
import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.validador.AbstractValidator;
import br.ueg.openodonto.controle.validador.ValidadorPadrao;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.validator.EmptyValidator;
import br.ueg.openodonto.validator.NullValidator;

public class ManterPaciente extends ManageBeanGeral<Paciente> {
	
	private static final long serialVersionUID = -2146469226044009908L;
	
	private ManageTelefone                manageTelefone;
	private ManageExample<Paciente>       manageExample;
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
		params.put("saidaContato", "messageTelefone");
	}

	public ManterPaciente() {
		super(Paciente.class);
	}

	protected void initExtra() {
		this.displayer = new ViewDisplayer("searchDefaultOutput");
		this.manageExample = new ManageExample<Paciente>(Paciente.class);
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
	
	private Paciente buildPacienteExample(Searchable<Paciente> searchable){
		ExampleRequest<Paciente> request  = new ExampleRequest<Paciente>(searchable);		
		request.getFilterRelation().add(request.new TypedFilter("nomeFilter", "nome"));
		request.getFilterRelation().add(request.new TypedFilter("idFilter","codigo"));
		request.getFilterRelation().add(request.new TypedFilter("emailFilter", "email"));
		request.getFilterRelation().add(request.new TypedFilter("cpfFilter", "cpf"));
		request.getInvalidPermiteds().add(NullValidator.class);
		request.getInvalidPermiteds().add(EmptyValidator.class);
		Paciente target = manageExample.processExampleRequest(request);
		return target;
	}

	protected class SearchPacienteHandler extends SearchBeanHandler<Paciente>{
		private String[] showColumns = {"codigo", "nome", "email", "cpf"}; 		
		@Override
		public Paciente buildExample(Searchable<Paciente> searchable) {
			return buildPacienteExample(searchable);
		}
		@Override
		public String[] getShowColumns() {
			return showColumns;
		}
	}
	
}
