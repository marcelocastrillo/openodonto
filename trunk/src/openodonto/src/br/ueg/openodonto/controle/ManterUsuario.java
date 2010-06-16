package br.ueg.openodonto.controle;

import java.util.List;

import br.ueg.openodonto.controle.validador.AbstractValidator;
import br.ueg.openodonto.dominio.Usuario;

public class ManterUsuario  extends ManageBeanGeral<Usuario>{

	public ManterUsuario() {
		super(Usuario.class);
	}

	@Override
	public String acaoPesquisar() {
		return null;
	}

	@Override
	protected void carregarExtra() {}

	@Override
	protected List<String> getCamposFormatados() {
		return null;
	}

	@Override
	protected List<AbstractValidator> getCamposObrigatorios() {
		return null;
	}

	@Override
	protected void initExtra() {
	}

}
