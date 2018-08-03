package ca.uwaterloo.softeng.advisor;

/* copyright 2010 Andrew Morton */


class Approval {
	private int uw_id;
	private String subject;
	private String catalog_nbr;
	private String sbj_list;
	private String cnbr_name;
	private String given_on;
	private String given_by;

	public Approval(int uw_id, String subject, String catalog_nbr,
			String sbj_list, String cnbr_name,
			String given_on, String given_by) {
		this.uw_id = uw_id;
		this.subject = new String(subject);
		this.catalog_nbr = new String(catalog_nbr);
		this.sbj_list = new String(sbj_list);
		this.cnbr_name = new String(cnbr_name);
		this.given_on = new String(given_on);
		this.given_by = new String(given_by);
	}

	public int get_uw_id() { return uw_id; }
	public String get_subject() { return subject; }
	public String get_catalog_nbr() { return catalog_nbr; }
	public String get_sbj_list() { return sbj_list; }
	public String get_cnbr_name() { return cnbr_name; }
	public String get_given_on() { return given_on; }
	public String get_given_by() { return given_by; }

	public String getOrigNameString() {
		return sbj_list + cnbr_name;
	}

	public String getSubstNameString() {
		return subject + catalog_nbr;
	}

	public String toString() {
		return subject + catalog_nbr + " &asymp; "
			+ (new Rqmt(sbj_list, cnbr_name)).toString()
			+ "&nbsp;&nbsp;&nbsp;(" + given_on + "," + given_by + ")";
	}
}
