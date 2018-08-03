package ca.uwaterloo.softeng.advisor;

/* copyright 2010 Andrew Morton */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Term {
	private int term_code;
	private String level;
	private String standing;
	private String awards;
	private String plan_10;
	private String plan_20;
	private String plan_30;
	private List<Course> courseList;
	private double gradeTotal;
	private double unitsInAverage;
	private double unitsTotal;
	private boolean hasPdCourse;
	private boolean hasNonPdCourse;

	static public int getCode(Calendar cal) {
		return ((cal.get(Calendar.YEAR) - 1900) * 10) +
			((cal.get(Calendar.MONTH) / 4) * 4 + 1);
	}

	static public int getCurrent() {
		return getCode(Calendar.getInstance());
	}

	static public int next(int term_code) {
		if(term_code % 10 == 9) {
			return term_code + 2;
		} else {
			return term_code + 4;
		}
	}

	static public int previous(int term_code) {
		if(term_code % 10 == 1) {
			return term_code - 2;
		} else {
			return term_code - 4;
		}
	}

	public Term(int term_code, String level, String standing, String awards,
			String plan_10, String plan_20, String plan_30) {
		this.term_code = term_code;
		this.level = level;
		this.standing = standing;
		this.awards = awards;
		this.plan_10 = plan_10;
		this.plan_20 = plan_20;
		this.plan_30 = plan_30;
		courseList = new ArrayList<Course>();
		gradeTotal = 0;
		unitsInAverage = 0;
		unitsTotal = 0;
		hasPdCourse = false;
		hasNonPdCourse = false;
	}

	public void add(Course course) {
		courseList.add(course);
		if(course.inAverage() && !course.inProgress()) {
			gradeTotal += (double)course.getIntGrade() * course.getUnits();
			unitsInAverage += course.getUnits();
		}
		unitsTotal += course.getUnits();

		if(course.getSubject().equals("PD"))
			hasPdCourse = true;
		else if(!course.getSubject().equals("COOP"))
			hasNonPdCourse = true;
	}

	public List<Course> getCourseList() {
		return courseList;
	}

	public double getUnitsInAverage() {
		return unitsInAverage;
	}

	public double getUnitsTotal() {
		return unitsTotal;
	}

	public double getGradeTotal() {
		return gradeTotal;
	}

	public double getAverage() {
		if(unitsInAverage > 0) {
			return (double)gradeTotal / unitsInAverage;
		} else {
			return 0;
		}
	}

	public int getFailCount() {
		int count = 0;

		for(Course c : courseList) {
			if(c.inFailCount()) {
				count++;
			}
		}

		return count;
	}

	public int getTerm_code() {
		return term_code;
	}

	public String getLevel() {
		return level;
	}

	public String getStanding() {
		return standing;
	}

	public String getAwards() {
		return awards;
	}

	public String getPlan_10() {
		return plan_10;
	}

	public String getPlans() {
		String plans = "";
		if(!plan_30.equals("")) {
			plans = "," + plan_30;
		}
		if(!plan_20.equals("")) {
			plans = " (" + plan_20 + plans + ")";
		}
		plans = plan_10 + plans;
		return plans;
	}

	public static String decodeShort(int code) {
		return decodeTermShort(code)
			+ String.format("%02d", decodeYear(code) % 100);
	}

	public static String decodeLong(int code) {
		return decodeTermLong(code) + " " + decodeYear(code);
	}

	public static String decodeYearShort(int code) {
		return String.format("%02d", ((code / 10) + 1900) % 100);
	}

	public static int decodeYear(int code) {
		return (code / 10) + 1900;
	}

	public static String decodeTermShort(int code) {
		int term = code % 10;
		switch (term) {
			case 1: return "W";
			case 5: return "S";
			case 9: return "F";
			default: return "x";
		}
	}

	/**
	 * Extacts term (Winter, Spring, Fall) from term code.
	 *
	 * @param code of term
	 * @return term string
	 */
	public static String decodeTermLong(int code) {
		int term = code % 10;
		switch (term) {
			case 1: return "Winter";
			case 5: return "Spring";
			case 9: return "Fall";
			default: return "Invalid";
		}
	}

	private static String decode(int code, int format) {
		int term = code % 10;
		int year = (code / 10) + 1900;
		String decodedTerm = "";

		switch(format) {
			case 0:
				switch (term) {
					case 1: decodedTerm +="W"; break;
					case 5: decodedTerm +="S"; break;
					case 9: decodedTerm +="F"; break;
				}
				decodedTerm += String.format("%02d", year % 100);
				break;
			case 1:
				switch (term) {
					case 1: decodedTerm +="Winter"; break;
					case 5: decodedTerm +="Spring"; break;
					case 9: decodedTerm +="Fall"; break;
				}
				decodedTerm += " " + year;
				break;
		}

		return decodedTerm;
	}

	/**
	 * Calculates number of terms between two term codes.
	 *
	 * @param code1 first term code
	 * @param code2 second term code
	 * @return difference in numbers of terms
	 */
	static public int distance(int code1, int code2) {
		// difference in years * 3 terms/year
		int distance = (code1/10 - code2/10) * 3;
		// difference in terms
		return distance + (code1%10 - code2%10)/4;
	}

	/**
	 * Tells if term has a PD course in it.
	 *
	 * @return true if has PD course
	 */
	public boolean hasPdCourse() {
		return hasPdCourse;
	}

	/**
	 * Tells if term has a non-PD (i.e. academic) course in it.
	 *
	 * @return true if has non-PD course
	 */
	public boolean hasNonPdCourse() {
		return hasNonPdCourse;
	}

}
