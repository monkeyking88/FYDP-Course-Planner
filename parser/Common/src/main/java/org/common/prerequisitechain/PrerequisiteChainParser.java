package org.common.prerequisitechain;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.common.course.AntiRequisiteDataSource;
import org.common.course.Course;
import org.common.course.CourseDataSource;
import org.common.exception.ReportableException;
import org.apache.poi.hwpf.usermodel.*;

public class PrerequisiteChainParser{
	
	public static final String Tab = "    ";
	
	private static class PreReqCourseDefMissing {
		public Map<Integer, List<String>> c_map = new LinkedHashMap<>();
		public Map<Integer, Boolean> d_map = new LinkedHashMap<>();
		
		public void add(int row_c, String c_str){
			if (c_map.get(row_c) == null) {
				List<String> c_list = new ArrayList<>();
				c_list.add(c_str);
				c_map.put(row_c, c_list);
			} else {
				c_map.get(row_c).add(c_str);
			}
		}
		
		public int count(int row_c){
			if (c_map.get(row_c) == null) {
				return 0;
			}
			return c_map.get(row_c).size();
		}
		
		public void ignore(int row_c){
			d_map.put(row_c, true);
		}
		
		public boolean isIgnored(int row_c){
			return d_map.get(row_c) != null && d_map.get(row_c);
		}
		
		@Override
		public String toString(){
			String out = "";
			for (Entry<Integer, List<String>> ent : c_map.entrySet()) {
				int row_c = ent.getKey();
				String ig = this.isIgnored(row_c) ? "Yes" : "No ";
				
				out += String.format("%3d", row_c) + "         " + ig + "            ";
				for (String c_str : ent.getValue()) {
					out += c_str + " ";
				}
				out += "\n";
			}
			return out;
		}
	}
	
	private static PreReqCourseDefMissing def_not_found = new PreReqCourseDefMissing();
	private static int r_c = 0;
	
