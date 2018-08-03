package org.language.symbol;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.common.course.Course;
import org.common.course.CourseDataSource;
import org.language.codegenerator.CourseId;

public class SubjectSymbol implements Symbol{
	
	private String name;
	private MainSubjectSymbol main;
	
	private Map<String, Set<Course> > candidateSubjects = new HashMap<String, Set<Course> >();
	
	public SubjectSymbol(String name, CourseId courseId, MainSubjectSymbol main){
		this.name = name;
		this.main = main;
		Set<Course> candidateCourses = main.getCourses();
		Set<String> subjects = new HashSet<String>();
		for(Course course : candidateCourses){
			if(!subjects.contains(course.getSubject())){
				subjects.add(course.getSubject());
			}
		}
		switch(courseId.getType()){
			case SUBJECT_CATALOG_WILDCARD:
				for(String subject : subjects){
					List<Course> matchedCourses = CourseDataSource.Defs.getDefByCatalogWildCard(candidateCourses, subject, courseId.getCatalogBegin());
					if(!matchedCourses.isEmpty()){
						candidateSubjects.put(subject, new HashSet<Course>(matchedCourses));
					}
				}
				break;
			case SUBJECT_CATALOG_RANGE:
				for(String subject : subjects){
					List<Course> matchedCourses = CourseDataSource.Defs.getDefByCatalogRange(candidateCourses, subject, courseId.getCatalogBegin(), courseId.getCatalogEnd());
					if(!matchedCourses.isEmpty()){	
						candidateSubjects.put(subject, new HashSet<Course>(matchedCourses));
					}
				}
				break;
		}
	}
	
	@Override
	public String getName(){
		return this.name;
	}
	
	@Override
	public Set<Course> getCourses(){
		Set<Course> result = new HashSet<Course>();
		for(Set<Course> subject : candidateSubjects.values()){
			for(Course course : subject){
				result.add(course);
			}
		}
		return result;
	}
	
	@Override
	public void accept(SymbolVisitor visitor){
		visitor.visit(this);
	}
	
	public Map<String, Set<Course> > getCandidateSubjects(){
		return this.candidateSubjects;
	}

	public MainSubjectSymbol getMain() {
		return main;
	}
	
	public void except(Collection<Course> courses){
		if(!courses.isEmpty()){
			for(Map.Entry<String, Set<Course>> entry : candidateSubjects.entrySet()){
				Set<Course> newValue = entry.getValue();
				newValue.removeAll(courses);
				entry.setValue(newValue);
			}
			Iterator<Map.Entry<String, Set<Course>>> iter = candidateSubjects.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Set<Course>> entry = iter.next();
				if(entry.getValue().isEmpty()){
					iter.remove();
				}
			}
		}
	}
	
}