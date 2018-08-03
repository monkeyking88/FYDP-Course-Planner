package org.language.blocktree;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.common.course.Course;
import org.common.exception.SemanticErrorException;
import org.language.codegenerator.OverrideMap;
import org.language.symbol.Symbol;

public abstract class AbstractBlockLeaf implements BlockTree {
	
	/**
	 * parent: parent of the block tree, this.parent==null if it is the root
	 */
	protected boolean hasSetParent = false;
	protected AbstractBlockTrunk parent;
	
	/**
	 * name: name of the block tree, will be used to generate the hashCode 
	 * degree: the requirement file that this BlockTree is generated from
	 */
	protected String name;
	protected String degree;
	
	/**
	 * discriminator
	 */
	protected Discriminator discriminator;
	/**
	 * quantifier
	 */
	protected Quantifier quantifier;
	protected OverrideMap overrideMap;

	/**
	 * symbols
	 */	
	protected Set<Symbol> plusSymbols = new HashSet<Symbol>();
	protected Set<Symbol> minusSymbols = new HashSet<Symbol>();
	
	/**
	 * candidate courses that are used to satisfy this condition block 
	 */
	protected Set<Course> candidateCoursesInTranscript = new HashSet<Course>();
	protected Set<Course> candidateCoursesInDatabase = new HashSet<Course>();
	
	public AbstractBlockLeaf(String degree, String name, Discriminator discriminator, Quantifier quantifier) {
		super();
		this.degree = degree;
		this.name = name;
		this.discriminator = discriminator;
		this.quantifier = quantifier;
	}
	
	public AbstractBlockLeaf(String degree, String name, Discriminator discriminator, Quantifier quantifier, 
			Collection<Symbol> plusSymbols, Collection<Symbol> minusSymbols) {
		super();
		this.degree = degree;
		this.name = name;
		this.discriminator = discriminator;
		this.quantifier = quantifier;
		this.addPlusSymbols(plusSymbols);
		this.addMinusSymbols(minusSymbols);
	}
	
	/**
	 * methods that manipulate candidateCoursesInTranscript
	 */
	public Set<Course> getCandidateCoursesInTranscript(){
		return (this.candidateCoursesInTranscript);
	}
	
	public boolean removeCandidateCourseInTranscript(Course candidateCourse){
		return (this.candidateCoursesInTranscript).remove(candidateCourse);
	}
	
	public boolean removeCandidateCoursesInTranscript(Collection<Course> candidateCourses){
		boolean hasChanged = false;
		for(Course course : candidateCourses){
			hasChanged = this.removeCandidateCourseInTranscript(course) | hasChanged;
		}
		return hasChanged;
	}
	
	public boolean addCandidateCourseInTranscript(Course candidateCourse){
		return (this.candidateCoursesInTranscript).add(candidateCourse);
	}

	public boolean addCandidateCoursesInTranscript(Collection<Course> candidateCourses){
		boolean hasChanged = false;
		for(Course course : candidateCourses){
			hasChanged = this.addCandidateCourseInTranscript(course) | hasChanged;
		}
		return hasChanged;
	}
	
	public Collection<Symbol> getPlusSymbols(){
		return this.plusSymbols;
	}
	public Collection<Symbol> getMinusSymbols(){
		return this.minusSymbols;
	}
	public boolean addPlusSymbol(Symbol symbol){
		// addCandidateCoursesInTranscript(symbol.getCourses());
		return this.plusSymbols.add(symbol);
	}
	public boolean addPlusSymbols(Collection<Symbol> symbols){
		boolean hasChanged = false;
		for(Symbol symbol : symbols){
			hasChanged = this.addPlusSymbol(symbol) | hasChanged;
		}
		return hasChanged;
	}
	public boolean addMinusSymbol(Symbol symbol){
		// addCandidateCoursesInTranscript(symbol.getCourses());
		return this.minusSymbols.add(symbol);
	}
	public boolean addMinusSymbols(Collection<Symbol> symbols){
		boolean hasChanged = false;
		for(Symbol symbol : symbols){
			hasChanged = this.addMinusSymbol(symbol) | hasChanged;
		}
		return hasChanged;
	}
	
