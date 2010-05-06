/**
 * 
 */
package br.ueg.openodonto.dominio;

import java.io.Serializable;

import br.ueg.openodonto.dominio.constante.TiposTelefone;

public class Telefone implements Serializable{	

	private static final long serialVersionUID = 77367905036522189L;

	private Long codigo;

	private String ddd;

	private String numero;

	private TiposTelefone tipoTelefone;
	
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

	public String toString(){
		return this.numero;
	}
		
}
