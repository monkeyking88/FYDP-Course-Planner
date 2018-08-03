package org.solver.test;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import kodkod.ast.*;
import kodkod.ast.operator.*;
import kodkod.instance.*;
import kodkod.util.nodes.PrettyPrinter;
import kodkod.engine.*;
import kodkod.engine.satlab.SATFactory;
import kodkod.engine.config.Options;

public class AlloyTest {

	@Test
	public void testSlow() {
		Relation x0 = Relation.unary("Int/min");
		Relation x1 = Relation.unary("Int/zero");
		Relation x2 = Relation.unary("Int/max");
		Relation x3 = Relation.nary("Int/next", 2);
		Relation x4 = Relation.unary("seq/Int");
		Relation x5 = Relation.unary("String");
		Relation x6 = Relation.unary("this/A");
		Relation x7 = Relation.unary("this/Relation");
		Relation x8 = Relation.nary("this/Relation.r", 5);

		List<String> atomlist = Arrays.asList("-1", "-2", "-3", "-4", "-5",
				"-6", "-7", "-8", "0", "1", "2", "3", "4", "5", "6", "7",
				"unused0", "unused1");

		Universe universe = new Universe(atomlist);
		TupleFactory factory = universe.factory();
		Bounds bounds = new Bounds(universe);

		TupleSet x0_upper = factory.noneOf(1);
		x0_upper.add(factory.tuple("-8"));
		bounds.boundExactly(x0, x0_upper);

		TupleSet x1_upper = factory.noneOf(1);
		x1_upper.add(factory.tuple("0"));
		bounds.boundExactly(x1, x1_upper);

		TupleSet x2_upper = factory.noneOf(1);
		x2_upper.add(factory.tuple("7"));
		bounds.boundExactly(x2, x2_upper);

		TupleSet x3_upper = factory.noneOf(2);
		x3_upper.add(factory.tuple("-8").product(factory.tuple("-7")));
		x3_upper.add(factory.tuple("-7").product(factory.tuple("-6")));
		x3_upper.add(factory.tuple("-6").product(factory.tuple("-5")));
		x3_upper.add(factory.tuple("-5").product(factory.tuple("-4")));
		x3_upper.add(factory.tuple("-4").product(factory.tuple("-3")));
		x3_upper.add(factory.tuple("-3").product(factory.tuple("-2")));
		x3_upper.add(factory.tuple("-2").product(factory.tuple("-1")));
		x3_upper.add(factory.tuple("-1").product(factory.tuple("0")));
		x3_upper.add(factory.tuple("0").product(factory.tuple("1")));
		x3_upper.add(factory.tuple("1").product(factory.tuple("2")));
		x3_upper.add(factory.tuple("2").product(factory.tuple("3")));
		x3_upper.add(factory.tuple("3").product(factory.tuple("4")));
		x3_upper.add(factory.tuple("4").product(factory.tuple("5")));
		x3_upper.add(factory.tuple("5").product(factory.tuple("6")));
		x3_upper.add(factory.tuple("6").product(factory.tuple("7")));
		bounds.boundExactly(x3, x3_upper);

		TupleSet x4_upper = factory.noneOf(1);
		x4_upper.add(factory.tuple("0"));
		bounds.boundExactly(x4, x4_upper);

		TupleSet x5_upper = factory.noneOf(1);
		bounds.boundExactly(x5, x5_upper);

		TupleSet x6_upper = factory.noneOf(1);
		x6_upper.add(factory.tuple("unused0"));
		bounds.bound(x6, x6_upper);

		TupleSet x7_upper = factory.noneOf(1);
		x7_upper.add(factory.tuple("unused1"));
		bounds.bound(x7, x7_upper);

		TupleSet x8_upper = factory.noneOf(5);
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("unused0"))
				.product(factory.tuple("unused0"))
				.product(factory.tuple("unused0"))
				.product(factory.tuple("unused0")));
		bounds.bound(x8, x8_upper);

		bounds.boundExactly(-8,
				factory.range(factory.tuple("-8"), factory.tuple("-8")));
		bounds.boundExactly(-7,
				factory.range(factory.tuple("-7"), factory.tuple("-7")));
		bounds.boundExactly(-6,
				factory.range(factory.tuple("-6"), factory.tuple("-6")));
		bounds.boundExactly(-5,
				factory.range(factory.tuple("-5"), factory.tuple("-5")));
		bounds.boundExactly(-4,
				factory.range(factory.tuple("-4"), factory.tuple("-4")));
		bounds.boundExactly(-3,
				factory.range(factory.tuple("-3"), factory.tuple("-3")));
		bounds.boundExactly(-2,
				factory.range(factory.tuple("-2"), factory.tuple("-2")));
		bounds.boundExactly(-1,
				factory.range(factory.tuple("-1"), factory.tuple("-1")));
		bounds.boundExactly(0,
				factory.range(factory.tuple("0"), factory.tuple("0")));
		bounds.boundExactly(1,
				factory.range(factory.tuple("1"), factory.tuple("1")));
		bounds.boundExactly(2,
				factory.range(factory.tuple("2"), factory.tuple("2")));
		bounds.boundExactly(3,
				factory.range(factory.tuple("3"), factory.tuple("3")));
		bounds.boundExactly(4,
				factory.range(factory.tuple("4"), factory.tuple("4")));
		bounds.boundExactly(5,
				factory.range(factory.tuple("5"), factory.tuple("5")));
		bounds.boundExactly(6,
				factory.range(factory.tuple("6"), factory.tuple("6")));
		bounds.boundExactly(7,
				factory.range(factory.tuple("7"), factory.tuple("7")));

		Variable x12 = Variable.unary("this");
		Decls x11 = x12.oneOf(x7);
		Expression x16 = x12.join(x8);
		Expression x19 = x6.product(x6);
		Expression x18 = x6.product(x19);
		Expression x17 = x6.product(x18);
		Formula x15 = x16.in(x17);
		Variable x22 = Variable.unary("x22");
		Decls x21 = x22.oneOf(x6);
		Expression x26 = x22.join(x16);
		Expression x28 = x6.product(x6);
		Expression x27 = x6.product(x28);
		Formula x25 = x26.in(x27);
		Variable x31 = Variable.unary("x31");
		Decls x30 = x31.oneOf(x6);
		Expression x35 = x31.join(x26);
		Expression x36 = x6.product(x6);
		Formula x34 = x35.in(x36);
		Variable x39 = Variable.unary("x39");
		Decls x38 = x39.oneOf(x6);
		Expression x42 = x39.join(x35);
		Formula x41 = x42.one();
		Formula x43 = x42.in(x6);
		Formula x40 = x41.and(x43);
		Formula x37 = x40.forAll(x38);
		Formula x33 = x34.and(x37);
		Variable x46 = Variable.unary("x46");
		Decls x45 = x46.oneOf(x6);
		Expression x48 = x35.join(x46);
		Formula x47 = x48.in(x6);
		Formula x44 = x47.forAll(x45);
		Formula x32 = x33.and(x44);
		Formula x29 = x32.forAll(x30);
		Formula x24 = x25.and(x29);
		Variable x52 = Variable.unary("x52");
		Decls x51 = x52.oneOf(x6);
		Variable x55 = Variable.unary("x55");
		Decls x54 = x55.oneOf(x6);
		Decls x50 = x51.and(x54);
		Expression x60 = x52.product(x55);
		Expression x61 = x6.product(x6);
		Formula x59 = x60.in(x61);
		Variable x64 = Variable.unary("x64");
		Decls x63 = x64.oneOf(x6);
		Expression x67 = x64.join(x60);
		Formula x66 = x67.one();
		Formula x68 = x67.in(x6);
		Formula x65 = x66.and(x68);
		Formula x62 = x65.forAll(x63);
		Formula x58 = x59.and(x62);
		Variable x71 = Variable.unary("x71");
		Decls x70 = x71.oneOf(x6);
		Expression x73 = x60.join(x71);
		Formula x72 = x73.in(x6);
		Formula x69 = x72.forAll(x70);
		Formula x57 = x58.and(x69);
		Expression x76 = x26.join(x55);
		Expression x75 = x76.join(x52);
		Formula x74 = x75.in(x6);
		Formula x56 = x57.implies(x74);
		Formula x49 = x56.forAll(x50);
		Formula x23 = x24.and(x49);
		Formula x20 = x23.forAll(x21);
		Formula x14 = x15.and(x20);
		Variable x80 = Variable.unary("x80");
		Decls x79 = x80.oneOf(x6);
		Variable x82 = Variable.unary("x82");
		Decls x81 = x82.oneOf(x6);
		Variable x84 = Variable.unary("x84");
		Decls x83 = x84.oneOf(x6);
		Decls x78 = x79.and(x81).and(x83);
		Expression x90 = x82.product(x84);
		Expression x89 = x80.product(x90);
		Expression x92 = x6.product(x6);
		Expression x91 = x6.product(x92);
		Formula x88 = x89.in(x91);
		Variable x95 = Variable.unary("x95");
		Decls x94 = x95.oneOf(x6);
		Expression x99 = x95.join(x89);
		Expression x100 = x6.product(x6);
		Formula x98 = x99.in(x100);
		Variable x103 = Variable.unary("x103");
		Decls x102 = x103.oneOf(x6);
		Expression x106 = x103.join(x99);
		Formula x105 = x106.one();
		Formula x107 = x106.in(x6);
		Formula x104 = x105.and(x107);
		Formula x101 = x104.forAll(x102);
		Formula x97 = x98.and(x101);
		Variable x110 = Variable.unary("x110");
		Decls x109 = x110.oneOf(x6);
		Expression x112 = x99.join(x110);
		Formula x111 = x112.in(x6);
		Formula x108 = x111.forAll(x109);
		Formula x96 = x97.and(x108);
		Formula x93 = x96.forAll(x94);
		Formula x87 = x88.and(x93);
		Variable x116 = Variable.unary("x116");
		Decls x115 = x116.oneOf(x6);
		Variable x118 = Variable.unary("x118");
		Decls x117 = x118.oneOf(x6);
		Decls x114 = x115.and(x117);
		Expression x123 = x116.product(x118);
		Expression x124 = x6.product(x6);
		Formula x122 = x123.in(x124);
		Variable x127 = Variable.unary("x127");
		Decls x126 = x127.oneOf(x6);
		Expression x130 = x127.join(x123);
		Formula x129 = x130.one();
		Formula x131 = x130.in(x6);
		Formula x128 = x129.and(x131);
		Formula x125 = x128.forAll(x126);
		Formula x121 = x122.and(x125);
		Variable x134 = Variable.unary("x134");
		Decls x133 = x134.oneOf(x6);
		Expression x136 = x123.join(x134);
		Formula x135 = x136.in(x6);
		Formula x132 = x135.forAll(x133);
		Formula x120 = x121.and(x132);
		Expression x139 = x89.join(x118);
		Expression x138 = x139.join(x116);
		Formula x137 = x138.in(x6);
		Formula x119 = x120.implies(x137);
		Formula x113 = x119.forAll(x114);
		Formula x86 = x87.and(x113);
		Expression x143 = x16.join(x84);
		Expression x142 = x143.join(x82);
		Expression x141 = x142.join(x80);
		Formula x140 = x141.in(x6);
		Formula x85 = x86.implies(x140);
		Formula x77 = x85.forAll(x78);
		Formula x13 = x14.and(x77);
		Formula x10 = x13.forAll(x11);
		Expression x148 = x8.join(Expression.UNIV);
		Expression x147 = x148.join(Expression.UNIV);
		Expression x146 = x147.join(Expression.UNIV);
		Expression x145 = x146.join(Expression.UNIV);
		Formula x144 = x145.in(x7);
		Formula x149 = x0.eq(x0);
		Formula x150 = x1.eq(x1);
		Formula x151 = x2.eq(x2);
		Formula x152 = x3.eq(x3);
		Formula x153 = x4.eq(x4);
		Formula x154 = x5.eq(x5);
		Formula x155 = x6.eq(x6);
		Formula x156 = x7.eq(x7);
		Formula x157 = x8.eq(x8);
		Formula x9 = Formula.compose(FormulaOperator.AND, x10, x144, x149,
				x150, x151, x152, x153, x154, x155, x156, x157);

		Solver solver = new Solver();
		solver.options().setSolver(SATFactory.DefaultSAT4J);
		solver.options().setBitwidth(4);
		solver.options().setFlatten(false);
		solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
		solver.options().setSymmetryBreaking(20);
		solver.options().setSkolemDepth(0);

		System.out.println(PrettyPrinter.print(x9, 0));

		System.out.println("Solving...");
		System.out.flush();
		Solution sol = solver.solve(x9, bounds);
		System.out.println(sol.toString());
	}

	@Test
	public void testSmallSlow() {
		Relation x0 = Relation.unary("Int/min");
		Relation x1 = Relation.unary("Int/zero");
		Relation x2 = Relation.unary("Int/max");
		Relation x3 = Relation.nary("Int/next", 2);
		Relation x4 = Relation.unary("seq/Int");
		Relation x5 = Relation.unary("String");
		Relation x6 = Relation.unary("this/A");
		Relation x7 = Relation.unary("this/Relation");
		Relation x8 = Relation.nary("this/Relation.r", 4);

		List<String> atomlist = Arrays.asList("-1", "-2", "-3", "-4", "-5",
				"-6", "-7", "-8", "0", "1", "2", "3", "4", "5", "6", "7",
				"A$0", "A$1", "A$2", "Relation$0", "unused0", "unused1");

		Universe universe = new Universe(atomlist);
		TupleFactory factory = universe.factory();
		Bounds bounds = new Bounds(universe);

		TupleSet x0_upper = factory.noneOf(1);
		x0_upper.add(factory.tuple("-8"));
		bounds.boundExactly(x0, x0_upper);

		TupleSet x1_upper = factory.noneOf(1);
		x1_upper.add(factory.tuple("0"));
		bounds.boundExactly(x1, x1_upper);

		TupleSet x2_upper = factory.noneOf(1);
		x2_upper.add(factory.tuple("7"));
		bounds.boundExactly(x2, x2_upper);

		TupleSet x3_upper = factory.noneOf(2);
		x3_upper.add(factory.tuple("-8").product(factory.tuple("-7")));
		x3_upper.add(factory.tuple("-7").product(factory.tuple("-6")));
		x3_upper.add(factory.tuple("-6").product(factory.tuple("-5")));
		x3_upper.add(factory.tuple("-5").product(factory.tuple("-4")));
		x3_upper.add(factory.tuple("-4").product(factory.tuple("-3")));
		x3_upper.add(factory.tuple("-3").product(factory.tuple("-2")));
		x3_upper.add(factory.tuple("-2").product(factory.tuple("-1")));
		x3_upper.add(factory.tuple("-1").product(factory.tuple("0")));
		x3_upper.add(factory.tuple("0").product(factory.tuple("1")));
		x3_upper.add(factory.tuple("1").product(factory.tuple("2")));
		x3_upper.add(factory.tuple("2").product(factory.tuple("3")));
		x3_upper.add(factory.tuple("3").product(factory.tuple("4")));
		x3_upper.add(factory.tuple("4").product(factory.tuple("5")));
		x3_upper.add(factory.tuple("5").product(factory.tuple("6")));
		x3_upper.add(factory.tuple("6").product(factory.tuple("7")));
		bounds.boundExactly(x3, x3_upper);

		TupleSet x4_upper = factory.noneOf(1);
		x4_upper.add(factory.tuple("0"));
		x4_upper.add(factory.tuple("1"));
		x4_upper.add(factory.tuple("2"));
		bounds.boundExactly(x4, x4_upper);

		TupleSet x5_upper = factory.noneOf(1);
		bounds.boundExactly(x5, x5_upper);

		TupleSet x6_upper = factory.noneOf(1);
		x6_upper.add(factory.tuple("A$0"));
		x6_upper.add(factory.tuple("A$1"));
		x6_upper.add(factory.tuple("A$2"));
		bounds.boundExactly(x6, x6_upper);

		TupleSet x7_upper = factory.noneOf(1);
		x7_upper.add(factory.tuple("unused0"));
		x7_upper.add(factory.tuple("unused1"));
		x7_upper.add(factory.tuple("Relation$0"));
		bounds.bound(x7, x7_upper);

		TupleSet x8_upper = factory.noneOf(4);
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$0"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$0"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$0"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$0"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$0"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$0"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$0"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$0"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$0"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$1"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$1"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$1"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$1"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$1"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$1"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$1"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$1"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$1"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$2"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$2"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$2"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$2"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$2"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$2"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$2"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$2"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("unused1").product(factory.tuple("A$2"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$0")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$1")).product(factory.tuple("A$2")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$0")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$1")));
		x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2"))
				.product(factory.tuple("A$2")).product(factory.tuple("A$2")));
		bounds.bound(x8, x8_upper);

		bounds.boundExactly(-8,
				factory.range(factory.tuple("-8"), factory.tuple("-8")));
		bounds.boundExactly(-7,
				factory.range(factory.tuple("-7"), factory.tuple("-7")));
		bounds.boundExactly(-6,
				factory.range(factory.tuple("-6"), factory.tuple("-6")));
		bounds.boundExactly(-5,
				factory.range(factory.tuple("-5"), factory.tuple("-5")));
		bounds.boundExactly(-4,
				factory.range(factory.tuple("-4"), factory.tuple("-4")));
		bounds.boundExactly(-3,
				factory.range(factory.tuple("-3"), factory.tuple("-3")));
		bounds.boundExactly(-2,
				factory.range(factory.tuple("-2"), factory.tuple("-2")));
		bounds.boundExactly(-1,
				factory.range(factory.tuple("-1"), factory.tuple("-1")));
		bounds.boundExactly(0,
				factory.range(factory.tuple("0"), factory.tuple("0")));
		bounds.boundExactly(1,
				factory.range(factory.tuple("1"), factory.tuple("1")));
		bounds.boundExactly(2,
				factory.range(factory.tuple("2"), factory.tuple("2")));
		bounds.boundExactly(3,
				factory.range(factory.tuple("3"), factory.tuple("3")));
		bounds.boundExactly(4,
				factory.range(factory.tuple("4"), factory.tuple("4")));
		bounds.boundExactly(5,
				factory.range(factory.tuple("5"), factory.tuple("5")));
		bounds.boundExactly(6,
				factory.range(factory.tuple("6"), factory.tuple("6")));
		bounds.boundExactly(7,
				factory.range(factory.tuple("7"), factory.tuple("7")));

		Variable x12 = Variable.unary("this");
		Decls x11 = x12.oneOf(x7);
		Expression x16 = x12.join(x8);
		Expression x18 = x6.product(x6);
		Expression x17 = x6.product(x18);
		Formula x15 = x16.in(x17);
		Variable x21 = Variable.unary("x21");
		Decls x20 = x21.oneOf(x6);
		Expression x25 = x21.join(x16);
		Expression x26 = x6.product(x6);
		Formula x24 = x25.in(x26);
		Variable x29 = Variable.unary("x29");
		Decls x28 = x29.oneOf(x6);
		Expression x32 = x29.join(x25);
		Formula x31 = x32.one();
		Formula x33 = x32.in(x6);
		Formula x30 = x31.and(x33);
		Formula x27 = x30.forAll(x28);
		Formula x23 = x24.and(x27);
		Variable x36 = Variable.unary("x36");
		Decls x35 = x36.oneOf(x6);
		Expression x38 = x25.join(x36);
		Formula x37 = x38.in(x6);
		Formula x34 = x37.forAll(x35);
		Formula x22 = x23.and(x34);
		Formula x19 = x22.forAll(x20);
		Formula x14 = x15.and(x19);
		Variable x42 = Variable.unary("x42");
		Decls x41 = x42.oneOf(Expression.UNIV);
		Variable x45 = Variable.unary("x45");
		Decls x44 = x45.oneOf(Expression.UNIV);
		Decls x40 = x41.and(x44);
		Expression x50 = x42.product(x45);
		Expression x51 = x6.product(x6);
		Formula x49 = x50.in(x51);
		Variable x54 = Variable.unary("x54");
		Decls x53 = x54.oneOf(x6);
		Expression x57 = x54.join(x50);
		Formula x56 = x57.one();
		Formula x58 = x57.in(x6);
		Formula x55 = x56.and(x58);
		Formula x52 = x55.forAll(x53);
		Formula x48 = x49.and(x52);
		Variable x61 = Variable.unary("x61");
		Decls x60 = x61.oneOf(x6);
		Expression x63 = x50.join(x61);
		Formula x62 = x63.in(x6);
		Formula x59 = x62.forAll(x60);
		Formula x47 = x48.and(x59);
		Expression x66 = x16.join(x45);
		Expression x65 = x66.join(x42);
		Formula x64 = x65.in(x6);
		Formula x46 = x47.implies(x64);
		Formula x39 = x46.forAll(x40);
		Formula x13 = x14.and(x39);
		Formula x10 = x13.forAll(x11);
		Expression x70 = x8.join(Expression.UNIV);
		Expression x69 = x70.join(Expression.UNIV);
		Expression x68 = x69.join(Expression.UNIV);
		Formula x67 = x68.in(x7);
		Formula x71 = x0.eq(x0);
		Formula x72 = x1.eq(x1);
		Formula x73 = x2.eq(x2);
		Formula x74 = x3.eq(x3);
		Formula x75 = x4.eq(x4);
		Formula x76 = x5.eq(x5);
		Formula x77 = x6.eq(x6);
		Formula x78 = x7.eq(x7);
		Formula x79 = x8.eq(x8);
		Formula x9 = Formula.compose(FormulaOperator.AND, x10, x67, x71, x72,
				x73, x74, x75, x76, x77, x78, x79);

		Solver solver = new Solver();
		solver.options().setSolver(SATFactory.DefaultSAT4J);
		solver.options().setBitwidth(4);
		solver.options().setFlatten(false);
		solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
		solver.options().setSymmetryBreaking(20);
		solver.options().setSkolemDepth(0);

		System.out.println(PrettyPrinter.print(x9, 0));
		System.out.println(bounds);

		System.out.println("Solving...");
		System.out.flush();
		Solution sol = solver.solve(x9, bounds);
		System.out.println(sol.toString());

		Instance inst = sol.instance();
		Evaluator ev = new Evaluator(inst);

		System.out.println("Universe: " + ev.evaluate(Expression.UNIV));
		Formula xx = x46.forAll(x40).forAll(x11);
		System.out.println(PrettyPrinter.print(xx, 2));
		System.out.println(ev.evaluate(xx));

		System.out.println(PrettyPrinter.print(x46, 4));
	}
	
	@Test
	public void testSmallFast(){
		Relation x0 = Relation.unary("Int/min");
        Relation x1 = Relation.unary("Int/zero");
        Relation x2 = Relation.unary("Int/max");
        Relation x3 = Relation.nary("Int/next", 2);
        Relation x4 = Relation.unary("seq/Int");
        Relation x5 = Relation.unary("String");
        Relation x6 = Relation.unary("this/A");
        Relation x7 = Relation.unary("this/Relation");
        Relation x8 = Relation.nary("this/Relation.r", 4);

        List<String> atomlist = Arrays.asList("-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "0",
                "1", "2", "3", "4", "5", "6", "7", "A$0", "A$1", "A$2", "Relation$0", "Relation$1",
                "Relation$2");

        Universe universe = new Universe(atomlist);
        TupleFactory factory = universe.factory();
        Bounds bounds = new Bounds(universe);

        TupleSet x0_upper = factory.noneOf(1);
        x0_upper.add(factory.tuple("-8"));
        bounds.boundExactly(x0, x0_upper);

        TupleSet x1_upper = factory.noneOf(1);
        x1_upper.add(factory.tuple("0"));
        bounds.boundExactly(x1, x1_upper);

        TupleSet x2_upper = factory.noneOf(1);
        x2_upper.add(factory.tuple("7"));
        bounds.boundExactly(x2, x2_upper);

        TupleSet x3_upper = factory.noneOf(2);
        x3_upper.add(factory.tuple("-8").product(factory.tuple("-7")));
        x3_upper.add(factory.tuple("-7").product(factory.tuple("-6")));
        x3_upper.add(factory.tuple("-6").product(factory.tuple("-5")));
        x3_upper.add(factory.tuple("-5").product(factory.tuple("-4")));
        x3_upper.add(factory.tuple("-4").product(factory.tuple("-3")));
        x3_upper.add(factory.tuple("-3").product(factory.tuple("-2")));
        x3_upper.add(factory.tuple("-2").product(factory.tuple("-1")));
        x3_upper.add(factory.tuple("-1").product(factory.tuple("0")));
        x3_upper.add(factory.tuple("0").product(factory.tuple("1")));
        x3_upper.add(factory.tuple("1").product(factory.tuple("2")));
        x3_upper.add(factory.tuple("2").product(factory.tuple("3")));
        x3_upper.add(factory.tuple("3").product(factory.tuple("4")));
        x3_upper.add(factory.tuple("4").product(factory.tuple("5")));
        x3_upper.add(factory.tuple("5").product(factory.tuple("6")));
        x3_upper.add(factory.tuple("6").product(factory.tuple("7")));
        bounds.boundExactly(x3, x3_upper);

        TupleSet x4_upper = factory.noneOf(1);
        x4_upper.add(factory.tuple("0"));
        x4_upper.add(factory.tuple("1"));
        x4_upper.add(factory.tuple("2"));
        bounds.boundExactly(x4, x4_upper);

        TupleSet x5_upper = factory.noneOf(1);
        bounds.boundExactly(x5, x5_upper);

        TupleSet x6_upper = factory.noneOf(1);
        x6_upper.add(factory.tuple("A$0"));
        x6_upper.add(factory.tuple("A$1"));
        x6_upper.add(factory.tuple("A$2"));
        bounds.boundExactly(x6, x6_upper);

        TupleSet x7_upper = factory.noneOf(1);
        x7_upper.add(factory.tuple("Relation$0"));
        x7_upper.add(factory.tuple("Relation$1"));
        x7_upper.add(factory.tuple("Relation$2"));
        bounds.boundExactly(x7, x7_upper);

        TupleSet x8_upper = factory.noneOf(4);
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$1")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$1")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$1")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$2")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$2")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$2")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1")).product(
                factory.tuple("A$0")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1")).product(
                factory.tuple("A$0")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1")).product(
                factory.tuple("A$0")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1")).product(
                factory.tuple("A$1")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1")).product(
                factory.tuple("A$1")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1")).product(
                factory.tuple("A$1")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1")).product(
                factory.tuple("A$2")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1")).product(
                factory.tuple("A$2")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$1")).product(
                factory.tuple("A$2")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2")).product(
                factory.tuple("A$0")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2")).product(
                factory.tuple("A$0")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2")).product(
                factory.tuple("A$0")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2")).product(
                factory.tuple("A$1")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2")).product(
                factory.tuple("A$1")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2")).product(
                factory.tuple("A$1")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2")).product(
                factory.tuple("A$2")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2")).product(
                factory.tuple("A$2")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$0").product(factory.tuple("A$2")).product(
                factory.tuple("A$2")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$0")).product(
                factory.tuple("A$1")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$0")).product(
                factory.tuple("A$1")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$0")).product(
                factory.tuple("A$1")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$0")).product(
                factory.tuple("A$2")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$0")).product(
                factory.tuple("A$2")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$0")).product(
                factory.tuple("A$2")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$1")).product(
                factory.tuple("A$0")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$1")).product(
                factory.tuple("A$0")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$1")).product(
                factory.tuple("A$0")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$1")).product(
                factory.tuple("A$1")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$1")).product(
                factory.tuple("A$1")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$1")).product(
                factory.tuple("A$1")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$1")).product(
                factory.tuple("A$2")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$1")).product(
                factory.tuple("A$2")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$1")).product(
                factory.tuple("A$2")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$2")).product(
                factory.tuple("A$0")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$2")).product(
                factory.tuple("A$0")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$2")).product(
                factory.tuple("A$0")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$2")).product(
                factory.tuple("A$1")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$2")).product(
                factory.tuple("A$1")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$2")).product(
                factory.tuple("A$1")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$2")).product(
                factory.tuple("A$2")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$2")).product(
                factory.tuple("A$2")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$1").product(factory.tuple("A$2")).product(
                factory.tuple("A$2")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$0")).product(
                factory.tuple("A$1")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$0")).product(
                factory.tuple("A$1")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$0")).product(
                factory.tuple("A$1")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$0")).product(
                factory.tuple("A$2")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$0")).product(
                factory.tuple("A$2")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$0")).product(
                factory.tuple("A$2")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$1")).product(
                factory.tuple("A$0")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$1")).product(
                factory.tuple("A$0")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$1")).product(
                factory.tuple("A$0")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$1")).product(
                factory.tuple("A$1")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$1")).product(
                factory.tuple("A$1")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$1")).product(
                factory.tuple("A$1")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$1")).product(
                factory.tuple("A$2")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$1")).product(
                factory.tuple("A$2")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$1")).product(
                factory.tuple("A$2")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$2")).product(
                factory.tuple("A$0")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$2")).product(
                factory.tuple("A$0")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$2")).product(
                factory.tuple("A$0")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$2")).product(
                factory.tuple("A$1")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$2")).product(
                factory.tuple("A$1")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$2")).product(
                factory.tuple("A$1")).product(factory.tuple("A$2")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$2")).product(
                factory.tuple("A$2")).product(factory.tuple("A$0")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$2")).product(
                factory.tuple("A$2")).product(factory.tuple("A$1")));
        x8_upper.add(factory.tuple("Relation$2").product(factory.tuple("A$2")).product(
                factory.tuple("A$2")).product(factory.tuple("A$2")));
        bounds.bound(x8, x8_upper);

        bounds.boundExactly(-8, factory.range(factory.tuple("-8"), factory.tuple("-8")));
        bounds.boundExactly(-7, factory.range(factory.tuple("-7"), factory.tuple("-7")));
        bounds.boundExactly(-6, factory.range(factory.tuple("-6"), factory.tuple("-6")));
        bounds.boundExactly(-5, factory.range(factory.tuple("-5"), factory.tuple("-5")));
        bounds.boundExactly(-4, factory.range(factory.tuple("-4"), factory.tuple("-4")));
        bounds.boundExactly(-3, factory.range(factory.tuple("-3"), factory.tuple("-3")));
        bounds.boundExactly(-2, factory.range(factory.tuple("-2"), factory.tuple("-2")));
        bounds.boundExactly(-1, factory.range(factory.tuple("-1"), factory.tuple("-1")));
        bounds.boundExactly(0, factory.range(factory.tuple("0"), factory.tuple("0")));
        bounds.boundExactly(1, factory.range(factory.tuple("1"), factory.tuple("1")));
        bounds.boundExactly(2, factory.range(factory.tuple("2"), factory.tuple("2")));
        bounds.boundExactly(3, factory.range(factory.tuple("3"), factory.tuple("3")));
        bounds.boundExactly(4, factory.range(factory.tuple("4"), factory.tuple("4")));
        bounds.boundExactly(5, factory.range(factory.tuple("5"), factory.tuple("5")));
        bounds.boundExactly(6, factory.range(factory.tuple("6"), factory.tuple("6")));
        bounds.boundExactly(7, factory.range(factory.tuple("7"), factory.tuple("7")));

        Variable x12 = Variable.unary("this");
        Decls x11 = x12.oneOf(x7);
        Expression x16 = x12.join(x8);
        Expression x18 = x6.product(x6);
        Expression x17 = x18.product(x6);
        Formula x15 = x16.in(x17);
        Variable x22 = Variable.unary("x22");
        Decls x21 = x22.oneOf(Expression.UNIV);
        Variable x25 = Variable.unary("x25");
        Decls x24 = x25.oneOf(Expression.UNIV);
        Decls x20 = x21.and(x24);
        Expression x28 = x25.product(x22);
        Expression x29 = x6.product(x6);
        Formula x27 = x28.in(x29);
        Expression x33 = x25.join(x16);
        Expression x32 = x22.join(x33);
        Formula x31 = x32.one();
        Formula x34 = x32.in(x6);
        Formula x30 = x31.and(x34);
        Formula x26 = x27.implies(x30);
        Formula x19 = x26.forAll(x20);
        Formula x14 = x15.and(x19);
        Variable x37 = Variable.unary("x37");
        Decls x36 = x37.oneOf(x6);
        Expression x39 = x16.join(x37);
        Expression x40 = x6.product(x6);
        Formula x38 = x39.in(x40);
        Formula x35 = x38.forAll(x36);
        Formula x13 = x14.and(x35);
        Formula x10 = x13.forAll(x11);
        Expression x44 = x8.join(Expression.UNIV);
        Expression x43 = x44.join(Expression.UNIV);
        Expression x42 = x43.join(Expression.UNIV);
        Formula x41 = x42.in(x7);
        Formula x45 = x0.eq(x0);
        Formula x46 = x1.eq(x1);
        Formula x47 = x2.eq(x2);
        Formula x48 = x3.eq(x3);
        Formula x49 = x4.eq(x4);
        Formula x50 = x5.eq(x5);
        Formula x51 = x6.eq(x6);
        Formula x52 = x7.eq(x7);
        Formula x53 = x8.eq(x8);
        Formula x9 = Formula.compose(FormulaOperator.AND, x10, x41, x45, x46, x47, x48, x49, x50,
                x51, x52, x53);

        Solver solver = new Solver();
        solver.options().setSolver(SATFactory.DefaultSAT4J);
        solver.options().setBitwidth(4);
        solver.options().setFlatten(false);
        solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
        solver.options().setSymmetryBreaking(20);
        solver.options().setSkolemDepth(0);
        
        System.out.println(PrettyPrinter.print(x9, 0));
        
        System.out.println("Solving...");
        System.out.flush();
        Solution sol = solver.solve(x9, bounds);
        System.out.println(sol.toString());
	}
	
	@Test
	public void test1(){
		Relation x0 = Relation.unary("Int/min");
        Relation x1 = Relation.unary("Int/zero");
        Relation x2 = Relation.unary("Int/max");
        Relation x3 = Relation.nary("Int/next", 2);
        Relation x4 = Relation.unary("seq/Int");
        Relation x5 = Relation.unary("String");
        Relation x6 = Relation.unary("this/R");

        List<String> atomlist = Arrays.asList("-1", "-2", "0", "1", "R$0", "R$1", "unused0");

        Universe universe = new Universe(atomlist);
        TupleFactory factory = universe.factory();
        Bounds bounds = new Bounds(universe);

        TupleSet x0_upper = factory.noneOf(1);
        x0_upper.add(factory.tuple("-2"));
        bounds.boundExactly(x0, x0_upper);

        TupleSet x1_upper = factory.noneOf(1);
        x1_upper.add(factory.tuple("0"));
        bounds.boundExactly(x1, x1_upper);

        TupleSet x2_upper = factory.noneOf(1);
        x2_upper.add(factory.tuple("1"));
        bounds.boundExactly(x2, x2_upper);

        TupleSet x3_upper = factory.noneOf(2);
        x3_upper.add(factory.tuple("-2").product(factory.tuple("-1")));
        x3_upper.add(factory.tuple("-1").product(factory.tuple("0")));
        x3_upper.add(factory.tuple("0").product(factory.tuple("1")));
        bounds.boundExactly(x3, x3_upper);

        TupleSet x4_upper = factory.noneOf(1);
        x4_upper.add(factory.tuple("0"));
        bounds.boundExactly(x4, x4_upper);

        TupleSet x5_upper = factory.noneOf(1);
        bounds.boundExactly(x5, x5_upper);

        TupleSet x6_upper = factory.noneOf(1);
        x6_upper.add(factory.tuple("unused0"));
        x6_upper.add(factory.tuple("R$0"));
        x6_upper.add(factory.tuple("R$1"));
        bounds.bound(x6, x6_upper);

        bounds.boundExactly(-2, factory.range(factory.tuple("-2"), factory.tuple("-2")));
        bounds.boundExactly(-1, factory.range(factory.tuple("-1"), factory.tuple("-1")));
        bounds.boundExactly(0, factory.range(factory.tuple("0"), factory.tuple("0")));
        bounds.boundExactly(1, factory.range(factory.tuple("1"), factory.tuple("1")));

        Formula x11 = x6.some();
        Formula x10 = x11.not();
        IntExpression x13 = x6.count();
        IntExpression x14 = IntConstant.constant(0);
        Formula x12 = x13.gt(x14);
        Formula x9 = x10.or(x12);
        Formula x8 = x9.not();
        Formula x15 = x0.eq(x0);
        Formula x16 = x1.eq(x1);
        Formula x17 = x2.eq(x2);
        Formula x18 = x3.eq(x3);
        Formula x19 = x4.eq(x4);
        Formula x20 = x5.eq(x5);
        Formula x21 = x6.eq(x6);
        Formula x7 = Formula.compose(FormulaOperator.AND, x8, x15, x16, x17, x18, x19, x20, x21);

        Solver solver = new Solver();
        solver.options().setSolver(SATFactory.DefaultSAT4J);
        solver.options().setBitwidth(2);
        solver.options().setFlatten(false);
        solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
        solver.options().setSymmetryBreaking(20);
        solver.options().setSkolemDepth(0);
        System.out.println("Solving...");
        System.out.println(PrettyPrinter.print(x7, 2));
        System.out.println(bounds);
        System.out.flush();
        Solution sol = solver.solve(x7, bounds);
        System.out.println(sol.toString());
        Evaluator ev = new Evaluator(sol.instance(), solver.options());
        System.out.println(ev.evaluate(x7));
        System.out.println(ev.evaluate(x6.count()));
        System.out.println(ev.evaluate(x6.count().gt(IntConstant.constant(0))));
	}
	
	@Test
	public void test(){
		Relation x5 = Relation.unary("String");
        Relation x6 = Relation.unary("this/A");
        Relation x7 = Relation.unary("this/B");

        List<String> atomlist = Arrays.asList("A$0", "unused0", "unused1", "unused2", "unused3", "unused4", "unused5", "unused6",
                "unused7", "unused8");

        Universe universe = new Universe(atomlist);
        TupleFactory factory = universe.factory();
        Bounds bounds = new Bounds(universe);

        TupleSet x5_upper = factory.noneOf(1);
        bounds.boundExactly(x5, x5_upper);

        TupleSet x6_upper = factory.noneOf(1);
        x6_upper.add(factory.tuple("unused0"));
        x6_upper.add(factory.tuple("unused1"));
        x6_upper.add(factory.tuple("unused2"));
        x6_upper.add(factory.tuple("unused3"));
        x6_upper.add(factory.tuple("A$0"));
        bounds.bound(x6, x6_upper);

        TupleSet x7_upper = factory.noneOf(1);
        x7_upper.add(factory.tuple("unused4"));
        x7_upper.add(factory.tuple("unused5"));
        x7_upper.add(factory.tuple("unused6"));
        x7_upper.add(factory.tuple("unused7"));
        x7_upper.add(factory.tuple("unused8"));
        bounds.bound(x7, x7_upper);

        IntExpression x10 = x6.count();
        IntExpression x12 = x7.count();
        IntExpression x13 = IntConstant.constant(1);
        IntExpression x11 = x12.plus(x13);
        Formula x9 = x10.eq(x11);
        Formula x19 = x5.eq(x5);
        Formula x20 = x6.eq(x6);
        Formula x21 = x7.eq(x7);
        Formula x8 = Formula.compose(FormulaOperator.AND, x9, x19, x20, x21);

        Solver solver = new Solver();
        solver.options().setSolver(SATFactory.DefaultSAT4J);
        solver.options().setBitwidth(4);
        solver.options().setFlatten(false);
        solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
        solver.options().setSymmetryBreaking(20);
        solver.options().setSkolemDepth(0);
        System.out.println("Solving...");
        System.out.flush();
        Solution sol = solver.solve(x8, bounds);
        System.out.println(sol.toString());
	}
	
	@Test
	public void testKK(){
		Relation x6 = Relation.unary("R");
        int[] ints = new int[] {0, 1, 2};
        
        List<Object> atomlist = new LinkedList<Object>();
        atomlist.add("R$0"); 
        atomlist.add("R$1");
        atomlist.add("R$2");
        for (int i : ints) atomlist.add(i);
        
        Universe universe = new Universe(atomlist);
        TupleFactory factory = universe.factory();
        Bounds bounds = new Bounds(universe);

        TupleSet x6_upper = factory.noneOf(1);
        x6_upper.add(factory.tuple("R$0"));
        x6_upper.add(factory.tuple("R$1"));
        x6_upper.add(factory.tuple("R$2"));
        bounds.bound(x6, x6_upper);

        for (int i : ints) {
            bounds.boundExactly(i, factory.setOf(i));
        }

        Formula x11 = x6.some();
        IntExpression x5 = x6.count();
        Formula x9 = x11.implies(x5.gt(IntConstant.constant(0)));
        Formula x7 = x9.not();

        Solver solver = new Solver();
        solver.options().setSolver(SATFactory.DefaultSAT4J);
        solver.options().setBitwidth(2);
        solver.options().setFlatten(false);
        solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
        solver.options().setSymmetryBreaking(20);
        solver.options().setSkolemDepth(0);
        System.out.println("Solving...");
        System.out.println(PrettyPrinter.print(x7, 0));
        System.out.println(bounds);
        Solution sol = solver.solve(x7, bounds);
        System.out.println(sol.toString());
        
        Instance inst = sol.instance();
        Evaluator ev = new Evaluator(inst);
        System.out.println(ev.evaluate(x6.some()));
        System.out.println(ev.evaluate(x5));
        System.out.println(ev.evaluate(x5.gt(IntConstant.constant(0))));
        System.out.println(ev.evaluate(x6.some().implies(x5.gt(IntConstant.constant(0))).not()));
        System.out.println(ev.evaluate(x7));
	}
	
	@Test
	public void testOK(){
		Relation x0 = Relation.unary("Int/min");
        Relation x1 = Relation.unary("Int/zero");
        Relation x2 = Relation.unary("Int/max");
        Relation x3 = Relation.nary("Int/next", 2);
        Relation x4 = Relation.unary("seq/Int");
        Relation x5 = Relation.unary("String");
        Relation x6 = Relation.unary("this/A");
        Relation x7 = Relation.unary("this/B");
        Relation x8 = Relation.unary("this/Relation1");
        Relation x9 = Relation.unary("this/Relation2");
        Relation x10 = Relation.unary("this/Relation3");
        Relation x11 = Relation.unary("this/Relation4");
        Relation x12 = Relation.nary("this/Relation1.r", 3);
        Relation x13 = Relation.nary("this/Relation2.r", 3);
        Relation x14 = Relation.nary("this/Relation3.r", 3);
        Relation x15 = Relation.nary("this/Relation4.r", 3);

        List<String> atomlist = Arrays.asList("-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "0",
                "1", "2", "3", "4", "5", "6", "7", "A$0", "B$0", "Relation1$0", "Relation2$0",
                "Relation3$0", "Relation4$0");

        Universe universe = new Universe(atomlist);
        TupleFactory factory = universe.factory();
        Bounds bounds = new Bounds(universe);

        TupleSet x0_upper = factory.noneOf(1);
        x0_upper.add(factory.tuple("-8"));
        bounds.boundExactly(x0, x0_upper);

        TupleSet x1_upper = factory.noneOf(1);
        x1_upper.add(factory.tuple("0"));
        bounds.boundExactly(x1, x1_upper);

        TupleSet x2_upper = factory.noneOf(1);
        x2_upper.add(factory.tuple("7"));
        bounds.boundExactly(x2, x2_upper);

        TupleSet x3_upper = factory.noneOf(2);
        x3_upper.add(factory.tuple("-8").product(factory.tuple("-7")));
        x3_upper.add(factory.tuple("-7").product(factory.tuple("-6")));
        x3_upper.add(factory.tuple("-6").product(factory.tuple("-5")));
        x3_upper.add(factory.tuple("-5").product(factory.tuple("-4")));
        x3_upper.add(factory.tuple("-4").product(factory.tuple("-3")));
        x3_upper.add(factory.tuple("-3").product(factory.tuple("-2")));
        x3_upper.add(factory.tuple("-2").product(factory.tuple("-1")));
        x3_upper.add(factory.tuple("-1").product(factory.tuple("0")));
        x3_upper.add(factory.tuple("0").product(factory.tuple("1")));
        x3_upper.add(factory.tuple("1").product(factory.tuple("2")));
        x3_upper.add(factory.tuple("2").product(factory.tuple("3")));
        x3_upper.add(factory.tuple("3").product(factory.tuple("4")));
        x3_upper.add(factory.tuple("4").product(factory.tuple("5")));
        x3_upper.add(factory.tuple("5").product(factory.tuple("6")));
        x3_upper.add(factory.tuple("6").product(factory.tuple("7")));
        bounds.boundExactly(x3, x3_upper);

        TupleSet x4_upper = factory.noneOf(1);
        x4_upper.add(factory.tuple("0"));
        bounds.boundExactly(x4, x4_upper);

        TupleSet x5_upper = factory.noneOf(1);
        bounds.boundExactly(x5, x5_upper);

        TupleSet x6_upper = factory.noneOf(1);
        x6_upper.add(factory.tuple("A$0"));
        bounds.bound(x6, x6_upper);

        TupleSet x7_upper = factory.noneOf(1);
        x7_upper.add(factory.tuple("B$0"));
        bounds.bound(x7, x7_upper);

        TupleSet x8_upper = factory.noneOf(1);
        x8_upper.add(factory.tuple("Relation1$0"));
        bounds.bound(x8, x8_upper);

        TupleSet x9_upper = factory.noneOf(1);
        x9_upper.add(factory.tuple("Relation2$0"));
        bounds.bound(x9, x9_upper);

        TupleSet x10_upper = factory.noneOf(1);
        x10_upper.add(factory.tuple("Relation3$0"));
        bounds.bound(x10, x10_upper);

        TupleSet x11_upper = factory.noneOf(1);
        x11_upper.add(factory.tuple("Relation4$0"));
        bounds.bound(x11, x11_upper);

        TupleSet x12_upper = factory.noneOf(3);
        x12_upper.add(factory.tuple("Relation1$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")));
        x12_upper.add(factory.tuple("Relation1$0").product(factory.tuple("A$0")).product(
                factory.tuple("B$0")));
        x12_upper.add(factory.tuple("Relation1$0").product(factory.tuple("B$0")).product(
                factory.tuple("A$0")));
        x12_upper.add(factory.tuple("Relation1$0").product(factory.tuple("B$0")).product(
                factory.tuple("B$0")));
        bounds.bound(x12, x12_upper);

        TupleSet x13_upper = factory.noneOf(3);
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("A$0")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("B$0")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-8")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-7")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-6")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-5")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-4")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-3")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-2")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("-1")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("0")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("1")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("2")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("3")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("4")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("5")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("6")).product(
                factory.tuple("7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("A$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("B$0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("-8")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("-7")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("-6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("-5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("-4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("-3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("-2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("-1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("0")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("1")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("2")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("3")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("4")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("5")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("6")));
        x13_upper.add(factory.tuple("Relation2$0").product(factory.tuple("7")).product(
                factory.tuple("7")));
        bounds.bound(x13, x13_upper);

        TupleSet x14_upper = factory.noneOf(3);
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("A$0")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("B$0")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-8")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-7")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-6")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-5")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-4")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-3")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-2")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("-1")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("0")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("1")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("2")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("3")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("4")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("5")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("6")).product(
                factory.tuple("7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("A$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("B$0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("-8")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("-7")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("-6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("-5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("-4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("-3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("-2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("-1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("0")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("1")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("2")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("3")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("4")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("5")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("6")));
        x14_upper.add(factory.tuple("Relation3$0").product(factory.tuple("7")).product(
                factory.tuple("7")));
        bounds.bound(x14, x14_upper);

        TupleSet x15_upper = factory.noneOf(3);
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("A$0")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("B$0")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-8")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-7")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-6")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-5")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-4")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-3")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-2")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("-1")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("0")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("1")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("2")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("3")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("4")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("5")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("6")).product(
                factory.tuple("7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("A$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("B$0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("-8")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("-7")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("-6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("-5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("-4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("-3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("-2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("-1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("0")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("1")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("2")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("3")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("4")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("5")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("6")));
        x15_upper.add(factory.tuple("Relation4$0").product(factory.tuple("7")).product(
                factory.tuple("7")));
        bounds.bound(x15, x15_upper);

        bounds.boundExactly(-8, factory.range(factory.tuple("-8"), factory.tuple("-8")));
        bounds.boundExactly(-7, factory.range(factory.tuple("-7"), factory.tuple("-7")));
        bounds.boundExactly(-6, factory.range(factory.tuple("-6"), factory.tuple("-6")));
        bounds.boundExactly(-5, factory.range(factory.tuple("-5"), factory.tuple("-5")));
        bounds.boundExactly(-4, factory.range(factory.tuple("-4"), factory.tuple("-4")));
        bounds.boundExactly(-3, factory.range(factory.tuple("-3"), factory.tuple("-3")));
        bounds.boundExactly(-2, factory.range(factory.tuple("-2"), factory.tuple("-2")));
        bounds.boundExactly(-1, factory.range(factory.tuple("-1"), factory.tuple("-1")));
        bounds.boundExactly(0, factory.range(factory.tuple("0"), factory.tuple("0")));
        bounds.boundExactly(1, factory.range(factory.tuple("1"), factory.tuple("1")));
        bounds.boundExactly(2, factory.range(factory.tuple("2"), factory.tuple("2")));
        bounds.boundExactly(3, factory.range(factory.tuple("3"), factory.tuple("3")));
        bounds.boundExactly(4, factory.range(factory.tuple("4"), factory.tuple("4")));
        bounds.boundExactly(5, factory.range(factory.tuple("5"), factory.tuple("5")));
        bounds.boundExactly(6, factory.range(factory.tuple("6"), factory.tuple("6")));
        bounds.boundExactly(7, factory.range(factory.tuple("7"), factory.tuple("7")));

        Variable x19 = Variable.unary("p1_this");
        Decls x18 = x19.oneOf(x8);
        Expression x23 = x19.join(x12);
        Expression x25 = x6.union(x7);
        Expression x26 = x6.union(x7);
        Expression x24 = x25.product(x26);
        Formula x22 = x23.in(x24);
        Variable x29 = Variable.unary("v60");
        Decls x28 = x29.oneOf(x25);
        Expression x32 = x29.join(x23);
        Formula x31 = x32.one();
        Expression x34 = x6.union(x7);
        Formula x33 = x32.in(x34);
        Formula x30 = x31.and(x33);
        Formula x27 = x30.forAll(x28);
        Formula x21 = x22.and(x27);
        Variable x37 = Variable.unary("v61");
        Decls x36 = x37.oneOf(x26);
        Expression x39 = x23.join(x37);
        Expression x40 = x6.union(x7);
        Formula x38 = x39.in(x40);
        Formula x35 = x38.forAll(x36);
        Formula x20 = x21.and(x35);
        Formula x17 = x20.forAll(x18);
        Expression x43 = x12.join(Expression.UNIV);
        Expression x42 = x43.join(Expression.UNIV);
        Formula x41 = x42.in(x8);
        Variable x47 = Variable.unary("p1_this");
        Decls x46 = x47.oneOf(x9);
        Expression x51 = x47.join(x13);
        Expression x54 = x6.union(x7);
        Expression x53 = x54.union(Expression.INTS);
        Expression x57 = x6.union(x7);
        Expression x56 = x57.union(Expression.INTS);
        Expression x52 = x53.product(x56);
        Formula x50 = x51.in(x52);
        Variable x60 = Variable.unary("v62");
        Decls x59 = x60.oneOf(x53);
        Expression x62 = x60.join(x51);
        Expression x64 = x6.union(x7);
        Expression x63 = x64.union(Expression.INTS);
        Formula x61 = x62.in(x63);
        Formula x58 = x61.forAll(x59);
        Formula x49 = x50.and(x58);
        Variable x67 = Variable.unary("v63");
        Decls x66 = x67.oneOf(x56);
        Expression x70 = x51.join(x67);
        Formula x69 = x70.one();
        Expression x73 = x6.union(x7);
        Expression x72 = x73.union(Expression.INTS);
        Formula x71 = x70.in(x72);
        Formula x68 = x69.and(x71);
        Formula x65 = x68.forAll(x66);
        Formula x48 = x49.and(x65);
        Formula x45 = x48.forAll(x46);
        Expression x76 = x13.join(Expression.UNIV);
        Expression x75 = x76.join(Expression.UNIV);
        Formula x74 = x75.in(x9);
        Variable x79 = Variable.unary("p1_this");
        Decls x78 = x79.oneOf(x10);
        Expression x83 = x79.join(x14);
        Expression x86 = x6.union(x7);
        Expression x85 = x86.union(Expression.INTS);
        Expression x88 = x6.union(x7);
        Expression x87 = x88.union(Expression.INTS);
        Expression x84 = x85.product(x87);
        Formula x82 = x83.in(x84);
        Variable x91 = Variable.unary("v64");
        Decls x90 = x91.oneOf(x85);
        Expression x94 = x91.join(x83);
        Formula x93 = x94.one();
        Expression x97 = x6.union(x7);
        Expression x96 = x97.union(Expression.INTS);
        Formula x95 = x94.in(x96);
        Formula x92 = x93.and(x95);
        Formula x89 = x92.forAll(x90);
        Formula x81 = x82.and(x89);
        Variable x100 = Variable.unary("v65");
        Decls x99 = x100.oneOf(x87);
        Expression x103 = x83.join(x100);
        Formula x102 = x103.one();
        Expression x106 = x6.union(x7);
        Expression x105 = x106.union(Expression.INTS);
        Formula x104 = x103.in(x105);
        Formula x101 = x102.and(x104);
        Formula x98 = x101.forAll(x99);
        Formula x80 = x81.and(x98);
        Formula x77 = x80.forAll(x78);
        Expression x109 = x14.join(Expression.UNIV);
        Expression x108 = x109.join(Expression.UNIV);
        Formula x107 = x108.in(x10);
        Variable x112 = Variable.unary("p1_this");
        Decls x111 = x112.oneOf(x11);
        Expression x114 = x112.join(x15);
        Expression x117 = x6.union(x7);
        Expression x116 = x117.union(Expression.INTS);
        Expression x119 = x6.union(x7);
        Expression x118 = x119.union(Expression.INTS);
        Expression x115 = x116.product(x118);
        Formula x113 = x114.in(x115);
        Formula x110 = x113.forAll(x111);
        Expression x122 = x15.join(Expression.UNIV);
        Expression x121 = x122.join(Expression.UNIV);
        Formula x120 = x121.in(x11);
        Variable x126 = Variable.unary("p1_r1");
        Decls x125 = x126.oneOf(x8);
        Variable x129 = Variable.unary("p1_x");
        Expression x130 = x6.union(x7);
        Decls x128 = x129.oneOf(x130);
        Expression x133 = x126.join(x12);
        Expression x132 = x129.join(x133);
        Formula x131 = x132.one();
        Formula x127 = x131.forAll(x128);
        Formula x124 = x127.forAll(x125);
        Formula x123 = x124.not();
        Formula x134 = x0.eq(x0);
        Formula x135 = x1.eq(x1);
        Formula x136 = x2.eq(x2);
        Formula x137 = x3.eq(x3);
        Formula x138 = x4.eq(x4);
        Formula x139 = x5.eq(x5);
        Formula x140 = x6.eq(x6);
        Formula x141 = x7.eq(x7);
        Formula x142 = x8.eq(x8);
        Formula x143 = x9.eq(x9);
        Formula x144 = x10.eq(x10);
        Formula x145 = x11.eq(x11);
        Formula x146 = x12.eq(x12);
        Formula x147 = x13.eq(x13);
        Formula x148 = x14.eq(x14);
        Formula x149 = x15.eq(x15);
        Formula x16 = Formula.compose(FormulaOperator.AND, x17, x41, x45, x74, x77, x107, x110,
                x120, x123, x134, x135, x136, x137, x138, x139, x140, x141, x142, x143, x144, x145,
                x146, x147, x148, x149);

        Solver solver = new Solver();
        solver.options().setSolver(SATFactory.DefaultSAT4J);
        solver.options().setBitwidth(4);
        solver.options().setFlatten(false);
        solver.options().setIntEncoding(Options.IntEncoding.TWOSCOMPLEMENT);
        solver.options().setSymmetryBreaking(20);
        solver.options().setSkolemDepth(0);
        System.out.println("Solving...");
        System.out.flush();
        Solution sol = solver.solve(x16, bounds);
        System.out.println(sol.toString());
	}

}
