package ca.uwaterloo.softeng.advisor;

/* copyright 2013 Andrew Morton */

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Audit class audits a student for a given plan.  The status of the audit
 * can be retrieved along with a list of courses matching each requirement.
 */
class Audit {
	/**
	 * Audit status.  Unknown if no audit done.  Provisionally_Complete
	 * means that it will be complete if all in-progress courses are
	 * passed.
	 */
	public enum Status { Unknown, Incomplete, Provisionally_Complete, Complete
	};
	
	/**
	 * Defines priority of course in matching a requirement.  Highest is
	 * approved which means the course is from the approvals entered
	 * manually.  Next highest is passed courses, followed last by
	 * in-progress courses.
	 */
	private enum Priority { InProgress, Passed, Approved };

	// given as parameters at construction
	private Plan plan;
	private Student student;
	private Db db;
	private Logger logger;

	// list of courses matching each requirement - set by check()
	private Map<Rqmt,List<Course>> rqmtCourseListMap;
	// maps statuses to plan categories
	private Map<Plan.Category,Status> c_statusMap;
	// indicates if the audit info should be updated in the db
	private boolean update;

	/**
	 * Constructor copies parameters, initializes the status map and
	 * invokes check() to perform the audit.
	 * @param plan to be audited for
	 * @param student the one to check plan for
	 * @param db need that, don't we?
	 * @param logger used for debugging messages
	 */
	public Audit(Plan plan, Student student, Db db, Logger logger)
			throws SQLException {
		this.plan = plan;
		this.student = student;
		this.db = db;
		this.logger = logger;
		this.rqmtCourseListMap = null;
		this.c_statusMap = new TreeMap<Plan.Category,Status>();
		Iterator<Plan.Category> cItr = plan.get_categoryIterator();
		while(cItr.hasNext()) {
			Plan.Category category = cItr.next();
			this.c_statusMap.put(category, Status.Unknown);
		}
		this.update = false;
		this.check();
	}

	/**
	 * Finds course matches to plan - either looking up from before or
	 * calculating afresh if need be.  Saves the result in
	 * rqmtCourseListMap.  Does not update the db.
	 */
	public void check() throws SQLException {
		// do audit if status unknown or grades have changed since last
		// audit - don't do if status is complete
		Status savedStatus = db.planAuditStatus(student, plan.get_name());
		Calendar savedDate = db.planAuditDate(student, plan.get_name());
		if((savedStatus == Status.Unknown) ||
				(savedStatus != Status.Complete &&
				 (db.lastUploadDate(student).compareTo(savedDate) >= 0 ||
				  db.lastApprovalDate(student).compareTo(savedDate) >= 0))) {
			matchCoursesToRqmts();
			update = true;
		} else {
			// just read them from db
			readMatches();
		}

		return;
	}

	/**
	 * Updates the db if needed.  As a safety measure, it won't overwrite
	 * a Complete plan.  There might be situations where it would make
	 * sense - will need to think about it.
	 */
	public void commit() throws SQLException {
		Status savedStatus = db.planAuditStatus(student, plan.get_name());
		if(update && (savedStatus != Status.Complete)) {
			db.auditCommit(this);
		}
	}

	/**
	 * Make a list of courses that the given course have been approved as a
	 * substitute for.
	 * @param student seems to be a superfluous argument
	 * @param course the course to find approved matches for
	 * @param approvalList list of approved substitutions
	 * @return list of courses that given course substitutes for
	 */
	private List<Course> getApprovedSubstList(Student student, Course course,
			List<Approval> approvalList)
			throws SQLException {
		// add any that match course to approvedFor list
		List<Course> approvedForList = new ArrayList<Course>();
		for(Approval approval: approvalList)
			if(course.getSubject().equals(approval.get_subject()) &&
								course.getCatalog_nbr().matches(
									approval.get_catalog_nbr().replaceAll(
										"\\*", ".+")))
				approvedForList.add(new Course(approval.get_sbj_list(),
							approval.get_cnbr_name()));

		return approvedForList;
	}

