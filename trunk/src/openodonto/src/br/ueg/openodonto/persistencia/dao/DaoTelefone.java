package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.simple.jdbc.annotation.Dao;
import br.com.simple.jdbc.dao.DaoCrud;
import br.com.simple.jdbc.orm.OrmFormat;
import br.com.simple.jdbc.sql.CrudQuery;
import br.com.simple.jdbc.sql.IQuery;
import br.ueg.openodonto.dominio.Telefone;

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
				}
			}
			for (Telefone telefone : telefones) {
				telefone.setIdPessoa(idPessoa);
				alterar(telefone);
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
		return new Telefone();
	}
}
