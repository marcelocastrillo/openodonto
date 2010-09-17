package br.ueg.openodonto.controle.busca;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.ueg.openodonto.controle.servico.ManageListagem;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.dominio.constante.CategoriaProduto;
import br.ueg.openodonto.persistencia.orm.OrmResolver;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;
import br.ueg.openodonto.servico.busca.FieldFacade;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.validator.Validator;
import br.ueg.openodonto.validator.ValidatorFactory;

public class SearchableProduto extends AbstractSearchable<Produto>{

	private static final long serialVersionUID = 844846365772534533L;

	public SearchableProduto(MessageDisplayer displayer){
		super(displayer);
	}
	
	public void buildFacade(){
		super.buildFacade();
		OrmTranslator translator = new OrmTranslator(OrmResolver.getAllFields(new ArrayList<Field>(), Produto.class, true));
		getFacade().add(new FieldFacade("Código",translator.getColumn("codigo")));
		getFacade().add(new FieldFacade("Nome",translator.getColumn("nome")));
		getFacade().add(new FieldFacade("Categoria","categoriaDesc"));
		getFacade().add(new FieldFacade("Descrição","shortDescription"));
	}
	
	public void buildFilter(){
		super.buildFilter();
		buildNameFilter();
		buildCategoriaFilter();
		buildDescricaoFilter();
		buildColaboradorFilter();
	}
	
	private void buildDescricaoFilter() {
		Validator validator = ValidatorFactory.newStrRangeLen(300,3, true);
		getFiltersMap().put("descricaoFilter", buildBasicFilter("descricaoFilter","Descrição",validator));
	}

	private void buildCategoriaFilter() {
		Validator validator = ValidatorFactory.newNull();
		List<CategoriaProduto> domain = ManageListagem.getLista(CategoriaProduto.class).getDominio(); 
		getFiltersMap().put("categoriaFilter", buildBasicFilter("categoriaFilter","Categoria",domain,validator));
	}

	private void buildNameFilter() {
		Validator validator = ValidatorFactory.newStrRangeLen(150,3, true);
		getFiltersMap().put("nomeFilter", buildBasicFilter("nomeFilter","Nome",validator));
	}

	private void buildColaboradorFilter() {
		Validator validator = ValidatorFactory.newStrRangeLen(100,3, true);
		getFiltersMap().put("colaboradorFilter", buildBasicFilter("colaboradorFilter","Colaborador",validator));
	}

}
