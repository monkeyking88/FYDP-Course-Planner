package org.language.blocktree;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.common.course.Course;
import org.common.helper.Helper;
import org.language.blocktree.BlockTree.Discriminator;
import org.language.blocktree.BlockTree.Quantifier;
import org.language.helper.LanguageHelper;
import org.language.symbol.DefineSymbolVisitor;
import org.language.symbol.SubjectSymbol;
import org.language.symbol.Symbol;

public class BlockTreeToAlloy implements BlockTreeVisitor{
	
	public final static String TRUE= "#none = 0";
	public final static String FALSE = "#none > 0";
	
	private BlockTree root;
	private boolean isRoot = true;
	
	
	private DefineSymbolVisitor defineSymbolVisitor;
	private PrintWriter writer;
	private Collection<BlockTree> uniqueBlocks = new HashSet<BlockTree>();
	
	// private Set<Course> allCourses = new HashSet<Course>();
	
	public BlockTreeToAlloy(OutputStream out){
		
		// this.root = root;
		this.writer = new PrintWriter(out);
		this.defineSymbolVisitor = new DefineSymbolVisitor(writer);
		// root.accept(this);
	}
	
	private void initialize(BlockTree tree){
		
		this.root = tree;
		
		/* declare Course sig
		 */
		writer.println("abstract sig Course{");
		writer.println("\tunit : one Int");
		writer.println("}");
		
		/* declare courses and assign units
		 */
		Set<Symbol> allSymbols = new HashSet<Symbol>(root.getPlusSymbols());
		allSymbols.addAll(root.getMinusSymbols());
		for(Symbol symbol : allSymbols){
			symbol.accept(defineSymbolVisitor);
		}
		
		Collection<Course> allCourses = new HashSet<Course>(root.getCandidateCoursesInTranscript());
		for(Symbol symbol : allSymbols){
			if(symbol instanceof SubjectSymbol){
				allCourses.addAll(((SubjectSymbol)symbol).getMain().getCourses());
			}
			allCourses.addAll(symbol.getCourses());
		}
		if(!allCourses.isEmpty()){
			for(Course course : allCourses){
				if(!(course.equals(Course.ANY)||
						course.equals(Course.NONE))){
					// declare course
					writer.print("one sig ");
					writer.print(course.getName());
					writer.print(" extends Course{}\n");
					// assign unit to each declared course
					// every 1 unit in alloy represents a 0.25 units in an actual course
					// e.g.) a course CS136 worthes 0.5 units will be assigned CS136.unit = 2 in alloy
					writer.print("fact{");
					writer.print(course.getName() + ".unit = " + course.getUnits().divide(new BigDecimal(0.25)).intValue());
					writer.println("}\n");
				}
			}
		}
	}
	
	private void close(){
		// create fact to handle unique
		if(!uniqueBlocks.isEmpty()){
			
			//TODO: for now, remove the root blockTree when handling unique
			uniqueBlocks.remove(root);
			
			writer.println("fact unique_fact {");
			for(BlockTree uniqueBlock : uniqueBlocks){
				Collection<BlockTree> siblings = uniqueBlock.getSiblings();
				if(!siblings.isEmpty()){
					writer.println("\t// " + uniqueBlock.getFullName() + " is unique");
					for(BlockTree sibling : siblings){
						writer.println("\tno (" + uniqueBlock.getFullName() + " & " + sibling.getFullName() + ")");
					}
				}
			}
			writer.println("}");
		}
		writer.println("run " + root.getFullName() + "_pred for 0 but 9 Int");

		Helper.close(writer);
	}
	
	public void visit(ConditionBlockTrunk tree){
		if(isRoot){
			isRoot = false;
			this.initialize(tree);
		}
		/* ************************************* */
		
		for(BlockTree child : tree.getChildren()){
			child.accept(this);
		}
		
		writer.println("// ConditionBlockTree");
		if(tree.getDiscriminator() == Discriminator.UNIQUE){
			uniqueBlocks.add(tree);
		}
		
		String ID = tree.getFullName();
		Quantifier quantifier = tree.getQuantifier();
		
		/*
		 * define sig of the block
		 */
		writer.println("sig " + ID + " in Course{}");
		writer.println("fact " + ID + "_fact {");
		if(tree.getChildren()==null || tree.getChildren().size()==0){
			writer.println(ID + " = none");
		} else {
			writer.println("(not " + ID + "_pred)  => " + ID + "=none\n");
			writer.println(ID + "_pred  => " + ID + " = (");
			writer.println("\t" + LanguageHelper.join(tree.getChildren(), "\n\t + "));
			writer.println(") & " + root.getFullName());
		}
		writer.println("}");
		
		/*
		 * define pred of the block
		 */
		writer.println("pred " + ID + "_pred {");
		// quantifier	: ALL
		if(quantifier.isAllOf()){
			writer.println(LanguageHelper.joinAppend(tree.getChildren(), "_pred", " and "));
		// infeasible condition block
		} else if (quantifier.getLowerBound() > tree.getChildren().size()){
			writer.println(FALSE);
		// quantifier	: INT
		} else if (quantifier.getUpperBound() == -1){
			printConditionChoiceInExpr(quantifier.getLowerBound(), tree);
		// quantifier : INT DASH INT
		} else {
			if(quantifier.getLowerBound() > 0){ // not e.g. 0-1 of {condition1 condition2 condition3}
				printConditionChoiceInExpr(quantifier.getLowerBound(), tree);
			}
			writer.println("not (");
			printConditionChoiceInExpr(quantifier.getUpperBound() + 1, tree);
			writer.println(")");
		}
		writer.println("} \n");
		

		/* ************************************* */
		if(root == tree){
			this.close();
		}
	}
	
