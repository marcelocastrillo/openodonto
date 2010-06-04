/**
 * 
 */
package br.ueg.openodonto.dominio;

import java.io.Serializable;

import br.ueg.openodonto.dominio.constante.TiposTelefone;
import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.EntityBase;
import br.ueg.openodonto.persistencia.orm.Enumerator;
import br.ueg.openodonto.persistencia.orm.Id;
import br.ueg.openodonto.persistencia.orm.Table;
import br.ueg.openodonto.persistencia.orm.value.EnumValue;
import br.ueg.openodonto.persistencia.orm.value.IdIncrementType;

@Table(name="telefones")
public class Telefone extends EntityBase implements Serializable,Entity{	

	private static final long serialVersionUID = 77367905036522189L;
	
	@Column(name="id")
	@Id(autoIncrement=IdIncrementType.IDENTITY)
	private Long codigo;

	@Column(name="ddd")
	private String ddd;

	@Column(name="numero")
	private String numero;

	@Column(name="tipo")
	@Enumerator(type=EnumValue.ORDINAL)
	private TiposTelefone tipoTelefone;
	
	@Column(name="id_pessoa")
	private Long id_pessoa;
	
	public Telefone() {
	}
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDdd() {
		return this.ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getNumero() {
		return this.numero;
	}
	

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public TiposTelefone getTipoTelefone() {
		return tipoTelefone;
	}

	public void setTipoTelefone(TiposTelefone tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}	

	public Long getId_pessoa() {
		return id_pessoa;
	}

	public void setId_pessoa(Long idPessoa) {
		id_pessoa = idPessoa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((id_pessoa == null) ? 0 : id_pessoa.hashCode());
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
		Telefone other = (Telefone) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (id_pessoa == null) {
			if (other.id_pessoa != null)
				return false;
		} else if (!id_pessoa.equals(other.id_pessoa))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Telefone [codigo=" + codigo + ", ddd=" + ddd + ", id_pessoa="
				+ id_pessoa + ", numero=" + numero + ", tipoTelefone="
				+ tipoTelefone + "]";
	}

		
}
