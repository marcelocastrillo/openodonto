package br.ueg.openodonto.persistencia;

import br.ueg.openodonto.dominio.Usuario;

/**
 * @author Vinicius
 *
 */
public interface DaoLoginIF {

	Usuario autenticar(Usuario usuario);
	
}
