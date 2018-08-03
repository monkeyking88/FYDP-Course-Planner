package org.language.blocktree;

import java.util.LinkedList;

import org.language.symbol.Symbol;

public class UnconditionalFeasibleBlockTrunk extends AbstractBlockTrunk {
	
	public UnconditionalFeasibleBlockTrunk(
			String degree, 
			String name, 
			Discriminator discriminator,
			Quantifier quantifier) {
		super(degree, name, discriminator, quantifier);
		// TODO Auto-generated constructor stub
	}
	
	public UnconditionalFeasibleBlockTrunk(AbstractBlockLeaf node) {
		super(node.degree, node.name, node.discriminator, node.quantifier);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public UnconditionalFeasibleBlockTrunk deepCloneBranch(){
		return new UnconditionalFeasibleBlockTrunk(degree, name, discriminator, quantifier);
	}
	
	public void accept(BlockTreeVisitor visitor){
		visitor.visit(this);
	}
}
