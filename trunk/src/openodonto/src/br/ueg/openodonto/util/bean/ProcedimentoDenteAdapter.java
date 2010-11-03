package br.ueg.openodonto.util.bean;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import br.ueg.openodonto.dominio.OdontogramaDenteProcedimento;
import br.ueg.openodonto.dominio.Procedimento;
import br.ueg.openodonto.util.WordFormatter;

public class ProcedimentoDenteAdapter {

	private OdontogramaDenteProcedimento 	odp;
	private Procedimento                 	procedimento;
	private String                          valor;
	private DecimalFormat                   dcf;
	
	public ProcedimentoDenteAdapter(OdontogramaDenteProcedimento odp,Procedimento procedimento) {
		this();
		this.odp = odp;
		this.procedimento = procedimento;
	}

	public ProcedimentoDenteAdapter() {
		super();
		this.dcf = (DecimalFormat) NumberFormat.getCurrencyInstance();
	}	
	
	public OdontogramaDenteProcedimento getOdp() {
		return odp;
	}
	public void setOdp(OdontogramaDenteProcedimento odp) {
		this.odp = odp;
	}
	public Procedimento getProcedimento() {
		return procedimento;
	}
	public void setProcedimento(Procedimento procedimento) {
		this.procedimento = procedimento;
	}
	public String getObservacao(){
		return WordFormatter.abstractStr(getOdp().getObservacao(), 30);
	}
	public void setObservacao(String observacao){
		if(observacao != null && !observacao.equals(getObservacao())){
			getOdp().setObservacao(observacao);
		}
	}
	private void formatValor(){
		if(valor == null && getOdp().getValor() != null){
			String valor = dcf.format(getOdp().getValor());
			this.valor = valor;
		}
	}
	public String getValor() {
		formatValor();
		return valor;
	}
	public void setValor(String valor) {
		if(valor != null && !valor.isEmpty()){
			try {
				Float nvalor = dcf.parse(valor).floatValue();
				getOdp().setValor(nvalor);
				this.valor = null;
				formatValor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	
	
}
