package br.ueg.openodonto.persistencia.dao;

import java.util.List;

import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.dao.sql.SqlExecutor;

public class DaoCrudUsuario extends BaseDAO<Usuario>{

	public DaoCrudUsuario() {
		super(Usuario.class);
	}

	private static final long serialVersionUID = 4857838625916905656L;

	@Override
	public Usuario getNewEntity() {
		return new Usuario();
	}

	@Override
	public void alterar(Usuario o) throws Exception {
	
	}

	@Override
	public SqlExecutor<Usuario> getSqlExecutor() {
		return null;
	}

	@Override
	public void inserir(Usuario o) throws Exception {
	
	}

	@Override
	public List<Usuario> listar() {
		return null;
	}

	@Override
	public Usuario pesquisar(Object key) {
		return null;
	}

	@Override
	public void remover(Usuario o) throws Exception {
		
	}

}
