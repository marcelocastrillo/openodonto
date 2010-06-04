package br.ueg.openodonto.dominio.constante;

public enum TiposTelefone{

	CELULAR("Celular"),RESIDENCIAL("Residencial"),COMERCIAL("Comercial");
	
	private String descricao;
	
	private TiposTelefone(String descricao){
		this.descricao = descricao;			
	}
	
	public static TiposTelefone parse(int index){
		TiposTelefone[] values = TiposTelefone.values();
		if(index >= 0 || index < values.length){
			return values[index];
		}else{
			return null;
		}
			
	}
	
	public String getDescricao(){
		return descricao;
	}
	
	public static int format(TiposTelefone tipoTelefone){
		return tipoTelefone.ordinal();
	}
	
	public String toString(){
		return this.descricao;
	}
}
