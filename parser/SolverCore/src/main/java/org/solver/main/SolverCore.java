package org.solver.main;

import java.util.List;
import java.util.Map;

import org.common.audit.AuditResult;
import org.common.audit.AuditResult.AuditOutcome;
import org.common.audit.AuditResult.AuditSingleResult;
import org.common.course.Course;
import org.solver.parser.RuleCourseMappingParser;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4viz.VizGUI;

public class SolverCore {

	A4Options	opt	= new A4Options();

	public AuditSingleResult handle(String filename, boolean showFile) throws Err {

		// The visualizer (We will initialize it to nonnull when we visualize an Alloy solution)
		@SuppressWarnings("unused")
		VizGUI viz = null;

		A4Reporter rep = new A4Reporter() {
			@Override
			public void warning(ErrorWarning msg) {
				System.out.print("Relevance Warning:\n" + (msg.toString().trim()) + "\n\n");
				System.out.flush();
			}
		};
		
		if (showFile) {
			System.out.println("\n=========== Parsing+Typechecking " + filename + " =============");
		} else {
			System.out.print("...");
		}
		
		Module world = CompUtil.parseEverything_fromFile(rep, null, filename);

		A4Options options = new A4Options();
		options.solver = A4Options.SatSolver.SAT4J;

		if (world.getAllCommands().size() != 1) {
			throw new RuntimeException("Invalid world command size: " + world.getAllCommands().size());
		}

		A4Solution ans = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), world.getAllCommands().get(0), options);
		if (!ans.satisfiable()) {
			return new AuditSingleResult(AuditOutcome.FAIL, "Graduation requirements not met", null);
		}
		
		Map<String, List<String>> rawMap = ans.reportCourseUsageWhenSatisfied();
		Map<String, List<Course>> courseMapping = RuleCourseMappingParser.parse(rawMap);
		return new AuditSingleResult(AuditOutcome.GRADUATE, "All graduation requirements satisfied", courseMapping);
	}

}
