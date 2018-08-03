package org.language.codegenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;
import org.common.course.Course;
import org.common.course.CourseDataSource;
import org.common.course.utilities.CourseUtilities;
import org.common.exception.CourseNotFoundException;
import org.common.exception.ReportableException;
import org.common.exception.SemanticErrorException;
import org.common.helper.Helper;
import org.common.transcript.Transcript;
import org.language.blocktree.BlockTree;
import org.language.blocktree.BlockTree.Discriminator;
import org.language.blocktree.BlockTree.Quantifier;
import org.language.blocktree.ConditionBlockTrunk;
import org.language.blocktree.CourseBlockLeaf;
import org.language.blocktree.CourseBlockTrunk;
import org.language.blocktree.InfeasibleBlockTrunk;
import org.language.blocktree.UnitBlockLeaf;
import org.language.blocktree.UnitBlockTrunk;
import org.language.codegenerator.CodeGeneratorVisitor.ArrayType;
import org.language.helper.LanguageHelper;
import org.language.main.TestParser;
import org.language.main.LanguageParser;
import org.language.symbol.MainSubjectSymbol;
import org.language.symbol.SubjectSymbol;
import org.language.symbol.Symbol;

public class ParseTreeToBlockTree {
	
	/*
	public enum ArrayType {
		BLOCK, LIST_DEF
	}
	*/
	
	private String degree;
	private Parser parser;
	public Set<Course> coursesInTranscript = new HashSet<Course>();
	private Map<String, CourseIdArray> listDefs = new HashMap<String, CourseIdArray>();
	private Map<Course, Course> courseOverrideMap = new HashMap<Course, Course>();
	private Map<String, CourseIdArray> IDoverrideMap = new HashMap<String, CourseIdArray>();
	private ConditionBlockTrunk root;
	private int subjectID = 0;
	private MainSubjectSymbol mainSubjectSymbol;
	
	/** 
	 * blockCount: count of nameless blocks, used to assign names of them
	 */
	private int blockCount = 0;
	
	public ParseTreeToBlockTree(String degree, Parser parser, InputStream transcriptIs) 
			throws Exception {
		this.degree = degree;
		this.parser = parser;
		// read transcript
		Scanner transcriptScanner = new Scanner(transcriptIs);
		try {
			//transcriptScanner.nextLine();
			String courseMeta = transcriptScanner.nextLine();
			for(Integer transcriptCourseId : CourseUtilities.CourseMetaToIds(courseMeta)){
				Course transcriptCourse = CourseDataSource.Defs.getDefById(transcriptCourseId);
				//System.err.println("\ntranscriptCourseId:" + transcriptCourseId);
				coursesInTranscript.add(transcriptCourse);
			}
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("transcript input must has at least 2 lines");
		} catch (NullPointerException e) {
			throw e;
		} finally {
			Helper.close(transcriptScanner);
		}
		
	}
	
	public ParseTreeToBlockTree(String degree, Parser parser, Transcript transcript) 
			throws Exception {
		this.degree = degree;
		this.parser = parser;
		// read transcript
		for(Integer transcriptCourseId : transcript.getCourseIds()){
			Course transcriptCourse = CourseDataSource.Defs.getDefById(transcriptCourseId);
			// System.err.println("\ntranscriptCourseId:" + transcriptCourseId);
			coursesInTranscript.add(transcriptCourse);
		}
		mainSubjectSymbol = new MainSubjectSymbol("same_subject_", coursesInTranscript);
	}
	
	public ParseTreeToBlockTree(String degree, Parser parser, Set<Course> coursesInTranscript) {
		this.degree = degree;
		this.parser = parser;
		this.coursesInTranscript = coursesInTranscript;
		mainSubjectSymbol = new MainSubjectSymbol("same_subject_", coursesInTranscript);
	}
	
	public ParseTreeToBlockTree(String degree, Parser parser, Set<Course> coursesInTranscript, 
			Map<String, CourseIdArray> listDefs, Map<Course, Course> courseOverrideMap) {
		this.degree = degree;
		this.parser = parser;
		this.coursesInTranscript = coursesInTranscript;
		this.listDefs = listDefs;
		this.courseOverrideMap = courseOverrideMap;
		mainSubjectSymbol = new MainSubjectSymbol("same_subject_", coursesInTranscript);
	}
	
