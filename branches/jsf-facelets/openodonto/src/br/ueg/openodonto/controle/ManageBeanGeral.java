package br.ueg.openodonto.controle;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.CommonSearchPessoaSelectedHandler;
import br.ueg.openodonto.controle.busca.CommonSearchSelectedHandler;
import br.ueg.openodonto.controle.context.ApplicationContext;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.util.PBUtil;
import br.ueg.openodonto.util.WordFormatter;
import br.ueg.openodonto.validator.Validator;
import br.ueg.openodonto.visao.ApplicationView;
import br.ueg.openodonto.visao.ApplicationViewFactory;
import br.ueg.openodonto.visao.ApplicationViewFactory.ViewHandler;
import br.ueg.openodonto.web.WebContext;

/**
 * @author vinicius.rodrigues
 * 
 * @param <T>
 */
public abstract class ManageBeanGeral<T extends Entity> implements Serializable{

	private static final long serialVersionUID = -6270953407778330292L;

	private T                   backBean;
	private Class<T>            classe;
	protected EntityManager<T>  dao;
	private ApplicationContext  context;
	private ApplicationView     view;
	private String              msgBundle;

	public ManageBeanGeral(Class<T> classe) {
		this.classe = classe;
		init();
	}
	
	protected void initExtra(){}

	protected void init() {
		this.backBean = fabricarNovoBean();
		this.dao = DaoFactory.getInstance().getDao(classe);		
		this.context = new WebContext();
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

	public void acaoShowed() {
		getView().showAction();
	}

	public void exibirSaida() {
		getView().showOut();
	}

	public void exibirPopUp(String message) {
		getView().showPopUp(message);
	}

	@SuppressWarnings("unchecked")
	protected List<ValidationRequest> getCamposObrigatorios(){
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	protected List<String> getCamposFormatados(){
		return Collections.EMPTY_LIST;
	}

	protected void ValidarCamposExtras(){};

	protected void acaoFormatarCampos() throws Exception {
		ValidarCamposExtras();
		List<String> camposFormatados = getCamposFormatados();
		for (String path : camposFormatados) {
			Object o = PBUtil.instance().getNestedProperty(getBackBean(), path);
			String campoParaFormatar = null;
			if (o != null){
				campoParaFormatar = String.valueOf(o);
			}else{
				continue;
			}
			if (!campoParaFormatar.isEmpty()) {
				String atributoFormatado = WordFormatter.clear(WordFormatter.remover(campoParaFormatar)).toUpperCase();
				PBUtil.instance().setNestedProperty(getBackBean(), path, atributoFormatado);
			}
		}
	}

	protected boolean checarCamposObrigatoriosExtras() {
		return true;
	}

	protected boolean checarCamposObrigatorios() throws Exception {
		boolean returned = true;
		List<ValidationRequest> camposObrigatorios = getCamposObrigatorios();
		for (ValidationRequest validation : camposObrigatorios) {
			Validator validador = validation.getValidator();
			validador.setValue(PBUtil.instance().getNestedProperty(getBackBean(), validation.getPath()));
			if (!validador.isValid()) {
				getView().addLocalDynamicMenssage("* " + validador.getErrorMessage(), validation.getOut(), false);
				returned = false;
			}
		}
		return checarCamposObrigatoriosExtras() && returned;
	}

	public void acaoSalvar() {
		boolean alredy = false;
		try {
			acaoFormatarCampos();
			if (!checarCamposObrigatorios()) {
				exibirPopUp(getView().getMessageFromResource("camposObrigatorios"));
				getView().addLocalDynamicMenssage("Campos obrigatorios invalidos.", "saidaPadrao", true);
				return;
			}
			if (dao.exists(getBackBean())){
				alredy = true;
			}
			acaoSalvarExtra();
			this.dao.alterar(this.backBean);
		} catch (Exception ex) {
			exibirPopUp(getView().getMessageFromResource("ErroSistema"));
			getView().addLocalMessage("ErroSistema", "saidaPadrao", true);
			ex.printStackTrace();
			return;
		} finally {
			dao.closeConnection();
		}
		init();
		exibirPopUp(getView().getMessageFromResource(alredy ? "Atualizado" : "Cadastro"));
		getView().addLocalMessage(alredy ? "Atualizado" : "Cadastro", "saidaPadrao", true);
	}

	public void acaoSalvarExtra() {}

	public void acaoRemoverSim() {
		try {
			this.dao.remover(this.backBean);
		} catch (Exception e) {
			exibirPopUp("Nao foi possivel remover o registro.");
			getView().addLocalDynamicMenssage("Nao foi possivel remover o registro.","saidaPadrao", true);
			return;
		} finally {
			dao.closeConnection();
		}
		init();
		exibirPopUp(getView().getMessageFromResource("removido"));
		getView().addLocalMessage("removido", "saidaPadrao", true);
	}

	public void acaoAtualizar() {
		getView().refresh();
	}

	protected Usuario getUsuarioSessao() {
		return getContext().getUsuarioSessao();
	}


	protected void carregarExtra(){};

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

	public void makeView(Map<String, String> params) {
		this.view = ApplicationViewFactory.getViewInstance(ViewHandler.JSF,	params);
	}
	
	protected class SearchPessoaSelectedHandler extends CommonSearchPessoaSelectedHandler<T>{
		private static final long serialVersionUID = 7996822907210618133L;
		@Override
		public EntityManager<T> getSuperDao() {
			return getDao();
		}
		@Override
		public void extraLoad() {
			carregarExtra();
		}
		@Override
		public Pessoa getBean() {
			return (Pessoa)getBackBean();
		}
		@SuppressWarnings("unchecked")
		@Override
		public void setBean(Pessoa bean) {
			setBackBean((T)bean);
		}	
	}	

	public class SearchSelectedHandler extends CommonSearchSelectedHandler<T>{
		private static final long serialVersionUID = 9152759135402687202L;
		public SearchSelectedHandler() {
			super(getDao());
		}
		@Override
		public void extraLoad() {
			carregarExtra();
		}
		@Override
		public T getBean() {
			return getBackBean();
		}
		@Override
		public void setBean(T bean) {
			setBackBean(bean);
		}
	}

}
