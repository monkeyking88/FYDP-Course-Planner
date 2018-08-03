package org.language.codegenerator;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;
import org.apache.commons.lang3.StringUtils;
import org.common.course.Course;
import org.common.course.CourseDataSource;
import org.common.course.utilities.CourseUtilities;
import org.common.exception.CourseNotFoundException;
import org.common.exception.SemanticErrorException;
import org.common.helper.Helper;

public class CodeGeneratorVisitor{
	
	private Parser parser;
	private PrintWriter writer;

	private Map<String, CourseIdArray> listDefs = new HashMap<String, CourseIdArray>();
	private Set<Course> allCourses = new HashSet<Course>();
	private Collection<Course> transcriptIds = null;
	private Set<SimpleBlock> uniqueBlocks = new HashSet<SimpleBlock>();
	private SimpleBlock root;
	public static final String ROOT = "result";
	// count of blocks, including condition_reqs, 
	private int blockCount = 0;
	
	public enum ArrayType {
		ALL_OF_BLOCK, BLOCK, LIST_DEF
	}
	
	public enum SetOperator {
		UNION, DIFFERENCE
	}
	
	public CodeGeneratorVisitor(Parser parser, InputStream transcriptIs, OutputStream out) throws Exception {
		this.parser = parser;
		this.writer = new PrintWriter(out);
		this.transcriptIds = new HashSet<Course>();
		// read transcript
		Scanner transcriptScanner = new Scanner(transcriptIs);
		try {
			//transcriptScanner.nextLine();
			String courseMeta = transcriptScanner.nextLine();
			for(Integer transcriptCourseId : CourseUtilities.CourseMetaToIds(courseMeta)){
				Course transcriptCourse = CourseDataSource.Defs.getDefById(transcriptCourseId);
				// System.err.println("\ntranscriptCourseId:" + transcriptCourseId);
				allCourses.add(transcriptCourse);
				transcriptIds.add(transcriptCourse);
			}
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("transcript input must has at least 2 lines");
		} catch (NullPointerException e) {
			throw e;
		} finally {
			Helper.close(transcriptScanner);
		}
		
	}
	
	public CodeGeneratorVisitor(Parser parser, InputStream transcriptIs, OutputStream out, boolean bl){
		this.parser = parser;
		this.writer = new PrintWriter(out);
		transcriptIds= new HashSet<Course>();
		transcriptIds.add(CourseDataSource.Defs.getDefByCourseName("MATH247"));
		transcriptIds.add(CourseDataSource.Defs.getDefByCourseName("CS371"));
		transcriptIds.add(CourseDataSource.Defs.getDefByCourseName("AMATH231"));
		transcriptIds.add(CourseDataSource.Defs.getDefByCourseName("AMATH251"));
		transcriptIds.add(CourseDataSource.Defs.getDefByCourseName("AMATH351"));
		transcriptIds.add(CourseDataSource.Defs.getDefByCourseName("AMATH353"));
		transcriptIds.add(CourseDataSource.Defs.getDefByCourseName("PHYS121"));
		transcriptIds.add(CourseDataSource.Defs.getDefByCourseName("AMATH332"));
		transcriptIds.add(CourseDataSource.Defs.getDefByCourseName("AMATH455"));
		transcriptIds.add(CourseDataSource.Defs.getDefByCourseName("AMATH475"));
		transcriptIds.add(CourseDataSource.Defs.getDefByCourseName("AMATH463"));
		allCourses.addAll(transcriptIds);
	}
	
	public CodeGeneratorVisitor(Parser parser, OutputStream out){
		this.parser = parser;
		this.writer = new PrintWriter(out);
	}
	
	public CodeGeneratorVisitor(Parser parser){
		this.parser = parser;
		this.writer = new PrintWriter(System.out);
	}
	
	public Void visit(ParseTree tree) throws Exception{
		try {
			if(getNodeText(tree).equals("init")){
				tree = tree.getChild(0);
			}
			visitDegree(tree);
			writer.flush();
		} catch (Exception e){
			throw e;
		} finally {
			Helper.close(writer);
		}
		return null;
	}
	
