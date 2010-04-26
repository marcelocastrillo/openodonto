package br.ueg.openodonto.persistencia;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory implements Serializable {

    private static final long serialVersionUID = 1566977400293412673L;

    private static ConnectionFactory instance;

    private ConnectionFactory(){
    	
    }
    
    public static ConnectionFactory getInstance(){
        if(ConnectionFactory.instance == null){
            ConnectionFactory.instance = new ConnectionFactory();
        }
        return ConnectionFactory.instance;
    }

    private Connection connection;

	/**
	 * Cria uma nova Conexao caso o objeto connection seja null ou esteja fechado.
	 * @return Um objeto Connection
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception {
		if(this.connection == null || this.connection.isClosed()){
            Properties p = new Properties();
            p.load(getClass().getResourceAsStream("/resources/agenda-ds.properties"));
            Class.forName(p.getProperty("driver-class"));
			this.connection = DriverManager.getConnection(p.getProperty("connection-url") ,
                    p.getProperty("user-name") ,
                    p.getProperty("password"));
		}
		return connection;
	}

	/**
	 * Define uma instancia especifica de Connection para a variavel connection. 
	 * @param connection
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	
	/**
	 * Fecha a conexao atual.
	 */
	public void closeConnection() {
		try {
			if(this.connection != null && !this.connection.isClosed()){
				this.connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnectionActive(){
		try {
	        return (this.connection != null && !this.connection.isClosed());
        } catch (SQLException e) {
	        e.printStackTrace();
        }
        return false;
	}


}
