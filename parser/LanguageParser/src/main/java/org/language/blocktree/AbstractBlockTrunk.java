package org.language.blocktree;

import java.util.Collection;
import java.util.HashSet;

import org.common.exception.SemanticErrorException;
import org.language.codegenerator.OverrideMap;
import org.language.symbol.Symbol;

public abstract class AbstractBlockTrunk extends AbstractBlockLeaf {
	
	protected AbstractBlockTrunk(
			String degree, 
			String name, 
			Discriminator discriminator,
			Quantifier quantifier){
		super(degree, name, discriminator, quantifier);
	}
	
	protected AbstractBlockTrunk(
			String degree, 
			String name, 
			Discriminator discriminator,
			Quantifier quantifier,
			Collection<BlockTree> children) {
		super(degree, name, discriminator, quantifier);
		this.addChildren(children);
		// TODO Auto-generated constructor stub
	}

	protected Collection<BlockTree> children = new HashSet<BlockTree>();
	
	public boolean addChild(BlockTree child){
		if(child.hasSetParent()){
			throw new SemanticErrorException("BlockTree " + child.getFullName() + " aldready has a parent");
		} 
		child.setParent(this);
		this.addCandidateCoursesInTranscript(child.getCandidateCoursesInTranscript());
		this.addCandidateCoursesInDatabase(child.getCandidateCoursesInDatabase());
		this.addPlusSymbols(child.getPlusSymbols());
		this.addMinusSymbols(child.getMinusSymbols());
		return children.add(child);
	}
	
	public boolean addChildren(Collection<BlockTree> children){
		boolean hasChanged = false;
		for(BlockTree child : children){
			hasChanged = addChild(child) | hasChanged;
		}
		return hasChanged;
	}
	
	public boolean removeChild(BlockTree child){
		boolean hasChanged = children.remove(child);
		this.removeCandidateCoursesInTranscript(child.getCandidateCoursesInTranscript());
		this.plusSymbols.remove(child.getPlusSymbols());
		this.minusSymbols.remove(child.getMinusSymbols());
		for(BlockTree remainChild : this.getChildren()){
			this.addCandidateCoursesInTranscript(remainChild.getCandidateCoursesInTranscript());
			this.addPlusSymbols(remainChild.getPlusSymbols());
			this.addMinusSymbols(remainChild.getMinusSymbols());
		}
		if(hasChanged){
			child.setParent(null);
		}
		return hasChanged;
	}
	
	public boolean removeChildren(Collection<BlockTree> children){
		boolean hasChanged = false;
		for(BlockTree child : children){
			hasChanged = removeChild(child) | hasChanged;
		}
		return hasChanged;
	}
	
	public Collection<BlockTree> getChildren() {
		return children;
	}
	
	@Override
	public void setOverrideMap(OverrideMap overrideMap){
		super.setOverrideMap(overrideMap);
		for(BlockTree child : this.getChildren()){
			child.setOverrideMap(overrideMap);
		}
	}
	
}