	/**
	 * methods that manipulate candidateCoursesInDatabase
	 */
	public Set<Course> getCandidateCoursesInDatabase(){
		return candidateCoursesInDatabase;
	}
	
	public boolean removeCandidateCourseInDatabase(Course candidateCourse){
		return candidateCoursesInDatabase.remove(candidateCourse);
	}
	
	public boolean removeCandidateCoursesInDatabase(Collection<Course> candidateCourses){
		boolean hasChanged = false;
		for(Course course : candidateCourses){
			hasChanged = this.removeCandidateCourseInDatabase(course) | hasChanged;
		}
		return hasChanged;
	}
	
	public boolean addCandidateCourseInDatabase(Course candidateCourse){
		return candidateCoursesInDatabase.add(candidateCourse);
	}

	public boolean addCandidateCoursesInDatabase(Collection<Course> candidateCourses){
		boolean hasChanged = false;
		for(Course course : candidateCourses){
			hasChanged = this.addCandidateCourseInDatabase(course) | hasChanged;
		}
		return hasChanged;
	}
	
	public boolean hasSetParent() {
		return hasSetParent;
	}

	public void setParent(AbstractBlockTrunk parent){
		if(parent!=null && hasSetParent){
			throw new SemanticErrorException("BlockTree " + this.getFullName() + " already has a parent");
		}
		hasSetParent = true;
		this.parent = parent;
	}
	
	public AbstractBlockTrunk getParent(){
		return parent;
	}
	
	/**
	 * @return	quantifier 
	 * 			in ConditionBlockTree: quantifier=n <=> n children block tree are satisfied
	 * 			in CourseBlockTree: quantifier=n <=> total satisifed courses in children is n
	 */
	public Quantifier getQuantifier() {
		return quantifier;
	}

	public Discriminator getDiscriminator() {
		return discriminator;
	}
		
	public int hashCode(){
		return this.getFullName().hashCode();
	}

	public String getFullName() {
		return degree + name;
	}
	
	public String getName() {
		return name;
	}

	public String getDegree() {
		return degree;
	}

	/** 
	 * @return	the BlockTrees that share the same parent	
	 * */
	public Collection<BlockTree> getSiblings(){
		if(parent == null){
			return null;
		}
		
		LinkedList<BlockTree> siblings = new LinkedList<BlockTree>();
		Iterator<BlockTree> it = parent.getChildren().iterator();
		while(it.hasNext()){
			BlockTree sibling = it.next();
			if(sibling != this){
				siblings.add(sibling);
			}
		}
		return siblings;
	}
	
	/**
	 * @return	the siblings that have candidate courses in common
	 */
	public Collection<BlockTree> getIntersectedSiblings(){
		if(parent == null){
			return null;
		}
		LinkedList<BlockTree> intersectedSiblings = new LinkedList<BlockTree>();
		Iterator<BlockTree> iteratorOfSiblings = parent.getChildren().iterator();
		while(iteratorOfSiblings.hasNext()){
			BlockTree sibling = iteratorOfSiblings.next();
			if(sibling != this){
				for(Course candidateCourseInSibling : sibling.getCandidateCoursesInTranscript()){
					if(this.getCandidateCoursesInTranscript().contains(candidateCourseInSibling)){
						intersectedSiblings.add(sibling);
						break;
					}
				}
			}
		}
		return intersectedSiblings;
	}
	
	public void setOverrideMap(OverrideMap overrideMap){
		this.overrideMap = overrideMap;
	}
	
	public OverrideMap getOverrideMap(){
		return this.overrideMap;
	}
	
	public abstract void accept(BlockTreeVisitor visitor);
}
