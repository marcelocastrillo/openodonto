package br.ueg.openodonto.persistencia.dao;

import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.orm.Dao;

@Dao(classe=Usuario.class)
public class DaoUsuario extends DaoAbstractPessoa<Usuario> {

	private static final long serialVersionUID = 4857838625916905656L;
	
	public DaoUsuario() {
		super(Usuario.class);
	}

	@Override
	public Usuario getNewEntity() {
		return new Usuario(); // Melhor que reflexão
	}
}
