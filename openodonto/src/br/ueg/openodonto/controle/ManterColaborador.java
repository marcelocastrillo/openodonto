package br.ueg.openodonto.controle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.CommonSearchAssociateHandler;
import br.ueg.openodonto.controle.busca.CommonSearchColaboradorHandler;
import br.ueg.openodonto.controle.busca.CommonSearchPessoaHandler;
import br.ueg.openodonto.controle.busca.CommonSearchProdutoHandler;
import br.ueg.openodonto.controle.busca.CommonSearchSelectableBeanHandler;
import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchableColaborador;
import br.ueg.openodonto.controle.busca.SearchablePessoa;
import br.ueg.openodonto.controle.busca.SearchableProduto;
import br.ueg.openodonto.controle.busca.SelectableSearchBase;
import br.ueg.openodonto.controle.busca.ViewDisplayer;
import br.ueg.openodonto.controle.servico.ManageProduto;
import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.dominio.constante.CategoriaProduto;
import br.ueg.openodonto.dominio.constante.TipoPessoa;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.servico.busca.ResultFacade;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.SelectableSearch;
import br.ueg.openodonto.validator.ValidatorFactory;

public class ManterColaborador extends ManageBeanGeral<Colaborador> {

	private static final long serialVersionUID = 7597358634869495788L;
	
	private ManageTelefone                manageTelefone;
	private ManageProduto                 manageProduto;
	private static Map<String, String>    params;
	private Search<Colaborador>           search;
	private SelectableSearch<Produto>     searchProduto; 
	private Search<Pessoa>                personSearch;
	private CategoriaProduto              categoria;
	private TipoPessoa                    tipoPessoa;
	
	static{
		params = new HashMap<String, String>();
		params.put("managebeanName", "manterColaborador");
		params.put("formularioSaida", "formColaborador");
		params.put("saidaPadrao", "formColaborador:output");
	}
	
	public ManterColaborador() {
		super(Colaborador.class);
	}

	@Override
	protected void initExtra() {
		makeView(params);
		this.manageTelefone = new ManageTelefone(getColaborador().getTelefone(), this.getView());
		this.manageProduto = new ManageProduto(getColaborador().getProdutos());
		this.search = new SearchBase<Colaborador>(
				new SearchableColaborador(new ViewDisplayer("searchDefaultOutput",getView())),
				"Buscar Colaborador",
				"painelBusca");
		this.personSearch = new SearchBase<Pessoa>(
				new SearchablePessoa(new ViewDisplayer("searchPerson",getView())),
				"Buscar Pessoa",
				"painelBuscaPessoa");
		this.searchProduto = new SelectableSearchBase<Produto>(
				new SearchableProduto(new ViewDisplayer("searchProduct",getView())),
				"Associar Produto",
				"painelBuscaProduto");
		this.search.addSearchListener(new SearchColaboradorHandler());
		this.search.addSearchListener(new SearchSelectedHandler());
		this.personSearch.addSearchListener(new CommonSearchPessoaHandler());
		this.personSearch.addSearchListener(new SearchPessoaSelectedHandler());
		this.searchProduto.addSearchListener(new SearchProdutoSelectableHandler());
		this.searchProduto.addSearchListener(new SearchColaboradorAssociateHandler());
		this.tipoPessoa = TipoPessoa.PESSOA_FISICA;
	}
	
	@Override
	public void acaoSalvar() {
		clearUnsedDocument();
		super.acaoSalvar();
	}
	
	@Override
	public void acaoSalvarExtra(){
		getColaborador().setCategoria(getCategoria());
	}
	
	private void clearUnsedDocument(){
		switch (tipoPessoa) {
		case PESSOA_FISICA:
			getColaborador().setCnpj(null);
			break;
		case PESSOA_JURIDICA:	
			getColaborador().setCpf(null);
			break;
		}
	}
	
