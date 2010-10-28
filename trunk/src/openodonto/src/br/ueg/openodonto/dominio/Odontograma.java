package br.ueg.openodonto.dominio;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import br.ueg.openodonto.dominio.constante.Dente;
import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.Id;
import br.ueg.openodonto.persistencia.orm.Relationship;
import br.ueg.openodonto.persistencia.orm.Table;
import br.ueg.openodonto.persistencia.orm.value.IdIncrementType;

@Table(name = "Odontograma")
public class Odontograma implements Entity {

	private static final long serialVersionUID = 6394723722499152433L;

	@Column(name = "id")
	@Id(autoIncrement = IdIncrementType.IDENTITY)
	private Long id;

	@Column(name = "id_pessoa")
	private Long idPessoa;

	@Column(name = "nome")
	private String nome;

	@Column(name = "descricao")
	private String descricacao;

	@Column(name = "data")
	private Date data;

	@Relationship
	private Map<String,Dente> dentes;
	
	public Odontograma() {
		dentes = new HashMap<String, Dente>();
		dentes.put("dente18", Dente.DENTE_18);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Long idPessoa) {
		this.idPessoa = idPessoa;
	}

	public String getDescricacao() {
		return descricacao;
	}

	public void setDescricacao(String descricacao) {
		this.descricacao = descricacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}	
	
	@Override
	public String toString() {
		return "Odontograma [data=" + data + ", descricacao=" + descricacao
				+ ", id=" + id + ", idPessoa=" + idPessoa + ", nome=" + nome
				+ "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result
				+ ((descricacao == null) ? 0 : descricacao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idPessoa == null) ? 0 : idPessoa.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Odontograma other = (Odontograma) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (descricacao == null) {
			if (other.descricacao != null)
				return false;
		} else if (!descricacao.equals(other.descricacao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idPessoa == null) {
			if (other.idPessoa != null)
				return false;
		} else if (!idPessoa.equals(other.idPessoa))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	public Map<String, Dente> getDentes() {
		return dentes;
	}

	public void setDentes(Map<String, Dente> dentes) {
		this.dentes = dentes;
	}
}
