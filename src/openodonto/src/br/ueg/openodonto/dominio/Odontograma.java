package br.ueg.openodonto.dominio;

import br.ueg.openodonto.dominio.constante.Dente;
import br.ueg.openodonto.dominio.constante.FaceDente;
import br.ueg.openodonto.dominio.constante.Procedimento;
import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Enumerator;
import br.ueg.openodonto.persistencia.orm.Id;
import br.ueg.openodonto.persistencia.orm.Table;
import br.ueg.openodonto.persistencia.orm.value.EnumValue;
import br.ueg.openodonto.persistencia.orm.value.IdIncrementType;

@Table(name="Odontograma")
public class Odontograma {

	@Column(name="id")
	@Id(autoIncrement=IdIncrementType.IDENTITY)
	private Long id;
	
	@Column(name="id_pessoa")
	private Long idPessoa;
	
	@Column(name="dente")
	@Enumerator(type=EnumValue.ORDINAL)
	private Dente dente;
	
	@Column(name="face")
	@Enumerator(type=EnumValue.ORDINAL)
	private FaceDente face;
	
	@Column(name="procedimento")
	@Enumerator(type=EnumValue.ORDINAL)
	private Procedimento procedimento;
	
	@Column(name="descricao")
	private String descricacao;

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

	public Dente getDente() {
		return dente;
	}

	public void setDente(Dente dente) {
		this.dente = dente;
	}

	public FaceDente getFace() {
		return face;
	}

	public void setFace(FaceDente face) {
		this.face = face;
	}

	public Procedimento getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(Procedimento procedimento) {
		this.procedimento = procedimento;
	}

	public String getDescricacao() {
		return descricacao;
	}

	public void setDescricacao(String descricacao) {
		this.descricacao = descricacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dente == null) ? 0 : dente.hashCode());
		result = prime * result
				+ ((descricacao == null) ? 0 : descricacao.hashCode());
		result = prime * result + ((face == null) ? 0 : face.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((idPessoa == null) ? 0 : idPessoa.hashCode());
		result = prime * result
				+ ((procedimento == null) ? 0 : procedimento.hashCode());
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
		if (dente == null) {
			if (other.dente != null)
				return false;
		} else if (!dente.equals(other.dente))
			return false;
		if (descricacao == null) {
			if (other.descricacao != null)
				return false;
		} else if (!descricacao.equals(other.descricacao))
			return false;
		if (face == null) {
			if (other.face != null)
				return false;
		} else if (!face.equals(other.face))
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
		if (procedimento == null) {
			if (other.procedimento != null)
				return false;
		} else if (!procedimento.equals(other.procedimento))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Odontograma [dente=" + dente + ", descricacao=" + descricacao
				+ ", face=" + face + ", id=" + id + ", idPessoa=" + idPessoa
				+ ", procedimento=" + procedimento + "]";
	}
	
	
	
}
