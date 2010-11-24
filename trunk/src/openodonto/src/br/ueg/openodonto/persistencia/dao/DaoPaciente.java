package br.ueg.openodonto.persistencia.dao;

import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Odontograma;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.PacienteQuestionarioAnamnese;
import br.ueg.openodonto.dominio.QuestionarioAnamnese;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.orm.Dao;

@Dao(classe=Paciente.class)
public class DaoPaciente extends DaoAbstractPessoa<Paciente>{

    private static final long serialVersionUID = -4278752127118870714L;

	public DaoPaciente() {
		super(Paciente.class);
	}

	@Override
	public Paciente getNewEntity() {
		return new Paciente();
	}
	
	@Override
	protected void afterUpdate(Paciente o) throws Exception {
		super.afterUpdate(o);
		updateRelationshipOdontograma(o);
		updateRelationshipAnaminese(o);
		updateRelationshipPlanejamento(o);
	}
	
	private void updateRelationshipOdontograma(Paciente o) throws Exception{
		DaoOdontograma daoOdontograma = (DaoOdontograma)DaoFactory.getInstance().getDao(Odontograma.class);
		daoOdontograma.updateRelationshipPaciente(o.getOdontogramas(), o.getCodigo());
	}
	
	private void updateRelationshipAnaminese(Paciente o) throws Exception{
		DaoQuestionarioAnamnese daoQA = (DaoQuestionarioAnamnese) DaoFactory.getInstance().getDao(QuestionarioAnamnese.class);
		daoQA.updateRelationshipPaciente(o);
	}
	
	private void updateRelationshipPlanejamento(Paciente o){
		
	}
	
	@Override
	protected void afterInsert(Paciente o) throws Exception {
		super.afterInsert(o);
		updateRelationshipOdontograma(o);
		updateRelationshipAnaminese(o);
		updateRelationshipPlanejamento(o);
	}	
	
	@Override
	public List<Paciente> listar(boolean lazy, String... fields) {
		DaoTelefone daoTelefone = (DaoTelefone)DaoFactory.getInstance().getDao(Telefone.class);
		//DaoQuestionarioAnamnese daoQA = (DaoQuestionarioAnamnese) DaoFactory.getInstance().getDao(QuestionarioAnamnese.class);
		DaoOdontograma daoOdontograma = (DaoOdontograma)DaoFactory.getInstance().getDao(Odontograma.class);
		List<Paciente> lista = super.listar(lazy, fields);
		if (lista != null && !lazy) {
			for (Paciente paciente : lista) {
				try {
					paciente.setTelefone(daoTelefone.getTelefonesRelationshipPessoa(paciente.getCodigo()));
					paciente.setOdontogramas(daoOdontograma.getOdontogramasRelationshipPaciente(paciente.getCodigo()));
					//TODO paciente.setAnamneses(daoQA.getQuestionariosAnamnesesRelationshipPaciente(paciente.getCodigo()));
				} catch (Exception ex) {
				}
			}
		}
		return lista;
	}
	
	@Override
	public void afterLoad(Paciente o) throws Exception {
		super.afterLoad(o);
		loadOdontograma(o);
		loadAnaminese(o);
		loadPlanejamento(o);
	}

	private void loadOdontograma(Paciente o) throws Exception{
		DaoOdontograma daoOdontograma = (DaoOdontograma)DaoFactory.getInstance().getDao(Odontograma.class);
		o.setOdontogramas(daoOdontograma.getOdontogramasRelationshipPaciente(o.getCodigo()));
	}
	
	private void loadAnaminese(Paciente o) throws Exception{
		DaoQuestionarioAnamnese daoQA = (DaoQuestionarioAnamnese) DaoFactory.getInstance().getDao(QuestionarioAnamnese.class);
		DaoPacienteQuestionarioAnamnese daoPQA = (DaoPacienteQuestionarioAnamnese) DaoFactory.getInstance().getDao(PacienteQuestionarioAnamnese.class);
		List<PacienteQuestionarioAnamnese> pqas = daoPQA.getPQARelationshipPaciente(o.getCodigo());
		for(PacienteQuestionarioAnamnese pqa : pqas){
			QuestionarioAnamnese qa = daoQA.findByKey(pqa.getQuestionarioAnamneseId());
			o.getAnamneses().put(pqa, qa);
		}
	}
	
	private void loadPlanejamento(Paciente o){		
	}
	
	@Override
	protected boolean beforeRemove(Paciente o, Map<String, Object> params)throws Exception {
		boolean referenced = super.beforeRemove(o, params);
		removeOdontograma(o);
		removeAnamise(o);
		removePlanejamento(o);
		return referenced;
	}
	
	private void removeOdontograma(Paciente o) throws Exception{
		EntityManager<Odontograma> entityManagerOdontograma = DaoFactory.getInstance().getDao(Odontograma.class);
		for (Odontograma odontograma : o.getOdontogramas()) {
			entityManagerOdontograma.remover(odontograma);			
		}
	}
	
	private void removeAnamise(Paciente o) throws Exception{
		/*TODO
		EntityManager<QuestionarioAnamnese> entityManagerQA = DaoFactory.getInstance().getDao(QuestionarioAnamnese.class);
		for(QuestionarioAnamnese qa : o.getAnamneses()){
			entityManagerQA.remover(qa);
		}
		*/
	}
	
	private void removePlanejamento(Paciente o){		
	}
	
}
