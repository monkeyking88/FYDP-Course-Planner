package ca.uwaterloo.softeng.advisor;

/* copyright 2010 Andrew Morton */

import java.util.ArrayList;
import java.util.List;

class Student implements Comparable {
	static public final int invalid_uw_id = -1;
	static public final int invalid_calendar = -1;

	private final int uw_id;
	private String first_name;
	private String middle_name;
	private String last_name;
	private String email;
	private int calendar;
	private String term_level;
	private String plan_10;
	private String plan_20;
	private String plan_30;

	public Student(int uw_id,
			String first_name, String middle_name, String last_name,
			String email, int calendar,
			String term_level,
			String plan_10, String plan_20, String plan_30) {
		this.uw_id = uw_id;
		this.first_name = first_name;
		this.middle_name = middle_name;
		this.last_name = last_name;
		this.email = email;
		this.calendar = calendar;
		this.term_level = term_level;
		this.plan_10 = plan_10;
		this.plan_20 = plan_20;
		this.plan_30 = plan_30;
	}

	public int compareTo(Object obj) throws ClassCastException {
		if(!(obj instanceof Student)) {
			throw new ClassCastException("A Student object expected.");
		}

		Student student = (Student)obj;
		int comp = 0;
		int i = 0;
		while(comp == 0 && i < 7) {
			switch(i) {
				case 0: comp = this.last_name.compareTo(student.last_name);
						break;
				case 1: comp = this.first_name.compareTo(student.first_name);
						break;
				case 2: comp = this.middle_name.compareTo(student.middle_name);
						break;
				case 3: comp = this.uw_id - student.uw_id; break;
				default: break;
			}
			i++;
		}

		return comp;
	}

	public int getUw_id() {
		return uw_id;
	}

	public String getNames(boolean hide) {
		if(hide)
			return "Firstname Lastname";
		else
			return first_name + " " + middle_name + " " + last_name;
	}

	public String getFirstName(boolean hide) {
		if(hide)
			return "Firstname";
		else
			return first_name;
	}

	public String getLastName(boolean hide) {
		if(hide)
			return "Lastname";
		else
			return last_name;
	}

	public String toString(boolean hide) {
		return getNames(hide) + " (" + String.format("%08d",uw_id) + ")";
	}

	public String getEmail(boolean hide) {
		if(hide)
			return "firstname.lastname@uwaterloo.ca";
		else
			return email;
	}

	public int getCalendar() {
		return calendar;
	}

	public void setCalendar(int calendar) {
		this.calendar = calendar;
	}

	public String getTerm_level() {
		return term_level;
	}

	public String getPlan_10() {
		return plan_10;
	}

	public String getPlans() {
		String plans = plan_10;
		if(!plan_20.equals("")) {
			plans = plans + " (" + plan_20;
			if(!plan_30.equals("")) {
				plans = plans + "," + plan_30;
			}
			plans = plans + ")";
		}
		return plans;
	}

}
