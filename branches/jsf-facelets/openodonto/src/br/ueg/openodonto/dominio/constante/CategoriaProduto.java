package br.ueg.openodonto.dominio.constante;

import br.ueg.openodonto.persistencia.orm.Entity;

public enum CategoriaProduto implements Entity{

	SERVICO("Serviço"), PRODUTO("Produto");

	private String descricao;

	private CategoriaProduto(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public long getCodigo() {
		return ordinal();
	}

	public static CategoriaProduto parseCategoria(Object id){
		int index = Integer.parseInt(id.toString());
		return values()[index];
	}
	
	public String toString() {
		return this.descricao;
	}

}
