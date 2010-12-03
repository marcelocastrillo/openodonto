package br.ueg.openodonto.dominio.constante;

import br.ueg.openodonto.persistencia.orm.Entity;

public interface PessoaFisica<T extends Entity> {
	String getCpf();
}
