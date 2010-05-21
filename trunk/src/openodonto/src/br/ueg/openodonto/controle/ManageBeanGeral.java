package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.EntityManagerIF;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.servico.SubBusca;
import br.ueg.openodonto.servico.listagens.core.codec.Encoder;
import br.ueg.openodonto.servico.validador.AbstractValidator;
import br.ueg.openodonto.util.DialogoConfirmacaoIF;
import br.ueg.openodonto.util.MessageBundle;
import br.ueg.openodonto.util.PalavrasFormatadas;
import br.ueg.openodonto.util.ReflexaoUtil;
import br.ueg.openodonto.util.MessageBundle.MSG_TIPO;


/**
 * @author vinicius.rodrigues
 *
 * @param <T>
 */

public abstract class ManageBeanGeral <T> {

	private T backBean;	
	
	private DialogoConfirmacaoIF dialogModalPanel;
	
	private Class<T> classe;
	
	protected EntityManagerIF<T> dao;
	
	private String formularioSaida;
	
	protected ResourceBundle resourceBundle;	
	
	private String msgBundle;
	
	private boolean showPopUp;
	
	private List<MessageBundle> msgBundleEstatica;
	
	private boolean show;
	
	private SubBusca<T> busca;
	
	private String managebeanName;
	
	public ManageBeanGeral(Class<T> classe,String formularioSaida,String managebeanName){
		this.classe = classe;
		this.formularioSaida = formularioSaida;
		this.managebeanName = managebeanName;
		init();
	}
	
	protected abstract void initExtra();
	
	@SuppressWarnings("unchecked")
	protected void init(){
		Object keepAliveManageBean = getRequest().getAttribute(managebeanName);		
		if(keepAliveManageBean != null){
			setDialogModalPanel(((ManageBeanGeral<T>)keepAliveManageBean).dialogModalPanel);			
		}
		this.dialogModalPanel = new DialogoConfirmacaoJSF();
		this.backBean = fabricarNovoBean();
		this.dao = DaoFactory.getInstance().getDao(classe);
		this.resourceBundle = ResourceBundle.getBundle("br.ueg.openodonto.visao.i18n.mensagens");
		this.busca = new SubBusca<T>(this,"backBean");
		this.msgBundleEstatica = new  ArrayList<MessageBundle>();
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
	
	Map<String, MethodExpression> getActionForModal(){
		Map<String,MethodExpression> actionsForDialogModal = new HashMap<String, MethodExpression>();		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ELContext el = facesContext.getELContext();
		ExpressionFactory ex = facesContext.getApplication().getExpressionFactory();							
		MethodExpression act1 = ex.createMethodExpression(el,"#{"+getManagebeanName()+".acaoRemoverSim}",String.class,new Class<?>[0]);
		actionsForDialogModal.put("removerBean",act1);		
		return actionsForDialogModal;		
	}
	
	public void acaoShowed(ActionEvent evt){
		exibirSaida();
		this.showPopUp = false;
	}
	
	public void exibirSaida(){
		for(MessageBundle mb : msgBundleEstatica)
			if(mb.getTipo().equals(MSG_TIPO.dinamica))
				this.adicionarMensagemDinamicaJSF(mb.getMensagem() , mb.getOutComponent());
			else if(mb.getTipo().equals(MSG_TIPO.resource))
				this.adicionarMensagemJSF(mb.getMensagem() ,mb.getOutComponent());
		msgBundleEstatica.clear();
	}
	
	public void adicionarMensagemJSF(String chaveMensagem, String componente) {
		FacesMessage facesMessage = new FacesMessage(this.resourceBundle.getString(chaveMensagem));
		FacesContext.getCurrentInstance().addMessage(componente, facesMessage);
	}

	public void adicionarMensagemDinamicaJSF(String Mensagem, String componente) {
		FacesMessage facesMessage = new FacesMessage(Mensagem);
		FacesContext.getCurrentInstance().addMessage(componente, facesMessage);
	}
	
	public HttpServletRequest getRequest() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		return (HttpServletRequest) externalContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		return (HttpServletResponse) externalContext.getResponse();
	}
	
	public void exibirPopUp(String message){
		this.showPopUp = true;
		this.msgBundle = message;
	}
	
	protected abstract List<AbstractValidator> getCamposObrigatorios();
	
	protected abstract List<String> getCamposFormatados();
	
	protected void ValidarCamposExtras(){};	

	protected void acaoValidarCampos(){		
		ValidarCamposExtras();
		List<String> camposFormatados = getCamposFormatados();
		for(String path : camposFormatados){
			Object o = ReflexaoUtil.getTsimples(getBackBean(),path);
			String campoParaFormatar = null;
			if(o != null)
				campoParaFormatar = (String)o;
			else
				continue;
			if(!campoParaFormatar.isEmpty()){
				String atributoFormatado = PalavrasFormatadas.clear(PalavrasFormatadas.remover(campoParaFormatar)).toUpperCase(); 
				ReflexaoUtil.setTsimples(getBackBean(), path, atributoFormatado);
			}
		}
	}
	
	protected boolean checarCamposObrigatoriosExtras(){return true;}
	
	protected boolean checarCamposObrigatorios(){		
		boolean returned = true;
		List<AbstractValidator> camposObrigatorios = getCamposObrigatorios();
		for(AbstractValidator validador : camposObrigatorios){
			if(validador.isValid(getBackBean())){				
				msgBundleEstatica.add(new MessageBundle(MSG_TIPO.dinamica,validador.getMessage(), getFormularioSaida()+":"+validador.getMessageOut()));
				returned = false;
			}
		}
		return checarCamposObrigatoriosExtras() && returned;
	}

