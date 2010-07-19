import br.ueg.openodonto.persistencia.dao.sql.IQuery;


public class Paging {

	public static void main(String[] args) throws Exception {
		Paging paging = new Paging(null);
		paging.query();
	}
	
	public Paging(IQuery query) {
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
