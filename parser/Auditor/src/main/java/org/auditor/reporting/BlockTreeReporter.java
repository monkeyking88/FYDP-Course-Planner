package org.auditor.reporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.common.audit.AuditResult.AuditOutcome;
import org.common.audit.AuditResult.AuditSingleResult;
import org.common.exception.ReportableException;
import org.common.helper.Helper;
import org.language.blocktree.BlockTree;
import org.language.blocktree.BlockTreeToAlloy;
import org.language.blocktree.BlockTreeVisitor;
import org.language.blocktree.ConditionBlockTrunk;
import org.language.blocktree.CourseBlockLeaf;
import org.language.blocktree.CourseBlockTrunk;
import org.language.blocktree.InfeasibleBlockTrunk;
import org.language.blocktree.UnconditionalFeasibleBlockTrunk;
import org.language.blocktree.UnitBlockLeaf;
import org.language.blocktree.UnitBlockTrunk;
import org.solver.main.SolverCore;

public class BlockTreeReporter implements BlockTreeVisitor {
	
	List<String> failedRules = new ArrayList<String>();
	
	public List<String> getFailedRules() {
		return failedRules;
	}

	public void visit(ConditionBlockTrunk tree){
		List<BlockTree> failureList = new ArrayList<BlockTree>();
		for(BlockTree child : tree.getChildren()){
			if (!doesPass(child)) {
				failureList.add(child);
			}
		}
			
		if (tree.getFullName().contains("prerequisitechainlength3_prerequisit_chain_len_3")){
			if (failureList.size() == tree.getChildren().size()) {
				failedRules.add(tree.getFullName());
				tree.getParent().addChild(new UnconditionalFeasibleBlockTrunk(tree));
				tree.getParent().removeChild(tree);
			}
			return;
		}
		
		//all child succeeds, must be current rule that fail
		if (failureList.size() == 0) {
			failedRules.add(tree.getFullName());
			if (tree.getParent() == null) {
				throw new ReportableException("Failing tree node: " + tree.getFullName() + " shouhd have a parent, please check requirement satisfiability");
			}
			tree.getParent().addChild(new UnconditionalFeasibleBlockTrunk(tree));
			tree.getParent().removeChild(tree);
		} else {
			for (BlockTree child : failureList) {
				child.accept(this);
			}
		}
	}
	
	public void visit(UnitBlockTrunk tree){
		List<BlockTree> failureList = new ArrayList<BlockTree>();
		for(BlockTree child : tree.getChildren()){
			if (!doesPass(child)) {
				failureList.add(child);
			}
		}
		
		if (tree.getFullName().contains("prerequisitechainlength3_prerequisit_chain_len_3")){
			if (failureList.size() == tree.getChildren().size()) {
				failedRules.add(tree.getFullName());
				tree.getParent().addChild(new UnconditionalFeasibleBlockTrunk(tree));
				tree.getParent().removeChild(tree);
			} else {
				return;
			}
		}
		
		//all child succeeds, must be current rule that fail
		if (failureList.size() == 0) {
			failedRules.add(tree.getFullName());
			if (tree.getParent() == null) {
				throw new ReportableException("Failing tree node: " + tree.getFullName() + " shouhd have a parent, please check requirement satisfiability");
			}
			tree.getParent().addChild(new UnconditionalFeasibleBlockTrunk(tree));
			tree.getParent().removeChild(tree);
		} else {
			for (BlockTree child : failureList) {
				child.accept(this);
			}
		}
	}
	
	public void visit(CourseBlockTrunk tree){
		List<BlockTree> failureList = new ArrayList<BlockTree>();
		for(BlockTree child : tree.getChildren()){
			if (!doesPass(child)) {
				failureList.add(child);
			}
		}
		
		if (tree.getFullName().contains("prerequisitechainlength3_prerequisit_chain_len_3")){
			if (failureList.size() == tree.getChildren().size()) {
				failedRules.add(tree.getFullName());
				tree.getParent().addChild(new UnconditionalFeasibleBlockTrunk(tree));
				tree.getParent().removeChild(tree);
			} else {
				return;
			}
		}
		
		//all child succeeds, must be current rule that fail
		if (failureList.size() == 0) {
			failedRules.add(tree.getFullName());
			if (tree.getParent() == null) {
				throw new ReportableException("Failing tree node: " + tree.getFullName() + " shouhd have a parent, please check requirement satisfiability");
			}
			tree.getParent().addChild(new UnconditionalFeasibleBlockTrunk(tree));
			tree.getParent().removeChild(tree);
		} else {
			for (BlockTree child : failureList) {
				child.accept(this);
			}
		}
	}

	@Override
	public void visit(UnconditionalFeasibleBlockTrunk tree) {
		// We don't need to go over this rule because we know it's satisfiable
		return;
	}
	
	public void visit(InfeasibleBlockTrunk tree){
		failedRules.add(tree.getFullName());
		if (tree.getParent() == null) {
			throw new ReportableException("Failing tree node: " + tree.getFullName() + " shouhd have a parent, please check requirement satisfiability");
		}
		tree.getParent().addChild(new UnconditionalFeasibleBlockTrunk(tree));
		tree.getParent().removeChild(tree);
	}
	
	public void visit(UnitBlockLeaf leaf){
		failedRules.add(leaf.getFullName());
		if (leaf.getParent() == null) {
			throw new ReportableException("Failing tree node: " + leaf.getFullName() + " shouhd have a parent, please check requirement satisfiability");
		}
		leaf.getParent().addChild(new UnconditionalFeasibleBlockTrunk(leaf));
		leaf.getParent().removeChild(leaf);
	}
	
	public void visit(CourseBlockLeaf leaf){
		failedRules.add(leaf.getFullName());
		if (leaf.getParent() == null) {
			throw new ReportableException("Failing tree node: " + leaf.getFullName() + " shouhd have a parent, please check requirement satisfiability");
		}
		leaf.getParent().addChild(new UnconditionalFeasibleBlockTrunk(leaf));
		leaf.getParent().removeChild(leaf);
	}
	
	private boolean doesPass(BlockTree node){
        try {
        	File alloy_requirementFile = File.createTempFile("tmp_requirement", ".als");
			OutputStream alloy_requirementOs = new FileOutputStream(alloy_requirementFile);
			
			//write to alloy file
			BlockTreeVisitor blockTreeVisitor = new BlockTreeToAlloy(alloy_requirementOs);
			node.accept(blockTreeVisitor);
			Helper.close(alloy_requirementOs);
			
			try {
				AuditSingleResult result = (new SolverCore()).handle(alloy_requirementFile.getAbsolutePath(), false);
				if (result.getOutcome() != AuditOutcome.GRADUATE) {
					return false;
				}
				return true;
			} catch (Exception e) {
				return false;
			}
		} catch (IOException e) {
			throw new ReportableException("Failure when checking failed rules", e);
		}
	}
	
}