	public abstract void acaoPesquisar(ActionEvent evt);
	
	public void acaoAlterar(ActionEvent evt) {
		boolean alredy = false;
		acaoValidarCampos();
		if(!checarCamposObrigatorios()){
			exibirPopUp("Campos obrigatorios nao preenchidos");
			getMsgBundleEstatica().add(new MessageBundle(MSG_TIPO.dinamica,"Campos obrigatorios invalidos.", getFormularioSaida()+":output"));
			return;
		}
		try{
			Long id = Encoder.encode(classe, this.backBean);
			if (dao.pesquisar(id) != null)
				alredy = true;
			acaoSalvarExtra(evt);
            this.dao.alterar(this.backBean);
			this.backBean = dao.getEntityBean();
		}catch(Exception ex){
			exibirPopUp(this.resourceBundle.getString("ErroSistema"));
			msgBundleEstatica.add(new MessageBundle(MSG_TIPO.resource,"ErroSistema", getFormularioSaida()+":output"));
			ex.printStackTrace();
			return;
		}finally{
			dao.closeConnection();
		}
		init();
		exibirPopUp(this.resourceBundle.getString(alredy ? "Atualizado" : "Cadastro"));
		msgBundleEstatica.add(new MessageBundle(MSG_TIPO.resource, alredy ? "Atualizado" : "Cadastro", getFormularioSaida()+":output"));
	}
	
	public void acaoSalvarExtra(ActionEvent evt){}

	@SuppressWarnings("unchecked")
	public void acaoRemover(ActionEvent evt) {
		show = false;
		Long id = Encoder.encode(classe, this.backBean);
		try{
		if (this.backBean == null || dao.pesquisar(id) == null)
			return;
		}finally{
			dao.closeConnection();	
		}
		ManageBeanGeral<T> manageBean = (ManageBeanGeral<T>) getRequest().getAttribute(managebeanName);
		Map<String, String> values = new HashMap<String, String>();
		values.put("yes", "Sim");
		values.put("no", "Nao");
		values.put("title", "Remover registro");
		values.put("message", "Deseja realmente excluir o registro ?");
		values.put("acaoYes", "removerBean");
		values.put("reRenderWhenOk", getFormularioSaida());
		manageBean.dialogModalPanel.setActionsForDialogModal(getActionForModal());
		manageBean.dialogModalPanel.renderConfirmDialog(null, values);
		show = true;
	}
	
	public void acaoRemoverSim(ActionEvent evt) {
		try {
			this.dao.remover(this.backBean);
		} catch (Exception e) {
			exibirPopUp("Nao foi possivel remover o registro.");
			msgBundleEstatica.add(new MessageBundle(MSG_TIPO.dinamica, "Nao foi possivel remover o registro.", formularioSaida+":output"));
			return;
		}finally{
			dao.closeConnection();
		}
		init();
		exibirPopUp(this.resourceBundle.getString("removido"));
		msgBundleEstatica.add(new MessageBundle(MSG_TIPO.resource, "removido", formularioSaida+":output"));
	}
	
	public void acaoAtualizar(ActionEvent evt){
		try {
			this.getRequest().getSession().removeAttribute(managebeanName);
			this.getResponse().sendRedirect("./index.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			exibirPopUp("Nao foi possivel recarregar as informacoes");
			getMsgBundleEstatica().add(new MessageBundle(MSG_TIPO.dinamica,"Nao foi possivel recarregar as informacoes",getFormularioSaida()+":output"));
		}
	}
	
	protected Usuario getUsuarioSessao() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Object usuarioObject = request.getSession().getAttribute("autenticacao");
		if(usuarioObject != null){
		    Usuario usuario = (Usuario)usuarioObject;
		    return usuario;
		}
		return null;
	}
	
	public void acaoLimpar(ActionEvent evt){
		this.busca.acaoLimpar(evt);
	}

	public void acaoCarregarBean(ActionEvent evt){
		this.busca.acaoCarregarRegistro(evt);
		carregarExtra();
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
	
	public boolean getShowPopUp() {	
		return this.showPopUp;
	}
	
	public void setShowPopUp(boolean showPopUp) {	
		this.showPopUp = showPopUp;
	}
	
	public List<MessageBundle> getMsgBundleEstatica() {	
		return this.msgBundleEstatica;
	}
	public boolean getShow() {	
		return this.show;
	}
	
	public void setShow(boolean show) {	
		this.show = show;
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
	
	public String getFormularioSaida() {	
		return this.formularioSaida;
	}
	
	public void setFormularioSaida(String formularioSaida) {	
		this.formularioSaida = formularioSaida;
	}
	
	public SubBusca<T> getBusca() {	
		return this.busca;
	}
	
	public void setBusca(SubBusca<T> busca) {	
		this.busca = busca;
	}

	
	public String getManagebeanName() {
	
		return this.managebeanName;
	}
	
	public void setManagebeanName(String managebeanName) {	
		this.managebeanName = managebeanName;
	}

	public DialogoConfirmacaoIF getDialogModalPanel() {		
		return dialogModalPanel;
	}

	public void setDialogModalPanel(DialogoConfirmacaoIF dialogModalPanel) {
		this.dialogModalPanel = dialogModalPanel;
	}	
	
	
}
