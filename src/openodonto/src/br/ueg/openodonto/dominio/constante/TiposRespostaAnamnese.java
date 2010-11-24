package br.ueg.openodonto.dominio.constante;

public enum TiposRespostaAnamnese {

	SIM("Sim"),NAO("Não"),OUTRO("Outro");
	
	private String value;
	
	private TiposRespostaAnamnese(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return getValue();
	}
}
