package org.language.main;

/***
 * Excerpted from "The Definitive ANTLR 4 Reference",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/tpantlr2 for more book information.
***/
// import ANTLR's runtime libraries
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.common.course.Course;
import org.common.transcript.Transcript;
import org.language.blocktree.BlockTree;
import org.language.blocktree.BlockTreeVisitor;
import org.language.blocktree.BlockTreeToAlloy;
import org.language.codegenerator.BlockTreeWithListDefs;
import org.language.codegenerator.CourseIdArray;
import org.language.codegenerator.ParseTreeToBlockTree;
import org.language.codegenerator.CodeGeneratorVisitor;
import org.language.main.LangLexer;
import org.language.main.LangParser;
import org.language.main.TestLexer;
import org.language.main.TestParser;

public class LanguageParser {
	public static void main(String[] args) throws Exception {
        //TODO run the program with the test g4 file instead of the real Lang.g4 to avoid messing it up
    	// ParseTest(System.in);
    	InputStream planIn = new FileInputStream(args[0]);
    	InputStream transcriptIn = new FileInputStream(args[1]);
		OutputStream alsOut = new FileOutputStream(args[2]);
		ParseTest1(planIn, transcriptIn, alsOut);
    }
    /*
    public static void ParseArrayInit(InputStream is) throws IOException{
    	// create a CharStream that reads from standard input
    	ANTLRInputStream input = new ANTLRInputStream(is);

        // create a lexer that feeds off of input CharStream
        ArrayInitLexer lexer = new ArrayInitLexer(input);

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        ArrayInitParser parser = new ArrayInitParser(tokens);

        ParseTree tree = parser.init(); // begin parsing at init rule
        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
    */
    
    public static void ParseLang(InputStream is) throws IOException{
    	ANTLRInputStream input = new ANTLRInputStream(is);
        LangLexer lexer = new LangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LangParser parser = new LangParser(tokens);

        ParseTree tree = parser.init(); // begin parsing at init rule
        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }
    
    public static void ParseTest1(InputStream is, InputStream transcriptIs, OutputStream out) throws Exception, IOException{
        ANTLRInputStream input = new ANTLRInputStream(is);
        TestLexer lexer = new TestLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TestParser parser = new TestParser(tokens);

        ParseTree tree = parser.init(); // begin parsing at init rule
        // System.out.println(tree.toStringTree(parser)); // print LISP-style tree
        // ParseTreeVisitor<Void> visitor = new PrettyPrinterVisitor(parser);
        // tree.accept(visitor);
        CodeGeneratorVisitor visitor = new CodeGeneratorVisitor(parser, transcriptIs, out, true);
        visitor.visit(tree);
    }
    
    public static void ParseTest(InputStream is) throws Exception, IOException{
        ANTLRInputStream input = new ANTLRInputStream(is);
        TestLexer lexer = new TestLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TestParser parser = new TestParser(tokens);

        ParseTree tree = parser.init(); // begin parsing at init rule
        // System.out.println(tree.toStringTree(parser)); // print LISP-style tree
        // ParseTreeVisitor<Void> visitor = new PrettyPrinterVisitor(parser);
        // tree.accept(visitor);
        CodeGeneratorVisitor visitor = new CodeGeneratorVisitor(parser);
        visitor.visit(tree);
    }
    
    public static BlockTree handle(String degree, InputStream is, Transcript transcript, OutputStream out) 
    		throws Exception, IOException {
        ANTLRInputStream input = new ANTLRInputStream(is);
        TestLexer lexer = new TestLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TestParser parser = new TestParser(tokens);

        ParseTree tree = parser.init(); // begin parsing at init rule
        
        ParseTreeToBlockTree visitor = new ParseTreeToBlockTree(degree, parser, transcript);
        BlockTree blockTree = visitor.visit(tree);
        BlockTreeVisitor blockTreeVisitor = new BlockTreeToAlloy(out);
        blockTree.accept(blockTreeVisitor);
        
        return blockTree;
    }
    
