import javax.naming.NamingException;

import br.com.simple.jdbc.config.StartupConfig;
import br.com.simple.jdbc.setup.JavaStandardSetup;


public class TestSetup {

	public static void setup()throws NamingException{
		StartupConfig config = StartupConfig.getConfig("WebContent/META-INF/context.xml");
		JavaStandardSetup setup = new JavaStandardSetup(config, true);
		setup.setup("openodonto", "br.ueg.openodonto.persistencia.dao");
	}	
}
