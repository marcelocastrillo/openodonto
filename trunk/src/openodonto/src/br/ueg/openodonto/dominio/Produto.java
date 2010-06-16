package br.ueg.openodonto.dominio;

import br.ueg.openodonto.dominio.constante.CategoriaProduto;
import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Enumerator;
import br.ueg.openodonto.persistencia.orm.Id;
import br.ueg.openodonto.persistencia.orm.Table;
import br.ueg.openodonto.persistencia.orm.value.EnumValue;
import br.ueg.openodonto.persistencia.orm.value.IdIncrementType;

@Table(name="produtos")
public class Produto {

	@Column(name="idProduto")
	@Id(autoIncrement=IdIncrementType.IDENTITY)
	private Long codigo;	
	
	@Column(name="categoria")
	@Enumerator(type=EnumValue.ORDINAL)
	private CategoriaProduto categoria;
	
	@Column(name="nome")
	private String nome;
	
	@Column(name="descricao")
	private String descricao;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public CategoriaProduto getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaProduto categoria) {
		this.categoria = categoria;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (categoria == null) {
			if (other.categoria != null)
				return false;
		} else if (!categoria.equals(other.categoria))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}	
	
}
