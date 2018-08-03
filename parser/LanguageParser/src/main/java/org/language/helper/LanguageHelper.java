package org.language.helper;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;
import org.common.course.Course;
import org.common.exception.SemanticErrorException;
import org.language.blocktree.BlockTree;
import org.language.codegenerator.CourseIdArray;
import org.language.symbol.Symbol;

public class LanguageHelper {
	
	public static final String EMPTY = "";
	
	public static String joinAppend(final Iterable iterable, 
									final String appendant, final String separator){
		final StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
		boolean isInit = true;
		for(Object item : iterable){
			String name;
			if(item instanceof BlockTree){
				name = ((BlockTree)item).getFullName();
			} else if(item instanceof Course){
				name = ((Course)item).getName();
			} else if (item instanceof Symbol){
				name = ((Symbol)item).getName();
			} else if (item instanceof String){
				name = (String)item;
			} else {
				throw new SemanticErrorException("iterable item must be type BlockTree, Course, or Symbol");
			}
			if(isInit){
				isInit = false;
			} else {
				buf.append(separator);
			}
			buf.append(name + appendant);
		}
		return buf.toString();
	}
	
	public static String join(final Iterable iterable, final String separator) {
        return joinAppend(iterable, "", separator);
    }
	
	public static int sumOfUnits(Collection<Course> courses){
		int sum = 0;
		for(Course course : courses){
			sum += course.getUnits().divide(new BigDecimal(0.25)).intValue();
		}
		return sum;
	}
	
	
	public static Collection<LinkedList<Integer>> getAllSubsetsOfSizeK(int upperBound, int k){
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
	
	public static Set<String> getIntersection(Map<String, CourseIdArray> listDefs1, Map<String, CourseIdArray> listDefs2){
		Set<String> intersection = new HashSet<String>();
		for(String key : listDefs1.keySet()){
			if(listDefs2.containsKey(key)){
				intersection.add(key);
			}
		}
		return intersection;
	}
	
	public static String getNodeText(ParseTree node, Parser parser){
		return getNodeTextbyDepth(node, parser, 0);
	}
	public static String getNodeTextbyDepth(ParseTree node, Parser parser, int depth){
		for(int i=0; i<depth; i++){
			node = node.getChild(0);
		}
		return Trees.getNodeText(node, parser);
	}
}