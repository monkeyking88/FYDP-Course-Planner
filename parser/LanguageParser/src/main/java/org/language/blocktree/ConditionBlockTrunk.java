package org.language.blocktree;

import java.util.Collection;

import org.language.symbol.Symbol;

public final class ConditionBlockTrunk extends AbstractBlockTrunk{

	public ConditionBlockTrunk(
			String degree, 
			String name, 
			Discriminator discriminator,
			Quantifier quantifier,
			Collection<BlockTree> children) {
		super(degree, name, discriminator, quantifier, children);
	}
	
	@Override
	public ConditionBlockTrunk deepCloneBranch(){
		return new ConditionBlockTrunk(degree, name, discriminator, quantifier, children);
	}
	
	public void accept(BlockTreeVisitor visitor){
		visitor.visit(this);
	}
}