	public BlockTree visit(ParseTree tree) throws Exception {
		if(getNodeText(tree).equals("init")){
			tree = tree.getChild(0);
		} else {
			while(!(tree instanceof TestParser.DegreeContext)){
				tree = tree.getParent();
			}
		}
		return visitDegree(tree);
	}
	
	@Deprecated
	public BlockTreeWithListDefs generateBlockTreeWithListDef(ParseTree tree) throws Exception {
		if(getNodeText(tree).equals("init")){
			tree = tree.getChild(0);
		} else {
			while(!(tree instanceof TestParser.DegreeContext)){
				tree = tree.getParent();
			}
		}
		return new BlockTreeWithListDefs(visitDegree(tree), listDefs);
	}
	
	public Map<String, CourseIdArray> generateListDefs(ParseTree tree) throws Exception {
		if(getNodeText(tree).equals("init")){
			tree = tree.getChild(0);
		} else {
			while(!(tree instanceof TestParser.DegreeContext)){
				tree = tree.getParent();
			}
		}
		visitImport_statementsForListDefs(tree.getChild(0));
		visitLists_defs(tree.getChild(1));
		return this.listDefs;
	}
	
	public BlockTree generateBlockTree(ParseTree tree) throws Exception {
		if(getNodeText(tree).equals("init")){
			tree = tree.getChild(0);
		} else {
			while(!(tree instanceof TestParser.DegreeContext)){
				tree = tree.getParent();
			}
		}

		Collection<BlockTree> importedBlockTrees = visitImport_statements(tree.getChild(0));
		
		Collection<BlockTree> children = visitReqs(tree.getChild(2));
		
		children.addAll(importedBlockTrees);
		
		root = new ConditionBlockTrunk(this.degree, "_result",
				Discriminator.NONE, Quantifier.getAllOfQuantifier(), children);
		root.addCandidateCoursesInTranscript(coursesInTranscript);
	
		return root;
	}
	
	/** degree  :  list_defs reqs ;
	 */
	public BlockTree visitDegree(ParseTree tree) throws Exception {
		
		// visit import_statements list_defs and reqs
		visitImport_statementsForListDefs(tree.getChild(0));
		
		visitLists_defs(tree.getChild(1));
		
		visitOverride_statements(tree.getChild(3));
		
		Collection<BlockTree> importedBlockTrees = visitImport_statements(tree.getChild(0));
		
		Collection<BlockTree> children = visitReqs(tree.getChild(2));
		
		children.addAll(importedBlockTrees);
		
		root = new ConditionBlockTrunk(this.degree, "_result",
				Discriminator.NONE, Quantifier.getAllOfQuantifier(), children);
		root.addCandidateCoursesInTranscript(coursesInTranscript);
		root.setOverrideMap(new OverrideMap(courseOverrideMap, IDoverrideMap));
		
		return root;
	}
	
	/**
	 * override_statements	: 	// can be empty
							| override_statement override_statements;
	 */
	public void visitOverride_statements(ParseTree tree){
		while(tree.getChildCount() > 0){
			visitOverride_statement(tree.getChild(0));
			tree = tree.getChild(1);
		}
	}
	
	public void visitOverride_statement(ParseTree tree){
		if(tree.getChildCount() == 5){
			String ID = LanguageHelper.getNodeText(tree.getChild(0), parser);
			//System.out.println(ID);
			//throw new SemanticErrorException("test");
			CourseIdArray overriddenArray = visitArray(tree.getChild(4));
			listDefs.put(ID + "_old", listDefs.get(ID));
			if(overriddenArray.removePlus(new CourseId(ID))){
				// System.out.println("hit");
				overriddenArray.addPlus(new CourseId(ID + "_old"));
			}
			if(overriddenArray.removeMinus(new CourseId(ID))){
				// System.out.println("hit");
				overriddenArray.addMinus(new CourseId(ID + "_old"));
			}
			listDefs.put(ID, overriddenArray);
			IDoverrideMap.put(ID, overriddenArray);
		} else if (tree.getChildCount() == 7){
			Course overdee = CourseDataSource.Defs.getDefByCourseName(LanguageHelper.getNodeText(tree.getChild(0), parser));
			Course overder = CourseDataSource.Defs.getDefByCourseName(LanguageHelper.getNodeText(tree.getChild(5), parser));
			if (overdee == null) {
				throw new CourseNotFoundException(LanguageHelper.getNodeText(tree.getChild(0), parser) + " being overriden");
			}
			if (overder == null) {
				throw new CourseNotFoundException(LanguageHelper.getNodeText(tree.getChild(5), parser) + " overriding " + overdee.getName());
			}
			courseOverrideMap.put(overdee, overder);
		} else {
			throw new SemanticErrorException("undefined override statement");
		}
	}
	
