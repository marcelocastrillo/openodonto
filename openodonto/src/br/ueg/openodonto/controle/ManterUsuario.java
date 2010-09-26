package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.SearchBase;
import br.ueg.openodonto.controle.busca.SearchablePessoa;
import br.ueg.openodonto.controle.busca.SearchableUsuario;
import br.ueg.openodonto.controle.servico.ManagePassword;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.validator.ValidatorFactory;

public class ManterUsuario extends ManageBeanGeral<Usuario> {

	private static final long serialVersionUID = 2655162625494306823L;
	
	private static Map<String, String>    params;
	private Search<Usuario>               search;
	private Search<Pessoa>                personSearch;
	private ManagePassword                managePassword;

	static{
        params = new HashMap<String, String>();
		params.put("managebeanName", "manterUsuario");
		params.put("formularioSaida", "formUsuario");
		params.put("saidaPadrao", "formUsuario:output");
	}
	
	public ManterUsuario() {
		super(Usuario.class);
	}
	
	@Override
	protected void initExtra() {
		this.search = new SearchBase<Usuario>(
				new SearchableUsuario(new ViewDisplayer("searchDefault")),
				"Buscar Usuário",
				"painelBusca");
		this.personSearch = new SearchBase<Pessoa>(
				new SearchablePessoa(new ViewDisplayer("searchPerson")),
				"Buscar Pessoa",
				"painelBuscaPessoa");
		this.search.addSearchListener(new SearchUsuarioHandler());
		this.search.addSearchListener(new SearchSelectedHandler());
		this.personSearch.addSearchListener(new SearchPessoaHandler());
		this.personSearch.addSearchListener(new SearchPessoaSelectedHandler());
		makeView(params);
		managePassword = new ManagePassword(getUsuario(),this.getView());
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
		obrigatorios.add(new ValidationRequest("nome",ValidatorFactory.newSrtEmpty(), "formUsuario:entradaNome"));
		obrigatorios.add(new ValidationRequest("user",ValidatorFactory.newSrtEmpty(), "formUsuario:entradaUser"));
		obrigatorios.add(new ValidationRequest("senha",ValidatorFactory.newSrtEmpty(), "formDentista:entradaSenha"));
		return obrigatorios;
	}

	protected void carregarExtra() {
		managePassword.setUsuario(getUsuario());
	}
	
	public Usuario getUsuario() {
		return super.getBackBean();
	}

	public void setUsuario(Usuario usuario) {
		super.setBackBean(usuario);
	}

	public ManagePassword getManagePassword() {
		return managePassword;
	}
	
	public Search<Usuario> getSearch() {
		return search;
	}
	
	public Search<Pessoa> getPersonSearch() {
		return personSearch;
	}
	
	protected class SearchUsuarioHandler extends SearchBeanHandler<Usuario>{
		private String[] showColumns = {"codigo", "nome", "user",};
		@Override
		public String[] getShowColumns() {
			return showColumns;
		}		
	}

}
