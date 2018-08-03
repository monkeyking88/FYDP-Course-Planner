package org.language.blocktree;

public interface BlockTreeVisitor {
	
	public void visit(ConditionBlockTrunk tree);
	public void visit(UnitBlockTrunk tree);
	public void visit(CourseBlockTrunk tree);
	public void visit(InfeasibleBlockTrunk tree);
	public void visit(UnconditionalFeasibleBlockTrunk tree);
	
	public void visit(UnitBlockLeaf leaf);
	public void visit(CourseBlockLeaf leaf);
}