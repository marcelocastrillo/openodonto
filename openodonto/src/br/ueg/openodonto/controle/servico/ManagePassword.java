package br.ueg.openodonto.controle.servico;

import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.visao.ApplicationView;

public class ManagePassword {

	private static String       falsePwd;
	private String              senhaCadastro;
	private String              senha;
	private String              novaSenha;
	private String              confirmaNovaSenha;
	private Usuario             usuario;
	private boolean             sucessChange;
	private ApplicationView     view;
	
	static{
		falsePwd = "****************";	
	}
	
	public ManagePassword(Usuario usuario,ApplicationView view) {
		setUsuario(usuario);
		this.view = view;
	}

	public void acaoCancelar(){		
	}
	
	public void acaoMudarSenha(){		
	}
	
	public boolean getEnableChangePassword(){
		return getUsuario() != null && getUsuario().getCodigo() != null && getUsuario().getCodigo() > 0;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isSucessChange() {
		return sucessChange;
	}

	public void setSucessChange(boolean sucessChange) {
		this.sucessChange = sucessChange;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmaNovaSenha() {
		return confirmaNovaSenha;
	}

	public void setConfirmaNovaSenha(String confirmaNovaSenha) {
		this.confirmaNovaSenha = confirmaNovaSenha;
	}

	public String getSenhaCadastro() {
		return getEnableChangePassword() ? falsePwd : senhaCadastro;
	}

	public void setSenhaCadastro(String senhaCadastro) {
		this.senhaCadastro = senhaCadastro;
	}
	
}
