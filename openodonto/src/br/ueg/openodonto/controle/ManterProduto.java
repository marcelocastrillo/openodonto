package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.ResultFacadeBean;
import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchableProduto;
import br.ueg.openodonto.controle.servico.ExampleRequest;
import br.ueg.openodonto.controle.servico.ManageExample;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.dominio.constante.CategoriaProduto;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.dao.sql.Query;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.servico.busca.ResultFacade;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.util.WordFormatter;
import br.ueg.openodonto.validator.EmptyValidator;
import br.ueg.openodonto.validator.NullValidator;
import br.ueg.openodonto.validator.ValidatorFactory;

public class ManterProduto extends ManageBeanGeral<Produto>{

	private static final long serialVersionUID = -7320449089337162282L;
	
	private ManageExample<Produto>       manageExample;
	private static Map<String, String>   params;
	private Search<Produto>              search;
	private MessageDisplayer             displayer;	
	
	static{
		params = new HashMap<String, String>();
		params.put("managebeanName", "manterProduto");
		params.put("formularioSaida", "formProduto");
		params.put("formModalSearch", "formSearch");
		params.put("nameModalSearch", "painelBusca");
		params.put("saidaPadrao", "formProduto:output");
	}
	
	public ManterProduto() {
		super(Produto.class);
	}	

	@Override
	protected void initExtra() {
		this.displayer = new ViewDisplayer("searchDefaultOutput");
		this.manageExample = new ManageExample<Produto>(Produto.class);
		this.search = new SearchBase<Produto>(new SearchableProduto(this.displayer),"Buscar Produto");
		this.search.addSearchListener(new SearchProdutoHandler());
		this.search.addSearchListener(new SearchSelectedHandler());
		makeView(params);
	}
	
	@Override
	protected List<String> getCamposFormatados() {
		List<String> formatados = new ArrayList<String>();
		formatados.add("nome");
		return formatados;
	}
	
	@Override
	protected List<ValidationRequest> getCamposObrigatorios() {
		List<ValidationRequest> obrigatorios = new ArrayList<ValidationRequest>();
		obrigatorios.add(new ValidationRequest("nome", ValidatorFactory.newStrRangeLen(100, 5,true), "formProduto:entradaNome"));
		obrigatorios.add(new ValidationRequest("categoria",	ValidatorFactory.newNull(),"formProduto:selectCategoria"));
		return obrigatorios;
	}
	
	public Produto getProduto(){
		return getBackBean();
	}
	
	public void setProduto(Produto produto){
		setBackBean(produto);
	}
	
	public Search<Produto> getSearch() {
		return search;
	}
	
	private Produto buildProdutoExample(Searchable<Produto> searchable){
		ExampleRequest<Produto> request  = new ExampleRequest<Produto>(searchable);		
		request.getFilterRelation().add(request.new TypedFilter("nomeFilter", "nome"));
		request.getFilterRelation().add(request.new TypedFilter("categoriaFilter","categoria"));
		request.getFilterRelation().add(request.new TypedFilter("descricaoFilter","descricao"));
		request.getInvalidPermiteds().add(NullValidator.class);
		request.getInvalidPermiteds().add(EmptyValidator.class);
		Produto target = manageExample.processExampleRequest(request);
		return target;
	}
	
	@Override
	protected List<ResultFacade> wrapResult(List<Map<String, Object>> result) {
		List<ResultFacade> resultWrap = new ArrayList<ResultFacade>(result.size());
		Iterator<Map<String, Object>> iterator = result.iterator();
		while(iterator.hasNext()){
			Map<String,Object> value = iterator.next();
			value.put("shortDescription", WordFormatter.abstractStr(value.get("descricao").toString(), 60));
			value.put("categoriaDesc", CategoriaProduto.parseCategoria(value.get("categoria")));
			resultWrap.add(new ResultFacadeBean(value));
		}
		return resultWrap;
	}
	
	protected class SearchProdutoHandler extends SearchBeanHandler<Produto>{
		private String[] showColumns = {"codigo", "nome", "categoria", "descricao"};
		@Override
		public Produto buildExample(Searchable<Produto> searchable) {
			return buildProdutoExample(searchable);
		}
		@Override
		public String[] getShowColumns() {
			return showColumns;
		}		
		@Override/*TODO Este método é temporário e será refatorado/extinto*/
		public IQuery getQuery(Produto example){
			OrmFormat format = new OrmFormat(example);
			Map<String, Object> params = format.formatNotNull();
			Object nome;
			if((nome = params.get("nome")) != null){
				params.put("nome", "%"+nome+"%");
			}
			Object descricao;
			if((descricao = params.get("descricao")) != null){
				params.put("descricao", "%"+descricao+"%");
			}
			Query query = (Query)CrudQuery.getSelectQuery(Produto.class,params, getShowColumns());
			if(nome != null){
			    query.setQuery(query.getQuery().replace("nome = ?", "nome like ?"));
			}
			if(descricao != null){
			    query.setQuery(query.getQuery().replace("descricao = ?", "descricao like ?"));
			}
			return query;
		}
	}	
	

}