	// degree  :  list_defs reqs ;
	public Void visitDegree(ParseTree tree) throws Exception{
		// print transcript
		writer.println("fun transcript[] : Course {");
		if(transcriptIds != null && transcriptIds.size() > 0){
			Iterator<Course> transcriptIt = transcriptIds.iterator();
			writer.println(transcriptIt.next().getName());
			while(transcriptIt.hasNext()){
				writer.println(" + " + transcriptIt.next().getName());
			}
		} else {
			// print none if no courses in transcript
			writer.println("none");
		}
		writer.println("}");
		// declare Course sig
		writer.println("abstract sig Course{");
		writer.println("\tunit : one Int");
		writer.println("}");
		// declare root
		root = new SimpleBlock(ROOT, null);
		writer.println("sig " + root.getName() + " in Course{}\n");
		
		// visit list_defs and reqs
		visitLists_defs(tree.getChild(0));
		visitReqs(tree.getChild(1), root);
		
		// declare courses and assign units
		if(!allCourses.isEmpty()){
			for(Course course : allCourses){
				if(!(course.equals(Course.ANY)||
						course.equals(Course.NONE))){
					// declare course
					String courseName = course.getName();
					writer.print("one sig ");
					writer.print(courseName);
					writer.print(" extends Course{}\n");
					// assign unit to each declared course
					// every 1 unit in alloy represents a 0.25 units in an actual course
					// e.g.) a course CS136 worthes 0.5 units will be assigned CS136.unit = 2 in alloy
					writer.print("fact{");
					writer.print(courseName + ".unit = " + course.getUnits().divide(new BigDecimal(0.25)).intValue());
					writer.println("}\n");
				}
			}
		}
		
		// create fact to handle unique
		if(!uniqueBlocks.isEmpty()){
			writer.println("fact unique_fact {");
			for(SimpleBlock uniqueBlock : uniqueBlocks){
				Collection<SimpleBlock> siblings = uniqueBlock.getSiblings();
				if(!siblings.isEmpty()){
					writer.println("\t// " + uniqueBlock.getName() + " is unique");
					for(SimpleBlock sibling : siblings){
						writer.println("\tno (" + uniqueBlock.getName() + " & " + sibling.getName() + ")");
					}
				}
			}
			writer.println("}");
		}
		
		// create pred (MAIN + "_pred")
		// that calls all reqs in the top levels
		writer.println("pred " + root.getName() + "_pred {");
		writer.println(root.getName() + "=" + join(root.getChildren(), " + "));
		writer.println(root.getName() + " in transcript");
		boolean isInit = true;
		for(SimpleBlock child : root.getChildren()){
			if(isInit){
				isInit = false;
			} else {
				writer.print(" and ");
			}
			writer.print(child.getName() + "_pred");
		}
		writer.println("\n}");
		
		writer.println("run " + root.getName() + "_pred for 0 but 9 Int");
		
		return null;
	}
	
	// list_defs 	: 	//can be empty
	// 					| list_def list_defs;
	private Void visitLists_defs(ParseTree tree){
		while(tree.getChildCount() > 0){
			visitList_def(tree.getChild(0));
			tree = tree.getChild(1);
		}
		
		return null;
	}
	
	// list_def	: ID IS array;	
	public Void visitList_def(ParseTree tree){
		String ID = getNodeText(tree.getChild(0));
		/*
		writer.println("sig " + ID + " in Course{}");
		writer.println("fact {");
		writer.println("\t" + ID + " = ");
		writer.print("\t");
		*/
		CourseIdArray courseIdArray = visitArray(tree.getChild(2), ID);
		listDefs.put(ID, courseIdArray);
		/*
		writer.println("}\n");
		*/
		return null;
	}
	
