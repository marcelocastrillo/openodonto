import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.ColaboradorProduto;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.persistencia.dao.DaoColaboradorProduto;
import br.ueg.openodonto.persistencia.dao.DaoFactory;




public class ManterProdutoTest {

	public static void main(String[] args) throws Exception{
		SetupConnection.setupJNDI();
		/*
		EntityManager<Produto> dao = DaoFactory.getInstance().getDao(Produto.class);
		Produto produto = new Produto();
		produto.setCategoria(CategoriaProduto.PRODUTO);
		produto.setDescricao("Leite");
		produto.setNome("Leite");
		dao.alterar(produto);
		*/
		/*
		EntityManager<Colaborador> dao = DaoFactory.getInstance().getDao(Colaborador.class);
		Colaborador colaborador = new Colaborador();
		colaborador.setCategoria(CategoriaProduto.PRODUTO);
		colaborador.setDataCadastro(new Date(System.currentTimeMillis()));
		colaborador.setEstado(TiposUF.GO);
		colaborador.setCidade("GOIANIA");
		colaborador.setNome("JOSE MARIA");		
		dao.alterar(colaborador);
		*/
		/*
		EntityManager<ColaboradorProduto> dao = DaoFactory.getInstance().getDao(ColaboradorProduto.class);
		ColaboradorProduto colaboradorProduto = new ColaboradorProduto();
		colaboradorProduto.setColaboradorIdPessoa(4L);
		colaboradorProduto.setProdutoIdProduto(3L);
		dao.alterar(colaboradorProduto);
		*/
		
		DaoColaboradorProduto dao = (DaoColaboradorProduto)DaoFactory.getInstance().getDao(ColaboradorProduto.class);
		for(Produto produto : dao.getProdutos(4L)){
			System.out.println(produto);
		}
		for(Colaborador colaborador : dao.getColaboradores(2L)){
			System.out.println(colaborador);
		}
	
	}

}
