package org.language.codegenerator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.common.course.Course;

public class OverrideMap {
	
	private Map<String, String> courseOverrideMap = new HashMap<String, String>();
	private Map<String, Map<Boolean, List<String>>> IDOverrideMap = new HashMap<String, Map<Boolean, List<String>>>();
	
	public OverrideMap(Map<Course, Course> courseOverrideMap,
				Map<String, CourseIdArray> IDOverrideMap){
		for(Map.Entry<Course, Course> entry : courseOverrideMap.entrySet()){
			this.courseOverrideMap.put(entry.getKey().getName(), 
										entry.getValue().getName());
		}
		for(Map.Entry<String, CourseIdArray> entry : IDOverrideMap.entrySet()){
			Map<Boolean, List<String>> plusMinusMap = new HashMap<Boolean, List<String>>();
			List<String> plusArray = new LinkedList<String>();
			for(CourseId courseId : entry.getValue().getPlusArray()){
				plusArray.add(courseId.getFullName());
			}
			List<String> minusArray = new LinkedList<String>();
			for(CourseId courseId : entry.getValue().getMinusArray()){
				minusArray.add(courseId.getFullName());
			}
			plusMinusMap.put(true, plusArray);
			plusMinusMap.put(false, minusArray);
			this.IDOverrideMap.put(entry.getKey(), plusMinusMap);
		}
	}
	
	public Map<String, String> getCourseOverrideMap() {
		return courseOverrideMap;
	}
	
	public Map<String, Map<Boolean, List<String>>> getIDOverrideMap() {
		return IDOverrideMap;
	}

	@Override
	public String toString() {
		String str = "";
		if (courseOverrideMap.size() == 0 && IDOverrideMap.size() == 0) {
			return str;
		}
		str += "Override detected:\n";
		if (courseOverrideMap.size() > 0) {
			for (Entry<String, String> course_override : courseOverrideMap.entrySet()) {
				str += "    Course " + course_override.getKey() + " is overriden by " + course_override.getValue() + "\n";
			}
		}
		if (IDOverrideMap.size() > 0) {
			for (Entry<String, Map<Boolean, List<String>>> id_override : IDOverrideMap.entrySet()) {
				List<String> plus_idList = id_override.getValue().get(true);
				List<String> minus_idList = id_override.getValue().get(false);
				if (plus_idList.size() > 0) {
					str += "    Following is added to " + id_override.getKey() + ":\n";
					str += "        ";
					for (String plus_id : plus_idList) {
						if (plus_id.contains("_old")) {
							continue;
						}
						str += plus_id + " ";
					}
					str += "\n";
				}
				if (minus_idList.size() > 0) {
					str += "    Following is removed from " + id_override.getKey() + ":\n";
					str += "        ";
					for (String minus_id : minus_idList) {
						str += minus_id + " ";
					}
					str += "\n";
				}
			}
		}
		return str;
	}
	
}