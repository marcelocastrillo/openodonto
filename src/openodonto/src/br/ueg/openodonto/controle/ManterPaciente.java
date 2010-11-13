package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.CommonSearchPacienteHandler;
import br.ueg.openodonto.controle.busca.CommonSearchPessoaHandler;
import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchablePaciente;
import br.ueg.openodonto.controle.busca.SearchablePessoa;
import br.ueg.openodonto.controle.busca.ViewDisplayer;
import br.ueg.openodonto.controle.servico.ManageOdontograma;
import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.validator.EmptyValidator;
import br.ueg.openodonto.validator.NullValidator;
import br.ueg.openodonto.validator.ValidatorFactory;

public class ManterPaciente extends ManageBeanGeral<Paciente> {
	
	private static final long serialVersionUID = -2146469226044009908L;
	
	private ManageTelefone                manageTelefone;
	private ManageOdontograma             manageOdontograma;
	private static Map<String, String>    params;
	private Search<Paciente>              search;
	private Search<Pessoa>                personSearch;
	
	static{
		params = new HashMap<String, String>();
		params.put("managebeanName", "manterPaciente");
		params.put("formularioSaida", "formPaciente");
		params.put("saidaPadrao", "formPaciente:output");
	}

	public ManterPaciente() {
		super(Paciente.class);
	}

	protected void initExtra() {
		makeView(params);
		this.search = new SearchBase<Paciente>(
				new SearchablePaciente(new ViewDisplayer("searchDefault",getView())),
				"Buscar Paciente",
				"painelBusca");
		this.personSearch = new SearchBase<Pessoa>(
				new SearchablePessoa(new ViewDisplayer("searchPerson",getView())),
				"Buscar Pessoa",
				"painelBuscaPessoa");
		this.search.addSearchListener(new CommonSearchPacienteHandler());
		this.search.addSearchListener(new SearchSelectedHandler());
		this.personSearch.addSearchListener(new CommonSearchPessoaHandler());
		this.personSearch.addSearchListener(new SearchPessoaSelectedHandler());
		this.manageTelefone = new ManageTelefone(getPaciente().getTelefone(), this.getView());
		this.manageOdontograma = new ManageOdontograma(getPaciente().getOdontogramas(), new ViewDisplayer("manageProcOdontograma",this.getView()));
	}

	protected void carregarExtra() {
		manageTelefone.setTelefones(getPaciente().getTelefone());
		manageOdontograma.setOdontogramas(getPaciente().getOdontogramas());
		manageOdontograma.loadLastOdontograma();
	}

	protected List<String> getCamposFormatados() {
		String[] formatados = {"nome","cpf","cidade","endereco","responsavel","referencia"};
		return Arrays.asList(formatados);
	}

	protected List<ValidationRequest> getCamposObrigatorios() {
		List<ValidationRequest> obrigatorios = new ArrayList<ValidationRequest>();
		obrigatorios.add((new ValidationRequest("nome", ValidatorFactory.newStrEmpty(),"formPaciente:entradaNome")));
		obrigatorios.add(new ValidationRequest("cpf",ValidatorFactory.newStrEmpty(),"formPaciente:entradaCpf"));
		return obrigatorios;
	}
	
	@Override
	protected List<ValidationRequest> getCamposValidados(){
		List<ValidationRequest> validados = new ArrayList<ValidationRequest>();
		Class<?>[] allowed = {NullValidator.class,EmptyValidator.class};
		validados.add(new ValidationRequest("observacao", ValidatorFactory.newStrMaxLen(500, false), "formPaciente:inTextBoxObs",allowed));
		validados.add(new ValidationRequest("email", ValidatorFactory.newEmail(45), "formPaciente:entradaEmail",allowed));
		validados.add(new ValidationRequest("nome", ValidatorFactory.newStrRangeLen(100,4, false), "formPaciente:entradaNome"));
		validados.add(new ValidationRequest("endereco", ValidatorFactory.newStrRangeLen(150,4, true), "formPaciente:entradaEndereco",allowed));		
		validados.add(new ValidationRequest("referencia", ValidatorFactory.newStrRangeLen(150,4, true), "formPaciente:entradaReferencia",allowed));
		validados.add(new ValidationRequest("responsavel", ValidatorFactory.newStrRangeLen(150,4, true), "formPaciente:entradaResponavel",allowed));		
		validados.add(new ValidationRequest("cidade", ValidatorFactory.newStrRangeLen(45,3, true), "formPaciente:entradaCidade",allowed));
		validados.add(new ValidationRequest("cpf", ValidatorFactory.newCpf(), "formPaciente:entradaCpf"));
		return validados;
	}
	
	public Search<Paciente> getSearch() {
		return search;
	}

	public Search<Pessoa> getPersonSearch() {
		return personSearch;
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

	public ManageOdontograma getManageOdontograma() {
		return manageOdontograma;
	}
	
	public void setManageTelefone(ManageTelefone manageTelefone) {
		this.manageTelefone = manageTelefone;
	}
	
}
