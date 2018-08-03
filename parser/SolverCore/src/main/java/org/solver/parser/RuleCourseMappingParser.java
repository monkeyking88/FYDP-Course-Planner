package org.solver.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.common.course.Course;
import org.common.course.CourseDataSource;

public class RuleCourseMappingParser {
	
	public static final String RULE_PREFIX = "this/";
	public static final String COURSE_SEPARATOR = "\\$";
	public static final String RULE_ROOT = "result";
	
	public static Map<String, List<Course>> parse(Map<String, List<String>> raw){
		Map<String, List<Course>> mapping = new LinkedHashMap<String, List<Course>>();
		
		for (Entry<String, List<String>> rawEnt : raw.entrySet()) {
			//format: this/_xxxxx
			String rule = rawEnt.getKey().substring(RULE_PREFIX.length());
			//ignore the final result of all courses
			if (RULE_ROOT.equals(rule)) {
				continue;
			}
			
			List<Course> courseList = new ArrayList<Course>();
			for (String rawName : rawEnt.getValue()) {
				String courseName = rawName.split(COURSE_SEPARATOR)[0];
				Course c = CourseDataSource.Defs.getDefByCourseName(courseName);
				courseList.add(c);
			}
			
			mapping.put(rule, courseList);
		}
		
		return mapping;
	}

}
