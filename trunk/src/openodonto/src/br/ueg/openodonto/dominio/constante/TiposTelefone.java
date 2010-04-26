package br.ueg.openodonto.dominio.constante;

public enum TiposTelefone{

	CELULAR("Celular"),RESIDENCIAL("Residencial"),COMERCIAL("Comercial");
	
	private String descricao;
	
	private TiposTelefone(String descricao){
		this.descricao = descricao;			
	}
	
	public String toString(){
		return this.descricao;
	}
}
