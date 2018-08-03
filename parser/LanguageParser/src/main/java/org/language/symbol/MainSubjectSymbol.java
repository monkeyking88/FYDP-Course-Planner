package org.language.symbol;

import java.util.Collection;
import java.util.Set;

import org.common.course.Course;

public class MainSubjectSymbol implements Symbol {
	
	private String name;
	private Set<Course> courses;
	
	public MainSubjectSymbol(String name, Set<Course> courses) {
		super();
		this.name = name;
		this.courses = courses;
	}

	public String getName(){
		return name;
	}
	
	public Set<Course> getCourses(){
		return courses;
	}
	
	public void accept(SymbolVisitor visitor){
		visitor.visit(this);
	}
}
