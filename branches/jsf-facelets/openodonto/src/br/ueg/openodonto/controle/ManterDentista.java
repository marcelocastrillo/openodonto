package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchableDentista;
import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.validator.ValidatorFactory;

public class ManterDentista extends ManageBeanGeral<Dentista> {

	private static final long serialVersionUID = -2538516769206620892L;
	private ManageTelefone                manageTelefone;
	private static Map<String, String>    params;
	private Search<Dentista>              search;

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
		this.manageTelefone = new ManageTelefone(getDentista().getTelefone(), this);
		this.search = new SearchBase<Dentista>(new SearchableDentista(),
				"Buscar Paciente",
				"painelBusca",
				new ViewDisplayer("searchDefault"));
		this.search.addSearchListener(new SearchDentistaHandler());
		this.search.addSearchListener(new SearchSelectedHandler());
		makeView(params);
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
		obrigatorios.add(new ValidationRequest("cro", ValidatorFactory.newSrtEmpty(), "formDentista:entradaCro"));
		obrigatorios.add(new ValidationRequest("especialidade",	ValidatorFactory.newStrRangeLen(150, 3,true),"formDentista:entradaEspecialidade"));
		return obrigatorios;
	}
	
	@Override
	protected void carregarExtra() {
		manageTelefone.setTelefones(getDentista().getTelefone());
	}	
	
	public Search<Dentista> getSearch() {
		return search;
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
	
	protected class SearchDentistaHandler extends SearchBeanHandler<Dentista>{
		private String[] showColumns = {"codigo", "nome", "cro", "especialidade"};
		@Override
		public String[] getShowColumns() {
			return showColumns;
		}		
	}

}
