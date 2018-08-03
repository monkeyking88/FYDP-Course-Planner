package org.language.prettyprinter;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;
import org.language.main.TestListener;
import org.language.main.TestParser;
import org.language.main.TestParser.DegreeContext;

public class PrettyPrinterTree extends TestParser.InitContext{
	
	public DegreeContext degree() {
		return getRuleContext(DegreeContext.class,0);
	}
	
	public PrettyPrinterTree(ParserRuleContext parent, int invokingState) {
		super(parent, invokingState);
	}
	
	public PrettyPrinterTree(ParserRuleContext initContext) {
		super(initContext, initContext.invokingState);
	}
	
	@Override public int getRuleIndex() { return TestParser.RULE_init; }
	@Override
	public void enterRule(ParseTreeListener listener) {
		if ( listener instanceof TestListener ) ((TestListener)listener).enterInit(this);
	}
	@Override
	public void exitRule(ParseTreeListener listener) {
		if ( listener instanceof TestListener ) ((TestListener)listener).exitInit(this);
	}
	
	@Override
	public <T> T accept(ParseTreeVisitor<? extends T> visitor) { return visitor.visit(this); }
}