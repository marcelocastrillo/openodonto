package br.ueg.openodonto.dominio.constante;

import br.ueg.openodonto.persistencia.orm.Entity;

public interface PessoaJuridica<T extends Entity> {
	String getCnpj();
}
