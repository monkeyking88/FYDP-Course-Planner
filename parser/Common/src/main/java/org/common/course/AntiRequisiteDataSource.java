package org.common.course;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.common.exception.ReportableException;
import org.common.helper.Helper;

public class AntiRequisiteDataSource {
	public static final String SEPARATER = "::";
	public static final String DELIMITERS = "() ,.:;-/\\\\&";
	public static final String NO_ANTI = "None";

	private static final Map<Course, List<Course>> antiReqMap = new LinkedHashMap<Course, List<Course>>();

	static {
		final InputStream defFileIs = CourseDataSource.class.getClassLoader().getResourceAsStream("courseDef/anti");

		try (BufferedReader br = new BufferedReader(new InputStreamReader(defFileIs))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(SEPARATER);
				// short circuit the non case
				if (parts[1].indexOf(NO_ANTI) >= 0 && parts[1].length() <= 6) {
					continue;
				}
				ArrayList<String> tokenList = new ArrayList<String>();
				for (String str : parts) {
					StringTokenizer tokenizer = new StringTokenizer(str, DELIMITERS);
					while (tokenizer.hasMoreElements()) {
						tokenList.add(tokenizer.nextToken());
					}
				}

				// if left hand side course does not exist in courseDef, ignore
				String lhsCourseName = tokenList.get(0) + tokenList.get(1);
				Course lhsCourse = CourseDataSource.Defs.getDefByCourseName(lhsCourseName);
				if (lhsCourse == null) {
					continue;
				}

				tokenList.remove(1);
				tokenList.remove(0);
				List<Course> rhsCourses = parseRHS(tokenList);
				// if no valid anti req, ignore it
				if (rhsCourses == null || rhsCourses.isEmpty()) {
					continue;
				}

				antiReqMap.put(lhsCourse, rhsCourses);
			}
			// CS 100 :: All second,third or fourth year CS courses or equivalents
			antiReqMap.put(CourseDataSource.Defs.getDefByCourseName("CS100"), CourseDataSource.Defs.getDefByCatalogRange("CS", "200", "499Z"));
			// CS 200 :: All second, third or fourth year computer science courses
			antiReqMap.put(CourseDataSource.Defs.getDefByCourseName("CS200"), CourseDataSource.Defs.getDefByCatalogRange("CS", "200", "499Z"));

			// a course cannot be anti-req of itself
			for (Course course : antiReqMap.keySet()) {
				List<Course> cList = antiReqMap.get(course);
				Iterator<Course> iter = cList.iterator();
				// use Iterator to avoid concurrent modification exception
				while (iter.hasNext()) {
					Course antiReqCourse = iter.next();
					if (antiReqCourse.equals(course)) {
						iter.remove();
					}
				}
			}

		} catch (IOException e) {
			throw new ReportableException("AntiRequisiteDataSource initialization error", e);
		} finally {
			try {
				defFileIs.close();
			} catch (IOException e) {
				throw new ReportableException("AntiRequisiteDataSource initialization error", e);
			}
		}
	}

	// parsing stage, worst case example: XXX 123 whatever whatever whatever XYZ
	// SWREN 231 342 250 A B 352A B R 213L bullshit bullshit XXX 123
	private static List<Course> parseRHS(List<String> tokenList) {
		List<Course> courseList = new ArrayList<Course>();

		List<String> subjectList = new ArrayList<String>();
		List<String> catalogList = new ArrayList<String>();
		for (String token : tokenList) {
			if (CourseDataSource.Defs.isSubjectValid(token)) {
				// flush previous
				if (catalogList.size() > 0) {
					courseList.addAll(parsePartial(subjectList, catalogList));
					subjectList.clear();
					catalogList.clear();
				}
				subjectList.add(token);
				continue;
			}
			// its ok we add bunch of garbage here, it will be filtered anyway, efficiency is not a concern here
			if (subjectList.size() > 0) {
				catalogList.add(token);
				continue;
			}
		}

		courseList.addAll(parsePartial(subjectList, catalogList));
		// remove duplicates before returning
		return new ArrayList<Course>(new LinkedHashSet<Course>(courseList));
	}

	private static List<Course> parsePartial(List<String> subjectList, List<String> catalogList) {
		List<Course> courseList = new ArrayList<Course>();

		List<String> processedCatalogList = new ArrayList<String>();
		for (int i = 0; i < catalogList.size(); i++) {
			String curCat = catalogList.get(i);
			processedCatalogList.add(curCat);

			// detect catalog suffix, eg 250A/B/C, 350/A/B
			if (Helper.isAlpha(curCat)) {
				int j = i - 1;
				while (j >= 0 && Helper.isAlpha(catalogList.get(j))) {
					j--;
				}
				if (j >= 0) {
					String prevCat = catalogList.get(j);
					processedCatalogList.add(prevCat + curCat);
					processedCatalogList.add(Helper.getLeadingNumeric(prevCat) + curCat);
				}
			}
		}
		for (String subject : subjectList) {
			for (String cat : processedCatalogList) {
				Course course = CourseDataSource.Defs.getDefByCourseName(subject, cat);
				// try leading numeric catalog, there are cases such as SUBJ111R not exist but SUBJ111 exists
				if (course == null) {
					course = CourseDataSource.Defs.getDefByCourseName(subject, Helper.getLeadingNumeric(cat));
				}
				if (course != null) {
					courseList.add(course);
				}
			}
		}
		// note this may contain duplicates
		return courseList;
	}

	public static void testPrint() {
		for (Course course : antiReqMap.keySet()) {
			System.out.print(course.getName() + " :: ");
			List<Course> cList = antiReqMap.get(course);
			for (Course antiReqC : cList) {
				System.out.print(antiReqC.getName() + " ");
			}
			System.out.println();
		}
	}

	public static Map<Course, List<Course>> hasAntiReq(List<Course> courseList) {
		Map<Course, List<Course>> conflictMap = new LinkedHashMap<Course, List<Course>>();
		for (Course course : courseList) {
			List<Course> antiReqList = antiReqMap.get(course);
			if (antiReqList == null || antiReqList.isEmpty()) {
				continue;
			}
			
			List<Course> conflictList = new ArrayList<Course>();
			conflictList.addAll(antiReqList);
			conflictList.retainAll(courseList);
			if (conflictList.size() > 0) {
				conflictMap.put(course, conflictList);
			}
		}
		return conflictMap;
	}

	public static void forceAntiReqFail(List<Course> courseList) {
		Map<Course, List<Course>> conflictMap = hasAntiReq(courseList);
		if (conflictMap.entrySet().size() == 0) {
			return;
		}
		String errStr = "--- Error, potential anti requisites detected. Here is the list: ---\n";
		for (Course course : conflictMap.keySet()) {
			errStr += course.getName() + " has anti requisites: ";
			List<Course> cList = conflictMap.get(course);
			for (Course antiReqC : cList) {
				errStr += antiReqC.getName() + " ";
			}
			errStr += "\n";
		}
		errStr += "--- End of anti requisites list ---\n";
		throw new ReportableException("Potential Anti Req Detected").setExplanation(errStr);
	}
	
	public static Map<Course, List<Course>> hasAntiReqWithId(List<Integer> idList) {
		List<Course> courseList = new ArrayList<Course>();
		for (Integer id : idList) {
			courseList.add(CourseDataSource.Defs.getDefById(id));
		}
		return hasAntiReq(courseList);
	}

	public static void forceAntiReqFailWithId(List<Integer> idList) {
		List<Course> courseList = new ArrayList<Course>();
		for (Integer id : idList) {
			courseList.add(CourseDataSource.Defs.getDefById(id));
		}
		forceAntiReqFail(courseList);
	}

}
