package br.ueg.openodonto.persistencia;

public interface ConnectionConfig {

	String getDriverClass();
	String getConnectionURL();
	String getUserName();
	String getPassword();
	
}
