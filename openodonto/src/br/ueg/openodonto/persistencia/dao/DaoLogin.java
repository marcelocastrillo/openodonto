package br.ueg.openodonto.persistencia.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.exception.LoginInvalidoException;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.LoginManager;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

/**
 * @author Vinicius
 * 
 */
public class DaoLogin implements LoginManager,Serializable{

	private static final long serialVersionUID = 2771520846029661291L;
	
	private EntityManager<Usuario> daoUsuario;

    public DaoLogin() {
	this.daoUsuario = DaoFactory.getInstance().getDao(Usuario.class);
    }

    public Usuario autenticar(Usuario usuario) {
	OrmFormat orm = new OrmFormat(usuario);
	Map<String, Object> paramsMap = orm.format("user", "senha");
	List<Object> params = new ArrayList<Object>();
	params.add(paramsMap.get("user"));
	params.add(paramsMap.get("senha"));
	try {
	    List<Usuario> usuarios = daoUsuario.getSqlExecutor()
		    .executarNamedQuery("Usuario.autenticar", params, 1, "*");
	    if (usuarios.size() == 1) {
		return usuarios.get(0);
	    } else {
		throw new LoginInvalidoException("Login invalido");
	    }
	} catch (Exception e) {
	    throw new LoginInvalidoException("Erro de autenticação");
	}
    }

}
