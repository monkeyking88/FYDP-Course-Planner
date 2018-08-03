package org.common.reporting;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.common.course.Course;

public class AuditReportingLeaf implements AuditReportingNode, Serializable {

	private static final long serialVersionUID = 997185065003909839L;

	@JsonProperty("ruleName")
	private String ruleName;

	@JsonProperty("courseList")
	private List<Course> courseList;
	
	public AuditReportingLeaf(String ruleName, List<Course> courseList) {
		super();
		this.ruleName = ruleName;
		this.courseList = courseList;
	}

	public String getRuleName() {
		return ruleName;
	}

	public List<Course> getCourseList() {
		return courseList;
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
		if (courseList.size() == 0) {
			// avoid explosive output
			if (RuleNameParser.prettyfy(ruleName).contains("prerequisitechainlength3")) {
				return "";
			} else {
				str += ("Degree requirement \"" + RuleNameParser.prettyfy(ruleName) + "\" fulfilled by alternatives\n");
			}
		} else {
			str += "Degree requirement \"" + RuleNameParser.prettyfy(ruleName) + "\" fulfilled by: ";
			for (Course c : courseList) {
				str += c.getName() + " ";
			}
			str += "\n";
		}
		return str;
	}

}
