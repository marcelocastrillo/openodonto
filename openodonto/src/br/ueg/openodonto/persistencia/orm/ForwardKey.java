package br.ueg.openodonto.persistencia.orm;

public @interface ForwardKey {
    String tableField();
    String foreginField();
}
