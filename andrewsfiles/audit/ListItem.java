package ca.uwaterloo.softeng.advisor;

/* copyright 2010 Andrew Morton */

class ListItem implements Comparable {
	private String sbj_list;
	private String cnbr_name;

	public ListItem(String sbj_list, String cnbr_name) {
		this.sbj_list = sbj_list;
		this.cnbr_name = cnbr_name;
	}

	public int compareTo(Object obj) throws ClassCastException {
		if (!(obj instanceof ListItem)) {
			throw new ClassCastException("ListItem object expected");
		}

		ListItem listItem = (ListItem)obj;

		int comp = this.sbj_list.compareTo(listItem.sbj_list);
		if(comp == 0) comp = this.cnbr_name.compareTo(listItem.cnbr_name);

		return comp;
	}

	public boolean equals(Object o) {
		return (o instanceof ListItem) && (
				((ListItem)o).sbj_list.equals(this.sbj_list) &&
				((ListItem)o).cnbr_name.equals(this.cnbr_name));
	}

	public String get_sbj_list() { return sbj_list; }
	public String get_cnbr_name() { return cnbr_name; }
}
