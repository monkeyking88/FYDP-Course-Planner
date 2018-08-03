package org.auditor.test.auditor;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.auditor.main.Auditor;
import org.common.audit.AuditResult;
import org.common.helper.Helper;
import org.junit.Test;

public class AuditorTest {

	Auditor auditor = new Auditor();
	
	@Test
	public void test() throws Exception {
		for (int i = 11; i <= 72; i++) {
			InputStream transcriptIs = getClass().getClassLoader().getResourceAsStream("transcript/byron_test/test_" + i +".json");
			
			List<String> degrees = new ArrayList<String>();
			degrees.add("CS");
			
			try{
				AuditResult result = auditor.handle(transcriptIs);
				System.out.println(result);
			} finally {
				Helper.close(transcriptIs);
			}
		}
		
	}
	
    @Test
    public void biomedscihTest() throws Exception {
        for (int i = 100; i <= 119; i++) {
            InputStream transcriptIs = getClass().getClassLoader().getResourceAsStream("transcript/biomedscih/" + i + ".json");
            
            try{
                AuditResult result = auditor.handle(transcriptIs);
                System.out.println(result);
            } finally {
                Helper.close(transcriptIs);
            }
        }
        
    }
}
