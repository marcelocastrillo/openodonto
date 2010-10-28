package br.ueg.openodonto.controle.servico;

import br.ueg.openodonto.dominio.Odontograma;
import br.ueg.openodonto.dominio.constante.FaceDente;

public class ManageOdontograma {

	private Odontograma odontograma;
	private FaceDente   face;
	
	public ManageOdontograma() {
		this.odontograma = new Odontograma();
	}
	
	public void acaoManageProcedimento(){	
		System.out.println(face);
	}
	
	public Odontograma getOdontograma() {
		return odontograma;
	}
	public void setOdontograma(Odontograma odontograma) {
		this.odontograma = odontograma;
	}
	public FaceDente getFace() {
		return face;
	}
	public void setFace(FaceDente face) {
		this.face = face;
	}	
	
}
