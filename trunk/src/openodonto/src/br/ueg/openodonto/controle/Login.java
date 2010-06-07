package br.ueg.openodonto.controle;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.ueg.openodonto.controle.exception.LoginInvalidoException;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.LoginManager;
import br.ueg.openodonto.persistencia.dao.DaoLogin;


/**
 * @author Vinicius
 *
 */
public class Login  {

	private Usuario usuario;
	private LoginManager loginDao;
	
	public Login() {
		super();
		this.usuario = new Usuario();
		this.loginDao = new DaoLogin();
	}

	
	public void acaoAutenticarUsuario(ActionEvent evt) {		
			
		try {				
			Usuario usuarioSessao = this.loginDao.autenticar(this.usuario);			
			getRequest().getSession().setAttribute("usuarioSessao", usuarioSessao);			
			getResponse().sendRedirect("../index.jsp");
			
		} catch (LoginInvalidoException e) {
			FacesContext.getCurrentInstance().addMessage("LoginForm:messageLogin", new FacesMessage("Usuario/senha incorreto(s)."));
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage("LoginForm:messageLogin", new FacesMessage("Falha ao redirecionar."));
			e.printStackTrace();
		}catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("LoginForm:messageLogin", new FacesMessage("Erro de causa desconhecida."));
			e.printStackTrace();
		}			
	}
	
	public void acaoLogout(ActionEvent evt){
		this.getRequest().getSession().invalidate();
		try {
			this.getResponse().sendRedirect("/openodonto/faces/login/login.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private HttpServletRequest getRequest(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return (HttpServletRequest) facesContext.getExternalContext().getRequest();
	}
	
	private HttpServletResponse getResponse(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return (HttpServletResponse) facesContext.getExternalContext().getResponse();
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}	
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
