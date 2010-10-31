package br.ueg.openodonto.dominio;

import java.sql.Date;
import java.util.TreeSet;

import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.Id;
import br.ueg.openodonto.persistencia.orm.Relationship;
import br.ueg.openodonto.persistencia.orm.Table;
import br.ueg.openodonto.persistencia.orm.value.IdIncrementType;
import br.ueg.openodonto.util.OdontogramaDenteComparator;

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
	private TreeSet<OdontogramaDente> odontogramaDentes;
	
	public Odontograma(Long idPessoa) {
		this();
		this.idPessoa = idPessoa;
	}

	public Odontograma() {
		super();
		odontogramaDentes = new TreeSet<OdontogramaDente>(new OdontogramaDenteComparator());
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
	
	public TreeSet<OdontogramaDente> getOdontogramaDentes() {
		return odontogramaDentes;
	}

	public void setOdontogramaDentes(TreeSet<OdontogramaDente> odontogramaDentes) {
		this.odontogramaDentes = odontogramaDentes;
	}	
	
	@Override
	public String toString() {
		return "Odontograma [data=" + data + ", descricacao=" + descricacao
				+ ", id=" + id + ", idPessoa=" + idPessoa + ", nome=" + nome
				+ ", odontogramaDentes=" + odontogramaDentes + "]";
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
		result = prime
				* result
				+ ((odontogramaDentes == null) ? 0 : odontogramaDentes
						.hashCode());
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
		if (odontogramaDentes == null) {
			if (other.odontogramaDentes != null)
				return false;
		} else if (!odontogramaDentes.equals(other.odontogramaDentes))
			return false;
		return true;
	}
}