	public static void sampleRead(){
		final InputStream reqIs = CourseDataSource.class.getClassLoader().getResourceAsStream("prerequisiteChain/req_chain.doc");
		WordExtractor extractor = null;

		try{

            HWPFDocument document = new HWPFDocument(reqIs);
            extractor = new WordExtractor(document);
            String[] fileData = extractor.getParagraphText();
            for (int i = 0; i < fileData.length; i++){
                if (fileData[i] != null)
                    System.out.println(fileData[i]);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
	}
	
	public static void v_print(String s, boolean verbose){
		if (verbose) {
			System.out.println(s);
		}
	}
	
	public static List<List<String>> sampleTable(){
		return parseTable(true);
	}
	
	public static List<List<String>> genTable(){
		return parseTable(false);
	}
	
	public static List<List<String>> parseTable(boolean verbose){
		final InputStream reqIs = CourseDataSource.class.getClassLoader().getResourceAsStream("prerequisiteChain/req_chain.doc");
		List<List<String>> parseTable = new ArrayList<>();
		
		try {
			POIFSFileSystem fs = new POIFSFileSystem(reqIs);
	        HWPFDocument doc = new HWPFDocument(fs);
	 
	        Range range = doc.getRange();
	        
	        ArrayList<String> parseCell = new ArrayList<>();
	        for (int i = 0; i<range.numParagraphs(); i++) {
	            Paragraph par = range.getParagraph(i);
	            if (!par.isInTable()) {
	            	continue;
	            }
	            
	            String txt = par.text().trim();
	            if (txt.isEmpty()) {
	            	v_print("Not adding paragraph " + (i+1) + " because it's empty", verbose);
	            } else {
	            	int e_idx = txt.indexOf("Exactly");
	            	int m_idx = txt.indexOf("Minimum");
	            	int split_idx = e_idx > 0 ? e_idx : m_idx;
	            	if (split_idx > 0) {
	            		v_print("caution paragraph " + (i+1) + " has nested lines", verbose);
	            		parseCell.add(txt.substring(0, split_idx).trim());
	            		parseCell.add(txt.substring(split_idx).trim());
	            	} else {
	            		parseCell.add(txt);
	            	}
	            }
	            
	            if (par.isTableRowEnd()) {
	            	parseTable.add(parseCell);
	            	parseCell = new ArrayList<>();
	            }
	            
	            v_print("paragraph "+(i+1), verbose);
	            v_print("is in table: "+par.isInTable(), verbose);
	            v_print("is table row end: "+par.isTableRowEnd(), verbose);
	            v_print(par.text(), verbose);
	        }
		} catch (Exception e) {
			throw new ReportableException("Reading and parsing pre-req table failed", e);
		}
		return parseTable;
	}
	
	public static String p_str(List<List<String>> parseTable){
		String result = "Prerequisite Table:\n";
		int row = 1;
		for ( List<String> cell : parseTable ) {
			result += "Row " + row++ + "\n";
			for (String str : cell) {
				result += "    " + str + "\n";
			}
		}
		return result;
	}
	
	//danger of global variables is over-rated (this is a joke)
	public synchronized static String genReq(List<List<String>> parseTable) {
		def_not_found = new PreReqCourseDefMissing();
		r_c = 0;
		
		String out =
			"_Math_subjects is {ACTSC*, AMATH*, CO*, COMM*, CS*, MATH*, MATBUS*, MTHEL*, PMATH*, SE*, STAT*}\n\n" + 
			"_prerequisit_chain_len_3 is 1 of {\n\n" + 
				Tab + "_same_subject is all of {\n" + 
					Tab + Tab + "_same_subject_1 is 3 of {SUBJECT*, except _Math_subjects, except PD*, except COOP*, except WKRPT*}\n" +
					Tab + Tab + "_same_subject_2 is 1 of {SUBJECT3*, except _Math_subjects, except PD*, except COOP*, except WKRPT*}\n" +
				Tab + "}\n\n" + 
				Tab + "_prerequisit_chain_len_3_main is 1 of {\n" + 
					genReqBody(parseTable) + 
				Tab + "}" +
			"\n}\n";
		
		String meta = "/* the followings courses are not found，if the not found course is critical in its chain, the entire chain is ignored\n";
		meta += "row    is chain ignored    not found courses\n";
		meta += def_not_found.toString();
		meta += "*/\n\n\n";
		
		return meta + out;
	}
	
	private static String genReqBody(List<List<String>> parseTable) {
		String out = "";
		for (List<String> cell : parseTable) {
			String req_cell = genReqCell(cell);
			if (def_not_found.isIgnored(r_c)) {
				out += "";
			} else {
				out += "\n" + Tab + Tab + "_prereq_chain_sub_" + r_c + " is all of{\n" + req_cell  + Tab + Tab + "}\n";
			}
			r_c++;
		}
		return out;
	}
	
	//if has nested, then the single courses must be put into a nested structure too, thus composite
	private static String genReqCell(List<String> cell) {
		boolean vanilla = true;
		for (String str : cell) {
			if (str.contains("Exactly") || str.contains("Minimum")) {
				vanilla = false;
			}
		}
		
		String out = "";
		if (vanilla) {
			String in = "";
			for (String str : cell) {
				in += str + " ";
			}
			out = Tab + Tab + Tab + genReqCellVanilla(in) + "\n";
		} else {
			int l_c = 0;
			for (String str : cell) {
				out += Tab + Tab + Tab + genReqCellComposite(str, l_c++);
			}
		}
		return out;
	}
	
	private static String genReqCellVanilla(String course_str){
		List<Course> course_list = genCourseList(course_str);
		if (course_list == null || course_list.size() + def_not_found.count(r_c) < 3) {
			throw new ReportableException("Bad reprequisite cell line: " + r_c + " chain length smaller than 3, actual length: " + course_list.size() + def_not_found.count(r_c));
		}
		if (def_not_found.count(r_c) > 0) {
			def_not_found.ignore(r_c);
		}
		return genCourseReqStr(course_list);
	}
	
	private static String genReqCellComposite(String com_str, int l_c){
		String out = "_prereq_chain_sub_" + r_c + "X" + l_c + " is ";
		String p_com = "";
		
		if (com_str.contains("Exactly") || com_str.contains("Minimum")) {
			int lb_idx = com_str.indexOf("(");
			int rb_idx = com_str.indexOf(")");
			
			p_com = com_str.substring(0, lb_idx);
			
			int qualifying_minimum;
			if (p_com.contains("Minimum 0.5")) {
				p_com = p_com.replace("Minimum 0.5", "1");
				qualifying_minimum = 1;
			} else if (p_com.contains("Minimum 1.0")) {
				p_com = p_com.replace("Minimum 1.0", "2");
				qualifying_minimum = 2;
			} else if (p_com.contains("Minimum 1.5")) {
				p_com = p_com.replace("Minimum 1.5", "3");
				qualifying_minimum = 3;
			} else if (p_com.contains("Exactly 0.5")) {
				p_com = p_com.replace("Exactly 0.5", "1-1");
				qualifying_minimum = 1;
			} else if (p_com.contains("Exactly 1.0")) {
				p_com = p_com.replace("Exactly 1.0", "2-2");
				qualifying_minimum = 2;
			} else if (p_com.contains("Exactly 1.5")) {
				p_com = p_com.replace("Exactly 1.5", "3-3");
				qualifying_minimum = 3;
			} else {
				throw new ReportableException("row " + r_c + " not recognized: " + p_com);
			}
			
			//System.out.println("wo" + r_c + " " + l_c + " pa_com_str: " + com_str.substring(lb_idx + 1, rb_idx) );
			List<Course> course_list = genCourseList(com_str.substring(lb_idx + 1, rb_idx));
			if (course_list.size() < qualifying_minimum) {
				def_not_found.ignore(r_c);
			}
			p_com = p_com + "{" + genCourseReqStr(course_list) + "}\n";
		} else {
			//System.out.println("lo" + r_c + " " + l_c + " pa_com_str: " + com_str );
			int pre_missing_count = def_not_found.count(r_c);
			List<Course> course_list = genCourseList(com_str);
			if (def_not_found.count(r_c) > pre_missing_count) {
				def_not_found.ignore(r_c);
			}
			p_com = "all of {" + genCourseReqStr(course_list) + "}\n";
		}
		
		out += p_com;
		return out;
	}
	
	private static String genCourseReqStr(List<Course> course_list) {
		if (course_list.size() == 0) {
			return "";
		}
		String out = "";
		int i = 0;
		for (; i < course_list.size() - 1; i++) {
			out += course_list.get(i).getName() + ", ";
		}
		out += course_list.get(i).getName();
		return out;
	}
	
	private static List<Course> genCourseList(String course_str) {
		ArrayList<String> tokenList = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(course_str, AntiRequisiteDataSource.DELIMITERS);
		while (tokenizer.hasMoreElements()) {
			String str = tokenizer.nextToken().trim();
			if (!str.isEmpty()) {
				tokenList.add(str);
			}
		}
		//System.out.println(tokenList.toString());
		List<Course> course_list = parseRHS(tokenList);
		return course_list;
	}
	
	/**
	 * The following is very similar to the parsing code in AntiReq
	 * It is duplicated here because language in prereq chain is better formed and we want more error information, eg course not being found
	 * */
	private static List<Course> parseRHS(List<String> tokenList) {
		List<Course> courseList = new ArrayList<Course>();

		List<String> subjectList = new ArrayList<String>();
		List<String> catalogList = new ArrayList<String>();
		for (String token : tokenList) {
			if (CourseDataSource.Defs.isSubjectValid(token) || Character.isAlphabetic(token.charAt(0))) {
				// flush previous
				if (catalogList.size() > 0) {
					courseList.addAll(parsePartial(subjectList, catalogList));
					subjectList.clear();
					catalogList.clear();
				}
				subjectList.add(token);
				continue;
			}
			// its ok we add bunch of garbage here, it will be filtered anyway, efficiency is not a concern here
			if (subjectList.size() > 0) {
				catalogList.add(token);
				continue;
			}
		}

		courseList.addAll(parsePartial(subjectList, catalogList));
		// remove duplicates before returning
		return new ArrayList<Course>(new LinkedHashSet<Course>(courseList));
	}

	private static List<Course> parsePartial(List<String> subjectList, List<String> catalogList) {
		List<Course> courseList = new ArrayList<Course>();
		for (String subject : subjectList) {
			for (String cat : catalogList) {
				Course course = CourseDataSource.Defs.getDefByCourseName(subject, cat);
				// try leading numeric catalog, there are cases such as SUBJ111R not exist but SUBJ111 exists
				if (course != null) {
					courseList.add(course);
				} else {
					def_not_found.add(r_c, subject + cat);
				}
			}
		}

		// note this may contain duplicates
		return courseList;
	}
    
}