	/**
	 * Find all lists that given course (or list) is on.  First accepts any
	 * matches, then builds an exclude list and does it again.
	 * @param student need to know their calendar year
	 * @param sbj_list course subject or "list"
	 * @param cnbr_name course catalog number or list name
	 * @return set of matched lists
	 */
	private Set<ListItem> getListMatchesSet(Student student, String sbj_list,
			String cnbr_name) throws SQLException {
		// find list matches regardless of the list type
		Set<ListItem> excludeSet = new TreeSet<ListItem>();
		Set<ListItem> matchesSet = getListMatchesSet(student, sbj_list,
				cnbr_name, excludeSet);

		// add invalid ones to excludeSet
		for(ListItem item: matchesSet) {
			// convenience ref
			String listName = item.get_cnbr_name();

			// corequisite lists
			// get replaced by substitutes earlier in the process and
			// shouldn't get matched now
			if(listName.matches(".*:"))
				excludeSet.add(item);

			// exclude lists
			// match on the exclude list means don't include in the
			// regular list
			if(item.get_cnbr_name().matches("\\^.*")) {
				// listname without leading '^' is the corresponding
				// regular list
				excludeSet.add(new ListItem("list", listName.substring(1)));
			}
		}

		// find list matches excluding invalid lists
		return getListMatchesSet(student, sbj_list, cnbr_name, excludeSet);
	}

	/**
	 * Find all lists that given course (or list) is on excepting those
	 * on the exclude list.
	 * @param student need to know their calendar year
	 * @param sbj_list course subject or "list"
	 * @param cnbr_name course catalog number or list name
	 * @param excludeSet those list matches to ignore
	 * @return set of matched lists
	 */
	private Set<ListItem> getListMatchesSet(Student student, String sbj_list,
			String cnbr_name, Set<ListItem> excludeSet) throws SQLException {
		Set<ListItem> listMatchesSet = new TreeSet<ListItem>();

		// get lists that contain matches to item
		List<String> listNameList = db.listMatchList(student.getCalendar(),
				sbj_list, cnbr_name);

		for(String listName : listNameList) {
			ListItem item = new ListItem("list", listName);
			if(!excludeSet.contains(item)) {
				listMatchesSet.add(item);
				listMatchesSet.addAll(getListMatchesSet(student,
							"list", listName));
			}
		}

		return listMatchesSet;
	}

	/**
	 * Return reference to plan that was supplied to constructor.
	 * @return plan being audited
	 */
	public Plan get_plan() {
		return plan;
	}

	/**
	 * Return reference to student that was supplied to constructor.
	 * @return student being audited
	 */
	public Student get_student() {
		return student;
	}

	/**
	 * Find set of plan requirements that the course/list matches.
	 * @param item course or list
	 * @return set of matched requirements
	 */
	private Set<Rqmt> getRqmtMatchesSet(ListItem item) {
		Set<Rqmt> rqmtSet = new TreeSet<Rqmt>();

		// search plan by category and rqmt for matches
		Iterator<Plan.Category> cItr = plan.get_categoryIterator();
		while(cItr.hasNext()) {
			Iterator<Rqmt> rItr = cItr.next().get_rqmtIterator();
			while(rItr.hasNext()) {
				Rqmt rqmt = rItr.next();
				if(rqmt.get_sbj_list().equals(item.get_sbj_list()) &&
						rqmt.get_cnbr_name().equals(item.get_cnbr_name()))
					rqmtSet.add(rqmt);
			}
		}

		return rqmtSet;
	}

