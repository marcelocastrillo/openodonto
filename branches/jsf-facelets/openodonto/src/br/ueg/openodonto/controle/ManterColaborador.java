package br.ueg.openodonto.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchableColaborador;
import br.ueg.openodonto.controle.servico.ManageExample;
import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.constante.CategoriaProduto;
import br.ueg.openodonto.dominio.constante.TipoPessoa;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.validator.ValidatorFactory;

public class ManterColaborador extends ManageBeanGeral<Colaborador> {

	private static final long serialVersionUID = 7597358634869495788L;
	
	private ManageTelefone                manageTelefone;
	private ManageExample<Colaborador>    manageExample;
	private static Map<String, String>    params;
	private Search<Colaborador>           search;
	private MessageDisplayer              displayer;	
	private CategoriaProduto              categoria;
	private TipoPessoa                    tipoPessoa;
	
	static{
		params = new HashMap<String, String>();
		params.put("managebeanName", "manterColaborador");
		params.put("formularioSaida", "formColaborador");
		params.put("formModalSearch", "formSearch");
		params.put("nameModalSearch", "painelBusca");
		params.put("saidaPadrao", "formColaborador:output");
		params.put("saidaContato", "messageTelefone");
	}
	
	public ManterColaborador() {
		super(Colaborador.class);
	}

	@Override
	protected void initExtra() {
		this.displayer = new ViewDisplayer("searchDefaultOutput");
		this.manageExample = new ManageExample<Colaborador>(Colaborador.class);
		this.manageTelefone = new ManageTelefone(getColaborador().getTelefone(), this);
		this.search = new SearchBase<Colaborador>(new SearchableColaborador(this.displayer),"Buscar Colaborador");
		this.search.addSearchListener(new SearchColaboradorHandler());
		this.search.addSearchListener(new SearchSelectedHandler());
		this.tipoPessoa = TipoPessoa.PESSOA_FISICA;
		makeView(params);
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

	protected class SearchColaboradorHandler extends SearchBeanHandler<Colaborador> implements Serializable{
		private static final long serialVersionUID = 5660539094298081485L;
		private String[] showColumns = {""};
		@Override
		public Colaborador buildExample(Searchable<Colaborador> searchable) {
			return null;
		}
		@Override
		public String[] getShowColumns() {
			return showColumns;
		}
		
	}

}
