package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.CommonSearchDentistaHandler;
import br.ueg.openodonto.controle.busca.CommonSearchPessoaHandler;
import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchableDentista;
import br.ueg.openodonto.controle.busca.SearchablePessoa;
import br.ueg.openodonto.controle.busca.ViewDisplayer;
import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.validator.ValidatorFactory;

public class ManterDentista extends ManageBeanGeral<Dentista> {

	private static final long serialVersionUID = -2538516769206620892L;
	private ManageTelefone                manageTelefone;
	private static Map<String, String>    params;
	private Search<Dentista>              search;
	private Search<Pessoa>                personSearch;

	static{
		params = new HashMap<String, String>();
		params.put("managebeanName", "manterDentista");
		params.put("formularioSaida", "formDentista");
		params.put("saidaPadrao", "formDentista:output");
	}
	
	public ManterDentista() {
		super(Dentista.class);		
	}

	@Override
	protected void initExtra() {
		makeView(params);
		this.manageTelefone = new ManageTelefone(getDentista().getTelefone(), this.getView());
		this.search = new SearchBase<Dentista>(
				new SearchableDentista(new ViewDisplayer("searchDefault",getView())),
				"Buscar Paciente",
				"painelBusca");
		this.personSearch = new SearchBase<Pessoa>(
				new SearchablePessoa(new ViewDisplayer("searchPerson",getView())),
				"Buscar Pessoa",
				"painelBuscaPessoa");
		this.search.addSearchListener(new CommonSearchDentistaHandler());
		this.search.addSearchListener(new SearchSelectedHandler());
		this.personSearch.addSearchListener(new CommonSearchPessoaHandler());
		this.personSearch.addSearchListener(new SearchPessoaSelectedHandler());
	}	

	@Override
	protected List<String> getCamposFormatados() {
		List<String> formatados = new ArrayList<String>();
		formatados.add("nome");
		formatados.add("especialidade");
		formatados.add("cidade");
		formatados.add("endereco");
		return formatados;
	}

	@Override
	protected List<ValidationRequest> getCamposObrigatorios() {
		List<ValidationRequest> obrigatorios = new ArrayList<ValidationRequest>();
		obrigatorios.add(new ValidationRequest("nome", ValidatorFactory.newStrRangeLen(100, 5,true), "formDentista:entradaNome"));
		obrigatorios.add(new ValidationRequest("cro", ValidatorFactory.newStrEmpty(), "formDentista:entradaCro"));
		obrigatorios.add(new ValidationRequest("especialidade",	ValidatorFactory.newStrRangeLen(150, 3,true),"formDentista:entradaEspecialidade"));
		return obrigatorios;
	}
	
	@Override
	protected List<ValidationRequest> getCamposValidados(){
		List<ValidationRequest> validados = new ArrayList<ValidationRequest>();
		return validados;
	}
	
	@Override
	protected void carregarExtra() {
		manageTelefone.setTelefones(getDentista().getTelefone());
	}	
	
	public Search<Dentista> getSearch() {
		return search;
	}

	public Search<Pessoa> getPersonSearch() {
		return personSearch;
	}
	
	public Dentista getDentista() {
		return getBackBean();
	}

	public void setDentista(Dentista dentista) {
		setBackBean(dentista);
	}

	public ManageTelefone getManageTelefone() {
		return manageTelefone;
	}

	public void setManageTelefone(ManageTelefone manageTelefone) {
		this.manageTelefone = manageTelefone;
	}	

}
