package br.ueg.openodonto.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.ueg.openodonto.dominio.constante.TiposUF;
import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.EntityBase;
import br.ueg.openodonto.persistencia.orm.Table;

@Table(name="pessoas")
public abstract class Pessoa<T> extends EntityBase<T> implements Serializable,Entity<T>{
 
	private static final long serialVersionUID = -7734113332277720024L;

	@Column(name="id")
	private Long           codigo;
	 
	@Column(name="email")
	private String         email;
	 
	@Column(name="nome")
	private String         nome;
	 
	private List<Telefone> telefone;
	
	@Column(name="endereco")
	private String         endereco;

	@Column(name="cidade")
	private String         cidade;
	
	@Column(name="estado")
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

	@Override
	public String toString() {
		return "Pessoa [cidade=" + cidade + ", codigo=" + codigo + ", email="
				+ email + ", endereco=" + endereco + ", estado=" + estado
				+ ", nome=" + nome + ", telefone=" + telefone + "]";
	}	
	
}
 
