package br.ueg.openodonto.controle;

import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.PropertyUtils;

import br.ueg.openodonto.controle.context.ApplicationContext;
import br.ueg.openodonto.controle.context.OpenOdontoContext;
import br.ueg.openodonto.controle.servico.ManageSearch;
import br.ueg.openodonto.controle.validador.AbstractValidator;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.EntityManagerIF;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.util.PalavrasFormatadas;
import br.ueg.openodonto.visao.ApplicationView;
import br.ueg.openodonto.visao.ApplicationViewFactory;
import br.ueg.openodonto.visao.ApplicationViewFactory.ViewHandler;


/**
 * @author vinicius.rodrigues
 *
 * @param <T>
 */

public abstract class ManageBeanGeral <T> {
	
	public static final String DEFAULT_RULE = "";

	private T                        backBean;	
	private Class<T>                 classe;	
	protected EntityManagerIF<T>     dao;	
	private ApplicationContext       context;	
	private ApplicationView          view;
	protected ResourceBundle         resourceBundle;	
	private String                   msgBundle;
	private ManageSearch<T>          busca;
	
	public ManageBeanGeral(Class<T> classe){
		this.classe = classe;
		init();
	}
	
	protected abstract void initExtra();
	
	protected void init(){
		this.backBean = fabricarNovoBean();
		this.dao = DaoFactory.getInstance().getDao(classe);
		this.resourceBundle = ResourceBundle.getBundle("br.ueg.openodonto.visao.i18n.mensagens");
		this.busca = new ManageSearch<T>(this,"backBean");
		this.context = new OpenOdontoContext();
		initExtra();
	}
	
	protected T fabricarNovoBean(){
		try {
			return classe.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String acaoShowed(){
		getView().showOut();
		return DEFAULT_RULE;
	}
	
	public void exibirSaida(){
		getView().showOut();
	}
	
	public void exibirPopUp(String message){
       getView().showPopUp(message);
	}
	
	protected abstract List<AbstractValidator> getCamposObrigatorios();
	
	protected abstract List<String> getCamposFormatados();
	
	protected void ValidarCamposExtras(){};	

	protected void acaoValidarCampos() throws Exception{		
		ValidarCamposExtras();
		List<String> camposFormatados = getCamposFormatados();
		for(String path : camposFormatados){
			Object o = PropertyUtils.getNestedProperty(getBackBean(),path);
			String campoParaFormatar = null;
			if(o != null)
				campoParaFormatar = (String)o;
			else
				continue;
			if(!campoParaFormatar.isEmpty()){
				String atributoFormatado = PalavrasFormatadas.clear(PalavrasFormatadas.remover(campoParaFormatar)).toUpperCase(); 
				PropertyUtils.setNestedProperty(getBackBean(), path, atributoFormatado);
			}
		}
	}
	
	protected boolean checarCamposObrigatoriosExtras(){return true;}
	
	protected boolean checarCamposObrigatorios(){		
		boolean returned = true;
		List<AbstractValidator> camposObrigatorios = getCamposObrigatorios();
		for(AbstractValidator validador : camposObrigatorios){
			if(validador.isValid(getBackBean())){
				getView().addLocalDynamicMenssage(validador.getMessage(), validador.getMessageOut(), false);
				returned = false;
			}
		}
		return checarCamposObrigatoriosExtras() && returned;
	}

	public abstract String acaoPesquisar();
	
	public String acaoAlterar() {
		boolean alredy = false;
		try{
			acaoValidarCampos();
			if(!checarCamposObrigatorios()){
				exibirPopUp("Campos obrigatorios nao preenchidos");
				getView().addLocalDynamicMenssage("Campos obrigatorios invalidos.", "saidaPadrao", true);
				return DEFAULT_RULE;
			}
			Long id = (Long)PropertyUtils.getNestedProperty(getBackBean(), "codigo");
			if (dao.pesquisar(id) != null)
				alredy = true;
			acaoSalvarExtra();
            this.dao.alterar(this.backBean);
			this.backBean = dao.getEntityBean();
		}catch(Exception ex){
			exibirPopUp(this.resourceBundle.getString("ErroSistema"));
			getView().addLocalMessage("ErroSistema", "saidaPadrao", true);
			ex.printStackTrace();
			return DEFAULT_RULE;
		}finally{
			dao.closeConnection();
		}
		init();
		exibirPopUp(this.resourceBundle.getString(alredy ? "Atualizado" : "Cadastro"));
		getView().addLocalMessage(alredy ? "Atualizado" : "Cadastro", "saidaPadrao", true);
		return DEFAULT_RULE;
	}
	
	public String acaoSalvarExtra(){return "";}

	
	public String acaoRemoverSim() {
		try {
			this.dao.remover(this.backBean);
		} catch (Exception e) {
			exibirPopUp("Nao foi possivel remover o registro.");
			getView().addLocalDynamicMenssage("Nao foi possivel remover o registro.", "saidaPadrao", true);
			return DEFAULT_RULE;
		}finally{
			dao.closeConnection();
		}
		init();
		exibirPopUp(this.resourceBundle.getString("removido"));
		getView().addLocalMessage("removido", "saidaPadrao", true);
		return DEFAULT_RULE;
	}
	
	public String acaoAtualizar(){
		getView().refresh();
		return DEFAULT_RULE;
	}
	
	protected Usuario getUsuarioSessao() {
		return getContext().getUsuarioSessao();
	}
	
	public String acaoLimpar(){
		this.busca.acaoLimpar();
		return DEFAULT_RULE;
	}

	public String acaoCarregarBean(){
		this.busca.acaoCarregarRegistro();
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

	public ApplicationView getView() {
		return view;
	}
	
	public void makeView(Properties params){
		this.view = ApplicationViewFactory.getViewInstance(ViewHandler.JSF, params);
	}
	
}
