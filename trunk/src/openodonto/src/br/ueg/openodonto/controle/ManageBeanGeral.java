package br.ueg.openodonto.controle;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import br.ueg.openodonto.controle.context.ApplicationContext;
import br.ueg.openodonto.controle.context.OpenOdontoWebContext;
import br.ueg.openodonto.controle.servico.ManageSearch;
import br.ueg.openodonto.controle.validador.AbstractValidator;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.util.PBUtil;
import br.ueg.openodonto.util.WordFormatter;
import br.ueg.openodonto.visao.ApplicationView;
import br.ueg.openodonto.visao.ApplicationViewFactory;
import br.ueg.openodonto.visao.ApplicationViewFactory.ViewHandler;

/**
 * @author vinicius.rodrigues
 * 
 * @param <T>
 */
public abstract class ManageBeanGeral<T extends Entity> implements Serializable{

	private static final long serialVersionUID = -6270953407778330292L;

	public static final String DEFAULT_RULE = null;

	private T backBean;
	private Class<T> classe;
	protected EntityManager<T> dao;
	private ApplicationContext context;
	private ApplicationView view;
	private String msgBundle;
	private ManageSearch<T> busca;

	public ManageBeanGeral(Class<T> classe) {
		this.classe = classe;
		init();
	}
	
	protected abstract void initExtra();

	protected void init() {
		this.backBean = fabricarNovoBean();
		this.dao = DaoFactory.getInstance().getDao(classe);		
		this.busca = new ManageSearch<T>(this, "backBean");
		this.context = new OpenOdontoWebContext();
		initExtra();
	}

	protected T fabricarNovoBean() {
		try {
			return classe.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String acaoShowed() {
		getView().showAction();
		return DEFAULT_RULE;
	}

	public void exibirSaida() {
		getView().showOut();
	}

	public void exibirPopUp(String message) {
		getView().showPopUp(message);
	}

	protected abstract List<AbstractValidator> getCamposObrigatorios();

	protected abstract List<String> getCamposFormatados();

	protected void ValidarCamposExtras() {
	};

	protected void acaoValidarCampos() throws Exception {
		ValidarCamposExtras();
		List<String> camposFormatados = getCamposFormatados();
		for (String path : camposFormatados) {
			Object o = PBUtil.instance().getNestedProperty(getBackBean(), path);
			String campoParaFormatar = null;
			if (o != null)
				campoParaFormatar = String.valueOf(o);
			else
				continue;
			if (!campoParaFormatar.isEmpty()) {
				String atributoFormatado = WordFormatter.clear(
						WordFormatter.remover(campoParaFormatar)).toUpperCase();
				PBUtil.instance().setNestedProperty(getBackBean(), path,
						atributoFormatado);
			}
		}
	}

	protected boolean checarCamposObrigatoriosExtras() {
		return true;
	}

	protected boolean checarCamposObrigatorios() {
		boolean returned = true;
		List<AbstractValidator> camposObrigatorios = getCamposObrigatorios();
		for (AbstractValidator validador : camposObrigatorios) {
			if (validador.isValid(getBackBean())) {
				getView().addLocalDynamicMenssage(validador.getMessage(),
						validador.getMessageOut(), false);
				returned = false;
			}
		}
		return checarCamposObrigatoriosExtras() && returned;
	}

	public abstract String acaoPesquisar();

	public String acaoAlterar() {
		boolean alredy = false;
		try {
			acaoValidarCampos();
			if (!checarCamposObrigatorios()) {
				exibirPopUp("Campos obrigatorios nao preenchidos");
				getView().addLocalDynamicMenssage(
						"Campos obrigatorios invalidos.", "saidaPadrao", true);
				return DEFAULT_RULE;
			}
			if (dao.exists(getBackBean()))
				alredy = true;
			acaoSalvarExtra();
			this.dao.alterar(this.backBean);
		} catch (Exception ex) {
			exibirPopUp(getView().getMessageFromResource("ErroSistema"));
			getView().addLocalMessage("ErroSistema", "saidaPadrao", true);
			ex.printStackTrace();
			return DEFAULT_RULE;
		} finally {
			dao.closeConnection();
		}
		init();
		exibirPopUp(getView().getMessageFromResource(alredy ? "Atualizado"
				: "Cadastro"));
		getView().addLocalMessage(alredy ? "Atualizado" : "Cadastro",
				"saidaPadrao", true);
		return DEFAULT_RULE;
	}

	public String acaoSalvarExtra() {
		return "";
	}

	public String acaoRemoverSim() {
		try {
			this.dao.remover(this.backBean);
		} catch (Exception e) {
			exibirPopUp("Nao foi possivel remover o registro.");
			getView()
					.addLocalDynamicMenssage(
							"Nao foi possivel remover o registro.",
							"saidaPadrao", true);
			return DEFAULT_RULE;
		} finally {
			dao.closeConnection();
		}
		init();
		exibirPopUp(getView().getMessageFromResource("removido"));
		getView().addLocalMessage("removido", "saidaPadrao", true);
		return DEFAULT_RULE;
	}

	public String acaoAtualizar() {
		getView().refresh();
		return DEFAULT_RULE;
	}

	protected Usuario getUsuarioSessao() {
		return getContext().getUsuarioSessao();
	}

	public String acaoLimpar() {
		this.busca.acaoLimpar();
		return DEFAULT_RULE;
	}

	public String acaoCarregarBean() {
		this.busca.acaoCarregarRegistro();
		try {
			getDao().load(getBackBean());
		} catch (Exception e) {
			e.printStackTrace();
		}
		carregarExtra();
		return DEFAULT_RULE;
	}

	protected abstract void carregarExtra();

	public T getBackBean() {
		return this.backBean;
	}

	public void setBackBean(T backBean) {
		this.backBean = backBean;
	}

	public String getMsgBundle() {
		return this.msgBundle;
	}

	public void setMsgBundle(String msgBundle) {
		this.msgBundle = msgBundle;
	}

	public String getParamBusca() {
		return this.getBusca().getParams().get("param");
	}

	public void setParamBusca(String paramBusca) {
		this.getBusca().getParams().put("param", paramBusca);
	}

	public String getOpcao() {
		return this.getBusca().getParams().get("opcao");
	}

	public void setOpcao(String opcao) {
		this.getBusca().getParams().put("opcao", opcao);
	}

	public List<T> getProcurado() {
		return this.getBusca().getResultados();
	}

	public void setProcurado(List<T> procurado) {
		this.getBusca().setResultados(procurado);
	}

	public ManageSearch<T> getBusca() {
		return this.busca;
	}

	public void setBusca(ManageSearch<T> busca) {
		this.busca = busca;
	}

	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public ApplicationView getView() {
		return view;
	}

	public EntityManager<T> getDao() {
		return dao;
	}

	public void setView(ApplicationView view) {
		this.view = view;
	}

	public void makeView(Properties params) {
		this.view = ApplicationViewFactory.getViewInstance(ViewHandler.JSF,
				params);
	}

}
