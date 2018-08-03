package org.language.blocktree;

import java.util.Collection;
import java.util.Set;

import org.common.course.Course;
import org.common.exception.SemanticErrorException;
import org.language.codegenerator.OverrideMap;
import org.language.symbol.Symbol;

public interface BlockTree {
	public enum Discriminator{
		UNIQUE, NONE
	}
	
	public class Quantifier{
		private boolean isAllOf = false;
		private int lowerBound = -1;
		private int upperBound = -1;
		
		private Quantifier(){
			isAllOf = true;
		}
		
		public static Quantifier getAllOfQuantifier(){
			Quantifier allOfQuantifier = new Quantifier();
			return allOfQuantifier;
		}
		
		public Quantifier(int lowerBound){
			if(lowerBound<0){
				throw new SemanticErrorException("assigned lowerBound<0 in quantifier");
			}
			this.lowerBound = lowerBound;
		}
		
		public Quantifier(int lowerBound, int upperBound){
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
			
			assertion();
		}
		
		private void assertion(){
			if(lowerBound>upperBound){
				throw new SemanticErrorException("lowerBound > upperBound in quantifier");
			} else if (lowerBound<0){
				throw new SemanticErrorException("lowerBound < 0 in quantifier");
			} else if (upperBound<0){
				throw new SemanticErrorException("upperBound < 0 in quantifier");
			}
		}
		
		public int getLowerBound(){
			return lowerBound;
		}
		
		public int getUpperBound(){
			return upperBound;
		}

		public boolean isAllOf() {
			return isAllOf;
		}
	}
	
	Set<Course> getCandidateCoursesInTranscript();
	
	boolean addCandidateCourseInTranscript(Course candidateCourse);
	
	boolean addCandidateCoursesInTranscript(Collection<Course> candidateCourses);
	
	Set<Course> getCandidateCoursesInDatabase();
	
	boolean addCandidateCourseInDatabase(Course candidateCourse);
	
	boolean addCandidateCoursesInDatabase(Collection<Course> candidateCourses);
	
	AbstractBlockTrunk getParent();
	
	void setParent(AbstractBlockTrunk parent);
	
	boolean hasSetParent();
	
	Collection<BlockTree> getSiblings();
	
	Collection<BlockTree> getIntersectedSiblings();
	
	Quantifier getQuantifier();

	Discriminator getDiscriminator();
		
	int hashCode();

	String getFullName();
	
	String getDegree();
	
	String getName();
	
	void accept(BlockTreeVisitor visitor);
	
	void setOverrideMap(OverrideMap overrideMap);
	
	OverrideMap getOverrideMap();
	
	BlockTree deepCloneBranch();
	
	boolean addPlusSymbol(Symbol symbol);
	boolean addPlusSymbols(Collection<Symbol> symbols);
	boolean addMinusSymbol(Symbol symbol);
	boolean addMinusSymbols(Collection<Symbol> symbols);
	
	Collection<Symbol> getPlusSymbols();
	Collection<Symbol> getMinusSymbols();
}