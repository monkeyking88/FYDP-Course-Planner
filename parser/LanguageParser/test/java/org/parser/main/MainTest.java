package org.parser.main;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

public class MainTest {

	@Test
	public void test() {
		try {
			Main.main(null);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
