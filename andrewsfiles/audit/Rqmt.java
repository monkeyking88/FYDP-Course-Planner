package ca.uwaterloo.softeng.advisor;

/* copyright 2010 Andrew Morton */


class Rqmt implements Comparable {
	private String sbj_list;
	private String cnbr_name;
	private int number;

	public Rqmt(String sbj_list, String cnbr_name) {
		this.sbj_list = new String(sbj_list);
		this.cnbr_name = new String(cnbr_name);
		//number = -1;
	}

	public Rqmt(String sbj_list, String cnbr_name, int number) {
		this.sbj_list = new String(sbj_list);
		this.cnbr_name = new String(cnbr_name);
		this.number = number;
	}

	public int compareTo(Object obj) throws ClassCastException {
		if(!(obj instanceof Rqmt)) {
			throw new ClassCastException("A Rqmt object expected.");
		}

		Rqmt rqmt = (Rqmt)obj;
		return this.number - rqmt.number;
	}

	public boolean isList() {
		return sbj_list.equals("list");
	}

	public String toString() {
		String rqmt;
		if(sbj_list.equals("list")) {
			int sepIndex = cnbr_name.indexOf("_");
			if(sepIndex >= 0) {
				String plan = cnbr_name.substring(0, sepIndex).toUpperCase();
				String list = cnbr_name.substring(sepIndex+1,
						cnbr_name.length());
				return plan + " " + list + " list";
			} else {
				return cnbr_name + " list";
			}
		} else {
			return sbj_list + cnbr_name;
		}
	}

	public String get_sbj_list() { return sbj_list; }
	public String get_cnbr_name() { return cnbr_name; }
	public int get_number() { return number; }

}
