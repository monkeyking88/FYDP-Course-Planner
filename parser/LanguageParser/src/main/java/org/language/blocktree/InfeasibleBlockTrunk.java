package org.language.blocktree;

import java.util.Collection;
import java.util.LinkedList;

import org.language.blocktree.BlockTree.Discriminator;
import org.language.blocktree.BlockTree.Quantifier;
import org.language.symbol.Symbol;

public final class InfeasibleBlockTrunk extends AbstractBlockTrunk {
	
	public InfeasibleBlockTrunk(
			String degree, 
			String name, 
			Discriminator discriminator,
			Quantifier quantifier) {
		super(degree, name, discriminator, quantifier);
	}
	
	@Override
	public InfeasibleBlockTrunk deepCloneBranch(){
		return new InfeasibleBlockTrunk(degree, name, discriminator, quantifier);
	}
	
	public void accept(BlockTreeVisitor visitor){
		visitor.visit(this);
	}
}