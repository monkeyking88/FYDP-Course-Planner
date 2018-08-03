package org.language.symbol;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.common.course.Course;

public class CourseSymbol implements Symbol{

	private Course course;
	
	public CourseSymbol(Course course) {
		super();
		this.course = course;
	}

	@Override
	public Set<Course> getCourses(){
		Set<Course> courses = new HashSet<Course>();
		courses.add(course);
		return courses;
	}
	
	public Course getCourse(){
		return course;
	}
	
	@Override
	public String getName(){
		return this.course.getName();
	}
	
	@Override
	public void accept(SymbolVisitor visitor){
		visitor.visit(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof CourseSymbol) && this.getCourse().equals(((CourseSymbol)obj).getCourse());
	}
}
