package br.ueg.openodonto.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.ueg.openodonto.persistencia.scan.EntityManagerSetup;

public class OpenOdontoLoadListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {		
		try {
			String binPath = contextEvent.getServletContext().getRealPath("WEB-INF/classes");
			String[] pacotes = contextEvent.getServletContext().getInitParameter("DaoScanPackages").split(";");
			EntityManagerSetup setup = new EntityManagerSetup(binPath, pacotes);
			setup.registerEntityManagers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
