package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;

import br.ueg.openodonto.controle.ManterPaciente;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=Produto.class)
public class DaoProduto extends DaoCrud<Produto>{

	public DaoProduto() {
		super(Produto.class);
	}

	private static final long serialVersionUID = 4731561150550714997L;

	@Override
	public Produto getNewEntity() {
		return new Produto();
	}

	@Override
	public Produto pesquisar(Object key) {
		if (key == null) {
			return null;
		}
		List<Produto> lista;
		try {
			Long id = Long.parseLong(String.valueOf(key));
			OrmFormat orm = new OrmFormat(new Produto(id));
			IQuery query = CrudQuery.getSelectQuery(Produto.class, orm.formatNotNull(), "*");
			lista = getSqlExecutor().executarQuery(query.getQuery(),query.getParams(), 1);
			if (lista.size() == 1) {
				return lista.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
