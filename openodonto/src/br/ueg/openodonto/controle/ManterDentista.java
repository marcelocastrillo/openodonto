package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchableDentista;
import br.ueg.openodonto.controle.servico.ExampleRequest;
import br.ueg.openodonto.controle.servico.ManageExample;
import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.dao.sql.Query;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.validator.EmptyValidator;
import br.ueg.openodonto.validator.NullValidator;
import br.ueg.openodonto.validator.ValidatorFactory;

public class ManterDentista extends ManageBeanGeral<Dentista> {

	private static final long serialVersionUID = -2538516769206620892L;
	private ManageTelefone                manageTelefone;
	private ManageExample<Dentista>       manageExample;
	private static Map<String, String>    params;
	private Search<Dentista>              search;
	private MessageDisplayer              displayer;

	static{
		params = new HashMap<String, String>();
		params.put("managebeanName", "manterDentista");
		params.put("formularioSaida", "formDentista");
		params.put("formModalSearch", "formSearch");
		params.put("nameModalSearch", "painelBusca");
		params.put("saidaPadrao", "formDentista:output");
		params.put("saidaContato", "messageTelefone");
	}
	
	public ManterDentista() {
		super(Dentista.class);		
	}

	@Override
	protected void initExtra() {
		this.displayer = new ViewDisplayer("searchDefaultOutput");
		this.manageExample = new ManageExample<Dentista>(Dentista.class);
		this.manageTelefone = new ManageTelefone(getDentista().getTelefone(), this);
		this.search = new SearchBase<Dentista>(new SearchableDentista(this.displayer),"Buscar Dentista");
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
		obrigatorios.add(new ValidationRequest("nome", ValidatorFactory.newSrtEmpty(), "formDentista:entradaNome"));
		obrigatorios.add(new ValidationRequest("cro", ValidatorFactory.newSrtEmpty(), "formDentista:entradaCro"));
		obrigatorios.add(new ValidationRequest("especialidade",	ValidatorFactory.newSrtEmpty(),"formDentista:entradaEspecialidade"));
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
	
	private Dentista buildDentistaExample(Searchable<Dentista> searchable){
		ExampleRequest<Dentista> request  = new ExampleRequest<Dentista>(searchable);		
		request.getFilterRelation().add(request.new TypedFilter("nomeFilter", "nome"));
		request.getFilterRelation().add(request.new TypedFilter("idFilter","codigo"));
		request.getFilterRelation().add(request.new TypedFilter("croFilter", "cro"));
		request.getFilterRelation().add(request.new TypedFilter("especialidadeFilter", "especialidade"));
		request.getInvalidPermiteds().add(NullValidator.class);
		request.getInvalidPermiteds().add(EmptyValidator.class);
		Dentista target = manageExample.processExampleRequest(request);
		return target;
	}
	
	protected class SearchDentistaHandler extends SearchBeanHandler<Dentista>{
		private String[] showColumns = {"codigo", "nome", "cro", "especialidade"};
		@Override
		public Dentista buildExample(Searchable<Dentista> searchable) {
			return buildDentistaExample(searchable);
		}

		@Override
		public String[] getShowColumns() {
			return showColumns;
		}		
		@Override/*TODO Este método é temporário e será refatorado/extinto*/
		public IQuery getQuery(Dentista example){
			OrmFormat format = new OrmFormat(example);
			Map<String, Object> params = format.formatNotNull();
			Object nome;
			if((nome = params.get("nome")) != null){
				params.put("nome", "%"+nome+"%");
			}
			Object especialidade;
			if((especialidade = params.get("especialidade")) != null){
				params.put("especialidade", "%"+especialidade+"%");
			}
			Query query = (Query)CrudQuery.getSelectQuery(Dentista.class,params, getShowColumns());
			if(nome != null){
			    query.setQuery(query.getQuery().replace("nome = ?", "nome like ?"));
			}
			if(especialidade != null){
				query.setQuery(query.getQuery().replace("especialidade = ?", "especialidade like ?"));
			}
			return query;
		}
		
		
	}

}
