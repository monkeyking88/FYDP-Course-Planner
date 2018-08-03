package org.auditor.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.auditor.reporting.AuditReportingTreeGenerator;
import org.auditor.reporting.BlockTreeReporter;
import org.common.audit.AuditResult;
import org.common.audit.AuditResult.AuditOutcome;
import org.common.audit.AuditResult.AuditSingleResult;
import org.common.course.Course;
import org.common.exception.ReportableException;
import org.common.exception.handling.AuditExceptionHandler;
import org.common.helper.FileUtilities;
import org.common.helper.Helper;
import org.common.reporting.AuditReportingNode;
import org.common.reporting.TranscriptReport;
import org.common.transcript.Plan;
import org.language.blocktree.BlockTree;
import org.language.blocktree.BlockTreeToAlloy;
import org.language.blocktree.BlockTreeVisitor;
import org.language.codegenerator.OverrideMap;
import org.language.main.LanguageParser;
import org.solver.main.SolverCore;
import org.transcript.main.TranscriptParser;

public class Auditor {

	public AuditResult handle(InputStream json_transcriptIs) throws Exception {
		return handle(json_transcriptIs, null);
	}
	
	
	public AuditResult handle(String jsonStr) throws Exception {
		return handle(null, jsonStr);
	}
	
	public AuditResult handle(InputStream json_transcriptIs, String jsonStr) throws Exception {
		TranscriptParser transcriptParser = new TranscriptParser();
		SolverCore solverCore = new SolverCore();

		AuditResult results = new AuditResult();

		InputStream antlr_requirementIs = null;

		InputStream alloy_tempTranscriptIs = null;
		OutputStream alloy_tempTranscriptOs = null;
		InputStream alloy_requirementIs = null;
		OutputStream alloy_requirementOs = null;

		try {
			File alloy_transcriptFile = File.createTempFile("tmp_transcript", ".als");
			alloy_tempTranscriptOs = new FileOutputStream(alloy_transcriptFile);

			// parse the transcript into alloy code and store in temp file
			TranscriptReport report = null;
			try {
				report = transcriptParser.handle(json_transcriptIs, jsonStr, alloy_tempTranscriptOs);
				Helper.closeMultiple(alloy_tempTranscriptOs);
			} catch (Exception e) {
				results.addResult("Transcript Parsing", (new AuditExceptionHandler()).handle(e));
				return results;
			}
			
			//TODO this printing code should be moved to somewhere else and decouple with file path
			//==============
			System.out.println("===========  Audit Started ===========");
			System.out.print(report.toString());
			List<Plan> existingPlanList = new ArrayList<Plan>();
			List<Plan> discardedPlanList = new ArrayList<Plan>();
			for (Plan plan : report.getTranscript().getPlans()) {
				File antlr_requirementFile = new File("requirements/plans/" + plan.getCode() + "/" + plan.getYear() + ".txt");
				if (!antlr_requirementFile.exists()) {
					discardedPlanList.add(plan);
				} else {
					existingPlanList.add(plan);
				}
			}
			if (discardedPlanList.size() > 0) {
				System.out.println("Plans discarded:");
				for (Plan plan : discardedPlanList) {
					System.out.println("    " + plan.getCode() + " : Degree requirement for " + plan.getCode() + " in year " + plan.getYear() + " does not exist");
				}
			}
			//==============
			
			// TODO Main audit loop, need to handle double counting, do we just concatenate all
			AUDITLOOP: for (Plan plan : existingPlanList) {
				File antlr_requirementFile = new File("requirements/plans/" + plan.getCode() + "/" + plan.getYear() + ".txt");
				if (!antlr_requirementFile.exists()) {
					results.addResult(plan.getCode(), AuditOutcome.UNSUPPORTED, "Failed to Find Degree Requirement for " + plan.getCode() + " in year " + plan.getYear());
					continue AUDITLOOP;
				}
				
				//overrides detection
				long student_id = report.getTranscript().getId();
				File merged_requirementFile = File.createTempFile("merged_requirement" + student_id, ".als");
				File overrideFile = new File("requirements/overrides/" + student_id + ".txt");
				if (overrideFile.exists()) {
					FileUtilities.joinFiles(merged_requirementFile, antlr_requirementFile, overrideFile);
				} else {
					FileUtilities.joinFiles(merged_requirementFile, antlr_requirementFile);
				}
				
				File alloy_requirementFile = File.createTempFile("tmp_requirement", ".als");
				
				alloy_tempTranscriptIs = new FileInputStream(alloy_transcriptFile);
				antlr_requirementIs = new FileInputStream(merged_requirementFile);
				alloy_requirementOs = new FileOutputStream(alloy_requirementFile);

				BlockTree blockTree = LanguageParser.handle(plan.getCode(), antlr_requirementIs, alloy_tempTranscriptIs, alloy_requirementOs);
				Helper.closeMultiple(alloy_tempTranscriptIs, antlr_requirementIs, alloy_requirementOs);
				
				//TODO this printing code should be moved to somewhere else
				OverrideMap overrideMap = blockTree.getOverrideMap();
				if (overrideMap != null) {
					System.out.print(overrideMap.toString());
				}
				
				// everything we need for the audit is in the requirement file, pass it to Alloy solver
				try {
					AuditSingleResult result = solverCore.handle(alloy_requirementFile.getAbsolutePath(), true);
					if (result.getOutcome() == AuditOutcome.FAIL) {
						//TODO detectFailedRules will cause mutation, please ensure original tree is immutable
						detectFailedRules(blockTree, result);
						
						//TODO this is a mess, clean up
						File partial_requirementFile = File.createTempFile("tmp_partial_requirement", ".als");
						OutputStream partial_requirementOs = new FileOutputStream(partial_requirementFile);
						BlockTreeVisitor blockTreeVisitor = new BlockTreeToAlloy(partial_requirementOs);
				        blockTree.accept(blockTreeVisitor);
				        Helper.closeMultiple(partial_requirementOs);
						
						AuditSingleResult partialResult = solverCore.handle(partial_requirementFile.getAbsolutePath(), true);
						if (partialResult.getOutcome() == AuditOutcome.FAIL) {
							throw new ReportableException("Cherry-picked partial result should not fail");
						}
						result.setCourseMapping(partialResult.getCourseMapping());
					}
					if (result.getCourseMapping() != null) {
						buildAuditReportingTree(blockTree, result, report);
					}
					results.addResult(plan.getCode(), result);
				} catch (Exception e) {
					results.addResult(plan.getCode(), (new AuditExceptionHandler()).handle(e));
				}
			}
		} catch (Exception e) {
			results.addResult("Audit", (new AuditExceptionHandler()).handle(e));
		} finally {
			Helper.closeMultiple(antlr_requirementIs, alloy_tempTranscriptIs, alloy_tempTranscriptOs, alloy_requirementIs, alloy_requirementOs);
		}

		return results;
	}
	
	//assuming block tree never gets mutated, do bfs for reporting
	private void detectFailedRules(BlockTree blockTree, AuditSingleResult result) throws Exception {
		BlockTreeReporter blockTreeReporter = new BlockTreeReporter();
        blockTree.accept(blockTreeReporter);
        result.setFailedRules(blockTreeReporter.getFailedRules());
	}
	
	private void buildAuditReportingTree(BlockTree blockTree, AuditSingleResult result, TranscriptReport report) throws Exception {
		Map<String, List<Course>> courseMapping = result.getCourseMapping();
		if (courseMapping == null) {
			return;
		}
		AuditReportingTreeGenerator treeGeneratorVisitor = new AuditReportingTreeGenerator(courseMapping, report);
        blockTree.accept(treeGeneratorVisitor);
        List<AuditReportingNode> courseTreeForest = treeGeneratorVisitor.getCourseTreeForest();
        result.setCourseTreeForest(courseTreeForest);
	}
}
