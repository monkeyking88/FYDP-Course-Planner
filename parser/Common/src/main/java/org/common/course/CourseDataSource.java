package org.common.course;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.codehaus.jackson.map.ObjectMapper;
import org.common.exception.ReportableException;
import org.common.exception.UnSupportedException;
import org.common.helper.CatalogComparator;

public class CourseDataSource {
	public static final CourseDataSource Defs;

	static {

		final InputStream jsonDefFileIs = CourseDataSource.class.getClassLoader().getResourceAsStream("courseDef/courseDef.json");
		final ObjectMapper mapper = new ObjectMapper();

		List<Course> courseDefs;
		try {
			courseDefs = Arrays.asList(mapper.readValue(jsonDefFileIs, Course[].class));
		} catch (IOException e) {
			throw new ReportableException("CourseDataSource initialization error", e);
		}
		Defs = new CourseDataSource(courseDefs);

	}

	private List<Course> courseList;
	private Map<Integer, Course> idMap;
	private Map<String, Course> nameMap;
	private Map<String, Map<String, Course>> subjectMap;
	
	private Course getByIdHelper(Course c){
		if (c == null) {
			return c;
		}
		return idMap.get(c.getId());
	}

	public CourseDataSource(List<Course> courseDefs) {
		this.courseList = courseDefs;
		idMap = new LinkedHashMap<Integer, Course>();
		nameMap = new LinkedHashMap<String, Course>();
		subjectMap = new LinkedHashMap<String, Map<String, Course>>();

		for (Course c : courseList) {
			idMap.put(c.getId(), c);
			nameMap.put(c.getName(), c);

			if (subjectMap.get(c.getSubject()) == null) {
				Map<String, Course> catalogMap = new LinkedHashMap<String, Course>();
				catalogMap.put(c.getCatalogNumber(), c);
				subjectMap.put(c.getSubject(), catalogMap);
			} else {
				subjectMap.get(c.getSubject()).put(c.getCatalogNumber(), c);
			}
		}
	}

	public Course getDefById(Integer id) {
		return idMap.get(id);
	}

	/**
	 * @param ids
	 *            list of course ids
	 * @return list of courses with corresponding ids, sequence in ids is
	 *         preserved. For ids that a corresponding course is not found, it
	 *         will be ignored, therefore it is recommended to check the size of
	 *         the returned list against the size of ids
	 */
	public List<Course> getDefById(List<Integer> ids) {
		List<Course> result = new ArrayList<Course>();
		for (Integer id : ids) {
			Course course = getDefById(id);
			if (course != null) {
				result.add(course);
			}
		}
		return result;
	}

	public Course getDefByCourseName(String courseName) {
		return getByIdHelper(nameMap.get(courseName));
	}

	public Course getDefByCourseName(String subject, String catalog) {
		return getByIdHelper(nameMap.get(subject + catalog));
	}

	public static final char DASH = '-';
	public static final char STAR = '*';

	/**
	 * @param subject
	 *            the subject of the range
	 * @param catalogBegin
	 *            starting catalog, if null then start from subject begin
	 * @param catalogEnd
	 *            ending catalog, if null then reach until subject end
	 * @return list of courses within range specified above, empty if no course
	 *         found, will never be null
	 */
	public List<Course> getDefByCatalogRange(String subject, String catalogBegin, String catalogEnd) {
		Map<String, Course> catalogMap = subjectMap.get(subject);
		List<Course> result = new ArrayList<Course>();

		for (Course c : catalogMap.values()) {
			if (catalogBegin == null && catalogEnd == null) {
				result.add(getByIdHelper(c));
			} else if (catalogBegin != null && catalogEnd == null) {
				if (CatalogComparator.compare(c.getCatalogNumber(), catalogBegin) >= 0) {
					result.add(getByIdHelper(c));
				}
			} else if (catalogBegin == null && catalogEnd != null) {
				if (CatalogComparator.compare(c.getCatalogNumber(), catalogEnd) <= 0) {
					result.add(getByIdHelper(c));
				}
			} else {
				if (CatalogComparator.compare(c.getCatalogNumber(), catalogBegin) >= 0 && CatalogComparator.compare(c.getCatalogNumber(), catalogEnd) <= 0) {
					result.add(getByIdHelper(c));
				}
			}
		}

		return result;
	}

	public List<Course> getDefByCatalogRange(final Set<Course> mask, String subject, String catalogBegin, String catalogEnd) {
		List<Course> allMatch = getDefByCatalogRange(subject, catalogBegin, catalogEnd);
		allMatch.retainAll(mask);
		return allMatch;
	}

	public List<Course> getDefByCatalogRange(final List<Integer> mask, String subject, String catalogBegin, String catalogEnd) {
		List<Course> allMatch = getDefByCatalogRange(subject, catalogBegin, catalogEnd);

		Collection<Course> maskedList = CollectionUtils.select(allMatch, new Predicate<Course>() {
			@Override
			public boolean evaluate(Course c) {
				return mask.contains(c.getId());
			}
		});

		return new ArrayList<Course>(maskedList);
	}

	public boolean isSubjectValid(String subject) {
		Map<String, Course> catalogMap = subjectMap.get(subject);
		return catalogMap != null && catalogMap.values().size() > 0;
	}

	public List<Course> getDefByCatalogWildCard(String subject, String catalogSegment) {
		Map<String, Course> catalogMap = subjectMap.get(subject);
		List<Course> result = new ArrayList<Course>();

		try {
			if (catalogSegment == null || catalogSegment.length() == 0) {
				for (Course c : catalogMap.values()) {
					result.add(getByIdHelper(c));
				}
			} else {
				for (Course c : catalogMap.values()) {
					if (c.getCatalogNumber().indexOf(catalogSegment) == 0) {
						result.add(getByIdHelper(c));
					}
				}
			}
		} catch (NullPointerException e) {
			throw new ReportableException("Subject [" + subject + "] does not exist", e);
		}

		return result;
	}

	public List<Course> getDefByCatalogWildCard(final Set<Course> mask, String subject, String catalogSegment) {
		List<Course> allMatch = getDefByCatalogWildCard(subject, catalogSegment);
		allMatch.retainAll(mask);
		return allMatch;
	}

	public List<Course> getDefByCatalogWildCard(final List<Integer> mask, String subject, String catalogSegment) {
		List<Course> allMatch = getDefByCatalogWildCard(subject, catalogSegment);

		Collection<Course> maskedList = CollectionUtils.select(allMatch, new Predicate<Course>() {
			@Override
			public boolean evaluate(Course c) {
				return mask.contains(c.getId());
			}
		});

		return new ArrayList<Course>(maskedList);
	}

	public List<Course> getDefByTopicWildCard(String subject, String catalog, String topicSegment) {
		// we do not have topics in our course def yet, did not see any from the
		// course definition
		throw new UnSupportedException("getDefByTopicWildCard");
	}

}
