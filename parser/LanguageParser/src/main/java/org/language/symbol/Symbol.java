package org.language.symbol;

import java.util.Collection;
import java.util.Set;

import org.common.course.Course;

public interface Symbol {

	String getName();
	
	Set<Course> getCourses();
	
	void accept(SymbolVisitor visitor);
}
