package org.language.codegenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CourseId {

	public enum CourseIdType {
		COURSENAME, CATALOG_WILDCARD, CATALOG_RANGE, ID, ANY, SUBJECT_CATALOG_WILDCARD, SUBJECT_CATALOG_RANGE
	}
	
	private String subject;
	private String catalogBegin;
	private String catalogEnd;
	private String fullName;
	private CourseIdType type;
	
	public CourseId(String courseId){
		this.fullName = courseId;
		this.type = getCourseIdType(courseId);
		Pattern pattern;
		Matcher matcher;
		try {
			switch(this.type){
				case ANY:
					this.subject = "Course";
					this.catalogBegin = "";
					this.catalogEnd = "";
					break;
				case CATALOG_WILDCARD:
					pattern = Pattern.compile("([A-Z]+)([0-9]*)[*]");
					matcher = pattern.matcher(courseId);
					matcher.find();
					this.subject = matcher.group(1);
					this.catalogBegin = matcher.group(2);
					this.catalogEnd = null;
					break;
				case SUBJECT_CATALOG_WILDCARD:
					pattern = Pattern.compile("SUBJECT([0-9]*)[*]");
					matcher = pattern.matcher(courseId);
					matcher.find();
					this.subject = null;
					this.catalogBegin = matcher.group(1);
					this.catalogEnd = null;
					break;
				case CATALOG_RANGE:
					pattern = Pattern.compile("([A-Z]+)([0-9]+)[-]([0-9]+)");
					matcher = pattern.matcher(courseId);
					matcher.find();
					this.subject = matcher.group(1);
					this.catalogBegin = matcher.group(2);
					this.catalogEnd = matcher.group(3);
					break;
				case SUBJECT_CATALOG_RANGE:
					pattern = Pattern.compile("SUBJECT([0-9]+)[-]([0-9]+)");
					matcher = pattern.matcher(courseId);
					matcher.find();
					this.subject = null;
					this.catalogBegin = matcher.group(1);
					this.catalogEnd = matcher.group(2);
					break;
				case COURSENAME:
					pattern = Pattern.compile("([A-Z]+)([0-9]+)");
					matcher = pattern.matcher(courseId);
					matcher.find();
					this.subject = matcher.group(1);
					this.catalogBegin = matcher.group(2);
					this.catalogEnd = null;
					break;
				default: 
					this.subject = null;
					this.catalogBegin = null;
					this.catalogEnd = null;
			} 
		} catch (IllegalStateException e){
			throw new IllegalStateException("fail to match courseId: "+courseId+"\n", e);
		}
	}
	
	public boolean equals(Object o){
		if(!(o instanceof CourseId)){
			return false;
		}
		CourseId courseId = (CourseId) o;
		if(courseId == null || 
				this.getType() != courseId.getType() ||
				!this.getFullName().equals(courseId.getFullName())){
			return false;
		}
		
		switch(this.type){
			case ANY:
				return true;
			case CATALOG_WILDCARD:
				return this.getSubject().equals(courseId.getSubject()) &&
						this.getCatalogBegin().equals(courseId.getCatalogBegin()) &&
						this.getCatalogEnd()==null && courseId.getCatalogEnd()==null;
			case CATALOG_RANGE:
				return this.getSubject().equals(courseId.getSubject()) &&
						this.getCatalogBegin().equals(courseId.getCatalogBegin()) &&
						this.getCatalogEnd().equals(courseId.getCatalogEnd());
			case COURSENAME:
				return this.getSubject().equals(courseId.getSubject()) &&
						this.getCatalogBegin().equals(courseId.getCatalogBegin()) &&
						this.getCatalogEnd()==null && courseId.getCatalogEnd()==null;
			default: 
				return true;
				/* this.getType()==CourseIdType.ID &&
					this.getSubject()==null && courseId.getSubject()==null &&
					this.getCatalogBegin()==null && courseId.getCatalogBegin()==null &&
					this.getCatalogEnd() == null && courseId.getCatalogEnd() == null
				 */
			}
	}
	
	public static CourseIdType getCourseIdType(String str){
		boolean isSubject = false;
		if(str.length() >= "SUBJECT".length() && str.startsWith("SUBJECT")){
			isSubject = true;
		}
		if(str.charAt(str.length() - 1) == '*'){
			if(isSubject){
				return CourseIdType.SUBJECT_CATALOG_WILDCARD;
			}
			return CourseIdType.CATALOG_WILDCARD;
		} else if(str.charAt(0) == '_'){
			return CourseIdType.ID;
		} else if(str.length() == 3 && str.equals("any")){
			return CourseIdType.ANY;
		} else {
			for(int i=1; i<=str.length()-2; i++){
				if(str.charAt(i) == '-'){
					if(isSubject){
						return CourseIdType.SUBJECT_CATALOG_RANGE;
					}
					return CourseIdType.CATALOG_RANGE;
				}
			}
			return CourseIdType.COURSENAME;
		}
	}
	
	public String getFullName() {
		return fullName;
	}

	public CourseIdType getType() {
		return type;
	}

	public String getSubject() {
		return subject;
	}

	public String getCatalogBegin() {
		return catalogBegin;
	}

	public String getCatalogEnd() {
		return catalogEnd;
	}

	// check if a String str is a course id
	// a course id starts with an upper case letter and doesn't end with a '*'
	public static boolean isCourseName(String str){
		if(str != null && str.length() > 1){
			return (str.charAt(0) >= 'A' && str.charAt(0) <= 'Z') && !isPattern(str);
		}
		return false;
	}
	
	public static boolean isPattern(String str){
		if(str != null && str.length() > 1){
			return str.charAt(str.length() - 1) == '*' 
					|| StringUtils.contains(str, '-');
		}
		return false;
	}
	
	public static boolean isCatalogRange(String str){
		if(str != null && str.length() > 1){
			return StringUtils.contains(str, '-');
		}
		return false;
	}
	
	public static boolean isCatalogWildCard(String str){
		if(str != null && str.length() > 1){
			return str.charAt(str.length() - 1) == '*';
		}
		return false;
	}
	
	public static boolean isID(String str){
		return str.charAt(0) == '_';
	}
	
	public static boolean isANY(String str){
		return str.equals("any");
	}
}
