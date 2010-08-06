package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.ueg.openodonto.controle.validador.AbstractValidator;
import br.ueg.openodonto.controle.validador.ValidadorPadrao;
import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

public class ManterUsuario extends ManageBeanGeral<Usuario> {

	private static final long serialVersionUID = 2655162625494306823L;

	public ManterUsuario() {
		super(Usuario.class);
		Properties params = new Properties();
		params.put("managebeanName", "manterUsuario");
		params.put("formularioSaida", "formUsuario");
		params.put("saidaPadrao", "formUsuario:output");
		makeView(null);
	}

	
	@Override
	public String acaoPesquisar() {
		if (this.getBusca().getParams().get("opcao") == null
				|| this.getBusca().getParams().get("opcao").isEmpty()
				|| this.getBusca().getParams().get("opcao").length() < 3) {
			getView().addResourceDynamicMenssage(
					"Selecine um filtro de pesquisa.",
					"formModalUsuario:buscar");
			return DEFAULT_RULE;
		}

		if (this.getBusca().getParams().get("param") == null
				|| this.getBusca().getParams().get("param").isEmpty()) {
			getView().addResourceDynamicMenssage(
					"Informe o parametro para pesquisa.",
					"formModalUsuario:buscar");
			return DEFAULT_RULE;
		}

		if (this.getBusca().getParams().get("opcao").equals("nome")
				&& this.getBusca().getParams().get("param").length() < 3) {
			getView().addResourceDynamicMenssage(
					"Informe pelo menos 3 caracteres.",
					"formModalUsuario:buscar");
			getBusca().getParams().put("opcao", null);
			return DEFAULT_RULE;
		}

		if (this.getBusca().getParams().get("opcao").equals("user")
				&& this.getBusca().getParams().get("param").length() < 4) {
			getView().addResourceDynamicMenssage(
					"A especialidade deve ter mais que 4 caracteres.",
					"formModalUsuario:buscar");
			getBusca().getParams().put("opcao", null);
			return DEFAULT_RULE;
		}

		try {
			List<Usuario> result = null;
			Usuario usuario = new Usuario();
			OrmFormat orm = new OrmFormat(usuario);
			String[] fields = { "codigo", "user", "nome" };
			if (this.getBusca().getParams().get("opcao").equals("codigo")) {
				long cod = 0;
				try {
					cod = Long.parseLong(this.getBusca().getParams().get(
							"param"));
				} catch (Exception ex) {
					getView().addResourceDynamicMenssage(
							"Digite Apenas numeros para esta opcao.",
							"formModalUsuario:buscar");
					getBusca().getParams().put("param", null);
					getBusca().getParams().put("opcao", null);
					return DEFAULT_RULE;
				}
				usuario.setCodigo(cod);
				IQuery query = CrudQuery.getSelectQuery(Dentista.class, orm
						.formatNotNull(), fields);
				result = dao.getSqlExecutor().executarQuery(query.getQuery(),
						query.getParams(), null);
			} else if (this.getBusca().getParams().get("opcao").equals("nome")) {
				usuario.setNome("%"+this.getBusca().getParams().get("param")+"%");
				result = dao.getSqlExecutor().executarNamedQuery(
						"Usuario.findByNome", orm.formatNotNull().values(),
						fields);
			} else if (this.getBusca().getParams().get("opcao").equals("user")) {
				usuario.setUser(this.getBusca().getParams().get("param"));
				result = dao.getSqlExecutor().executarNamedQuery(
						"Usuario.findUser", orm.formatNotNull().values(),
						fields);
			}
			if (result != null) {
				this.getBusca().acaoLimpar();
				getBusca().getResultados().addAll(result);
				getView().addResourceDynamicMenssage(
						"Foram encontrados " + result.size() + " resultados.",
						"formModalUsuario:buscar");
			} else
				getView().addResourceDynamicMenssage(
						"Nao foi encontrado nenhum resultado.",
						"formModalUsuario:buscar");
		} catch (Exception ex) {
			ex.printStackTrace();
			getView().addResourceMessage("ErroSistema.",
					"formModalUsuario:buscar");
		} finally {
			dao.closeConnection();
		}
		getBusca().getParams().put("param", null);
		getBusca().getParams().put("opcao", null);
		return DEFAULT_RULE;
	}

	@Override
	protected void carregarExtra() {
	}

	@Override
	protected List<String> getCamposFormatados() {
		List<String> formatados = new ArrayList<String>();
		formatados.add("nome");
		return formatados;
	}

	@Override
	public String acaoAlterar() {
		
		return super.acaoAlterar();
	}
	
	@Override
	protected List<AbstractValidator> getCamposObrigatorios() {
		List<AbstractValidator> obrigatorios = new ArrayList<AbstractValidator>();
		obrigatorios.add(new ValidadorPadrao("nome", "formUsuario:entradaNome"));
		obrigatorios.add(new ValidadorPadrao("user", "formUsuario:entradaUser"));
		obrigatorios.add(new ValidadorPadrao("senha", "formDentista:entradaSenha"));
		return null;
	}

	public Usuario getUsuario() {
		return super.getBackBean();
	}

	public void setUsuario(Usuario usuario) {
		super.setBackBean(usuario);
	}

	@Override
	protected void initExtra() {
	}
	
	public boolean getEnableChangePassword(){
		return getBackBean() != null && getBackBean().getCodigo() != null && getBackBean().getCodigo() != 0;
	}

}
