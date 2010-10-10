package br.ueg.openodonto.persistencia.scan;

import java.io.File;
import java.io.FileFilter;

public class ClassFilter implements FileFilter{

	@Override
	public boolean accept(File file) {
		return file != null &&
		file.isFile() &&
		file.canRead() &&
		file.getName().endsWith(".class");
	}

}
