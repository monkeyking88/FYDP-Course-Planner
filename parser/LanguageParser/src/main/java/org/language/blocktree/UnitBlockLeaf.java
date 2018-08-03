package org.language.blocktree;

import java.util.Collection;

import org.common.course.Course;
import org.language.symbol.Symbol;

public final class UnitBlockLeaf extends AbstractBlockLeaf {
	
	public UnitBlockLeaf(String degree, String name, Discriminator discriminator,
			Quantifier quantifier, 
			Collection<Symbol> plusSymbols,
			Collection<Symbol> minusSymbols,
			Collection<Course> candidateCoursesInTranscript,
			Collection<Course> candidateCoursesIDatabase) {
		super(degree, name, discriminator, quantifier, plusSymbols, minusSymbols);
		this.addCandidateCoursesInTranscript(candidateCoursesInTranscript);
		this.addCandidateCoursesInDatabase(candidateCoursesInDatabase);
	}
	
	@Override
	public UnitBlockLeaf deepCloneBranch(){
		return new UnitBlockLeaf(degree, name, discriminator, quantifier, plusSymbols, minusSymbols, candidateCoursesInTranscript, candidateCoursesInDatabase);
	}

	public void accept(BlockTreeVisitor visitor){
		visitor.visit(this);
	}
}