	public void visit(UnitBlockTrunk tree){
		if(isRoot){
			isRoot = false;
			this.initialize(tree);
		}
		/* ************************************* */
		
		for(BlockTree child : tree.getChildren()){
			child.accept(this);
		}
		
		writer.println("//UnitBlockTree");
		if(tree.getDiscriminator() == Discriminator.UNIQUE){
			uniqueBlocks.add(tree);
		}
		
		String ID = tree.getFullName();
		Quantifier quantifier = tree.getQuantifier();
		
		/*
		 * define sig of the block
		 */
		writer.println("sig " + ID + " in Course{}");
		writer.println("fact " + ID + "_fact {");
		if(tree.getChildren()==null || tree.getChildren().size()==0){
			writer.println(ID + " = none");
		} else {
			writer.println("(not " + ID + "_pred)  => " + ID + "=none\n");
			writer.println(ID + "_pred  => " + ID + " = (");
			writer.println(LanguageHelper.join(tree.getChildren(), " + "));
			writer.println(") & " + root.getFullName());
		}
		writer.println("}");
		
		/*
		 * define pred of the block
		 */
		writer.println("pred " + ID + "_pred {");
		// quantifier	: ALL
		if(quantifier.isAllOf()){
			writer.println(LanguageHelper.joinAppend(tree.getChildren(), "_pred", " and "));
			/*
			writer.println("all c : (");
			writer.println("\t" + LanguageHelper.join(tree.getChildren(), "\n\t + "));
			writer.println(") | c in " + root.getName());
			*/
		// infeasible unit block
		} else if (quantifier.getLowerBound() > LanguageHelper.sumOfUnits(tree.getCandidateCoursesInTranscript()) ||
				tree.getCandidateCoursesInTranscript().size() <= 0){
			writer.println(FALSE);
		// quantifier	: DEC
		} else if (quantifier.getUpperBound() == -1){
			int lowerBound = (int)(quantifier.getLowerBound()/0.25);
			writer.println("(sum c : " + ID + " | c.unit) >= " + lowerBound);
		// quantifier : DEC DASH DEC
		} else {
			int lowerBound = (int)(quantifier.getLowerBound()/0.25);
			int upperBound = (int)(quantifier.getLowerBound()/0.25);
			writer.println("(sum c : " + ID + " | c.unit) >= " + lowerBound);
			writer.println("(sum c : " + ID + " | c.unit) <= " + upperBound);
		}
		writer.println("} \n");
		
		/* ************************************* */
		if(root == tree){
			this.close();
		}
	}
	
	public void visit(CourseBlockTrunk tree){
		if(isRoot){
			isRoot = false;
			this.initialize(tree);
		}
		/* ************************************* */
		
		for(BlockTree child : tree.getChildren()){
			child.accept(this);
		}
		
		writer.println("// CourseBlockTreef");
		if(tree.getDiscriminator() == Discriminator.UNIQUE){
			uniqueBlocks.add(tree);
		}
		
		String ID = tree.getFullName();
		Quantifier quantifier = tree.getQuantifier();
		
		/*
		 * define sig of the block
		 */
		writer.println("sig " + ID + " in Course{}");
		writer.println("fact " + ID + "_fact {");
		if(tree.getChildren()==null || tree.getChildren().size()==0){
			writer.println(ID + " = none");
		} else {
			writer.println("(not " + ID + "_pred)  => " + ID + "=none\n");
			writer.println(ID + "_pred  => " + ID + " = (");
			writer.println("\t" + LanguageHelper.join(tree.getChildren(), "\t\n + "));
			writer.println(") & " + root.getFullName());
		}
		writer.println("}");
		
		/*
		 * define pred of the block
		 */
		writer.println("pred " + ID + "_pred {");
		// quantifier	: ALL
		if(quantifier.isAllOf()){
			writer.println(LanguageHelper.joinAppend(tree.getChildren(), "_pred", " and "));
			/*
			writer.println("all c : (");
			writer.println("\t" + LanguageHelper.join(tree.getChildren(), "\n\t + "));
			writer.println(") | c in " + root.getName());
			*/
		// infeasible
		} else if (quantifier.getLowerBound() > tree.getCandidateCoursesInTranscript().size()){
			writer.println(FALSE);
		// quantifier	: INT
		} else if (quantifier.getUpperBound() == -1){
			writer.println("#" + ID + " >= " + quantifier.getLowerBound());
		// quantifier : INT DASH INT
		} else {
			writer.println("#" + ID + " >= " + quantifier.getLowerBound());
			writer.println("#" + ID + " <= " + quantifier.getUpperBound());
		}
		writer.println("} \n");	
		
		/* ************************************* */
		if(root == tree){
			this.close();
		}
	}
	
