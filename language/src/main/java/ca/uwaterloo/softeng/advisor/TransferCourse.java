package ca.uwaterloo.softeng.advisor;

/* copyright 2013 Andrew Morton */

/**
 * Record of credit transfered from another institution.
 */
class TransferCourse extends Course {
	String institution;
	String subject;
	String catalog_nbr;
	double units;

	/**
	 * Constructor makes a base class with credit and the uw course info
	 * and saves the transfer institution info.
	 */
	public TransferCourse(String institution, int term_code,
			String ex_sbj, String ex_cnbr, double ex_credits,
			String uw_sbj, String uw_cnbr, double uw_credits) {
		super(uw_sbj, uw_cnbr, term_code, -1, "", "", "", uw_credits, "",
				"CNC", "CR", "", "", -1);
		this.institution = institution;
		this.subject = ex_sbj;
		this.catalog_nbr = ex_cnbr;
		this.units = ex_credits;
	}

	/** institution getter. */
	public String get_institution() { return institution; }
	/** term_code getter. */
	public int get_term_code() { return term_code; }
	/** subject getter. */
	public String get_subject() { return subject; }
	/** catalog_nbr getter. */
	public String get_catalog_nbr() { return catalog_nbr; }

	/** Name of other institution's course. */
	public String courseName() {
		return subject + catalog_nbr;
	}

	/** Name of equivalent UW course. */
	public String equivName() {
		return super.getNameString();
	}

	/**
	 * Needs different output than from Course base class.  Show
	 * institution and course but that's it.
	 */
	public String toString() {
		return institution + "/" + subject + catalog_nbr;
	}

}
