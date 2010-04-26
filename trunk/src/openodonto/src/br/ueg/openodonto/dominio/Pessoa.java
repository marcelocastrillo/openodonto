package br.ueg.openodonto.dominio;

import java.util.ArrayList;
import java.util.List;

import br.ueg.openodonto.dominio.constante.TiposUF;


public abstract class Pessoa {
 
	private Long           codigo;
	 
	private String         email;
	 
	private String         nome;
	 
	private List<Telefone> telefone;
	
	private String         endereco;
	
	private String         cidade;
	
	private TiposUF        estado;
	 
	public Pessoa() {
		this.telefone = new ArrayList<Telefone>();
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Telefone> getTelefone() {
		return telefone;
	}

	public void setTelefone(List<Telefone> telefone) {
		this.telefone = telefone;
	}	
	
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public TiposUF getEstado() {
		return estado;
	}

	public void setEstado(TiposUF estado) {
		this.estado = estado;
	}
	
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	
}
 
