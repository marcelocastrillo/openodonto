package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;

import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=Telefone.class)
public class DaoTelefone extends DaoCrud<Telefone> {

	private static final long serialVersionUID = -8028356632411640718L;

	public DaoTelefone() {
		super(Telefone.class);
	}
	
	public void updateRelationshipPessoa(List<Telefone> telefones,Long idPessoa) throws Exception {
		if(telefones != null){
			List<Telefone> todos = getTelefonesRelationshipPessoa(idPessoa);
			for (Telefone telefone : todos) {
				if (!telefones.contains(telefone)) {
					remover(telefone);
					getConnection().setAutoCommit(false);
				}
			}
			for (Telefone telefone : telefones) {
				telefone.setIdPessoa(idPessoa);
				alterar(telefone);
				getConnection().setAutoCommit(false);
			}
		}
	}
	
	public List<Telefone> getTelefonesRelationshipPessoa(Long idPessoa)throws SQLException {
		OrmFormat orm = new OrmFormat(new Telefone(idPessoa));
		IQuery query = CrudQuery.getSelectQuery(Telefone.class, orm.formatNotNull(), "*");
		return getSqlExecutor().executarQuery(query);
	}	

	@Override
	public Telefone getNewEntity() {
		return new Telefone(); //Melhor que reflexão
	}
}
