package br.ueg.openodonto.dominio;

import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.ForwardKey;
import br.ueg.openodonto.persistencia.orm.Id;
import br.ueg.openodonto.persistencia.orm.Table;
import br.ueg.openodonto.persistencia.orm.value.IdIncrementType;

@Table(name = "procedimentos_dentes")
public class ProcedimentoDente implements Entity{

	private static final long serialVersionUID = -2263101580965612324L;

	@Column(name="id")
	@Id(autoIncrement = IdIncrementType.IDENTITY)
	private Long codigo;
	
	@Id
	@Column(name="fk_dente",joinFields={@ForwardKey(tableField="dente",foreginField="fk_dente")})	
	private Long odontogramaDente;
	
	@Id
	@Column(name="fk_face",joinFields={@ForwardKey(tableField="face",foreginField="fk_face")})	
	private Long odontogramaFace;
	
	@Id
	@Column(name="fk_odontograma",joinFields={@ForwardKey(tableField="id_odontograma",foreginField="fk_odontograma")})	
	private Long procedimentoId;
	
	private Double valor;

	public ProcedimentoDente() {
	}
	
	public ProcedimentoDente(Long odontogramaDente, Long odontogramaFace,Long procedimentoId) {
		super();
		this.odontogramaDente = odontogramaDente;
		this.odontogramaFace = odontogramaFace;
		this.procedimentoId = procedimentoId;
	}

	public ProcedimentoDente(Long codigo) {
		super();
		this.codigo = codigo;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getOdontogramaDente() {
		return odontogramaDente;
	}

	public void setOdontogramaDente(Long odontogramaDente) {
		this.odontogramaDente = odontogramaDente;
	}

	public Long getOdontogramaFace() {
		return odontogramaFace;
	}

	public void setOdontogramaFace(Long odontogramaFace) {
		this.odontogramaFace = odontogramaFace;
	}

	public Long getProcedimentoId() {
		return procedimentoId;
	}

	public void setProcedimentoId(Long procedimentoId) {
		this.procedimentoId = procedimentoId;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime
				* result
				+ ((odontogramaDente == null) ? 0 : odontogramaDente.hashCode());
		result = prime * result
				+ ((odontogramaFace == null) ? 0 : odontogramaFace.hashCode());
		result = prime * result
				+ ((procedimentoId == null) ? 0 : procedimentoId.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
		ProcedimentoDente other = (ProcedimentoDente) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (odontogramaDente == null) {
			if (other.odontogramaDente != null)
				return false;
		} else if (!odontogramaDente.equals(other.odontogramaDente))
			return false;
		if (odontogramaFace == null) {
			if (other.odontogramaFace != null)
				return false;
		} else if (!odontogramaFace.equals(other.odontogramaFace))
			return false;
		if (procedimentoId == null) {
			if (other.procedimentoId != null)
				return false;
		} else if (!procedimentoId.equals(other.procedimentoId))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProcedimentoDente [codigo=" + codigo + ", odontogramaDente="
				+ odontogramaDente + ", odontogramaFace=" + odontogramaFace
				+ ", procedimentoId=" + procedimentoId + ", valor=" + valor
				+ "]";
	}	
	
}
