package br.ueg.openodonto.controle.validador;


public class ValidadorCPFeCNPJ extends AbstractValidator{

	public ValidadorCPFeCNPJ(String message) {
		super(message); //TODO Descomentar o código e arrumar
	}

	@Override
	public boolean isValid(Object root) {
		return false;
	}
/*
	private ManageCpf manageCpf;
	
	public boolean isValid(Object root) {
		return !getManageCpf().isDocValido();
	}
	
	public ValidadorCPFeCNPJ(String ELCampo , String messageOut,ManageCpf manageCpf){
		super(ELCampo,messageOut,"* CPF ou CNPJ duplicado !");
		this.setManageCpf(manageCpf);
	}

	
	private ManageCpf getManageCpf() {	
		return this.manageCpf;
	}
	
	public void setManageCpf(ManageCpf manageCpf) {	
		this.manageCpf = manageCpf;
	}	
	*/

}
