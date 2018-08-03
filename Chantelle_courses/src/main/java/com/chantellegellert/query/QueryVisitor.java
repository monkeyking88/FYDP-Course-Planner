package com.chantellegellert.query;

import com.chantellegellert.parser.CourseQueryBaseVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

public class QueryVisitor extends CourseQueryBaseVisitor {
    @Override
    public void visit(org.antlr.v4.runtime.tree.ParseTree tree) {
        super.visit(tree);

        // NOTE!!!
        // accessing children directly ONLY because this particular grammar is not recursive.
        // for recursive grammars or anything more complex than this do a specific visitor per branch type!
        ParseTree subject = tree.getChild(0);
        String course = subject.getText();


        int start = Integer.parseInt(tree.getChild(1).getText());
        int end = Integer.parseInt(tree.getChild(3).getText());

        for(int i = start; i <= end; i++) {
            System.out.println(course+i);
        }

    }

}