	// array : LBRACE course_id_list RBRACE;
	public CourseIdArray visitArray(ParseTree tree, String id){
		CourseIdArray courseIdArray = getArray(id, tree);
				// TODO: ensure acyclicity of list_defs 
		return courseIdArray;
	}
	public CourseIdArray getArray(String ID, ParseTree tree){
		CourseIdArray courseIdArray = new CourseIdArray(ID);
		// array : LBRACE course_id_list RBRACE;
		tree = tree.getChild(1);
		while(true) {
			String course_id;
			// course_id_list	: course_id
			// 					| course_id COMMA course_id_list;
			if(tree.getChild(0).getChildCount() == 1){
				// course_id 	: COURSE
				//				| COURSESTAR
				//				| COURSERANGE
				//				| ID
				//				| ANY;
				course_id = getNodeTextbyDepth(tree.getChild(0), 1);
				CourseId courseIdObj = new CourseId(course_id);
				courseIdArray.addPlus(courseIdObj);
			} else {
				// course_id_list	: course_id
				// 					| course_id COMMA course_id_list;
				// course_id 	: except course_id
				course_id = getNodeTextbyDepth(tree.getChild(0).getChild(1), 1);
				CourseId courseIdObj = new CourseId(course_id);
				courseIdArray.addMinus(courseIdObj);
			}
			if(tree.getChildCount() == 3){
				tree = tree.getChild(2);
			} else {
				break;
			}
		}
		return courseIdArray;
	}
	public Collection<Course> expandArray(CourseIdArray courseIdArray, ArrayType arrayType){
		Collection<Course> plusArray = new LinkedList<Course>();
		Collection<Course> minusArray = new LinkedList<Course>();
		
		for(CourseId plusCourseId : courseIdArray.getPlusArray()){
			try {
				Collection<Course> matchedCourses;
				switch(plusCourseId.getType()){
					case CATALOG_WILDCARD:
						/*
						System.err.println("Full Name is :" + plusCourseId.getFullName());
						System.err.println("Subject is :" + plusCourseId.getSubject());
						System.err.println("Subject is :" + plusCourseId.getCatalogBegin());
						*/
						matchedCourses = CourseDataSource.Defs.getDefByCatalogWildCard(
								plusCourseId.getSubject(), 
								plusCourseId.getCatalogBegin());
						if(arrayType.equals(ArrayType.BLOCK)){
							matchedCourses.retainAll(transcriptIds);
						}
						plusArray.addAll(matchedCourses);
						break;
					case CATALOG_RANGE:
						matchedCourses = CourseDataSource.Defs.getDefByCatalogRange(
										plusCourseId.getSubject(), 
										plusCourseId.getCatalogBegin(), 
										plusCourseId.getCatalogEnd());
						if(arrayType.equals(ArrayType.BLOCK)){
							matchedCourses.retainAll(transcriptIds);
						}
						plusArray.addAll(matchedCourses);
						break;
					case ANY:
						plusArray.clear();
						plusArray.add(Course.ANY);
						break;
					case ID:
						// @TODO: allow ID in list_def
						if(arrayType.equals(ArrayType.LIST_DEF)){
							throw new SemanticErrorException("Reference of "+ 
																plusCourseId.getFullName() + 
																" in " + 
																courseIdArray.getId()+
																"\nSorry, we do not support "
																+ "reference of list_def in a list_def");
						}
						// @TODO: expand wildcard/range in ID
						CourseIdArray listDef = listDefs.get(plusCourseId.getFullName());
						if(listDef == null){
							throw new SemanticErrorException("ID " + plusCourseId.getFullName() + " not found.\n"
									+ "You might have used an requirement id."
									+ "Note that ONLY list_Def ID is available for reference.");
						}
						Collection<Course> additive = expandArray(listDefs.get(plusCourseId.getFullName()), arrayType);
						plusArray.addAll(additive);
						break;
					case COURSENAME: 
						Course matchedCouse = CourseDataSource.Defs.getDefByCourseName(
								plusCourseId.getFullName());
						if(matchedCouse == null){
							throw new CourseNotFoundException(plusCourseId.getFullName());
						}
						if(!(arrayType.equals(ArrayType.BLOCK) && 
								transcriptIds.contains(matchedCouse)==false)){
							plusArray.add(matchedCouse);
						}
						break;
				} 
				if(plusCourseId.getType().
						equals(CourseId.CourseIdType.ANY)){
					if(courseIdArray.getPlusArray().size() > 1){
						throw new SemanticErrorException("the \"any\" keyword means the set of all courses "
								+ "and thus should not appear more than once in a list");
					}
					break;
				}
			} catch (NullPointerException e){
				throw new SemanticErrorException("failed to match plusCourseId: " + plusCourseId + "\n", e);
			}
		}
		
		for(CourseId minusCourseId : courseIdArray.getMinusArray()){
			try{
				Collection<Course> matchedCourses;
				switch(minusCourseId.getType()){
					case CATALOG_WILDCARD:
						matchedCourses = CourseDataSource.Defs.getDefByCatalogWildCard(
								minusCourseId.getSubject(), 
								minusCourseId.getCatalogBegin());
						if(arrayType.equals(ArrayType.BLOCK)){
							matchedCourses.retainAll(transcriptIds);
						}
						minusArray.addAll(matchedCourses);
						break;
					case CATALOG_RANGE:
						matchedCourses = CourseDataSource.Defs.getDefByCatalogRange(
										minusCourseId.getSubject(), 
										minusCourseId.getCatalogBegin(), 
										minusCourseId.getCatalogEnd());
						if(arrayType.equals(ArrayType.BLOCK)){
							matchedCourses.retainAll(transcriptIds);
						}
						minusArray.addAll(matchedCourses);
						break;
					case ANY:
						plusArray.clear();
						minusArray.clear();
						minusArray.add(Course.NONE);
						break;
					case ID:
						// @TODO: expand wildcard/range in ID
						// @TODO: allow ID in list_def
						if(arrayType.equals(ArrayType.LIST_DEF)){
							throw new SemanticErrorException("Reference of "+ 
																minusCourseId.getFullName() + 
																" in " + 
																courseIdArray.getId()+
																"\nSorry, we do not support "
																+ "reference of list_def in a list_def");
						}
						CourseIdArray listDef = listDefs.get(minusCourseId.getFullName());
						if(listDef == null){
							throw new SemanticErrorException("ID " + minusCourseId.getFullName() + " not found.\n"
									+ "You might have used an requirement id."
									+ "Note that ONLY list_Def ID is available for reference.");
						}
						Collection<Course> subtrahend = expandArray(listDef, arrayType);
						minusArray.addAll(subtrahend);
						break;
					case COURSENAME: 
						Course matchedCouse = CourseDataSource.Defs.getDefByCourseName(
								minusCourseId.getFullName());
						if(matchedCouse == null){
							throw new CourseNotFoundException(minusCourseId.getFullName() + " is not found");
						}
						minusArray.add(matchedCouse);
						break;
				} 
				if(minusCourseId.getType().
						equals(CourseId.CourseIdType.ANY)){
					if(courseIdArray.getPlusArray().size() > 0){
						throw new SemanticErrorException("the \"any\" keyword means the set of all courses "
								+ "and thus should not appear more than once in a list");
					}
					break;
				}
			} catch (NullPointerException e){
				throw new SemanticErrorException("failed to match minusCourseId: " + minusCourseId + "\n", e);
			}
		}
		
		plusArray.removeAll(minusArray);
		return plusArray;
	}
	public void printArray(Collection<Course> array){
		
		if(!array.isEmpty()){
			boolean isFirst = true;
			for(Course course : array){
				if(isFirst){
					isFirst = false;
				} else {
					writer.print("\t + ");
				}
				if(!(course.equals(Course.ANY)||
						course.equals(Course.NONE))){
					allCourses.add(course);
				}
				writer.println(course.getName());
			}
		} else {
			writer.println("none");
		}
	}
	
