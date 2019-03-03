package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarHelper {
	public static String getJar(Class<?> clazz) throws Exception {
		
		ClassLoader loader = clazz.getClassLoader();
		String classFile = clazz.getName().replaceAll("\\.", "/") + ".class";

		String fqPath = "./bin";
		URL url = loader.getResource(classFile);
		if(url != null) {
			fqPath = url.getFile();
		}

		fqPath = fqPath.replace(classFile, "");
		System.out.println("JarHelper :: Jar'ing directory : " + fqPath);

		File jarFile = Files.createTempFile("job", ".jar").toFile();
		FileOutputStream fOut = new FileOutputStream(jarFile);
		JarOutputStream jOut = new JarOutputStream(fOut, new Manifest());
		File dirToJar = new File(fqPath);
		jar(dirToJar, jOut);
		jOut.close();
		
		
		String jar = jarFile.getAbsolutePath();
		System.out.println("JarHelper :: Temporary JAR created at : " + jar);
		return jar;
	}
	
	private static void jar(File file, JarOutputStream jOut) throws IOException {
		if(file.isDirectory()) {
			for (File f : file.listFiles()) {
				_jar("", f, jOut);
			}
		} else {
			_jar("", file, jOut);
		}
	}
	
	private static void _jar(String parent, File file, JarOutputStream jOut) throws IOException {
		String jPath = parent + (parent.isEmpty() ? "" : "/") + file.getName();
		
		System.out.println("JarHelper :: Adding : " + jPath);
		JarEntry jEnt = new JarEntry(jPath + (file.isDirectory() ? "/" : ""));
		jOut.putNextEntry(jEnt);
		
		
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				_jar(jPath, child, jOut);
			}
		} else {
			Files.copy(file.toPath(), jOut);
		}
	}
}