	/**
	 * Match courses, or lists of courses, to plan requirements.  Result
	 * saved in rqmtCourseListMap.
	 */
	public void matchCoursesToRqmts() throws SQLException {
		// create a map of maps of requirements to priorities to courses
		Map<Course,Map<Rqmt,Priority>> courseRqmtMap = new TreeMap<Course,
			Map<Rqmt,Priority>>();

		// list student courses
		List<Course> courseList = db.courseList(student);

		// add transfer credits
		List<TransferCourse> transferList = db.transferCourseList(student);
		for(TransferCourse transfer: transferList)
			courseList.add((Course)transfer);

		// trim out courses that do not count in degree
		Iterator<Course> courseItr = courseList.iterator();
		while(courseItr.hasNext())
			if(!courseItr.next().valid()) courseItr.remove();

		// merge corequisites, replacing with a holder in courseList
		Map<String,List<Course>> coreqMap = moveCoursesToCoreqMap(
				student.getCalendar(), courseList);

		// get approved course substitutes
		List<Approval> approvalList = db.stdntApprovalList(student.getUw_id());
		coreqMap.putAll(moveApprovedCoursesToCoreqMap(courseList,
					approvalList));

		// find requirements met by each course
		// use an index so that the list can be added to during traversal
		for(int i=0; i<courseList.size(); i++) {
			Course course = courseList.get(i);

			// combine course with any approved equivalents
			List<Course> courseEquivList = new ArrayList<Course>();
			courseEquivList.add(course); // should be first
			// approvals have higher priority and will replace identical
			// rqmt keys with the higher priority values, or add their own
			courseEquivList.addAll(getApprovedSubstList(student, course,
					approvalList));

			Map<Rqmt,Priority> rpm = new TreeMap<Rqmt,Priority>();
			for(Course equiv: courseEquivList) {
				// the priority of the match depends
				// - approved is highest
				// - passed is next
				// - inprogress is last
				Priority priority;
				if(equiv != course) priority = Priority.Approved;
				else if(course.pass()) priority = Priority.Passed;
				else priority = Priority.InProgress;

				// combine course with any lists it is on
				Set<ListItem> listMatchesSet = getListMatchesSet(student,
						equiv.getSubject(), equiv.getCatalog_nbr());
				listMatchesSet.add(new ListItem(equiv.getSubject(),
							equiv.getCatalog_nbr()));

				// match those items to the plan requirements
				for(ListItem item : listMatchesSet) {
					Set<Rqmt> rqmtSet = getRqmtMatchesSet(item);
					for(Rqmt rqmt: rqmtSet)
						rpm.put(rqmt, priority);
				}
			}

			// add rqmt-priority map to course-rqmt-priority map unless it
			// is a corequisite list with no matches
			if((rpm.size() == 0) &&
					(course.getSubject().equals("list") &&
					 coreqMap.containsKey(course.getCatalog_nbr()))) {
				// replace unmatched coreq list with its constituent
				// courses
				courseList.remove(i--);
				courseList.addAll(coreqMap.get(course.getCatalog_nbr()));
			} else {
				courseRqmtMap.put(course, rpm);
			}
		}

		/*
		// log matches for specified student
		if(student.getUw_id() == 20xxxx01) {
			logger.logp(Level.INFO, this.getClass().getName(),
				"matchCoursesToRqmts", plan.get_name()
				+ " matches: " + courseRqmtMap.toString());
		}
		*/

		rqmtCourseListMap = solve(courseRqmtMap, coreqMap);

		/*
		// log matches for specified student
		if(student.getUw_id() == 20xxxx01) {
			logger.logp(Level.INFO, this.getClass().getName(),
				"matchCoursesToRqmts", plan.get_name()
				+ " matches: " + rqmtCourseListMap.toString());
		}
		*/
	}

	/**
	 * Combine multiple approvals with same target (which is not a list)
	 * into a temporary coreq
	 * list and generate a coreqMap entry if the courses are in courseList
	 * (likely would be).
	 * @param courseList list of courses
	 * @param approvalList list of approvals i.e. substitutions
	 * @return map of coreq list names to course lists
	 */
	private Map<String,List<Course>> moveApprovedCoursesToCoreqMap(
			List<Course> courseList, List<Approval> approvalList)
		throws SQLException {
		Map<String,List<Course>> coreqMap = new TreeMap<String,List<Course>>();

		// look for approvals with same sbj_list|cnbr_name - the
		// courses combined match the target - i.e. a coreq list
		// sort approval list based on sbj_list|cnbr_name using insertion sort
		for(int i=1; i<approvalList.size(); i++) {
			Approval key = approvalList.get(i);
			int j = i;
			do {
				Approval cur = approvalList.get(j - 1);
				if(cur.get_sbj_list().concat(cur.get_cnbr_name()).compareTo(
							key.get_sbj_list().concat(key.get_cnbr_name())) > 0)
					approvalList.set(j, cur);
				else
					break;
				j--;
			} while(j > 0);
			approvalList.set(j, key);
		}
		// search for consecutive sbj_list|cnbr_name the same
		for(int i=0; i<approvalList.size() - 1; i++) {
			Approval key = approvalList.get(i);
			int j = i;
			do {
				Approval cur = approvalList.get(j + 1);
				if(!cur.get_sbj_list().equals("list") &&
						cur.get_sbj_list().concat(cur.get_cnbr_name()).equals(
						key.get_sbj_list().concat(key.get_cnbr_name())))
					j++;
				else
					break;
			} while(j<approvalList.size() - 1);
			if(j-i > 0) {
				// generate a unique name for coreq list
				String listName = key.get_sbj_list().concat(key.get_cnbr_name())
					+ ":";

				// move approvals to a list of items to match courses to
				List<ListItem> listItems = new ArrayList<ListItem>();
				for(int k=0; k<j-i+1; k++) {
					Approval cur = approvalList.remove(i);
					listItems.add(new ListItem(cur.get_subject(),
								cur.get_catalog_nbr()));
				}
				// insert a replacement approval where the course is the
				// new coreq list and the target is the duplicated
				// sbj_list, cnbr_name
				approvalList.add(i, new Approval(key.get_uw_id(), "list",
							listName, key.get_sbj_list(), key.get_cnbr_name(),
							"", ""));

				// move completed coreq list courses into a coreq map item
				moveListCoursesToCoreqMap(listName, listItems, courseList,
						coreqMap);
			}
		}

		return coreqMap;
	}

