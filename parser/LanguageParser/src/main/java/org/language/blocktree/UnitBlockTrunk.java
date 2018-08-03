package org.language.blocktree;

import java.util.Collection;

import org.language.symbol.Symbol;

public final class UnitBlockTrunk extends AbstractBlockTrunk {
	
	public UnitBlockTrunk(
			String degree, 
			String name, 
			Discriminator discriminator,
			Quantifier quantifier, 
			Collection<BlockTree> children) {
		super(degree, name, discriminator, quantifier, children);
	}
	
	@Override
	public UnitBlockTrunk deepCloneBranch(){
		return new UnitBlockTrunk(degree, name, discriminator, quantifier, children);
	}

	public void accept(BlockTreeVisitor visitor){
		visitor.visit(this);
	}
}