    // Use this method to run the code generator with transcript
    public static BlockTree handle(String degree, InputStream is, InputStream transcriptIs, OutputStream out) throws Exception, IOException{
        ANTLRInputStream input = new ANTLRInputStream(is);
        TestLexer lexer = new TestLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TestParser parser = new TestParser(tokens);

        ParseTree tree = parser.init(); // begin parsing at init rule
        
        /* old code generator
        CodeGeneratorVisitor visitor = new CodeGeneratorVisitor(parser, transcriptIs, out);
        visitor.visit(tree);
        */
        
        // new code generator - begin
        ParseTreeToBlockTree visitor = new ParseTreeToBlockTree(degree, parser, transcriptIs);
        BlockTree blockTree = visitor.visit(tree);
        BlockTreeVisitor blockTreeVisitor = new BlockTreeToAlloy(out);
        blockTree.accept(blockTreeVisitor);
        
        return blockTree;
     // new code generator - end
    }
    
    public static BlockTree handle(String degree, InputStream is, Set<Course> coursesInTranscript, OutputStream out) 
    		throws Exception, IOException {
        ANTLRInputStream input = new ANTLRInputStream(is);
        TestLexer lexer = new TestLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TestParser parser = new TestParser(tokens);

        ParseTree tree = parser.init(); // begin parsing at init rule

        ParseTreeToBlockTree visitor = new ParseTreeToBlockTree(degree, parser, coursesInTranscript);
        BlockTree blockTree = visitor.visit(tree);
        BlockTreeVisitor blockTreeVisitor = new BlockTreeToAlloy(out);
        blockTree.accept(blockTreeVisitor);
        
        return blockTree;
    }
    
    public static BlockTree generateBlockTree(String degree, InputStream is, Set<Course> coursesInTranscript, 
    		Map<String, CourseIdArray> listDefs, Map<Course, Course> courseOverrideMap)
    		throws Exception, IOException {
    	ANTLRInputStream input = new ANTLRInputStream(is);
        TestLexer lexer = new TestLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TestParser parser = new TestParser(tokens);

        ParseTree tree = parser.init(); // begin parsing at init rule
        ParseTreeToBlockTree visitor = new ParseTreeToBlockTree(degree, parser, coursesInTranscript, listDefs, courseOverrideMap);
        BlockTree blockTree = visitor.generateBlockTree(tree);
        
        return blockTree;
    }
    
    public static  Map<String, CourseIdArray> generateListDefs(String degree, InputStream is, Set<Course> coursesInTranscript)
    		throws Exception, IOException {
    	ANTLRInputStream input = new ANTLRInputStream(is);
        TestLexer lexer = new TestLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TestParser parser = new TestParser(tokens);

        ParseTree tree = parser.init(); // begin parsing at init rule
        ParseTreeToBlockTree visitor = new ParseTreeToBlockTree(degree, parser, coursesInTranscript);
        Map<String, CourseIdArray> listDefs = visitor.generateListDefs(tree);
        
        return listDefs;
    }
    
    @Deprecated
    public static BlockTreeWithListDefs generateBlockTreeWithListDef(String degree, InputStream is, Set<Course> coursesInTranscript)
    		throws Exception, IOException {
    	ANTLRInputStream input = new ANTLRInputStream(is);
        TestLexer lexer = new TestLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TestParser parser = new TestParser(tokens);

        ParseTree tree = parser.init(); // begin parsing at init rule
        ParseTreeToBlockTree visitor = new ParseTreeToBlockTree(degree, parser, coursesInTranscript);
        BlockTreeWithListDefs blockTreeWithListDefs = visitor.generateBlockTreeWithListDef(tree);
        
        return blockTreeWithListDefs;
    }
    
    public static void printTree(Tree tree, Parser parser){
    	printTreeHelper(tree, parser, "");
    }
    
    private static void printTreeHelper(Tree tree, Parser parser, String indent){
    	System.out.println(Trees.getNodeText(tree, parser));
    	for(int i=0; i<tree.getChildCount(); i++){
    		System.out.print(indent + "|->");
    		String appendedIndent = "|  ";
    		if(i == tree.getChildCount()-1){
    			appendedIndent = "   ";
    		}
    		printTreeHelper(tree.getChild(i), parser, indent + appendedIndent);
    	}
    }
}