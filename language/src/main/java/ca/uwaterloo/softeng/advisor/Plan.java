package ca.uwaterloo.softeng.advisor;

/* copyright 2009 Andrew Morton */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

class Plan {

	/**
	 * Course requirements are grouped into named categories.  A category
	 * maintains a list of matches of course to requirement.
	 */
	public class Category implements Comparable {
		private String name;
		private List<Rqmt> rqmtList;

		/**
		 * Constructor assigns name and empty list of requirements.
		 */
		public Category(String name) {
			this.name = name;
			this.rqmtList = new ArrayList<Rqmt>();
		}

		/**
		 * Add a requirement with null course to matches.
		 * @param rqmt requirement
		 */
		public void add(Rqmt rqmt) {
			rqmtList.add(rqmt);
		}

		public int compareTo(Object obj) throws ClassCastException {
			if (!(obj instanceof Category)) {
				throw new ClassCastException("Category object expected");
			}

			Category category = (Category)obj;

			return this.name.compareTo(category.name);
		}

		public String get_name() {
			return name;
		}

		public Iterator<Rqmt> get_rqmtIterator() {
			return rqmtList.iterator();
		}

	}

	private String name;
	private int calendar;
	private List<Category> categoryList;

	public Plan(String name, int calendar) {
		this.name = name;
		this.calendar = calendar;
		this.categoryList = new ArrayList<Category>();
	}

	public void add(String category_name, Rqmt rqmt) {
		boolean new_category = true;
		// usually rqmts added in category order so start looking for
		// category from the end of the list
		ListIterator<Category> itr = categoryList.listIterator(
				categoryList.size());
		while(new_category && itr.hasPrevious()) {
			Category category = itr.previous();
			if(category.name.equals(category_name)) {
				category.add(rqmt);
				new_category = false;
			}
		}
		if(new_category) {
			categoryList.add(new Category(category_name));
			categoryList.get(categoryList.size()-1).add(rqmt);
		}
	}

	public Iterator<Category> get_categoryIterator() {
		return categoryList.iterator();
	}

	public String get_name() {
		return name;
	}

}
