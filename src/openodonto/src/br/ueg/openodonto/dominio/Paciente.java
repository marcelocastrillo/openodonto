package br.ueg.openodonto.dominio;

import java.sql.Date;

public class Paciente extends Pessoa{

	private static final long serialVersionUID = -8543328508793753975L;

	private String      cpf;	
	
	private Date        dataInicioTratamento;
	
	private Date        dataTerminoTratamento;
	
	private Date        dataRetorno;
	
	private Date        dataNascimento;
	
	private String      responsavel;
	
	private String      referencia;
	
	private String      observacao;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataInicioTratamento() {
		return dataInicioTratamento;
	}

	public void setDataInicioTratamento(Date dataInicioTratamento) {
		this.dataInicioTratamento = dataInicioTratamento;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Date getDataTerminoTratamento() {
		return dataTerminoTratamento;
	}

	public void setDataTerminoTratamento(Date dataTerminoTratamento) {
		this.dataTerminoTratamento = dataTerminoTratamento;
	}

	public Date getDataRetorno() {
		return dataRetorno;
	}

	public void setDataRetorno(Date dataRetorno) {
		this.dataRetorno = dataRetorno;
	}	

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public String toString() {
		return super.toString() + "Paciente [cpf=" + cpf + ", dataInicioTratamento="
				+ dataInicioTratamento + ", dataNascimento=" + dataNascimento
				+ ", dataRetorno=" + dataRetorno + ", dataTerminoTratamento="
				+ dataTerminoTratamento + ", observacao=" + observacao
				+ ", referencia=" + referencia + ", responsavel=" + responsavel
				+ "]";
	}	
	
}
