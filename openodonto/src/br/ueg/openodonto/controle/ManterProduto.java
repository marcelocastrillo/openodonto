package br.ueg.openodonto.controle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.ResultFacadeBean;
import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchableProduto;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.ColaboradorProduto;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.dominio.constante.CategoriaProduto;
import br.ueg.openodonto.persistencia.dao.DaoColaboradorProduto;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.servico.busca.ResultFacade;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.util.WordFormatter;
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
		public String[] getShowColumns() {
			return showColumns;
		}
		
		@Override
		protected List<ResultFacade> wrapResult(List<Map<String, Object>> result) {
			return ManterProduto.this.wrapResult(result);
		}
		
		public List<Map<String,Object>> evaluteResult(Search<Produto> search) throws SQLException{
			SearchableProduto searchable = (SearchableProduto)search.getSearchable();
			Produto produto = searchable.buildExample();
			Colaborador colaborador = searchable.buildExampleColaborador();
			DaoColaboradorProduto dao = (DaoColaboradorProduto)DaoFactory.getInstance().getDao(ColaboradorProduto.class);
			List<Map<String,Object>> result;
			if(colaborador == null){
				result = getDao().getSqlExecutor().executarUntypedQuery(getQuery(produto));
			}else{
				result = dao.getUntypeProdutos(colaborador, produto);
			}
			return result;			
		}

	}	
	

}