	public void visit(InfeasibleBlockTrunk tree){
		if(isRoot){
			isRoot = false;
			this.initialize(tree);
		}
		/* ************************************* */
		
		writer.println("//InfeasibleBlockTree");
		String ID = tree.getFullName();
		
		/*
		 * define sig of the block
		 */
		writer.println("sig " + ID + " in Course{}");
		writer.println("fact " + ID + "_fact {");
		writer.print(ID + " = none");
		writer.println("}");
		writer.println("pred " + ID + "_pred {");
		writer.println(FALSE);	
		writer.println("} \n");	

		/* ************************************* */
		if(root == tree){
			this.close();
		}
	}
	
	@Override
	public void visit(UnconditionalFeasibleBlockTrunk tree) {
		if(isRoot){
			isRoot = false;
			this.initialize(tree);
		}
		/* ************************************* */
		
		writer.println("//UnconditionalFeasibleBlockTree");
		String ID = tree.getFullName();
		
		/*
		 * define sig of the block
		 */
		writer.println("sig " + ID + " in Course{}");
		writer.println("fact " + ID + "_fact {");
		writer.print(ID + " = none");
		writer.println("}");
		writer.println("pred " + ID + "_pred {");
		writer.println(TRUE);	
		writer.println("} \n");	

		/* ************************************* */
		if(root == tree){
			this.close();
		}
		
	}
	
	public void visit(CourseBlockLeaf leaf){
		if(isRoot){
			isRoot = false;
			this.initialize(leaf);
		}
		/* ************************************* */
		
		writer.println("// CourseBlockLeaf");
		if(leaf.getDiscriminator() == Discriminator.UNIQUE){
			uniqueBlocks.add(leaf);
		}
		
		String ID = leaf.getFullName();
		Quantifier quantifier = leaf.getQuantifier();
		
		/*
		 * define sig of the block
		 */
		writer.println("sig " + ID + " in Course{}");
		writer.println("fact " + ID + "_fact {");
		
		if(
			(quantifier.getLowerBound() > leaf.getCandidateCoursesInTranscript().size() || leaf.getCandidateCoursesInTranscript().size() <= 0)
			&& (leaf.getPlusSymbols().size() <= 0)
		){
			writer.println(ID + "=none\n");
		} else {
		
			writer.println("(not " + ID + "_pred)  => " + ID + "=none\n");
			writer.println(ID + "_pred  =>" + ID );
			if(quantifier.isAllOf()){
				writer.print(" = ");
			} else {
				writer.print(" in ");
			}
			writer.println("( ");
			writer.println("\t" + LanguageHelper.join(leaf.getCandidateCoursesInTranscript(), "\n\t + "));
			if(leaf.getCandidateCoursesInTranscript().size() > 0 && leaf.getPlusSymbols().size() > 0) {
				writer.println(" + ");
			}
			writer.println("\t" + LanguageHelper.join(leaf.getPlusSymbols(), "\n\t + "));
			if( (leaf.getCandidateCoursesInTranscript().size() > 0 || leaf.getPlusSymbols().size() > 0) 
					&& (leaf.getMinusSymbols().size() > 0) ){
				writer.println(" - ");
				writer.println("\t" + LanguageHelper.join(leaf.getMinusSymbols(), "\n\t + "));
			}
			writer.println(") & " + root.getFullName());
		
		}
		
		writer.println("}");
		
		/*
		 * define pred of the block
		 */
		writer.println("pred " + ID + "_pred {");
		// quantifier	: ALL
		if(quantifier.isAllOf()){
			writer.println("all c : (");
			writer.println("\t" + LanguageHelper.join(leaf.getCandidateCoursesInTranscript(), " + "));
			writer.println(") | c in " + root.getFullName());
		// infeasible
		/*
		} else if (quantifier.getLowerBound() > leaf.getCandidateCoursesInTranscript().size()){
			writer.println(FALSE);
		*/
		// quantifier	: INT
		} else if (quantifier.getUpperBound() == -1){
			writer.println("#" + ID + " >= " + quantifier.getLowerBound());
		// quantifier : INT DASH INT
		} else {
			writer.println("#" + ID + " >= " + quantifier.getLowerBound());
			writer.println("#" + ID + " <= " + quantifier.getUpperBound());
		}
		writer.println("}\n");
		


		/* ************************************* */
		if(root == leaf){
			this.close();
		}
	}
	
