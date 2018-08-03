package org.auditor.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.common.audit.AuditResult;
import org.common.helper.Helper;

public class AuditorMain {
	
	public static void main(String... args) throws Exception {
		String fileName = null;
		
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-f")) {
				if (i + 1 >= args.length) {
					usage("must provide transcript json file after -f");
				}
				fileName = args[i + 1]; 
				i += 2;
			} else {
				usage("invalid option: " + args[i]);
			}
		}
		
		if (fileName == null) {
			usage("must provide transcript json file");
		}
		
		InputStream transcriptIs = null;
		try {
			File transcript = new File(fileName);
			transcriptIs = new FileInputStream(transcript);
		} catch (NullPointerException | IOException e) {
			usage("cannot find or open file with path: " + fileName);
		}
		
		Auditor auditor = new Auditor();
		try{
			AuditResult result = auditor.handle(transcriptIs);
			System.out.println(result);
		} finally {
			Helper.close(transcriptIs);
		}
	}
	
	public static void usage(String msg) {
		System.out.println(msg);
		System.out.println("Usage: \n    java -jar auditor.jar -f [path to transcript json file]");
		System.exit(1);
	}
}
