package br.ueg.openodonto.dominio;

import java.sql.Date;

import br.ueg.openodonto.dominio.constante.TipoStatusProcedimento;
import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.Enumerator;
import br.ueg.openodonto.persistencia.orm.ForwardKey;
import br.ueg.openodonto.persistencia.orm.Id;
import br.ueg.openodonto.persistencia.orm.Table;
import br.ueg.openodonto.persistencia.orm.value.EnumValue;
import br.ueg.openodonto.persistencia.orm.value.IdIncrementType;

@Table(name = "procedimentos_dentes")
public class OdontogramaDenteProcedimento implements Entity{ //ColaboradorProduto

	private static final long serialVersionUID = -2263101580965612324L;

	@Column(name="id")
	@Id(autoIncrement = IdIncrementType.IDENTITY)
	private Long codigo;
	
	@Column(name="fk_procedimento",joinFields={@ForwardKey(tableField="id_procedimento",foreginField="fk_procedimento")})	
	private Long procedimentoId;
	
	@Column(name="fk_odontograma_dente",joinFields={@ForwardKey(tableField="id_odontograma_dente",foreginField="fk_odontograma_dente")})	
	private Long odontogramaDenteId;
	
	private Float valor;

	@Column(name = "status")
	@Enumerator(type = EnumValue.ORDINAL)
	private TipoStatusProcedimento status;
	
	@Column(name="data_procedimento")
	private Date data;
	
		
	public OdontogramaDenteProcedimento(Long procedimentoId,Long odontogramaDenteId) {
		this();
		this.procedimentoId = procedimentoId;
		this.odontogramaDenteId = odontogramaDenteId;
	}
	
	public OdontogramaDenteProcedimento(Long codigo) {
		this();
		this.codigo = codigo;
	}
	
	public OdontogramaDenteProcedimento() {
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getProcedimentoId() {
		return procedimentoId;
	}

	public void setProcedimentoId(Long procedimentoId) {
		this.procedimentoId = procedimentoId;
	}
	
	public Long getOdontogramaDenteId() {
		return odontogramaDenteId;
	}

	public void setOdontogramaDenteId(Long odontogramaDenteId) {
		this.odontogramaDenteId = odontogramaDenteId;
	}	

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public TipoStatusProcedimento getStatus() {
		return status;
	}

	public void setStatus(TipoStatusProcedimento status) {
		this.status = status;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime
				* result
				+ ((odontogramaDenteId == null) ? 0 : odontogramaDenteId
						.hashCode());
		result = prime * result
				+ ((procedimentoId == null) ? 0 : procedimentoId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		OdontogramaDenteProcedimento other = (OdontogramaDenteProcedimento) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (odontogramaDenteId == null) {
			if (other.odontogramaDenteId != null)
				return false;
		} else if (!odontogramaDenteId.equals(other.odontogramaDenteId))
			return false;
		if (procedimentoId == null) {
			if (other.procedimentoId != null)
				return false;
		} else if (!procedimentoId.equals(other.procedimentoId))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
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
		return "ProcedimentoDente [codigo=" + codigo + ", data=" + data
				+ ", odontogramaDenteId=" + odontogramaDenteId
				+ ", procedimentoId=" + procedimentoId + ", status=" + status
				+ ", valor=" + valor + "]";
	}
	
}
