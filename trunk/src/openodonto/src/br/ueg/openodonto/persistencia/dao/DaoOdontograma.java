package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;

import br.ueg.openodonto.dominio.Odontograma;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=Odontograma.class)
public class DaoOdontograma extends DaoCrud<Odontograma>{

	private static final long serialVersionUID = -4979683578095554032L;

	public DaoOdontograma() {
		super(Odontograma.class);
	}

	@Override
	public Odontograma getNewEntity() {
		return new Odontograma();
	}
	
	public List<Odontograma> getOdontogramasRelationshipPaciente(Long idPaciente) throws SQLException{
		OrmFormat orm = new OrmFormat(new Odontograma(idPaciente));
		IQuery query = CrudQuery.getSelectQuery(Odontograma.class, orm.formatNotNull(), "*");
		return getSqlExecutor().executarQuery(query);
	}
	
	public void updateRelationshipPaciente(List<Odontograma> odontogramas,Long idPaciente) throws Exception {
		if(odontogramas != null){
			List<Odontograma> todos = getOdontogramasRelationshipPaciente(idPaciente);
			for (Odontograma odontograma : todos) {
				if (!odontogramas.contains(odontograma)) {
					remover(odontograma);
				}
			}
			for (Odontograma odontograma : odontogramas) {
				odontograma.setIdPessoa(idPaciente);
				alterar(odontograma);
			}
		}
	}

}
