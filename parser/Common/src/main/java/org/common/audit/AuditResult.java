package org.common.audit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonProperty;
import org.common.course.Course;
import org.common.reporting.AuditReportingNode;
import org.common.reporting.RuleNameParser;

public class AuditResult implements Serializable {

	private static final long	serialVersionUID	= -42241804549884586L;

	public enum AuditOutcome {
		GRADUATE, FAIL, UNSUPPORTED, ERROR, INPROGRESS
	}
	
	public String coursePlan; 
	
	public static class AuditSingleResult implements Serializable{

		private static final long	serialVersionUID	= 2701044911976857679L;
		
		@JsonProperty("outcome")
		private AuditOutcome outcome;
		
		@JsonProperty("explanation")
		private String explanation;
		
		//map of rule to courses that satisfy such rule
		@JsonProperty("courseMapping")
		private Map<String, List<Course>> courseMapping;
		
		//tree of rule to rules or rule to courses to print indentation
		@JsonProperty("courseTreeForest")
		private List<AuditReportingNode> courseTreeForest;
		
		@JsonProperty("failedRules")
		private List<String> failedRules;
		
		public AuditSingleResult() {
			super();
			this.outcome = null;
			this.explanation = null;
			this.courseMapping = null;
			this.courseTreeForest = null;
			this.failedRules = null;
		}
		
		public AuditSingleResult(AuditOutcome outcome, String explanation, Map<String, List<Course>> courseMapping) {
			super();
			this.outcome = outcome;
			this.explanation = explanation;
			this.courseMapping = courseMapping;
			this.courseTreeForest = null;
			this.failedRules = null;
		}

		public AuditOutcome getOutcome() {
			return this.outcome;
		}

		public String getExplanation() {
			return this.explanation;
		}
		
		public Map<String, List<Course>> getCourseMapping(){
			return this.courseMapping;
		}
		
		public AuditSingleResult setCourseMapping(Map<String, List<Course>> courseMapping){
			this.courseMapping = courseMapping;
			return this;
		}
		
		public List<String> getFailedRules() {
			return failedRules;
		}

		public AuditSingleResult setFailedRules(List<String> failedRules) {
			this.failedRules = failedRules;
			return this;
		}
		
		public List<AuditReportingNode> getCourseTreeForest() {
			return courseTreeForest;
		}

		public AuditSingleResult setCourseTreeForest(List<AuditReportingNode> courseTreeForest) {
			this.courseTreeForest = courseTreeForest;
			return this;
		}

		@Override
		public String toString() {
			String str = "Outcome: [" + this.outcome + "]\n";
			str += "Brief explanation: " + this.explanation + "\n";
			List<String> unsatisfiedrules = new ArrayList<String>();
			if (this.courseMapping != null && this.courseTreeForest == null) {
				str += "Rules to courses mapping: \n";
				for (Entry<String, List<Course>> ent : this.courseMapping.entrySet()) {
					
					if (ent.getValue().size() == 0) {
						unsatisfiedrules.add( "Degree requirement \"" + RuleNameParser.prettyfy(ent.getKey()) + "\"\n" );
					}
					else {
						str += "Degree requirement \"" + RuleNameParser.prettyfy(ent.getKey()) + "\" fulfilled by: ";
						for (Course c : ent.getValue()) {
							str += c.getName() + " ";
						}
						str += "\n";
					}
				}
				if (unsatisfiedrules.size() != 0) str += "\nThe following requirements were satisfied by alternatives:\n";
				for(int i=0; i<unsatisfiedrules.size(); i++){
				    str += unsatisfiedrules.get(i);
				}
			}
			if (this.courseTreeForest != null) {
				str += "\nRules to courses mapping with indentation: \n";
				for (AuditReportingNode courseTree: this.courseTreeForest) {
					str += courseTree.toString();
				}
			}
			
			if (this.failedRules != null) {
				str += "\nTranscript failed to meet following rules: \n";
				for (String rule : this.failedRules) {
					str += "Degree requirement \"" + RuleNameParser.prettyfy(rule) + "\" was not satisfied\n";	
				}
			}
			
			return str;
		}
	}

	
	@JsonProperty("degrees")
	private List<String> degrees;
	
	@JsonProperty("results")
	private Map<String, AuditSingleResult> results;

	public AuditResult() {
		super();
		degrees = new ArrayList<String>();
		results = new HashMap<String, AuditSingleResult>();
		coursePlan = "";
	}
	
	public void addResult(String degree, AuditOutcome outcome, String explanation){
		AuditSingleResult result = new AuditSingleResult(outcome, explanation, null);
		this.degrees.add(degree);
		this.results.put(degree, result);
	}
	
	public void addResult(String degree, AuditOutcome outcome, String explanation, Map<String, List<Course>> courseMapping){
		AuditSingleResult result = new AuditSingleResult(outcome, explanation, courseMapping);
		this.degrees.add(degree);
		this.results.put(degree, result);
	}
	
	public void addResult(String degree, AuditSingleResult result){
		this.degrees.add(degree);
		this.results.put(degree, result);
	}

	public AuditResult(ArrayList<String> degrees, Map<String, AuditSingleResult> results) {
		super();
		this.degrees = degrees;
		this.results = results;
	}


	public List<String> getDegrees() {
		return this.degrees;
	}


	public Map<String, AuditSingleResult> getResults() {
		return this.results;
	}


	@Override
	public String toString() {
		String str = "===========  Audit Result =========== \n";
		for (String degree : this.degrees) {
			str += "Degree: [" + degree + "] \n";
			str += this.results.get(degree).toString();
		}
		return str;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.degrees == null) ? 0 : this.degrees.hashCode());
		result = prime * result + ((this.results == null) ? 0 : this.results.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditResult other = (AuditResult) obj;
		if (this.degrees == null) {
			if (other.degrees != null)
				return false;
		} else if (!this.degrees.equals(other.degrees))
			return false;
		if (this.results == null) {
			if (other.results != null)
				return false;
		} else if (!this.results.equals(other.results))
			return false;
		return true;
	}
	
}
