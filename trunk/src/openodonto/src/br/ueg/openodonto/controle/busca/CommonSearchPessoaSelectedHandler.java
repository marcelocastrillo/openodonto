package br.ueg.openodonto.controle.busca;

import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.persistencia.orm.Entity;

public abstract class CommonSearchPessoaSelectedHandler<T extends Entity> extends CommonSearchSelectedHandler<Pessoa>{
	
	private static final long serialVersionUID = 987102873548453955L;
	
	public CommonSearchPessoaSelectedHandler() {
		super(null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void load() {
		try {
			if(!getSuperDao().exists((T)getBean())){
				loadJustPerson();
			}else{
				loadAlreadyEntity();
			}
			extraLoad();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		
	@SuppressWarnings("unchecked")
	protected void loadAlreadyEntity() throws Exception{
		getSuperDao().load((T)getBean());
	}		
	protected void loadJustPerson() throws Exception{
	    EntityManager<Pessoa> dao = DaoFactory.getInstance().getDao(Pessoa.class);
	    dao.load((Pessoa)getBean());			
	}
	
	public abstract EntityManager<T> getSuperDao();

}
