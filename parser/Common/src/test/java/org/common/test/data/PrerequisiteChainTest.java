package org.common.test.data;

import static org.junit.Assert.*;

import java.util.List;

import org.common.prerequisitechain.PrerequisiteChainParser;
import org.junit.Test;

public class PrerequisiteChainTest {

	@Test
	public void test() {
		List<List<String>> table = PrerequisiteChainParser.genTable();
		//System.out.println(PrerequisiteChainParser.p_str(table));
		System.out.println(PrerequisiteChainParser.genReq(table));
	}

}
