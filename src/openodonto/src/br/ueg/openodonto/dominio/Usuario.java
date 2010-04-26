package br.ueg.openodonto.dominio;

import br.ueg.openodonto.util.PalavrasFormatadas;

public class Usuario extends Pessoa{

	private String user;
	
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
