package br.ueg.openodonto.controle.servico;

import java.util.List;

import br.ueg.openodonto.dominio.Produto;

public class ManageProduto {

	private List<Produto> produtos;
	private Produto       produto;
	
	public ManageProduto(List<Produto> produtos) {
		this.produtos = produtos;
	}
	
	public void acaoRemoverProduto(){
		getProdutos().remove(getProduto());
	}
	
	public List<Produto> getProdutos() {
		return produtos;
	}
	
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
	
	public void associarProdutos(List<Produto> produtos){
		this.produtos.addAll(produtos);
	}
	
}
