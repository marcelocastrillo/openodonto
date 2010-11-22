package br.ueg.openodonto.dominio;

import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.ForwardKey;
import br.ueg.openodonto.persistencia.orm.Id;
import br.ueg.openodonto.persistencia.orm.Relationship;
import br.ueg.openodonto.persistencia.orm.Table;

@Table(name="paciente_anamnese")
public class PacienteQuestionarioAnamnese implements Entity{
	
	private static final long serialVersionUID = -1345966759980880798L;
	
	@Id
	@Column(name="fk_pacientes",joinFields={@ForwardKey(tableField="id_pessoa",foreginField="fk_pacientes")})
	private Long pacienteId;
	
	@Id
	@Column(name="fk_questionario_anamnese",joinFields={@ForwardKey(tableField="id_questionario_anamnese",foreginField="fk_questionario_anamnese")})
	private Long questionarioAnamneseId;
	
	@Relationship
	private PacienteAnamnese anamnese;
	
	public PacienteQuestionarioAnamnese(Long pacienteId,Long questionarioAnamneseId) {
		this();
		this.pacienteId = pacienteId;
		this.questionarioAnamneseId = questionarioAnamneseId;
	}

	public PacienteQuestionarioAnamnese() {
	}
	
	public Long getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Long pacienteId) {
		this.pacienteId = pacienteId;
	}

	public Long getQuestionarioAnamneseId() {
		return questionarioAnamneseId;
	}

	public void setQuestionarioAnamneseId(Long questionarioAnamneseId) {
		this.questionarioAnamneseId = questionarioAnamneseId;
	}	
	
	public PacienteAnamnese getAnamnese() {
		return anamnese;
	}

	public void setAnamnese(PacienteAnamnese anamnese) {
		this.anamnese = anamnese;
	}
	
	@Override
	public String toString() {
		return "PacienteQuestionarioAnamnese [anamnese=" + anamnese
				+ ", pacienteId=" + pacienteId + ", questionarioAnamneseId="
				+ questionarioAnamneseId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((anamnese == null) ? 0 : anamnese.hashCode());
		result = prime * result
				+ ((pacienteId == null) ? 0 : pacienteId.hashCode());
		result = prime
				* result
				+ ((questionarioAnamneseId == null) ? 0
						: questionarioAnamneseId.hashCode());
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
		PacienteQuestionarioAnamnese other = (PacienteQuestionarioAnamnese) obj;
		if (anamnese == null) {
			if (other.anamnese != null)
				return false;
		} else if (!anamnese.equals(other.anamnese))
			return false;
		if (pacienteId == null) {
			if (other.pacienteId != null)
				return false;
		} else if (!pacienteId.equals(other.pacienteId))
			return false;
		if (questionarioAnamneseId == null) {
			if (other.questionarioAnamneseId != null)
				return false;
		} else if (!questionarioAnamneseId.equals(other.questionarioAnamneseId))
			return false;
		return true;
	}
	
}
