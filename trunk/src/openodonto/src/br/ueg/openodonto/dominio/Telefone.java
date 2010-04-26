/**
 * 
 */
package br.ueg.openodonto.dominio;

import br.ueg.openodonto.dominio.constante.TiposTelefone;

public class Telefone{	

	private Long codigo;

	private String ddd;

	private String numero;

	private TiposTelefone tipoTelefone;
	
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

	public String toString(){
		return this.numero;
	}	

		
}