	public void visit(UnitBlockLeaf leaf){
		if(isRoot){
			isRoot = false;
			this.initialize(leaf);
		}
		/* ************************************* */
		
		writer.println("// UnitBlockLeaf");
		if(leaf.getDiscriminator() == Discriminator.UNIQUE){
			uniqueBlocks.add(leaf);
		}
		
		String ID = leaf.getFullName();
		Quantifier quantifier = leaf.getQuantifier();
		
		/*
		 * define sig of the block
		 */
		writer.println("sig " + ID + " in Course{}");
		writer.println("fact " + ID + "_fact {");
		
		if(
			(quantifier.getLowerBound() > LanguageHelper.sumOfUnits(leaf.getCandidateCoursesInTranscript()) || leaf.getCandidateCoursesInTranscript().size() <= 0)
			&& (leaf.getPlusSymbols().size() <= 0)
		){
			writer.println(ID + "=none\n");
		} else {
		
			writer.println("//" + LanguageHelper.sumOfUnits(leaf.getCandidateCoursesInTranscript()));
			writer.println("(not " + ID + "_pred)  => " + ID + "=none\n");
			writer.println(ID + "_pred  =>" + ID );
			if(quantifier.isAllOf()){
				writer.print(" = ");
			} else {
				writer.print(" in ");
			}
			writer.println("( ");
			writer.println("\t" + LanguageHelper.join(leaf.getCandidateCoursesInTranscript(), "\n\t + "));
			if(leaf.getCandidateCoursesInTranscript().size() > 0 && leaf.getPlusSymbols().size() > 0) {
				writer.println(" + ");
			}
			writer.println("\t" + LanguageHelper.join(leaf.getPlusSymbols(), "\n\t + "));
			if( (leaf.getCandidateCoursesInTranscript().size() > 0 || leaf.getPlusSymbols().size() > 0) 
					&& (leaf.getMinusSymbols().size() > 0) ){
				writer.println(" - ");
				writer.println("\t" + LanguageHelper.join(leaf.getMinusSymbols(), "\n\t + "));
			}
			writer.println(") & " + root.getFullName());
		
		}
		
		
		writer.println("}");
		
		/*
		 * define pred of the block
		 */
		writer.println("pred " + ID + "_pred {");
		// quantifier	: ALL
		if(quantifier.isAllOf()){
			writer.println("all c : (");
			writer.println("\t " + LanguageHelper.join(leaf.getCandidateCoursesInTranscript(), " + "));
			writer.println(") | c in " + root.getFullName());
		// infeasible
			/*
		} else if (quantifier.getLowerBound() > LanguageHelper.sumOfUnits(leaf.getCandidateCoursesInTranscript()) ||
				leaf.getCandidateCoursesInTranscript().size() <= 0){
			writer.println(FALSE);
		*/
		// quantifier	: INT
		} else if (quantifier.getUpperBound() == -1){
			writer.println("(sum c : " + ID + " | c.unit) >= " + quantifier.getLowerBound());
		// quantifier : INT DASH INT
		} else {
			writer.println("(sum c : " + ID + " | c.unit) >= " + quantifier.getLowerBound());
			writer.println("(sum c : " + ID + " | c.unit) <= " + quantifier.getUpperBound());
		}
		writer.println("}\n");

		/* ************************************* */
		if(root == leaf){
			this.close();
		}
	}
	
	public void printConditionChoiceInExpr(int lowerBound, ConditionBlockTrunk tree) {
		boolean isInit = true;
		int childCount = 0;
		for(BlockTree child : tree.getChildren()){
			if(isInit){
				isInit = false;
			} else {
				writer.print(" | ");
			}
			writer.print("let x" + childCount + " = " + child.getFullName() + "_pred");
			childCount++;
		}
		if(!isInit){
			Collection<LinkedList<Integer>> combinations = LanguageHelper.getAllSubsetsOfSizeK(childCount-1, lowerBound);
			writer.println(" | ");
			boolean isInit0 = true;
			for(LinkedList<Integer> subset : combinations){
				if(isInit0){
					isInit0 = false;
				} else {
					writer.print(" or ");
				}
				writer.print("(");
				boolean isInit1 = true;
				for(Integer i : subset){
					if(isInit1){
						isInit1 = false;
					} else {
						writer.print(" and ");
					}
					writer.print("x" + i);
				}
				writer.println(")");
			}
		}
	}
}