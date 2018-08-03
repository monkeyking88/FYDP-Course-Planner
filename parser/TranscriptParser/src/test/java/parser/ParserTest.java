package parser;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.common.reporting.TranscriptReport;
import org.junit.Test;
import org.transcript.main.TranscriptParser;

public class ParserTest {

	@Test
	public void test() throws Exception {
		InputStream is = getClass().getClassLoader().getResourceAsStream("transcript/golsonTranscript");
		OutputStream os = new ByteArrayOutputStream();
		
		TranscriptParser parser = new TranscriptParser();
		parser.handle(is, os);
		is.close();
		os.close();
	}
	
	@Test
	public void testMore() throws Exception {
		for (int i = 1; i < 5; i++){
			InputStream is = getClass().getClassLoader().getResourceAsStream("transcript/test_" + i + ".json");
			TranscriptParser parser = new TranscriptParser();
			TranscriptReport report = parser.handle(is, System.out);
			assertTrue(report.getNotFoundList().size() == 0);
			System.out.println(report);
			is.close();
		}
		InputStream is = getClass().getClassLoader().getResourceAsStream("transcript/test_5.json");;
		TranscriptParser parser = new TranscriptParser();
		TranscriptReport report = parser.handle(is, System.out);
		assertTrue(report.getNotFoundList().size() == 0);
		System.out.println(report);
		is.close();
		
	}
	
	
}
