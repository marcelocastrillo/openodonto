package br.ueg.openodonto.persistencia.dao.sql;

public enum SqlOperationType {

	SELECT("SELECT"),
	SELECT_DISTINCT("SELECT DISTINCT"),
	INSERT("INSERT"),
	UPDATE("UPDATE"),
	DELETE("DELETE");
	
	private SqlOperationType(String command) {
		this.command = command;
	}
	
	private String command;
	
	public String getCommand() {
		return command;
	}
	
	@Override
	public String toString() {
		return getCommand();
	}
	
}
