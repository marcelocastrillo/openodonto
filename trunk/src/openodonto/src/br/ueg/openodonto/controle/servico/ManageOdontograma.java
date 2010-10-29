package br.ueg.openodonto.controle.servico;

import br.ueg.openodonto.dominio.Odontograma;
import br.ueg.openodonto.dominio.constante.Dente;
import br.ueg.openodonto.dominio.constante.FaceDente;

public class ManageOdontograma {

	private Odontograma odontograma;
	private Integer numero;
	private String face;
	private Dente dente;
	private FaceDente faceObj;
	
	public ManageOdontograma() {
		this.odontograma = new Odontograma();
	}	

	public void acaoManageProcedimento(){
	    System.out.println("[ACAO]");
	    evaluateSelected();
	}
	
	private void evaluateSelected(){
	    Dente dente = Dente.getDente(getNumero());
	    FaceDente faceObj = null;
	    try {
            faceObj = FaceDente.valueOf(getFace().toUpperCase());
        }catch (Exception e) {
            e.printStackTrace();
        }        
	    setDente(dente);
	    if(dente != null){
	       dente.setGenericFace(faceObj); 
	    }
	    setFaceObj(faceObj);
	}
	public Odontograma getOdontograma() {
		return odontograma;
	}	
	public void setOdontograma(Odontograma odontograma) {
		this.odontograma = odontograma;
	}	
    public Integer getNumero() {
        return numero;
    }    
    public void setNumero(Integer numero) {
        this.numero = numero;
    }
    public String getFace() {
        return face;
    }
    public void setFace(String face) {
        this.face = face;
    }
    public Dente getDente() {
        return dente;
    }
    public void setDente(Dente dente) {
        this.dente = dente;
    }
    public FaceDente getFaceObj() {
        return faceObj;
    }
    public void setFaceObj(FaceDente faceObj) {
        this.faceObj = faceObj;
    }   
}
