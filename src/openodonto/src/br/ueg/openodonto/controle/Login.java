package br.ueg.openodonto.controle;

import java.util.Properties;

import br.ueg.openodonto.controle.context.ApplicationContext;
import br.ueg.openodonto.controle.context.OpenOdontoWebContext;
import br.ueg.openodonto.controle.exception.LoginInvalidoException;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.LoginManager;
import br.ueg.openodonto.persistencia.dao.DaoLogin;
import br.ueg.openodonto.util.ShaUtil;
import br.ueg.openodonto.visao.ApplicationView;
import br.ueg.openodonto.visao.ApplicationViewFactory;
import br.ueg.openodonto.visao.ApplicationViewFactory.ViewHandler;

/**
 * @author Vinicius
 * 
 */
public class Login {

    private Usuario usuario;
    private LoginManager loginDao;
    private ApplicationContext context;
    private ApplicationView view;

    public Login() {
	super();
	this.usuario = new Usuario();
	this.loginDao = new DaoLogin();
	this.context = new OpenOdontoWebContext();
	Properties params = new Properties();
	makeView(params);
    }

    public String acaoAutenticarUsuario() {

	try {
	    usuario.setSenha(ShaUtil.hash(usuario.getSenha()));
	    Usuario usuarioSessao = this.loginDao.autenticar(this.usuario);
	    context.addAttribute("usuarioSessao", usuarioSessao);
	    return "index";

	} catch (LoginInvalidoException e) {
	    usuario.setSenha(null);
	    view.addResourceDynamicMenssage("Usuario/senha incorreto(s).",
		    "LoginForm:messageLogin");
	} catch (Exception e) {
	    usuario.setSenha(null);
	    view.addResourceDynamicMenssage("Usuario/senha incorreto(s).",
		    "Erro de causa desconhecida.");
	    e.printStackTrace();
	}
	return null;
    }

    public String acaoLogout() {
	context.removeAttribute("usuarioSessao");
	return "login";
    }

    public Usuario getUsuario() {
	return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
    }

    public void makeView(Properties params) {
	this.view = ApplicationViewFactory.getViewInstance(ViewHandler.JSF,
		params);
    }
}
