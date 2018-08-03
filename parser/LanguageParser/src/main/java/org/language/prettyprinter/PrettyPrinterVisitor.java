package org.language.prettyprinter;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class PrettyPrinterVisitor implements ParseTreeVisitor<Void> {
	
	private Parser parser;
	
	public PrettyPrinterVisitor(Parser parser){
		this.parser = parser;
	}
	
	public Void visit(ParseTree tree){
		return printTreeHelper(tree, "");
	}
	
	public Void visitChildren(RuleNode node){
		for(int i=0; i<node.getChildCount(); i++){
			visit(node.getChild(i));
		}
		return null;
	}

	public Void visitTerminal(TerminalNode node){
		return defaultResult(node, parser);
	}
	
	public Void visitErrorNode(ErrorNode node){
		return defaultResult(node, parser);
	}
	
	protected Void printTreeHelper(Tree tree, String indent){
		defaultResult(tree, parser);
    	for(int i=0; i<tree.getChildCount(); i++){
    		System.out.print(indent + "|->");
    		String appendedIndent = "|  ";
    		if(i == tree.getChildCount()-1){
    			appendedIndent = "   ";
    		}
    		printTreeHelper(tree.getChild(i), indent + appendedIndent);
    	}
    	return null;
    }
	
	protected Void defaultResult(Tree tree, Parser parser) {
		System.out.println(Trees.getNodeText(tree, parser));
		return null;
	}
}