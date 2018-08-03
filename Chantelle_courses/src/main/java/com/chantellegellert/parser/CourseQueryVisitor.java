// Generated from /home/fai/chantelle/courses/CourseQuery.g4 by ANTLR 4.5.1
package com.chantellegellert.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CourseQueryParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CourseQueryVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CourseQueryParser#c}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitC(CourseQueryParser.CContext ctx);
}