	/**
	 * Find coreq lists and replace constituent courses of satisfied lists
	 * with a placeholder in the courseList.
	 * @param calendar year of ug cal to satisfy
	 * @param courseList list of courses
	 * @return map of coreq list names to constituent course list
	 */
	private Map<String,List<Course>> moveCoursesToCoreqMap(int calendar,
			List<Course> courseList) throws SQLException {
		Map<String,List<Course>> coreqMap = new TreeMap<String,List<Course>>();

		// get list names
		List<String> listList = db.listList(calendar);

		// find coreq lists
		for(String listName : listList)
			// coreq lists end with ':'
			if(listName.matches(".*:")) {
				// get list of items in list
				List<ListItem> listItems = db.listItemList(listName, calendar);
				// move completed coreq list courses into a coreq map item
				moveListCoursesToCoreqMap(listName, listItems, courseList,
						coreqMap);
			}

		return coreqMap;
	}

	/**
	 * If all items in coreq list are present in course list, move them to
	 * coreqMap and replace with a placeholder in the course list.
	 * @param listName of coreq list
	 * @param listItems courses in coreq list
	 * @param coreqMap maps a coreq list name to a list of courses that
	 * satisfy it
	 */
	private void moveListCoursesToCoreqMap(String listName,
			List<ListItem> listItems, List<Course> courseList,
			Map<String,List<Course>> coreqMap) {
		List<Course> coreqCourseList = new ArrayList<Course>();

		for(ListItem item : listItems) {
			// scan courseList for match to item
			boolean itemFound = false;
			for(Course course: courseList)
				// will match wildcards - not sure if that's good
				if(course.getSubject().equals(item.get_sbj_list())
						&& course.getCatalog_nbr().matches(
							item.get_cnbr_name().replaceAll(
								"\\*", ".+"))) {
					coreqCourseList.add(course);
					itemFound = true;
					break;
				}
			if(!itemFound) break;
		}

		// if coreq list is complete, add to coreq map, remove
		// courses from courseList and insert placeholder
		if(coreqCourseList.size() == listItems.size()) {
			// transfer courses from courseList to coreqMap
			coreqMap.put(listName, coreqCourseList);
			courseList.removeAll(coreqCourseList);

			// add placeholder course to courseList
			courseList.add(new CourseCredit("list", listName,
						"coreq list placeholder"));
		}

	}

	/**
	 * Get list of most recently enrolled plans and their
	 * status/statuses/stati.  (Apparently status with a long U is the
	 * proper latin plural.)
	 * @param db needed for this static method
	 * @param student the one to check for
	 * @return map of plan names to status
	 */
	public static Map<String,Status> planNameStatusMap(Db db, Student student)
			throws SQLException {
		Map<String,Status> planNameStatusMap = new TreeMap<String,Status>();
		List<Plan> planList = db.planList(student);
		for(Plan plan: planList)
			planNameStatusMap.put(plan.get_name(), db.planAuditStatus(student,
						plan.get_name()));
		return planNameStatusMap;
	}

	/**
	 * Read plan requirement matches from db and populate
	 * rqmtCourseListMap.
	 */
	private void readMatches() throws SQLException {
		rqmtCourseListMap = new TreeMap<Rqmt,List<Course>>();

		Iterator<Plan.Category> cItr = plan.get_categoryIterator();
		while(cItr.hasNext()) {
			Plan.Category category = cItr.next();
			Iterator<Rqmt> rItr = category.get_rqmtIterator();
			while(rItr.hasNext()) {
				Rqmt rqmt = rItr.next();
				rqmtCourseListMap.put(rqmt, db.courseList(student, rqmt));
			}
		}
	}