	// reqs	: 
	//		| req reqs;
	public Void visitReqs(ParseTree tree, SimpleBlock parent) throws Exception{
		while(tree.getChildCount() > 0){
			visitReq(tree.getChild(0), parent);
			tree = tree.getChild(1);
		}
		return null;
	}
	//	unit_reqs : 
	//				| unit_req unit_reqs;
	public Void visitUnit_reqs(ParseTree tree, SimpleBlock parent) throws Exception{
		while(tree.getChildCount() > 0){
			visitUnit_req(tree.getChild(0), parent);
			tree = tree.getChild(1);
		}
		return null;
	}
	//	course_reqs : 
	//				| course_req course_reqs;
	public Void visitCourse_reqs(ParseTree tree, SimpleBlock parent) throws Exception{
		while(tree.getChildCount() > 0){
			visitCourse_req(tree.getChild(0), parent);
			tree = tree.getChild(1);
		}
		return null;
	}
	
	// req : condition_req
	//		| unit_req
	//		| course_req; 
	public Void visitReq(ParseTree tree, SimpleBlock parent) throws Exception{
		if(getNodeText(tree.getChild(0)).equals("condition_req")){
			visitCondition_req(tree.getChild(0), parent);
		} else if (getNodeText(tree.getChild(0)).equals("unit_req")){
			visitUnit_req(tree.getChild(0), parent);
		} else if (getNodeText(tree.getChild(0)).equals("course_req")){
			visitCourse_req(tree.getChild(0), parent);
		}
		return null;
	}
	
