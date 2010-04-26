package br.ueg.openodonto.persistencia.dao;

import br.ueg.openodonto.controle.exception.LoginInvalidoException;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.DaoLoginIF;



/**
 * @author Vinicius
 *
 */
public class DaoLoginIMPL implements DaoLoginIF{

	public DaoLoginIMPL() {
	}
	
	public Usuario autenticar(Usuario usuario) {		
		if(usuario != null && usuario.getUser().equals("admin") && usuario.getSenha().endsWith("123")){
			Usuario logado = new Usuario();
			logado.setNome("Vinicius G G R");
			logado.setSenha("12345");
			return logado;		
		}else{
			throw new LoginInvalidoException("Login invalido");
		}

	}
	
	
}
