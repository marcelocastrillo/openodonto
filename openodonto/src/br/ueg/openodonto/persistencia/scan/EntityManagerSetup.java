package br.ueg.openodonto.persistencia.scan;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.Entity;

public class EntityManagerSetup {

	private String path;
	private List<String> pacotes;
	
	public EntityManagerSetup(String path,String... pacotes) {
		this(path,Arrays.asList(pacotes));
	}
	
	public EntityManagerSetup(String path,List<String> pacotes) {
		this.path = path;
		this.pacotes = pacotes;		
	}

	public void registerEntityManagers(){
		File pathFile = new File(getPath());
		if(pathFile.isDirectory() && pathFile.canRead()){
			for(String pacote : getPacotes()){
				File packageFile = new File(pathFile , tralatePackageToPath(pacote));
				if(packageFile.isDirectory() && packageFile.canRead()){
					scanPath(packageFile,pacote);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void scanPath(File destino,String pacote){
		File[] classes = destino.listFiles(new ClassFilter());
		DaoFactory factory = DaoFactory.getInstance();
		for(File classe : classes){
			String fullName = pacote + "." + formatClassName(classe.getName());
			try {
				Class<?> scanned = getClass().getClassLoader().loadClass(fullName);
				if(EntityManager.class.isAssignableFrom(scanned)){
					Class<EntityManager<? extends Entity>> daoClass = (Class<EntityManager<? extends Entity>>) scanned;
					Dao dao;
					if((dao = daoClass.getAnnotation(Dao.class)) != null){
						Class<? extends Entity> modelo = dao.classe();
						factory.registerDao(modelo, daoClass);
					}
					
				}				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}	

	private String formatClassName(String name){
		name = name.endsWith(".class") ? name.substring(0,name.lastIndexOf(".")) : name ;
		return name;
	}
	
	private String tralatePackageToPath(String pacote){
		return pacote.replace(".", "/");
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getPacotes() {
		return pacotes;
	}

	public void setPacotes(List<String> pacotes) {
		this.pacotes = pacotes;
	}
	
}
