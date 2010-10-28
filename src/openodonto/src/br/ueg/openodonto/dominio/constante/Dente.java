package br.ueg.openodonto.dominio.constante;

public enum Dente {
	
	DENTE_18(18,"Terceiro Molar"),	DENTE_17(17,"Segundo Molar"),	DENTE_16(16,"Primeiro Molar"),	DENTE_15(15,"Segundo Pré-Molar"),	DENTE_14(14,"Primeiro Pré-Molar"),	DENTE_13(13,"Canino"),	DENTE_12(12,"Incisivo Lateral"),	DENTE_11(11,"Incisivo Central"),
	DENTE_21(21,"Incisivo Central"),	DENTE_22(22,"Incisivo Lateral"),	DENTE_23(23,"Canino"),	DENTE_24(24,"Primeiro Pré-Molar"),	DENTE_25(25,"Segundo Pré-Molar"),	DENTE_26(26,"Primeiro Molar"),	DENTE_27(27,"Segundo Molar"),	DENTE_28(28,"Terceiro Molar"),
	
	DENTE_48(48,"Terceiro Molar"),	DENTE_47(47,"Segundo Molar"),	DENTE_46(46,"Primeiro Molar"),	DENTE_45(45,"Segundo Pré-Molar"),	DENTE_44(44,"Primeiro Pré-Molar"),	DENTE_43(43,"Canino"),	DENTE_42(42,"Incisivo Lateral"),	DENTE_41(41,"Incisivo Central"),
	DENTE_31(31,"Incisivo Central"),	DENTE_32(32,"Incisivo Lateral"),	DENTE_33(33,"Canino"),	DENTE_34(34,"Primeiro Pré-Molar"),	DENTE_35(35,"Segundo Pré-Molar"),	DENTE_36(36,"Primeiro Molar"),	DENTE_37(37,"Segundo Molar"),	DENTE_38(38,"Terceiro Molar");
		
	private FaceDente   lingual;
	private FaceDente   vestibular;
	private FaceDente   mesial;
	private FaceDente   distal;
	private FaceDente   oclusal;
	private Integer     numero;
	private String      nome;
	
	private Dente(Integer numero,String nome) {
		this.nome = nome;
		this.numero = numero;
		this.lingual = FaceDente.LIGUAL;
		this.vestibular = FaceDente.VESTIBULAR;
		this.mesial = FaceDente.MESIAL;
		this.distal = FaceDente.DISTAL;
		this.oclusal = FaceDente.OCLUSAL;
	}

	public FaceDente getLingual() {
		return lingual;
	}

	public void setLingual(FaceDente lingual) {
		this.lingual = lingual;
	}

	public FaceDente getVestibular() {
		return vestibular;
	}

	public void setVestibular(FaceDente vestibular) {
		this.vestibular = vestibular;
	}

	public FaceDente getMesial() {
		return mesial;
	}

	public void setMesial(FaceDente mesial) {
		this.mesial = mesial;
	}

	public FaceDente getDistal() {
		return distal;
	}

	public void setDistal(FaceDente distal) {
		this.distal = distal;
	}

	public FaceDente getOclusal() {
		return oclusal;
	}

	public void setOclusal(FaceDente oclusal) {
		this.oclusal = oclusal;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	@Override
	public String toString() {
		return "Dente [distal=" + distal + ", lingual=" + lingual + ", mesial="
				+ mesial + ", nome=" + nome + ", numero=" + numero
				+ ", oclusal=" + oclusal + ", vestibular=" + vestibular + "]";
	}	
	
	
	
}