	/**
	 * 
	 * import_statements	: 	// can be empty
     *               		| import_statement import_statements;
	 */
	public Collection<BlockTree> visitImport_statements(ParseTree tree) throws Exception {
		Collection<BlockTree> importedBlockTrees = new HashSet<BlockTree>();
		while(tree.getChildCount() > 0){
			BlockTree importedBlockTree = visitImport_statement(tree.getChild(0));
			importedBlockTrees.add(importedBlockTree);
			tree = tree.getChild(1);
		}
		return importedBlockTrees;
	}
	
	public void visitImport_statementsForListDefs(ParseTree tree) throws Exception {
		while(tree.getChildCount() > 0){
			Map<String, CourseIdArray> importedListDefs = visitImport_statementForListDefs(tree.getChild(0));
			Set<String> keyIntersection = LanguageHelper.getIntersection(listDefs, importedListDefs);
			if(keyIntersection.isEmpty()){
				listDefs.putAll(importedListDefs);
			} else {
				throw new SemanticErrorException("duplicate ID(s) " + keyIntersection.toString() +
			" is found.");
			}
			tree = tree.getChild(1);
		}
	}
	
	/**
	 * import_statement : IMPORT FILE;
	 */
	public BlockTree visitImport_statement(ParseTree tree) throws Exception{
		String relativePath = LanguageHelper.getNodeText(tree.getChild(1), this.parser);
		File absolutePath = new File("requirements/plans/" + relativePath + ".txt");
		if (!absolutePath.exists() || absolutePath.isDirectory()) {
			throw new ReportableException("Cannot find " + relativePath + " to be imported");
		}
		InputStream importedFileIs = new FileInputStream(absolutePath);
		BlockTree importedBlockTree = LanguageParser.generateBlockTree(relativePath.replace("/", "_"), importedFileIs, 
				this.coursesInTranscript, this.listDefs, this.courseOverrideMap);
		
		return importedBlockTree;
	}
	
	public Map<String, CourseIdArray> visitImport_statementForListDefs(ParseTree tree) throws Exception{
		String relativePath = LanguageHelper.getNodeText(tree.getChild(1), this.parser);
		File absolutePath = new File("requirements/plans/" + relativePath + ".txt");
		if (!absolutePath.exists() || absolutePath.isDirectory()) {
			throw new ReportableException("Cannot find " + relativePath + " to be imported");
		}
		InputStream importedFileIs = new FileInputStream(absolutePath);
		Map<String, CourseIdArray> importedListDefs = LanguageParser.generateListDefs(relativePath.replace("/", "_"), importedFileIs, this.coursesInTranscript);
		
		return importedListDefs;
	}
	
	/** reqs	: 
	/*			| req reqs;
	 */
	public Collection<BlockTree> visitReqs(ParseTree tree){
		Collection<BlockTree> reqs = new HashSet<BlockTree>();
		while(tree.getChildCount() > 0){
			reqs.add(visitReq(tree.getChild(0)));
			tree = tree.getChild(1);
		}
		return reqs;
	}
	
	/** req : condition_req
	/*		| unit_req
	/*		| course_req;
	 */
	public BlockTree visitReq(ParseTree tree){
		BlockTree req;
		if(getNodeText(tree.getChild(0)).equals("condition_req")){
			req = visitCondition_req(tree.getChild(0));
		} else if (getNodeText(tree.getChild(0)).equals("unit_req")){
			req = visitUnit_req(tree.getChild(0));
		} else if (getNodeText(tree.getChild(0)).equals("course_req")){
			req = visitCourse_req(tree.getChild(0));
		} else {
			throw new SemanticErrorException("Unrecoginzed req type: " + getNodeText(tree.getChild(0)));
		}
		return req;
	}
	
