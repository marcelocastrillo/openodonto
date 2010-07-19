package br.ueg.openodonto.persistencia;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory implements Serializable {

	private static final long serialVersionUID = 1566977400293412673L;

	private  static  ConnectionFactory          instance;
	private  static  ThreadLocal<Connection>    session;
	public   static  ConnectionConfig           config;

	static {
		instance = new ConnectionFactory();
		session = new ThreadLocal<Connection>();
		final Properties p = new Properties();
		try {
			p.load(ConnectionFactory.class.getResourceAsStream("/resources/openodonto-ds.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		config = new ConnectionConfig() {
			@Override
			public String getUserName() {
				return p.getProperty("user-name");
			}

			@Override
			public String getPassword() {
				return p.getProperty("password");
			}

			@Override
			public String getDriverClass() {
				return p.getProperty("driver-class");
			}

			@Override
			public String getConnectionURL() {
				return p.getProperty("connection-url");
			}
		};
		try {
			Class.forName(config.getDriverClass());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
		/*
		try {
			Context initCtx = new InitialContext();
			Connection connection = ((DataSource) initCtx.lookup("java:comp/env/jdbc/openodonto")).getConnection();
			return connection;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		*/
		return DriverManager.getConnection(config.getConnectionURL(),config.getUserName(), config.getPassword());
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
