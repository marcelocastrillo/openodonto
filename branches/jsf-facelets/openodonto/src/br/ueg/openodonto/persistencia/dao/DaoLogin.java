package br.ueg.openodonto.persistencia.dao;

import java.io.Serializable;
import java.util.List;

import br.ueg.openodonto.controle.exception.LoginInvalidoException;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.LoginManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

/**
 * @author Vinicius
 * 
 */
public class DaoLogin implements LoginManager, Serializable {

	private static final long serialVersionUID = 2771520846029661291L;

	private EntityManager<Usuario> daoUsuario;

	public DaoLogin() {
		this.daoUsuario = DaoFactory.getInstance().getDao(Usuario.class);
	}

	public Usuario autenticar(Usuario usuario) {
		OrmFormat orm = new OrmFormat(usuario);		
		IQuery query = CrudQuery.getSelectQuery(Usuario.class, orm.format("user", "senha"),  "*");
		try {
			List<Usuario> usuarios = daoUsuario.getSqlExecutor().executarQuery(query);
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