	/**	course_req : ID IS course_expr
	/*				| course_expr;
	 */
	public BlockTree visitCourse_req(ParseTree tree){
		String ID;
		ParseTree expr;
		// course_req : ID IS course_expr
		if(tree.getChildCount() == 3){
			ID = getNodeText(tree.getChild(0));
			expr = tree.getChild(2);
		// course_req : course_expr
		} else {
			ID = getNextBlockID();
			expr = tree.getChild(0);
		}
		return visitCourse_expr(expr, ID);
	}

	/**	unit_req : ID IS unit_expr
	/*			| unit_expr;
	 */
	public BlockTree visitUnit_req(ParseTree tree){
		String ID;
		ParseTree expr;
		// unit_req : ID IS unit_expr
		if(tree.getChildCount() == 3){
			ID = getNodeText(tree.getChild(0));
			expr = tree.getChild(2);
		// unit_req : unit_expr
		} else {
			ID = getNextBlockID();
			expr = tree.getChild(0);
		}
		return visitUnit_expr(expr, ID);
	}
	
	/**	condition_req : ID IS expr
	/*					| expr;
	 */
	public BlockTree visitCondition_req(ParseTree tree){
		String ID;
		ParseTree expr;
		// condition_req : ID IS expr
		if(tree.getChildCount() == 3){
			ID = getNodeText(tree.getChild(0));
			expr = tree.getChild(2);
		// condition_req : expr
		} else {
			ID = getNextBlockID();
			expr = tree.getChild(0);
		}
		return visitExpr(expr, ID);
	}
	
	
	/** course_expr	: discriminator course_quantifier COURSES OF LBRACE course_reqs RBRACE
	 * 				| discriminator course_quantifier COURSES OF array;
	 */
	public BlockTree visitCourse_expr(ParseTree tree, String ID) {
		
		Discriminator discriminator;
		if(tree.getChild(0).getChildCount() != 0){
			discriminator = Discriminator.UNIQUE;
		} else {
			discriminator = Discriminator.NONE;
		}
		
		Quantifier quantifier = visitQuantifier(tree.getChild(1));
		
		// course_expr	: discriminator course_quantifier COURSES OF array
		if(tree.getChildCount() == 5){
			CourseIdArray courseIdArray = visitArray(tree.getChild(4));
			List<Symbol> inPlusSymbols = new LinkedList<Symbol>();
			List<Symbol> inMinusSymbols = new LinkedList<Symbol>();
			Collection<Course> candidateCoursesInTranscript = 
					replaceOverriddenCourses(expandArray(courseIdArray, inPlusSymbols, inMinusSymbols, ArrayType.BLOCK));
			Collection<Course> candidateCoursesInDatabase = new HashSet<Course>();
			candidateCoursesInDatabase.addAll(candidateCoursesInTranscript);
			boolean isAllCoursesInTranscript = !candidateCoursesInTranscript.retainAll(coursesInTranscript);
			if(quantifier.isAllOf() && !isAllCoursesInTranscript){
				return new InfeasibleBlockTrunk(this.degree, ID, discriminator, quantifier);
			}
			return new CourseBlockLeaf(
					this.degree, 
					ID, 
					discriminator, 
					quantifier, 
					inPlusSymbols,
					inMinusSymbols,
					candidateCoursesInTranscript,
					candidateCoursesInDatabase);
		// course_expr	: discriminator course_quantifie[r COURSES OF LBRACE course_reqs RBRACE
		} else {
			Collection<BlockTree> children = visitReqs(tree.getChild(5));
			return new CourseBlockTrunk(this.degree, ID, discriminator, quantifier, children);
		}
	}
	