	@Override
	protected List<String> getCamposFormatados() {
		List<String> formatados = new ArrayList<String>();
		formatados.add("nome");
		formatados.add("cidade");
		formatados.add("endereco");
		formatados.add("cpf");
		formatados.add("cnpj");
		return formatados;
	}

	@Override
	protected List<ValidationRequest> getCamposObrigatorios() {
		List<ValidationRequest> obrigatorios = new ArrayList<ValidationRequest>();
		obrigatorios.add(new ValidationRequest("nome", ValidatorFactory.newSrtEmpty(), "formColaborador:entradaNome"));
		if(getTipoPessoa().isPf()){
			obrigatorios.add(new ValidationRequest("cpf",ValidatorFactory.newCpf(),"formColaborador:entradaCpf"));
		}else if(getTipoPessoa().isPj()){
			obrigatorios.add(new ValidationRequest("cnpj",ValidatorFactory.newCnpj(),"formColaborador:entradaCnpj"));
		}
		return obrigatorios;
	}
	
	@Override
	protected void carregarExtra() {
		manageTelefone.setTelefones(getColaborador().getTelefone());
		manageProduto.setProdutos(getColaborador().getProdutos());
	}	
	
	public Colaborador getColaborador(){
		return getBackBean();
	}
	
	public CategoriaProduto getPrestadorMode(){
		this.categoria = CategoriaProduto.SERVICO;
		return getCategoria();
	}
	
	public CategoriaProduto getFornecedorMode(){
		this.categoria = CategoriaProduto.PRODUTO;
		return getCategoria();
	}	
	
	public Search<Colaborador> getSearch() {
		return search;
	}
	
	public Search<Pessoa> getPersonSearch() {
		return personSearch;
	}

	public SelectableSearch<Produto> getSearchProduto() {
		return searchProduto;
	}
	
	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public CategoriaProduto getCategoria() {
		return categoria;
	}
	
	public void setColaborador(Colaborador colaborador){
		setBackBean(colaborador);
	}	
	
	public ManageTelefone getManageTelefone() {
		return manageTelefone;
	}

	public void setManageTelefone(ManageTelefone manageTelefone) {
		this.manageTelefone = manageTelefone;
	}	
	
	public ManageProduto getManageProduto() {
		return manageProduto;
	}

	public void setManageProduto(ManageProduto manageProduto) {
		this.manageProduto = manageProduto;
	}



	protected class SearchProdutoSelectableHandler extends CommonSearchSelectableBeanHandler<Produto>{
		private CommonSearchProdutoHandler searchProduto;		
		public SearchProdutoSelectableHandler() {
			super(Produto.class, DaoFactory.getInstance().getDao(Produto.class));
			searchProduto = new CommonSearchProdutoHandler(){
				@Override
				protected void addResults(Search<Produto> search,List<Map<String, Object>> result) {
					SearchProdutoSelectableHandler.this.addResults(search, result);
				}
				@Override
				protected ResultFacade buildWrapBean(Map<String, Object> value) {
					return SearchProdutoSelectableHandler.this.buildWrapBean(value);
				}
			};
		}
		@Override
		public String[] getShowColumns() {
			return searchProduto.getShowColumns();
		}
		@Override
		protected List<ResultFacade> wrapResult(List<Map<String, Object>> result) {
			return searchProduto.wrapResult(result);
		}
		@Override
		public List<Map<String, Object>> evaluateResult(Search<Produto> search)	throws SQLException {
			return searchProduto.evaluateResult(search);
		}
	}
	
	protected class SearchColaboradorAssociateHandler extends CommonSearchAssociateHandler<Produto>{
		public SearchColaboradorAssociateHandler() {
			super(DaoFactory.getInstance().getDao(Produto.class));
		}
		@Override
		public void associateBeans(List<Produto> associated) {
			getManageProduto().associarProdutos(associated);
		}
	}
	
	protected class SearchColaboradorHandler extends CommonSearchColaboradorHandler{
		@Override
		public CategoriaProduto getCategoria() {
			return ManterColaborador.this.getCategoria();
		}
		
	}
}
