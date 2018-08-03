package org.language.symbol;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.common.course.Course;
import org.common.course.CourseDataSource;
import org.language.helper.LanguageHelper;

public class DefineSymbolVisitor implements SymbolVisitor {
	
	private PrintWriter writer;
	private Set<String> declaredSymbols = new HashSet<String>();
	
	public DefineSymbolVisitor(PrintWriter writer){
		this.writer = writer;
	}
	
	public void visit(SubjectSymbol subjectSymbol){
		Map<String, Set<Course> > candidateSubjects = subjectSymbol.getCandidateSubjects();
		String ID = subjectSymbol.getName();
		// Declaration of subject symbol
		if(!declaredSymbols.contains(subjectSymbol.getMain().getName())){
			this.visit(subjectSymbol.getMain());
		}
		writer.println("sig " + ID + " in Course{}");
		// Definition of subject symbol
		writer.println("fact " + ID + "_fact {");
		//writer.println("\t" + ID + " = ");
		LinkedList<String> ors = new LinkedList<String>();
		if(candidateSubjects.isEmpty()){
			writer.println(ID + " = none");
		} else {
			writer.println(ID + " in " + subjectSymbol.getMain().getName());
			for(Set<Course> subject : candidateSubjects.values()){
				String or = LanguageHelper.join(subject, " + ");
				if(subject.size() > 0){
					or = ID + " = (" +  or + ")";
				}
				ors.add(or);
			}
			writer.println(LanguageHelper.join(ors, "\n or "));
		}
		writer.println("}");
	}
	
	public void visit(MainSubjectSymbol mainSubjectSymbol){
		String ID = mainSubjectSymbol.getName();
		Map<String, Set<Course> > candidateSubjectsRaw = new HashMap<String, Set<Course>>();
		writer.println("sig " + ID + " in Course{}");
		declaredSymbols.add(ID);
		Set<String> subjects = new HashSet<String>();
		for(Course course : mainSubjectSymbol.getCourses()){
			if(!subjects.contains(course.getSubject())){
				subjects.add(course.getSubject());
			}
		}
		for(String subject : subjects){
			List<Course> matchedCourses = CourseDataSource.Defs.getDefByCatalogWildCard(mainSubjectSymbol.getCourses(), subject, null);
			if(!matchedCourses.isEmpty()){
				candidateSubjectsRaw.put(subject, new HashSet<Course>(matchedCourses));
			}
		}
		writer.println("fact " + ID + "_fact {");
		//writer.println("\t" + ID + " = ");
		LinkedList<String> ors = new LinkedList<String>();
		if(candidateSubjectsRaw.isEmpty()){
			writer.println(ID + " = none");
		} else {
			for(Set<Course> subject : candidateSubjectsRaw.values()){
				String or = LanguageHelper.join(subject, " + ");
				if(subject.size() > 0){
					or = "(" + ID + " = (" + or + "))";
				}
				ors.add(or);
			}
			writer.println(LanguageHelper.join(ors, "\n or "));
		}
		writer.println("}");
	}
	
	public void visit(CourseSymbol courseSymbol){}
}
