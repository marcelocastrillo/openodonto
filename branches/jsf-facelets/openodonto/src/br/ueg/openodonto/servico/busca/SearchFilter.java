package br.ueg.openodonto.servico.busca;


public interface SearchFilter {
	String              getLabel();
	String              getOutMessage();
	SearchField         getField();
}
