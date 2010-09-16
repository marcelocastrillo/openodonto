import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class SetupConnection {

	public static void setupJNDI()throws NamingException{
		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
	    System.setProperty(Context.PROVIDER_URL, "file:///.");
	    InitialContext ic = new InitialContext();
	    
		SAXBuilder sax = new SAXBuilder();
		Document doc = null;
		try {
			doc = sax.build(new FileInputStream("WebContent/META-INF/context.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		Element dsResource = root.getChild("Resource");
		String driverClass = dsResource.getAttributeValue("driverClassName");
		String connectionURL = dsResource.getAttributeValue("url");
		String userName = dsResource.getAttributeValue("username");
		String passWord = dsResource.getAttributeValue("password");
		String maxActive = dsResource.getAttributeValue("maxActive");
		String maxIdle = dsResource.getAttributeValue("maxIdle");
		String maxWait = dsResource.getAttributeValue("maxWait");
		String jndiName = dsResource.getAttributeValue("name");
	    
	    Reference dbcpReference = new Reference("org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS", "org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS", null);
	    dbcpReference.add(new StringRefAddr("driver", driverClass));
	    dbcpReference.add(new StringRefAddr("url", connectionURL));
	    dbcpReference.add(new StringRefAddr("user", userName));
	    dbcpReference.add(new StringRefAddr("password", passWord));	    
	    ic.rebind(jndiName, dbcpReference);


	    Reference poolReference = new Reference("org.apache.commons.dbcp.datasources.SharedPoolDataSource", "org.apache.commons.dbcp.datasources.SharedPoolDataSourceFactory", null);
	    poolReference.add(new StringRefAddr("dataSourceName", "jdbc/openodonto"));
	    poolReference.add(new StringRefAddr("maxActive", maxActive));
	    poolReference.add(new StringRefAddr("maxIdle", maxIdle));
	    poolReference.add(new StringRefAddr("maxWait", maxWait));
	    ic.rebind("java:comp/env/"+jndiName, poolReference);
	}
	
}
