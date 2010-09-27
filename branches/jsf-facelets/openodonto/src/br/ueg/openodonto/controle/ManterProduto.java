package br.ueg.openodonto.controle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.CommonSearchProdutoHandler;
import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchableProduto;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.servico.busca.ResultFacade;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.validator.ValidatorFactory;

public class ManterProduto extends ManageBeanGeral<Produto>{

	private static final long serialVersionUID = -7320449089337162282L;
	
	private static Map<String, String>   params;
	private Search<Produto>              search;
	
	static{
		params = new HashMap<String, String>();
		params.put("managebeanName", "manterProduto");
		params.put("formularioSaida", "formProduto");
		params.put("saidaPadrao", "formProduto:output");
	}
	
	public ManterProduto() {
		super(Produto.class);
	}	

	@Override
	protected void initExtra() {
		this.search = new SearchBase<Produto>(
				new SearchableProduto(new ViewDisplayer("searchDefault")),
				"Buscar Produto",
				"painelBusca");
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
	
	
	protected class SearchProdutoHandler extends SearchBeanHandler<Produto>{
		private String[] showColumns = {"codigo", "nome", "categoria", "descricao"};
		private CommonSearchProdutoHandler produtoHandler = null;
		public SearchProdutoHandler(){
			produtoHandler = new CommonSearchProdutoHandler(getDao()) {			
				@Override
				public IQuery getQuery(Produto example) {
					return SearchProdutoHandler.this.getQuery(example);
				}				
				@Override
				public ResultFacade buildWrapBean(Map<String, Object> value) {
					return SearchProdutoHandler.this.buildWrapBean(value);
				}
			};
		}
		@Override
		public String[] getShowColumns() {
			return showColumns;
		}
		@Override
		protected List<ResultFacade> wrapResult(List<Map<String, Object>> result) {
			return produtoHandler.wrapResult(result);
		}
		@Override
		public List<Map<String, Object>> evaluateResult(Search<Produto> search)	throws SQLException {
			return produtoHandler.evaluateResult(search);
		}
		
	}	
	

}
