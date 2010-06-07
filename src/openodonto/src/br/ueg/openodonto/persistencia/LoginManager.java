package br.ueg.openodonto.persistencia;

import br.ueg.openodonto.dominio.Usuario;

/**
 * @author Vinicius
 *
 */
public interface LoginManager {

	Usuario autenticar(Usuario usuario);
	
}
