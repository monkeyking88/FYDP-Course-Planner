package ca.uwaterloo.softeng.advisor;

/* copyright 2013 Andrew Morton */

class CourseCredit extends Course implements Comparable {

	public CourseCredit(String subject, String catalog_nbr, String title) {
		super(subject, catalog_nbr, 0, 0, "", title, "", 0.0, "", "CNC", "CR",
				"", "", 0);
	}

}