	/**
	 * Return list of courses (usually length 1 or 0, sometime more)
	 * matching given requirement.
	 * @param rqmt the requirement to return matched courses for
	 * @return list of courses matched to requirement
	 */
	public List<Course> rqmtCourseList(Rqmt rqmt) {
		return rqmtCourseListMap.get(rqmt);
	}

	/**
	 * Solves for a maximum covering of requirements by courses, one course
	 * per requirement.  The heart of the audit.
	 * @param courseRqmtMap map of matched requirements plus priorities to
	 * courses
	 * @param coreqMap map of satisfied coreq lists and the courses that
	 * satisfy them
	 * @return map of (list of) courses to requirements
	 */
	private Map<Rqmt,List<Course>> solve(
			Map<Course,Map<Rqmt,Priority>> courseRqmtMap,
			Map<String,List<Course>> coreqMap) {
		// while use SetSelect which solve a matrix of integers
		// map student courses to rows
		// map plan requirements to columns

		// make list of all plan requirements
		List<Rqmt> rqmtList = new ArrayList<Rqmt>();
		Iterator<Plan.Category> cItr = plan.get_categoryIterator();
		while(cItr.hasNext()) {
			Plan.Category category = cItr.next();
			Iterator<Rqmt> rItr = category.get_rqmtIterator();
			while(rItr.hasNext())
				rqmtList.add(rItr.next());
		}

		// add course rqmt matches to solver
		SetSelect setSelect = new SetSelect(courseRqmtMap.size(),
				rqmtList.size());
		int courseIndex = 0;
		for(Map<Rqmt,Priority> rpMap : courseRqmtMap.values()) {
			for(Map.Entry<Rqmt,Priority> rpEntry : rpMap.entrySet()) {
				int rqmtIndex = rqmtList.indexOf(rpEntry.getKey());
				int priority = rpEntry.getValue().ordinal();
				setSelect.set(courseIndex, rqmtIndex, priority);
			}
			courseIndex++;
		}

		setSelect.solve();

		// read results into a rqmt-courselist map
		Map<Rqmt,List<Course>> solution = new TreeMap<Rqmt,List<Course>>();
		// convert the courseRqmtMap keyset to an array for easy indexing 
		Course[] courseArray = courseRqmtMap.keySet().toArray(
				new Course[courseRqmtMap.keySet().size()]);
		for(int rqmtIndex=0; rqmtIndex<rqmtList.size(); rqmtIndex++) {
			List<Course> courseList = new ArrayList<Course>();

			// finds which row (course) matched the column (rqmt)
			courseIndex = setSelect.firstRow(rqmtIndex);
			// should return -1, not N, if not found
			if(courseIndex < courseRqmtMap.size()) {
				Course course = courseArray[courseIndex];

				// check if a coreq list or just a course
				if(course.getSubject().equals("list")) {
					courseList.addAll(coreqMap.get(course.getCatalog_nbr()));
				} else
					courseList.add(course);
			}

			solution.put(rqmtList.get(rqmtIndex), courseList);
		}

		return solution;
	}

	/**
	 * Returns overall status of audit.
	 * @return status
	 */
	public Status status() {
		if(c_statusMap.isEmpty()) return Status.Unknown;

		Status status = Status.Complete;
		Iterator<Plan.Category> c_itr = plan.get_categoryIterator();
		while(c_itr.hasNext()) {
			Plan.Category category = c_itr.next();

			if(status(category).ordinal() < status.ordinal())
				status = status(category);
		}

		return status;
	}

	/**
	 * Return status of audit for given plan category.
	 * @param category of plan
	 * @return status
	 */
	public Status status(Plan.Category category) {
		Status c_status = c_statusMap.get(category);

		// if category status not set, calculate it
		if(c_status == Status.Unknown) {
			c_status = Status.Complete;
			Iterator<Rqmt> r_itr = category.get_rqmtIterator();
			while(r_itr.hasNext()) {
				Rqmt rqmt = r_itr.next();
				List<Course> courseList = rqmtCourseListMap.get(rqmt);
				Status r_status;
				if(courseList.isEmpty())
					r_status = Status.Incomplete;
				else {
					r_status = Status.Complete;
					for(Course course: courseList)
						if(course.inProgress())
							r_status = Status.Provisionally_Complete;
				}

				if(r_status.ordinal() < c_status.ordinal())
					c_status = r_status;
			}
			c_statusMap.put(category, c_status);
		}

		return c_status;
	}

}
