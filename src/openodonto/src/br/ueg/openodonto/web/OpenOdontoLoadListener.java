package br.ueg.openodonto.web;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.com.simple.jdbc.config.StartupConfig;
import br.com.simple.jdbc.setup.WebContainerSetup;

public class OpenOdontoLoadListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {		
		try {
			ServletContext context = contextEvent.getServletContext(); 
			InputStream in = context.getResourceAsStream("META-INF/context.xml");
			StartupConfig config = StartupConfig.getConfig(in);			
			String[] pacotes = context.getInitParameter("DaoScanPackages").split(";");
			WebContainerSetup setup = new WebContainerSetup(config);
			setup.setup("openodonto", pacotes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
