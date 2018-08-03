package org.common.reporting;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.common.exception.ReportableException;

public class AuditReportingTrunk implements AuditReportingNode, Serializable {

	private static final long serialVersionUID = -3305889433099693139L;

	@JsonProperty("ruleName")
	private String ruleName;

	@JsonProperty("childList")
	private List<AuditReportingNode> childList;
	
	public AuditReportingTrunk(String ruleName, List<AuditReportingNode> childList) {
		super();
		this.ruleName = ruleName;
		this.childList = childList;
	}

	public String getRuleName() {
		return ruleName;
	}

	public List<AuditReportingNode> getChildList() {
		return childList;
	}

	@Override
	public int getCount() {
		int total = 1;
		for (AuditReportingNode child : childList) {
			total += child.getCount();
		}
		if (total == 1) {
			throw new ReportableException("Error while reporting, reporting trunk with rule name: " + ruleName + " has no child count");
		}
		return total;
	}
	
	@Override
	public String toString(){
		return this.toStringWithIndent(0);
	}
	
	@Override
	public String toStringWithIndent(int depth) {
		if (childList.size() == 0) {
			return "";
		}
		String base_str = "";
		for (int i = 0; i < depth; i++) {
			base_str += "    ";
		}
		String str = base_str + "Degree requirement \"" + RuleNameParser.prettyfy(ruleName) + "\" fulfilled by: \n";
		String newStr = "";
		for (AuditReportingNode child : childList) {
			newStr += child.toStringWithIndent(depth + 1);
		}

		if (newStr.isEmpty() && RuleNameParser.prettyfy(ruleName).contains("prerequisitechainlength3")) {
			str = "";

			if (RuleNameParser.prettyfy(ruleName).equalsIgnoreCase("prerequisitechainlength3 result")) {
				str += base_str + "Degree requirement \"" + RuleNameParser.prettyfy(ruleName) + "\" is not fulfilled \n";
				str += base_str + "    " + "Degree requirement \"prerequisitechainlength3 prerequisit chain len 3\" is not fulfilled \n";
			}
		} else {
			str += newStr;
		}
		return str;
	}

}
