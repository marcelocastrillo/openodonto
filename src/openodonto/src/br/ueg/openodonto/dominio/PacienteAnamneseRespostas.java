package br.ueg.openodonto.dominio;

import br.ueg.openodonto.dominio.constante.TiposRespostaAnamnese;
import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.Enumerator;
import br.ueg.openodonto.persistencia.orm.ForwardKey;
import br.ueg.openodonto.persistencia.orm.Id;
import br.ueg.openodonto.persistencia.orm.Table;
import br.ueg.openodonto.persistencia.orm.value.EnumValue;
import br.ueg.openodonto.persistencia.orm.value.IdIncrementType;

@Table(name="paciente_anamnese_respostas")
public class PacienteAnamneseRespostas implements Entity{

	private static final long serialVersionUID = -6522681709270082326L;
	
	@Column(name="id")
	@Id(autoIncrement=IdIncrementType.IDENTITY)
	private Long codigo;
	
	@Column(name="resposta")
	@Enumerator(type=EnumValue.ORDINAL)
	private TiposRespostaAnamnese resposta;
	
	@Column(name="fk_questao_anamnese",joinFields={@ForwardKey(tableField="id",foreginField="fk_questao_anamnese")})
	private Long questaoAnamneseId;
	
	@Column(name="fk_questionario_anamnese",joinFields={@ForwardKey(tableField="id_questionario_anamnese",foreginField="fk_questionario_anamnese")})
	private Long questionarioAnamneseId;
	
	@Column(name="fk_pacientes",joinFields={@ForwardKey(tableField="id_pessoa",foreginField="fk_pacientes")})
	private Long pacienteId;	
	
	public PacienteAnamneseRespostas(Long codigo) {
		this();
		this.codigo = codigo;
	}

	public PacienteAnamneseRespostas(Long questaoAnamneseId, Long questionarioAnamneseId, Long pacienteId) {
		this();
		this.questaoAnamneseId = questaoAnamneseId;
		this.questionarioAnamneseId = questionarioAnamneseId;
		this.pacienteId = pacienteId;
	}

	public PacienteAnamneseRespostas() {
	}
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public TiposRespostaAnamnese getResposta() {
		return resposta;
	}

	public void setResposta(TiposRespostaAnamnese resposta) {
		this.resposta = resposta;
	}

	public Long getQuestaoAnamneseId() {
		return questaoAnamneseId;
	}

	public void setQuestaoAnamneseId(Long questaoAnamneseId) {
		this.questaoAnamneseId = questaoAnamneseId;
	}

	public Long getQuestionarioAnamneseId() {
		return questionarioAnamneseId;
	}

	public void setQuestionarioAnamneseId(Long questionarioAnamneseId) {
		this.questionarioAnamneseId = questionarioAnamneseId;
	}

	public Long getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Long pacienteId) {
		this.pacienteId = pacienteId;
	}

	@Override
	public String toString() {
		return "PacienteAnamneseRespostas [codigo=" + codigo + ", pacienteId="
				+ pacienteId + ", questaoAnamneseId=" + questaoAnamneseId
				+ ", questionarioAnamneseId=" + questionarioAnamneseId
				+ ", resposta=" + resposta + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result
				+ ((pacienteId == null) ? 0 : pacienteId.hashCode());
		result = prime
				* result
				+ ((questaoAnamneseId == null) ? 0 : questaoAnamneseId
						.hashCode());
		result = prime
				* result
				+ ((questionarioAnamneseId == null) ? 0
						: questionarioAnamneseId.hashCode());
		result = prime * result
				+ ((resposta == null) ? 0 : resposta.hashCode());
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
		PacienteAnamneseRespostas other = (PacienteAnamneseRespostas) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (pacienteId == null) {
			if (other.pacienteId != null)
				return false;
		} else if (!pacienteId.equals(other.pacienteId))
			return false;
		if (questaoAnamneseId == null) {
			if (other.questaoAnamneseId != null)
				return false;
		} else if (!questaoAnamneseId.equals(other.questaoAnamneseId))
			return false;
		if (questionarioAnamneseId == null) {
			if (other.questionarioAnamneseId != null)
				return false;
		} else if (!questionarioAnamneseId.equals(other.questionarioAnamneseId))
			return false;
		if (resposta == null) {
			if (other.resposta != null)
				return false;
		} else if (!resposta.equals(other.resposta))
			return false;
		return true;
	}
	
	
	

}
