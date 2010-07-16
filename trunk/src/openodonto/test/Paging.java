import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import br.ueg.openodonto.persistencia.ConnectionFactory;

import com.sun.rowset.CachedRowSetImpl;


public class Paging {

	public static void main(String[] args) throws Exception {
		Paging paging = new Paging();
		paging.query();
	}
	
	public void query() throws Exception{
		/*
		Connection connection = ConnectionFactory.getInstance().getConnection();
		PreparedStatement statement = connection.prepareStatement("select * from pessoas inner join pacientes on pessoas.id = pacientes.id_pessoa inner join telefones on pessoas.id = telefones.id_pessoa");
		connection.close();*/
		long time = 203;
		double ftime = time/1000.0;
		System.out.format("%.3f segundos", ftime);
    }
	
}
