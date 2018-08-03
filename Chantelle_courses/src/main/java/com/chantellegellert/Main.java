package com.chantellegellert;

import com.chantellegellert.parser.CourseQueryLexer;
import com.chantellegellert.parser.CourseQueryParser;
import com.chantellegellert.query.QueryVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {

    public static void main(String[] args) {
	    String input = "SE100-110";
        CourseQueryLexer lexer = new CourseQueryLexer(new ANTLRInputStream(input));
        CourseQueryParser parser = new CourseQueryParser(new CommonTokenStream(lexer));

        QueryVisitor visitor = new QueryVisitor();
		visitor.visit(parser.c());
    }
}
