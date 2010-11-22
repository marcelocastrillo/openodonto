package br.ueg.openodonto.dominio;

import java.util.Map;

public class PacienteAnamnese {

	private Paciente 										paciente;
	private QuestionarioAnamnese 							questionario;	
	private Map<QuestaoAnamnese,PacienteAnamneseRespostas> 	respotas;
	
	public Paciente getPaciente() {
		return paciente;
	}
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	public QuestionarioAnamnese getQuestionario() {
		return questionario;
	}
	public void setQuestionario(QuestionarioAnamnese questionario) {
		this.questionario = questionario;
	}
	public Map<QuestaoAnamnese, PacienteAnamneseRespostas> getRespotas() {
		return respotas;
	}
	public void setRespotas(Map<QuestaoAnamnese, PacienteAnamneseRespostas> respotas) {
		this.respotas = respotas;
	}
	@Override
	public String toString() {
		return "PacienteAnamnese [paciente=" + paciente + ", questionario="
				+ questionario + ", respotas=" + respotas + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((paciente == null) ? 0 : paciente.hashCode());
		result = prime * result
				+ ((questionario == null) ? 0 : questionario.hashCode());
		result = prime * result
				+ ((respotas == null) ? 0 : respotas.hashCode());
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
		PacienteAnamnese other = (PacienteAnamnese) obj;
		if (paciente == null) {
			if (other.paciente != null)
				return false;
		} else if (!paciente.equals(other.paciente))
			return false;
		if (questionario == null) {
			if (other.questionario != null)
				return false;
		} else if (!questionario.equals(other.questionario))
			return false;
		if (respotas == null) {
			if (other.respotas != null)
				return false;
		} else if (!respotas.equals(other.respotas))
			return false;
		return true;
	}
	
}