	/**	unit_expr	: discriminator dec_quantifier UNITS OF LBRACE unit_reqs RBRACE
	/*				| discriminator dec_quantifier UNITS OF array;
	 */
	public BlockTree visitUnit_expr(ParseTree tree, String ID){
		
		Discriminator discriminator;
		if(tree.getChild(0).getChildCount() != 0){
			discriminator = Discriminator.UNIQUE;
		} else {
			discriminator = Discriminator.NONE;
		}
		
		Quantifier quantifier = visitQuantifier(tree.getChild(1));
		
		// unit_expr	: discriminator dec_quantifier UNITS OF array
		if(tree.getChildCount() == 5){
			CourseIdArray courseIdArray = visitArray(tree.getChild(4));
			List<Symbol> inPlusSymbols = new LinkedList<Symbol>();
			List<Symbol> inMinusSymbols = new LinkedList<Symbol>();
			Collection<Course> candidateCoursesInTranscript = 
					replaceOverriddenCourses(expandArray(courseIdArray, inPlusSymbols, inMinusSymbols, ArrayType.BLOCK));
			Collection<Course> candidateCoursesInDatabase = new HashSet<Course>();
			candidateCoursesInDatabase.addAll(candidateCoursesInTranscript);
			boolean isAllCoursesInTranscript = !candidateCoursesInTranscript.retainAll(coursesInTranscript);
			if(quantifier.isAllOf() && !isAllCoursesInTranscript){
				return new InfeasibleBlockTrunk(this.degree, ID, discriminator, quantifier);
			}
			return new UnitBlockLeaf(
					this.degree, 
					ID, 
					discriminator, 
					quantifier, 
					inPlusSymbols,
					inMinusSymbols,
					candidateCoursesInTranscript, 
					candidateCoursesInDatabase);
		// unit_expr	: discriminator dec_quantifier UNITS OF LBRACE unit_reqs RBRACE
		} else {
			Collection<BlockTree> children = visitReqs(tree.getChild(5));
			return new UnitBlockTrunk(this.degree, ID, discriminator, quantifier, children);
		}
	}
	
	/**	expr	: discriminator quantifier OF LBRACE reqs RBRACE
	/*			| discriminator quantifier OF array;
	 */
	public BlockTree visitExpr(ParseTree tree, String ID){
		
		Discriminator discriminator;
		if(tree.getChild(0).getChildCount() != 0){
			discriminator = Discriminator.UNIQUE;
		} else {
			discriminator = Discriminator.NONE;
		}
		
		Quantifier quantifier = visitQuantifier(tree.getChild(1));
		
		// expr	: discriminator quantifier OF array;
		if(tree.getChildCount() == 4){
			List<Symbol> inPlusSymbols = new LinkedList<Symbol>();
			List<Symbol> inMinusSymbols = new LinkedList<Symbol>();
			CourseIdArray courseIdArray = visitArray(tree.getChild(3));
			Collection<Course> candidateCoursesInTranscript = 
					replaceOverriddenCourses(expandArray(courseIdArray, inPlusSymbols, inMinusSymbols, ArrayType.BLOCK));
			Collection<Course> candidateCoursesInDatabase = new HashSet<Course>();
			candidateCoursesInDatabase.addAll(candidateCoursesInTranscript);
			boolean isAllCoursesInTranscript = !candidateCoursesInTranscript.retainAll(coursesInTranscript);
			if(quantifier.isAllOf() && !isAllCoursesInTranscript){
				return new InfeasibleBlockTrunk(this.degree, ID, discriminator, quantifier);
			}
			return new CourseBlockLeaf(
					this.degree, 
					ID, 
					discriminator, 
					quantifier, 
					inPlusSymbols,
					inMinusSymbols,
					candidateCoursesInTranscript, 
					candidateCoursesInDatabase);
		// expr	: discriminator quantifier OF LBRACE reqs RBRACE
		} else {
			Collection<BlockTree> children = visitReqs(tree.getChild(4));
			return new ConditionBlockTrunk(this.degree, ID, discriminator, quantifier, children);
		}
	}
	
	/**	quantifier	: INT
	 *				| ALL
	 *				| INT DASH INT;
	 * 
	 * or 
	 * 
	 *  quantifier	: ALL
	 *  			| DEC
	 *  			| DEC DASH DEC
	 */
	public Quantifier visitQuantifier(ParseTree tree){
		//	quantifier	: ALL
		// or
		// 	dec_quantifier	: ALL
		if(getNodeText(tree.getChild(0)).equals("all")){
			return Quantifier.getAllOfQuantifier();
		} else if (getNodeText(tree.getChild(0)).contains(".")){
			// dec_quantifier	: DEC
			if(tree.getChildCount() == 1){
				int lowerBound = (int)(Double.parseDouble(getNodeText(tree.getChild(0)))/0.25);
				return new Quantifier(lowerBound);
			// dec_quantifier	: DEC DASH DEC
			} else {
				int lowerBound = (int)(Double.parseDouble(getNodeText(tree.getChild(0)))/0.25);
				int upperBound = (int)(Double.parseDouble(getNodeText(tree.getChild(2)))/0.25);
				return new Quantifier(lowerBound, upperBound);
			}
		} else {
			// quantifier	: INT
			if(tree.getChildCount() == 1){
				int lowerBound = Integer.parseInt(getNodeText(tree.getChild(0)));
				return new Quantifier(lowerBound);
			// quantifier	: INT DASH INT
			} else {
				int lowerBound = Integer.parseInt(getNodeText(tree.getChild(0)));
				int upperBound = Integer.parseInt(getNodeText(tree.getChild(2)));
				return new Quantifier(lowerBound, upperBound);
			}
		}
	}
	
