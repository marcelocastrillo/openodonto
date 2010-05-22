package br.ueg.openodonto.dominio;

import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Table;
import br.ueg.openodonto.util.PalavrasFormatadas;

@Table(name="usuarios")
public class Usuario extends Pessoa<Usuario>{

	private static final long serialVersionUID = -8779459291420609676L;

	@Column(name="user")
	private String user;
	
	@Column(name="senha")
	private String senha;
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}	
	
	public String getNomeApresentacao(){
		return PalavrasFormatadas.formatarNome(getNome());
	}
	
}