	//	course_req : ID IS course_expr
	//				| course_expr;
	public Void visitCourse_req(ParseTree tree, SimpleBlock parent) throws Exception{
		String name;
		ParseTree course_expr;
		if(tree.getChildCount() == 3){
			name = getNodeText(tree.getChild(0));
			course_expr = tree.getChild(2);
		} else {
			name = getNextBlockID();
			course_expr = tree.getChild(0);
		}
		writer.println("// visitCourse_req: name = " + name);
		SimpleBlock currBlock = new SimpleBlock(name, parent);
		visitCourse_expr(course_expr, currBlock);
		return null;
	}
	//	course_expr	: discriminator quantifier COURSES OF LBRACE course_reqs RBRACE
	//				| discriminator quantifier COURSES OF array;
	public Void visitCourse_expr(ParseTree tree, SimpleBlock currBlock) throws Exception{
		// handles unique
		if(tree.getChild(0).getChildCount() != 0){
			uniqueBlocks.add(currBlock);
		}
		String ID = currBlock.getName();
		if(tree.getChildCount() == 5){
			// course_expr	: discriminator quantifier COURSES OF array;
			ParseTree quantifier = tree.getChild(1);
			CourseIdArray courseIdArray = visitArray(tree.getChild(4), currBlock.getName());
			writer.println("pred " + ID + "_pred {");
			boolean isConsistent = visitQuantifierInCourse_expr(quantifier, ID, courseIdArray);
			writer.println("}\n");
			
			Collection<Course> expandedCourses = expandArray(courseIdArray, ArrayType.BLOCK);
			writer.println("sig " + ID + " in Course{}");
			writer.println("fact " + ID + "_fact {");
			if(isConsistent && expandedCourses.size()>0){
				writer.println("(not " + ID + "_pred)  => " + ID + "=none\n");
				writer.println(ID + "_pred  =>" + ID );
				if(isAllQuantifier(quantifier)){
					writer.print(" = ");
				} else {
					writer.print(" in ");
				}
				writer.println("( ");
				printArray(expandedCourses);
				writer.println(") & " + root.getName());
			} else {
				writer.println(ID + "=none\n");
			}
			writer.println("}");
		} else {
			// course_expr	: discriminator quantifier COURSES OF LBRACE course_reqs RBRACE
			visitCourse_reqs(tree.getChild(5), currBlock);
			writer.println("sig " + ID + " in Course{}");
			writer.println("fact " + ID + "_fact {");
			writer.print(ID + " = (");
			writer.print(join(currBlock.getChildren(), " + "));
			writer.println(") & " + root.getName());
			writer.println("}");
			writer.println("pred " + ID + "_pred {");
			visitQuantifierInCourse_expr(tree.getChild(1), ID, currBlock);
			writer.println("} \n");
		}
		return null;
	}
	
