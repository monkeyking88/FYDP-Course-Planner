package org.common.reporting;

import org.common.exception.ReportableException;

public class RuleNameParser {
	
	public static String prettyfy(String ruleName){
		String ruleWithNameSpace = ruleName.replaceAll("_", " ").trim();
		if (ruleWithNameSpace.indexOf(" ") < 0) {
			throw new ReportableException("Rule name should always contain '_', invalid name: " + ruleName);
		}
		return ruleWithNameSpace.split(" ", 2)[1];
	}

}
