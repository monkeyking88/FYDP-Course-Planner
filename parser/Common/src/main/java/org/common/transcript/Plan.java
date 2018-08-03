package org.common.transcript;

import java.io.Serializable;
import org.common.transcript.PlanCodes;
import org.common.exception.ReportableException;

import org.codehaus.jackson.annotate.JsonProperty;

public class Plan implements Serializable {

	private static final long serialVersionUID = -6999282125296771891L;
	
	@JsonProperty("year")
	private int year;
	
	@JsonProperty("planName")
	private String planName;
	
	@JsonProperty("position")
	private int position;
	
	@JsonProperty("planCode")
	private String planCode;
	
	public Plan() {
		super();
	}
	
	public Plan(int year, int position, String planCode) {
		super();
		this.year = year;
		this.planCode = planCode;
		try {
			this.planName = PlanCodes.codeMap.get(planCode);
		}
		catch (Exception e) {
			throw new ReportableException("Plan Code <" + planCode + "> not found.");
		}
		
		this.position = position;
	}

	public boolean isCoop() {
		return Plan.isCoop(this.planName);
	}
	
	public int getYear() {
		return year;
	}

	public int getPosition() {
		return position;
	}
	
	public String getCode() {
		return planCode;
	}
	
	// Hacky method to determine whether a plan is Co-op
	// In plan name, if the plan is Co-op, it will contain one of "Co-operative program", "Co-op prog", or "Co-op"
	public static boolean isCoop(String planName) {
		return planName.toLowerCase().contains("Co-op".toLowerCase());
	}

	@Override
	public String toString() {
		return "Plan Name [" + planName + "]  Year [" + year + "]  Plan Code [" + planCode + "]  " + (this.isCoop() ? " Coop" : "Regular");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + year;
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
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
		Plan other = (Plan) obj;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
				if (year != other.year)
			return false;
		return true;
	}
}