	//	unit_req : ID IS unit_expr
	//			| unit_expr;
	public Void visitUnit_req(ParseTree tree, SimpleBlock parent) throws Exception{
		String name;
		ParseTree unit_expr;
		if(tree.getChildCount() == 3){
			name = getNodeText(tree.getChild(0));
			unit_expr = tree.getChild(2);
		} else {
			name = getNextBlockID();
			unit_expr = tree.getChild(0);
		}
		writer.println("// visitUnit_req: name = " + name);
		SimpleBlock currBlock = new SimpleBlock(name, parent);
		visitUnit_expr(unit_expr, currBlock);
		return null;
	}
	//	unit_expr	: discriminator dec_quantifier UNITS OF LBRACE unit_reqs RBRACE
	//				| discriminator dec_quantifier UNITS OF array;
	public Void visitUnit_expr(ParseTree tree, SimpleBlock currBlock) throws Exception{
		// handles unique
		if(tree.getChild(0).getChildCount() != 0){
			uniqueBlocks.add(currBlock);
		}
		String ID = currBlock.getName();
		if(tree.getChildCount() == 5){
			// unit_expr	: discriminator dec_quantifier UNITS OF array
			ParseTree dec_quantifier = tree.getChild(1);
			CourseIdArray courseIdArray = visitArray(tree.getChild(4), currBlock.getName());
			writer.println("pred " + ID + "_pred {");
			boolean isConsistent = visitQuantifierInUnit_expr(dec_quantifier, ID, courseIdArray);
			writer.println("}\n");
			writer.println("sig " + ID + " in Course{}");
			
			Collection<Course> expandedCourses = expandArray(courseIdArray, ArrayType.BLOCK);
			writer.println("fact " + ID + "_fact {");
			if(isConsistent && expandedCourses.size()>0){
				writer.println("(not " + ID + "_pred)  => " + ID + "=none\n");
				writer.println(ID + "_pred  =>" + ID );
				if(isAllQuantifier(dec_quantifier)){
					writer.print(" = ");
				} else {
					writer.print(" in ");
				}
				writer.println("( ");
				printArray(expandedCourses);
				writer.println(") & " + root.getName());
			} else {
				writer.println(ID + "=none\n");
			}
			writer.println("}");
			/*
			ParseTree dec_quantifier = tree.getChild(1);
			CourseIdArray courseIdArray;
			writer.println("sig " + ID + " in Course{}");
			writer.println("fact " + ID + "_fact {");
			writer.println("(not " + ID + "_pred)  => " + ID + "=none\n");
			writer.println(ID + "_pred  =>");
			writer.println(ID + " = (");
			if(isAllQuantifier(dec_quantifier)){
				courseIdArray = visitArray(tree.getChild(4), currBlock.getName(), ArrayType.ALL_OF_BLOCK);
			} else {
				courseIdArray = visitArray(tree.getChild(4), currBlock.getName(), ArrayType.BLOCK);
			}
			writer.println(") & " + root.getName());
			writer.println("}");
			writer.println("pred " + ID + "_pred {");
			visitQuantifierInUnit_expr(dec_quantifier, ID, courseIdArray);
			writer.println("}\n");
			*/
		} else {
			// unit_expr	: discriminator dec_quantifier UNITS OF LBRACE unit_reqs RBRACE;
			visitUnit_reqs(tree.getChild(5), currBlock);
			writer.println("sig " + ID + " in Course{}");
			writer.println("fact " + ID + "_fact {");
			writer.print(ID + " = (");
			writer.print(join(currBlock.getChildren(), " + "));
			writer.println(") & " + root.getName());
			writer.println("}");
			writer.println("pred " + ID + "_pred {");
			visitQuantifierInUnit_expr(tree.getChild(1), ID, currBlock);
			writer.println("} \n");
		}
		return null;
	}
	
	
	//	condition_req : ID IS expr
	//				| expr;
	public Void visitCondition_req(ParseTree tree, SimpleBlock parent) throws Exception{
		String name;
		ParseTree expr;
		if(tree.getChildCount() == 3){
			name = getNodeText(tree.getChild(0));
			expr = tree.getChild(2);
		} else {
			name = getNextBlockID();
			expr = tree.getChild(0);
		}
		SimpleBlock currBlock = new SimpleBlock(name, parent);
		visitExpr(expr, currBlock);
		return null;
	}
	//	expr	: discriminator quantifier OF LBRACE reqs RBRACE
	//			| discriminator quantifier OF array;
	public Void visitExpr(ParseTree tree, SimpleBlock currBlock) throws Exception{
		writer.println("//" + getNodeText(tree.getChild(0)));
		if(tree.getChild(0).getChildCount() != 0){
			uniqueBlocks.add(currBlock);
		}
		String ID = currBlock.getName();
		if(tree.getChildCount() == 4){
			// expr : discriminator quantifier OF array;
			ParseTree quantifier = tree.getChild(1);
			CourseIdArray courseIdArray = visitArray(tree.getChild(3), currBlock.getName());
			writer.println("pred " + ID + "_pred {");
			boolean isConsistent = visitQuantifierInCourse_expr(quantifier, ID, courseIdArray);
			writer.println("}\n");
			writer.println("sig " + ID + " in Course{}");
			
			Collection<Course> expandedCourses = expandArray(courseIdArray, ArrayType.BLOCK);
			writer.println("fact " + ID + "_fact {");
			if(isConsistent && expandedCourses.size()>0){
				writer.println("(not " + ID + "_pred)  => " + ID + "=none\n");
				writer.println(ID + "_pred  =>" + ID );
				if(isAllQuantifier(quantifier)){
					writer.print(" = ");
				} else {
					writer.print(" in ");
				}
				writer.println("( ");
				printArray(expandedCourses);
				writer.println(") & " + root.getName());
			} else {
				writer.println(ID + "=none\n");
			}
			writer.println("}");
			/*
			ParseTree quantifier = tree.getChild(1);
			CourseIdArray courseIdArray;
			writer.println("sig " + ID + " in Course{}");
			writer.println("fact " + ID + "_fact {");
			writer.println("(not " + ID + "_pred)  => " + ID + "=none\n");
			writer.println(ID + "_pred  =>");
			writer.println(ID + " = (");
			if(isAllQuantifier(quantifier)){
				courseIdArray = visitArray(tree.getChild(3), currBlock.getName(), ArrayType.ALL_OF_BLOCK);
			} else {
				courseIdArray = visitArray(tree.getChild(3), currBlock.getName(), ArrayType.BLOCK);
			}
			writer.println(") & " + root.getName());
			writer.println("}");
			writer.println("pred " + ID + "_pred {");
			visitQuantifierInCourse_expr(quantifier, ID, courseIdArray);
			writer.println("}\n");
			*/
		} else {
			// expr : discriminator quantifier OF LBRACE reqs RBRACE
			visitReqs(tree.getChild(4), currBlock);
			writer.println("sig " + ID + " in Course{}");
			writer.println("fact " + ID + "_fact {");
			writer.println("(not " + ID + "_pred)  => " + ID + "=none\n");
			writer.println(ID + "_pred  =>");
			writer.print(ID + " = (");
			writer.print(join(currBlock.getChildren(), " + "));
			writer.println(") & " + root.getName());
			writer.println("}");
			writer.println("pred " + ID + "_pred {");
			visitQuantifierInExpr(tree.getChild(1), currBlock);
			writer.println("} \n");
		}
		return null;
	}
	//	quantifier	: INT
	//				| ALL
	//				| INT DASH INT;
	public Void visitQuantifierInExpr(ParseTree tree, SimpleBlock currBlock) throws Exception{
		if(tree.getChildCount() == 1){
			if(getNodeText(tree.getChild(0)).equals("all")){
				//	quantifier	: ALL
				boolean isInit = true;
				for(SimpleBlock child : currBlock.getChildren()){
					if(isInit){
						isInit = false;
					} else {
						writer.print(" and ");
					}
					writer.print(child.getName() + "_pred");
				}
			} else {
				//	quantifier	: INT
				int lowerBound = Integer.parseInt(getNodeText(tree.getChild(0)));
				if(lowerBound > currBlock.getChildren().size()){
					StringBuffer errMsg = new StringBuffer();
					errMsg.append("Semantic Error:\n");
					errMsg.append("in expr, lowerBound > upperBound\n");
					errMsg.append("k of { \n");
					errMsg.append("\t condition_1{...} \n");
					errMsg.append("\t condition_2{...} \n");
					errMsg.append("\t ... \n");
					errMsg.append("\t condition_n{...} \n");
					errMsg.append("} \n");
					errMsg.append("and k > n \n");
					throw(new Exception(errMsg.toString()));
				}
				printConditionChoiceInExpr(lowerBound, currBlock);
				
			}
		} else {
			// quantifier : INT DASH INT
			int lowerBound = Integer.parseInt(getNodeText(tree.getChild(0)));
			int upperBound = Integer.parseInt(getNodeText(tree.getChild(2)));
			if(lowerBound != 0){ // e.g. 0-1 of {condition1 condition2 condition3}
				printConditionChoiceInExpr(lowerBound, currBlock);
			}
			writer.println("not (");
			printConditionChoiceInExpr(upperBound + 1, currBlock);
			writer.println(")");
		}
		return null;
	}
	public Void printConditionChoiceInExpr(int lowerBound, SimpleBlock currBlock) throws Exception{
		boolean isInit = true;
		int childCount = 0;
		for(SimpleBlock child : currBlock.getChildren()){
			if(isInit){
				isInit = false;
			} else {
				writer.print(" | ");
			}
			writer.print("let x" + childCount + " = " + child.getName() + "_pred");
			childCount++;
		}
		if(!isInit){
			Collection<LinkedList<Integer>> combinations = getAllSubsetsOfSizeK(childCount-1, lowerBound);
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
		return null;
	}
	public boolean visitQuantifierInCourse_expr(ParseTree tree, String ID, CourseIdArray courseIdArray){
		if(tree.getChildCount() == 1){
			if(getNodeText(tree.getChild(0)).equals("all")){
				//	quantifier	: ALL
				Collection<Course> expandedCourses = expandArray(courseIdArray, ArrayType.ALL_OF_BLOCK);
				if(!expandedCourses.retainAll(transcriptIds)){
					writer.print("all c : (");
					printArray(expandedCourses);
					writer.println(") | c in " + root.getName());
				} else {
					writer.println("#none > 0");
					return false;
				}
			} else {
				//	quantifier	: INT
				writer.println("#" + ID + " >= " + getNodeText(tree.getChild(0)));
			}
		} else {
			// quantifier : INT DASH INT
			writer.println("#" + ID + " >= " + getNodeText(tree.getChild(0)));
			writer.println("#" + ID + " <= " + getNodeText(tree.getChild(2)));
		}
		return true;
	}
	public Void visitQuantifierInCourse_expr(ParseTree tree, String ID, SimpleBlock currBlock){
		if(tree.getChildCount() == 1){
			if(getNodeText(tree.getChild(0)).equals("all")){
				//	quantifier	: ALL
				writer.print("all c : (");
				join(currBlock.getChildren(), " + ");
				writer.println(") | c in " + root.getName());
			} else {
				//	quantifier	: INT
				writer.println("#" + ID + " >= " + getNodeText(tree.getChild(0)));
			}
		} else {
			// quantifier : INT DASH INT
			writer.println("#" + ID + " >= " + getNodeText(tree.getChild(0)));
			writer.println("#" + ID + " <= " + getNodeText(tree.getChild(2)));
		}
		return null;
	}
	public boolean visitQuantifierInUnit_expr(ParseTree tree, String ID, CourseIdArray courseIdArray){
		if(tree.getChildCount() == 1){
			if(getNodeText(tree.getChild(0)).equals("all")){
				//	quantifier	: ALL
				Collection<Course> expandedCourses = expandArray(courseIdArray, ArrayType.ALL_OF_BLOCK);
				if(!expandedCourses.retainAll(transcriptIds)){
					writer.print("all c : (");
					printArray(expandedCourses);
					writer.println(") | c in " + root.getName());
				} else {
					writer.println("#none > 0");
					return false;
				}
			} else {
				//	quantifier	: DEC
				int lowerBound = (int)(Double.parseDouble(getNodeText(tree.getChild(0)))/0.25);
				writer.println("(sum c : " + ID + " | c.unit) >= " + lowerBound);
			}
		} else {
			// quantifier : DEC DASH DEC
			int lowerBound = (int)(Double.parseDouble(getNodeText(tree.getChild(0)))/0.25);
			int upperBound = (int)(Double.parseDouble(getNodeText(tree.getChild(2)))/0.25);
			writer.println("(sum c : " + ID + " | c.unit) >= " + lowerBound);
			writer.println("(sum c : " + ID + " | c.unit) <= " + upperBound);
		}
		return true;
	}
	public boolean visitQuantifierInUnit_expr(ParseTree tree, String ID, SimpleBlock currBlock){
		if(tree.getChildCount() == 1){
			if(getNodeText(tree.getChild(0)).equals("all")){
				//	quantifier	: ALL
				writer.print("all c : (");
				writer.print(join(currBlock.getChildren(), " + "));
				writer.println(") | c in " + root.getName());
			} else {
				//	quantifier	: DEC
				int lowerBound = (int)(Double.parseDouble(getNodeText(tree.getChild(0)))/0.25);
				writer.println("(sum c : " + ID + " | c.unit) >= " + lowerBound);
			}
		} else {
			// quantifier : DEC DASH DEC
			int lowerBound = (int)(Double.parseDouble(getNodeText(tree.getChild(0)))/0.25);
			int upperBound = (int)(Double.parseDouble(getNodeText(tree.getChild(2)))/0.25);
			writer.println("(sum c : " + ID + " | c.unit) >= " + lowerBound);
			writer.println("(sum c : " + ID + " | c.unit) <= " + upperBound);
		}
		return true;
	}
	
	// Helper Functions
	public String getNodeText(ParseTree node){
		return getNodeTextbyDepth(node, 0);
	}
	public String getNodeTextbyDepth(ParseTree node, int depth){
		for(int i=0; i<depth; i++){
			node = node.getChild(0);
		}
		return Trees.getNodeText(node, this.parser);
	}
	// TODO: check against existing courses as well as existing list_def to avoid duplicate names
	private String getNextBlockID(){
		blockCount++;
		return "Block" + blockCount;
	}
	public Collection<LinkedList<Integer>> getAllSubsetsOfSizeK(int upperBound, int k){
		Collection<LinkedList<Integer>> result = new LinkedList<LinkedList<Integer>>();
		result.add(new LinkedList<Integer>());
		int size = 0;
		while(size < k){
			Collection<LinkedList<Integer>> newResult = new LinkedList<LinkedList<Integer>>();
			for(LinkedList<Integer> subset : result){
				for(int i=(subset.isEmpty() ? 0 : subset.peekLast()+1); i<=(upperBound-(k-size-1)); i++){
					LinkedList<Integer> newSubset = new LinkedList<Integer>();
					for(Integer e : subset){
						newSubset.add(e);
					}
					newSubset.add(i);
					newResult.add(newSubset);
				}
			}
			result = newResult;
			size++;
		}
		
		return result;
	}
	
	public static String join(final Iterable<SimpleBlock> iterable, final String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }
	
	public static String join(final Iterator<SimpleBlock> iterator, final String separator){
		// handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return StringUtils.EMPTY;
        }
        final SimpleBlock first = iterator.next();
        if (!iterator.hasNext()) {
            final String result = first.getName();
            return result;
        }

        // two or more elements
        final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first.getName());
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            final SimpleBlock bt = iterator.next();
            if (bt != null) {
                buf.append(bt.getName());
            }
        }
        return buf.toString();
	}
	
	public boolean isAllQuantifier(ParseTree quantifier){
		if(getNodeText(quantifier.getChild(0)).equals("all")){
			return true;
		}
		return false;
	}
	
	public static Collection<String> getCourses(String str){
		LinkedList<String> result = new LinkedList<String>();
		if(CourseId.isCourseName(str)){
			result.add(str);
		} else if(str.charAt(str.length() - 1) == '*') {
			int count = 0;
			int j = str.length()-2;
			while(Character.isDigit(str.charAt(j))){
				j--;
				count++;
			}
			for(int i=0; i<(int)Math.pow((double)10, (double)(3-count)); i++){
				result.add(str.substring(0, str.length()-1) + i);
			}
		}
		return result;
	}
}