	/** list_defs 	: 	//can be empty
	 * 					| list_def list_defs;
	 */
	private void visitLists_defs(ParseTree tree){
		while(tree.getChildCount() > 0){
			visitList_def(tree.getChild(0));
			tree = tree.getChild(1);
		}
	}
	
	/** list_def	: ID IS array;	
	 */
	public void visitList_def(ParseTree tree){
		String ID = getNodeText(tree.getChild(0));
		CourseIdArray courseIdArray = visitArray(tree.getChild(2));
		if(listDefs.containsKey(ID)){
			throw new SemanticErrorException("duplicate ID(s) " + ID +
					" is found."); 
		}
		listDefs.put(ID, courseIdArray);
	}
	
	/** array : LBRACE course_id_list RBRACE;
	 */
	public CourseIdArray visitArray(ParseTree tree){
		CourseIdArray courseIdArray = getArray(tree);
		// TODO: ensure acyclicity of list_defs 
		return courseIdArray;
	}
	public CourseIdArray getArray(ParseTree tree){
		CourseIdArray courseIdArray = new CourseIdArray();
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
	
	public Collection<Course> expandArray(CourseIdArray courseIdArray, 
			Collection<Symbol> inPlusSymbols, Collection<Symbol> inMinusSymbols,
			ArrayType arrayType){
		Collection<Course> plusArray = new HashSet<Course>();
		Collection<Course> minusArray = new HashSet<Course>();
		
		PlusArrayLoop: for(CourseId plusCourseId : courseIdArray.getPlusArray()){
			try {
				Collection<Course> matchedCourses;
				switch(plusCourseId.getType()){
					case SUBJECT_CATALOG_WILDCARD:
					case SUBJECT_CATALOG_RANGE:
						Symbol symbol = new SubjectSymbol(mainSubjectSymbol.getName() + subjectID++, plusCourseId, mainSubjectSymbol);
						inPlusSymbols.add(symbol);
						break;
					case CATALOG_WILDCARD:
						/*
						System.err.println("Full Name is :" + plusCourseId.getFullName());
						System.err.println("Subject is :" + plusCourseId.getSubject());
						System.err.println("Subject is :" + plusCourseId.getCatalogBegin());
						*/
						matchedCourses = CourseDataSource.Defs.getDefByCatalogWildCard(
								plusCourseId.getSubject(), 
								plusCourseId.getCatalogBegin());
						plusArray.addAll(matchedCourses);
						break;
					case CATALOG_RANGE:
						matchedCourses = CourseDataSource.Defs.getDefByCatalogRange(
										plusCourseId.getSubject(), 
										plusCourseId.getCatalogBegin(), 
										plusCourseId.getCatalogEnd());
						plusArray.addAll(matchedCourses);
						break;
					case ANY:
						plusArray.clear();
						plusArray.addAll(coursesInTranscript);
						break PlusArrayLoop;
					case ID:
						// @TODO: allow ID in list_def
						/*
						if(arrayType.equals(ArrayType.LIST_DEF)){
							throw new SemanticErrorException("Reference of "+ 
																plusCourseId.getFullName() + 
																" in " + 
																courseIdArray.getId()+
																"\nSorry, we do not support "
																+ "reference of list_def in a list_def");
						}
						*/
						// @TODO: expand wildcard/range in ID
						CourseIdArray listDef = listDefs.get(plusCourseId.getFullName());
						if(listDef == null){
							throw new SemanticErrorException("ID " + plusCourseId.getFullName() + " not found.\n"
									+ "You might have used an requirement id."
									+ "Note that ONLY list_Def ID is available for reference.");
						}
						Collection<Course> additive = expandArray(listDefs.get(plusCourseId.getFullName()), inPlusSymbols, inMinusSymbols, ArrayType.LIST_DEF);
						plusArray.addAll(additive);
						break;
					case COURSENAME: 
						Course matchedCouse = CourseDataSource.Defs.getDefByCourseName(
								plusCourseId.getFullName());
						if(matchedCouse == null){
							throw new CourseNotFoundException(plusCourseId.getFullName());
						}
						plusArray.add(matchedCouse);
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
		
		MinusArrayLoop: for(CourseId minusCourseId : courseIdArray.getMinusArray()){
			try{
				Collection<Course> matchedCourses;
				switch(minusCourseId.getType()){
					case SUBJECT_CATALOG_WILDCARD:
					case SUBJECT_CATALOG_RANGE:
						Symbol symbol = new SubjectSymbol(mainSubjectSymbol.getName() + subjectID++, minusCourseId, mainSubjectSymbol);
						inMinusSymbols.add(symbol);
						break;
					case CATALOG_WILDCARD:
						matchedCourses = CourseDataSource.Defs.getDefByCatalogWildCard(
								minusCourseId.getSubject(), 
								minusCourseId.getCatalogBegin());
						minusArray.addAll(matchedCourses);
						break;
					case CATALOG_RANGE:
						matchedCourses = CourseDataSource.Defs.getDefByCatalogRange(
										minusCourseId.getSubject(), 
										minusCourseId.getCatalogBegin(), 
										minusCourseId.getCatalogEnd());
						minusArray.addAll(matchedCourses);
						break;
					case ANY:
						plusArray.clear();
						minusArray.clear();
						plusArray.add(Course.NONE);
						break MinusArrayLoop;
					case ID:
						// @TODO: expand wildcard/range in ID
						// @TODO: allow ID in list_def
						/*
						if(arrayType.equals(ArrayType.LIST_DEF)){
							throw new SemanticErrorException("Reference of "+ 
																minusCourseId.getFullName() + 
																" in " + 
																courseIdArray.getId()+
																"\nSorry, we do not support "
																+ "reference of list_def in a list_def");
						}
						*/
						CourseIdArray listDef = listDefs.get(minusCourseId.getFullName());
						if(listDef == null){
							throw new SemanticErrorException("ID " + minusCourseId.getFullName() + " not found.\n"
									+ "You might have used an requirement id."
									+ "Note that ONLY list_Def ID is available for reference.");
						}
						Collection<Course> subtrahend = expandArray(listDef, inPlusSymbols, inMinusSymbols, ArrayType.LIST_DEF);
						minusArray.addAll(subtrahend);
						break;
					case COURSENAME: 
						Course matchedCouse = CourseDataSource.Defs.getDefByCourseName(
								minusCourseId.getFullName());
						if(matchedCouse == null){
							throw new CourseNotFoundException(minusCourseId.getFullName() + " is not found in CourseDataSource");
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
		for(Symbol symbol : inPlusSymbols){
			if(symbol instanceof SubjectSymbol){
				SubjectSymbol subjectSymbol = (SubjectSymbol) symbol;
				subjectSymbol.except(minusArray);
			}
		}
		/*
		for(Symbol symbol : inPlusSymbols){
			plusArray.addAll(symbol.getCourses());
		}
		for(Symbol symbol : inMinusSymbols){
			plusArray.addAll(symbol.getCourses());
		}
		*/
		return plusArray;
	}
	
	/* ************************************************************************** */
	
	// Helper Functions
	public Collection<Course> replaceOverriddenCourses(Collection<Course> courses){
		Collection<Course> coursesToBeOverridden = new LinkedList<Course>();
		Collection<Course> coursesToReplace = new LinkedList<Course>();
		for(Map.Entry<Course, Course> entry : courseOverrideMap.entrySet()){
			if(courses.contains(entry.getKey())){
				coursesToBeOverridden.add(entry.getKey());
				coursesToReplace.add(entry.getValue());
			}
		}
		courses.removeAll(coursesToBeOverridden);
		courses.addAll(coursesToReplace);
		return courses;
	}
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
}
