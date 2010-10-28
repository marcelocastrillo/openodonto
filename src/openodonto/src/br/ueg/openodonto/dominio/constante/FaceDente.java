package br.ueg.openodonto.dominio.constante;

public enum FaceDente {
	
	LIGUAL("Lingual ou Palatina"),VESTIBULAR("Vestibular"),MESIAL("Mesial"),DISTAL("Distal"),OCLUSAL("Oclusal ou Incisal");
	
	private String nome;
	
	private FaceDente(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	@Override
	public String toString() {
		return nome;
	}
	
}
