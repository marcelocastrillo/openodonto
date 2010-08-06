package br.ueg.openodonto.persistencia;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ConnectionFactory implements Serializable {

	private static final long serialVersionUID = 1566977400293412673L;

	private  static  ConnectionFactory          instance;
	private  static  ThreadLocal<Connection>    session;

	static {
		instance = new ConnectionFactory();
		session = new ThreadLocal<Connection>();
	}

	private ConnectionFactory() {
	}

	public static ConnectionFactory getInstance() {
		return instance;
	}

	/**
	 * Cria uma nova Conexao caso o objeto connection seja null ou esteja
	 * fechado.
	 * 
	 * @return Um objeto Connection
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {
		Connection connection = session.get();
		if (connection == null || connection.isClosed()) {
			connection = buildConnection();
			session.set(connection);
		}
		return connection;
	}

	private Connection buildConnection() throws SQLException {
		try {
			Context initCtx = new InitialContext();
			Connection connection = ((DataSource) initCtx.lookup("java:comp/env/jdbc/openodonto")).getConnection();
			return connection;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Fecha a conexao atual.
	 */
	public void closeConnection() {
		Connection connection = session.get();
		try {
			if (isConnectionActive()) {
				if (!connection.getAutoCommit()) {
					connection.commit();
				}
				connection.close();
			}
			connection = null;
			session.set(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnectionActive() {
		Connection connection = session.get();
		try {
			return (connection != null && !connection.isClosed());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
