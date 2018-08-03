package org.parser.lang;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;
import org.parser.main.Main;

public class LangTest {

	@Test
	public void test() {
		final File folder = new File(getClass().getResource("/requirements/sketches").getPath());
		
		try {
			RecursiveDirectoryParsing(folder);
		} catch (Exception e) {
			fail();
		}
		
	}
	
	//parse requirements in current folder and any sub folders
	public static void RecursiveDirectoryParsing(final File folder) throws Exception {
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isFile()) {
	        	FileInputStream inputStream = null;
	        	try {
	        		inputStream = new FileInputStream(fileEntry);
	        		Main.ParseLang(inputStream);
	        	} catch (Exception e) {
	    			System.err.println("Failed when parsing: " + fileEntry.getPath());
	    			System.err.println("Failing reason: " + e.getMessage());
	    			e.printStackTrace();
	    			throw e;
	    		} finally {
	        		inputStream.close();
	        	}
	        } else if (fileEntry.isDirectory()) {
	        	RecursiveDirectoryParsing(fileEntry);
	        }
	    }
	}

}
