package org.language.symbol;

public interface SymbolVisitor {

	public void visit(MainSubjectSymbol symbol);
	public void visit(SubjectSymbol symbol);
	public void visit(CourseSymbol symbol);
}