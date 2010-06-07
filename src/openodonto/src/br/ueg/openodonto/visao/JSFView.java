package br.ueg.openodonto.visao;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ueg.openodonto.util.MessageBundle;
import br.ueg.openodonto.util.MessageBundle.MSG_TIPO;

public class JSFView implements ApplicationView{

	private Properties               params;
	private List<MessageBundle>      messageBundleList;
	protected ResourceBundle         resourceBundle;
	private boolean                  showPopUp;	
	private boolean                  show;
	private String                   msgPopUp;
	
	public JSFView(Properties params){
		if(params == null){
			throw new IllegalArgumentException("Params property cannot be NULL");
		}
		this.params = params;
		this.messageBundleList = new  ArrayList<MessageBundle>();
		this.resourceBundle = ResourceBundle.getBundle("br.ueg.openodonto.visao.i18n.mensagens");
	}

	public String getFormName(){
		return String.valueOf(params.get("formularioSaida"));
	}
	
	public String getManageBeanName(){
		return String.valueOf(params.get("managebeanName"));
	}
	
	@Override
	public void showOut() {
		for(MessageBundle mb : messageBundleList)
			if(mb.getTipo().equals(MSG_TIPO.dinamica))
				this.addResourceDynamicMenssage(mb.getMensagem() , mb.getOutComponent());
			else if(mb.getTipo().equals(MSG_TIPO.resource))
				this.addResourceMessage(mb.getMensagem() ,mb.getOutComponent());
		messageBundleList.clear();		
	}
	
	@Override
	public void showAction() {
		showOut();
		this.showPopUp = false;
	}

	@Override
	public void showPopUp(String msgPopUp) {
		this.showPopUp = true;
		this.msgPopUp = msgPopUp;		
	}

	@Override
	public void addResourceDynamicMenssage(String msg, String target) {
        FacesMessage facesMessage = new FacesMessage(msg);
        FacesContext.getCurrentInstance().addMessage(target, facesMessage);
	}
	
	@Override
	public void addResourceMessage(String key, String target) {
        FacesMessage facesMessage = new FacesMessage(this.resourceBundle.getString(key));
        FacesContext.getCurrentInstance().addMessage(target, facesMessage);
	}
	
	@Override
	public void addLocalDynamicMenssage(String msg,String target, boolean targetParam) {
		messageBundleList.add(new MessageBundle(MSG_TIPO.dinamica,
				msg,
				targetParam ? params.getProperty(target) : target));
	}
	
	@Override	
	public void addLocalMessage(String key,String target,boolean targetParam) {
		messageBundleList.add(new MessageBundle(MSG_TIPO.dinamica,
				this.resourceBundle.getString(key),
				targetParam ? params.getProperty(target) : target));
	}
	
	protected HttpServletRequest getRequest() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		return (HttpServletRequest) externalContext.getRequest();
	}

	protected HttpServletResponse getResponse() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		return (HttpServletResponse) externalContext.getResponse();
	}

	@Override
	public void refresh() {
		try {
			this.getRequest().getSession().removeAttribute(getManageBeanName());
			this.getResponse().sendRedirect("./index.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			showPopUp("Nao foi possivel recarregar as informacoes");
			messageBundleList.add(new MessageBundle(MSG_TIPO.dinamica,"Nao foi possivel recarregar as informacoes",getFormName()+":output"));
		}		
	}

	@Override
	public boolean getDisplayMessages() {		
		return show;
	}

	@Override
	public boolean getDisplayPopUp() {
		return showPopUp;
	}

	@Override
	public String getPopUpMsg() {
		return msgPopUp;
	}

	@Override
	public Properties getParams() {
		return params;
	}

}
