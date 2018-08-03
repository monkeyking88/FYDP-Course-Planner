package org.common.reporting;

import java.io.Serializable;
import org.codehaus.jackson.annotate.JsonProperty;

public class AuditReportUnconditionalLeaf implements AuditReportingNode, Serializable {

	private static final long serialVersionUID = 7706596149274733438L;

	@JsonProperty("ruleName")
	private String ruleName;

	
	public AuditReportUnconditionalLeaf(String ruleName) {
		super();
		this.ruleName = ruleName;
	}

	public String getRuleName() {
		return ruleName;
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public String toString() {
		return this.toStringWithIndent(0);
	}
	
	@Override
	public String toStringWithIndent(int depth) {
		String str = "";
		for (int i = 0; i < depth; i++) {
			str += "    ";
		}
		//avoid explosive output
		if (RuleNameParser.prettyfy(ruleName).contains("prerequisitechainlength3")) {
			return "";
		}
		str += ("Degree requirement \"" + RuleNameParser.prettyfy(ruleName) + "\" is not fulfilled\n");
		return str;
	}

}
