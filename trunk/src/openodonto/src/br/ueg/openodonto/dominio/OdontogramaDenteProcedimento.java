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
public class OdontogramaDenteProcedimento implements Entity{

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
	
	@Column(name="obs")
	private String observacao;
		
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

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public String toString() {
		return "OdontogramaDenteProcedimento [codigo=" + codigo + ", data="
				+ data + ", observacao=" + observacao + ", odontogramaDenteId="
				+ odontogramaDenteId + ", procedimentoId=" + procedimentoId
				+ ", status=" + status + ", valor=" + valor + "]";
	}
	
}
