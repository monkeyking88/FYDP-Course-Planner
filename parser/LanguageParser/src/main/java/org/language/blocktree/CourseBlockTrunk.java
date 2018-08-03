package org.language.blocktree;

import java.util.Collection;

import org.language.blocktree.BlockTree.Discriminator;
import org.language.blocktree.BlockTree.Quantifier;
import org.language.symbol.Symbol;

public final class CourseBlockTrunk extends AbstractBlockTrunk{
	
	public CourseBlockTrunk(
			String degree, 
			String name, 
			Discriminator discriminator,
			Quantifier quantifier,
			Collection<BlockTree> children) {
		super(degree, name, discriminator, quantifier, children);
	}
	
	@Override
	public CourseBlockTrunk deepCloneBranch(){
		return new CourseBlockTrunk(degree, name, discriminator, quantifier, children);
	}
	
	public void accept(BlockTreeVisitor visitor){
		visitor.visit(this);
	}
}