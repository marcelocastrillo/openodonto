package br.ueg.openodonto.controle.context;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ueg.openodonto.dominio.Usuario;

public class OpenOdontoContext implements ApplicationContext{
	
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
	public Usuario getUsuarioSessao() {
		return getAttribute("autenticacao", Usuario.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name,Class<T> classe) {
		Object bean = getRequest().getSession().getAttribute(name);
		if(bean != null){
			return (T)bean;
		}else{
			return  null;
		}	
	}

	@Override
	public String getParameter(String name) {
		return getRequest().getParameter(name);
	}
	
}
