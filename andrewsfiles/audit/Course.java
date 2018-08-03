package ca.uwaterloo.softeng.advisor;

/* copyright 2009 Andrew Morton */

class Course implements Comparable {
	static private String[] letterGrade = {
		"A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-",
		"F+", "F", "F-" };
	static private int[] letterGradeVal = {
		95, 89, 83, 78, 75, 72, 68, 65, 62, 58, 55, 52, 46, 38, 32 };

	String subject;
	String catalog_nbr;
	int term_code;
	int class_nbr;
	String section;
	String title;
	String campus;
	double units;
	String instructor;
	String grading_basis;
	String grade;
	String req_desig;
	String req_desig_grade;
	int ticket;

	public Course(String subject, String catalog_nbr,
			int term_code, int class_nbr,
			String section, String title, String campus, double units,
			String instructor, String grading_basis, String grade,
			String req_desig, String req_desig_grade, int ticket)
	{
		this.subject = subject;
		this.catalog_nbr = catalog_nbr;
		this.term_code = term_code;
		this.class_nbr = class_nbr;
		this.section = section;
		this.title = title;
		this.campus = campus;
		this.units = units;
		this.instructor = instructor;
		this.grading_basis = grading_basis;
		this.grade = grade;
		this.req_desig = req_desig;
		this.req_desig_grade = req_desig_grade;
		this.ticket = ticket;
	}

	public Course(String subject, String catalog_nbr) {
		this.subject = subject;
		this.catalog_nbr = catalog_nbr;
		this.term_code = 0;
		this.class_nbr = 0;
		this.section = "";
		this.title = "";
		this.campus = "";
		this.units = 0.0;
		this.instructor = "";
		this.grading_basis = "";
		this.grade = "";
		this.req_desig = "";
		this.req_desig_grade = "";
		this.ticket = 0;
	}

	public int compareTo(Object obj) throws ClassCastException {
		if (!(obj instanceof Course)) {
			throw new ClassCastException("A Course object expected.");
		}

		Course course = (Course)obj;
		int comp = 0;
		int i = 0;
		while(comp == 0 && i < 8) {
			switch(i) {
				case 0: comp = this.subject.compareTo(course.subject); break;
				case 1: comp = this.catalog_nbr.compareTo(course.catalog_nbr);
						break;
				case 2: comp = this.term_code - course.term_code; break;
				case 3: comp = this.section.compareTo(course.section);
						break;
				case 4: comp = this.grading_basis.compareTo(
										course.grading_basis); break;
				case 5: comp = this.grade.compareTo(course.grade); break;
				case 6: comp = this.req_desig.compareTo(course.req_desig);
						break;
				case 7: comp = this.req_desig_grade.compareTo(
										course.req_desig_grade); break;
				default: break;
			}
			i++;
		}

		return comp;
	}

	public boolean valid() {
		return ((inProgress() || pass()) && inDegree());
	}

	public boolean pass() {
		boolean pass = false;
		if(grading_basis.equals("NUM") || grading_basis.equals("NGP")
				|| grading_basis.equals("DRN") || grading_basis.equals("NRN")) {
			if(getIntGrade() >= 50) {
				pass = true;
			} else if(grade.equals("CR")) {
				pass = true;
			} else if(grade.equals("AEG")) {
				pass = true;
			} else if(req_desig.equals("SUPP") && req_desig_grade.equals("S")) {
				pass = true;
			}
		} else if(grading_basis.equals("CLF") || grading_basis.equals("CLR")) {
			if(req_desig.equals("SUPP") && req_desig_grade.equals("S")) {
				pass = true;
			}
		} else if(grading_basis.equals("CNW")) {
			if(grade.equals("CR")) {
				pass = true;
			}
		} else if(grading_basis.equals("CNP")) {
			if(grade.equals("CR")) {
				pass = true;
			}
		} else if(grading_basis.equals("CNC")) {
			if(grade.equals("CR")) {
				pass = true;
			}
		} else if(grading_basis.equals("ABC")) {
			if(grade.equals("A+") || grade.equals("A") || grade.equals("A-")
				|| grade.equals("B+") || grade.equals("B") || grade.equals("B-")
				|| grade.equals("C+") || grade.equals("C") || grade.equals("C-")
				|| grade.equals("D+") || grade.equals("D") || grade.equals("D-")
				) {
				pass = true;
			}
		}
		return pass;
	}

	public boolean inProgress() {
		return inProgress(this.grade);
	}

	public static boolean inProgress(String grade) {
		return (grade.equals("") || grade.equals("INC") || grade.equals("IP")
				|| grade.equals("NG") || grade.equals("UR"));
	}

	public boolean inDegree() {
		return !(req_desig.equals("NRNA") || req_desig.equals("TRIA")
				|| grading_basis.equals("CLC")
				|| ((grading_basis.equals("CLF") || grading_basis.equals("CLR"))
					&& !(req_desig.equals("SUPP")
						&& req_desig_grade.equals("S")))
				|| grading_basis.equals("MEX"));
	}

	public boolean inAverage() {
		return ((grading_basis.equals("NUM")
					|| grading_basis.equals("CLF")
					|| grading_basis.equals("CLC")
					//|| grading_basis.equals("CLR") - used for out of avg
					|| grading_basis.equals("ABC"))
				&& !(req_desig.equals("DRNA") || req_desig.equals("DRNC")
					|| req_desig.equals("NRNA"))
				&& !grade.equals("WD")
				&& !subject.equals("WKRPT"));
	}

	public boolean inFailCount() {
		return (inDegree() && units>0 && !pass() && !inProgress()
			&& !grade.equals("WD")
			//&& !grading_basis.equals("CLR") -- taken care of by inDegree()
			//&& !grading_basis.equals("CLF")
			&& !grading_basis.equals("CNW")
			&& !grading_basis.equals("CNP"));
	}

	public int getIntGrade() {
		return parseIntGrade(this.grade);
	}

	public static int parseIntGrade(String grade) {
		int gradeVal = 0;
		try { gradeVal = Integer.parseInt(grade); }
		catch (NumberFormatException nfe) {
			for(int i=0; i<letterGrade.length; i++) {
				if(grade.equals(letterGrade[i])) {
					gradeVal = letterGradeVal[i];
				}
			}
			if(grade.equals("DNW") || grade.equals("FTC")
					|| grade.equals("WF")) {
				gradeVal = 32;
			}
		}
		return gradeVal;
	}

	public String getGrading_basis() {
		return grading_basis;
	}

	public String getGrade() {
		return grade;
	}

	public String getReq_desig() {
		return req_desig;
	}

	public String getGradeString() {
		return grade + " " + req_desig + " " + req_desig_grade;
	}

	public String getNameString() {
		return subject + catalog_nbr;
	}

	public String getSubject() {
		return subject;
	}

	public String getCatalog_nbr() {
		return catalog_nbr;
	}

	public int getTerm_code() {
		return term_code;
	}

	public int getClass_nbr() {
		return class_nbr;
	}

	public String getSection() {
		return section;
	}

	public String getTitle() {
		return title;
	}

	public String getCampus() {
		return campus;
	}

	public double getUnits() {
		return units;
	}

	public String getInstructor() {
		return instructor;
	}

	public int getTicket() {
		return ticket;
	}

	public String toString() {
		return subject + catalog_nbr + "/"
			+ Term.decodeShort(term_code) + "/"
			+ grade + " " + req_desig + " " + req_desig_grade;
	}

}
