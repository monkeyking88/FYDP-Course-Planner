package org.auditor.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.common.course.Course;
import org.common.exception.ReportableException;
import org.common.reporting.AuditReportUnconditionalLeaf;
import org.common.reporting.AuditReportingLeaf;
import org.common.reporting.AuditReportingNode;
import org.common.reporting.AuditReportingTrunk;
import org.common.reporting.TranscriptReport;
import org.language.blocktree.AbstractBlockLeaf;
import org.language.blocktree.AbstractBlockTrunk;
import org.language.blocktree.BlockTree;
import org.language.blocktree.BlockTreeVisitor;
import org.language.blocktree.ConditionBlockTrunk;
import org.language.blocktree.CourseBlockLeaf;
import org.language.blocktree.CourseBlockTrunk;
import org.language.blocktree.InfeasibleBlockTrunk;
import org.language.blocktree.UnconditionalFeasibleBlockTrunk;
import org.language.blocktree.UnitBlockLeaf;
import org.language.blocktree.UnitBlockTrunk;
import org.solver.parser.RuleCourseMappingParser;

/**
 * 
 * @author mattyang
 * 
 *         this visitor takes course mapping, walks block tree, and generates
 *         course tree
 */
public class AuditReportingTreeGenerator implements BlockTreeVisitor {

	Map<String, List<Course>> courseMapping;

	TranscriptReport transReport;
	
	List<AuditReportingNode> courseTreeForest;

	public AuditReportingTreeGenerator(Map<String, List<Course>> courseMapping, TranscriptReport report) {
		super();
		if (courseMapping == null) {
			throw new ReportableException("Course Mapping is null when trying to generate audit reporting tree");
		}
		this.transReport = report;
		this.courseMapping = courseMapping;
		this.courseTreeForest = null;
	}

	public List<AuditReportingNode> getCourseTreeForest() {
		return this.courseTreeForest;
	}

	@Override
	public void visit(ConditionBlockTrunk tree) {
		handleTrunk(tree);
	}

	@Override
	public void visit(UnitBlockTrunk tree) {
		handleTrunk(tree);
	}

	@Override
	public void visit(CourseBlockTrunk tree) {
		handleTrunk(tree);
	}

	@Override
	public void visit(InfeasibleBlockTrunk tree) {
		handleTrunk(tree);
	}
	
	@Override
	public void visit(UnconditionalFeasibleBlockTrunk tree) {
		if (this.courseTreeForest == null) {
			this.courseTreeForest = new LinkedList<AuditReportingNode>();
		}
		AuditReportingNode reportingNode = new AuditReportUnconditionalLeaf(tree.getFullName());
		this.courseTreeForest.add(reportingNode);
	}

	@Override
	public void visit(UnitBlockLeaf leaf) {
		handleLeaf(leaf);
	}

	@Override
	public void visit(CourseBlockLeaf leaf) {
		handleLeaf(leaf);
	}

	private void handleTrunk(AbstractBlockTrunk tree) {
		if (this.courseTreeForest == null) {
			this.courseTreeForest = new LinkedList<AuditReportingNode>();
		}

		for (BlockTree child : tree.getChildren()) {
			child.accept(this);
		}
		
		//do not include root of parse tree
		if (RuleCourseMappingParser.RULE_ROOT.equals(tree.getFullName())) {
			return;
		}
		
		List<AuditReportingNode> childList = new LinkedList<AuditReportingNode>();
		for (int i = 0; i < tree.getChildren().size(); i++) {
			// removing like this keeps insertion order
			AuditReportingNode child = this.courseTreeForest.remove(this.courseTreeForest.size() - tree.getChildren().size() + i);
			childList.add(child);
		}

		AuditReportingNode reportingNode = new AuditReportingTrunk(tree.getFullName(), childList);
		this.courseTreeForest.add(reportingNode);
	}

	private void handleLeaf(AbstractBlockLeaf leaf) {
		String ruleName = leaf.getFullName();
		List<Course> courseList = courseMapping.get(ruleName);
		if (courseList == null) {
			throw new ReportableException("Block Tree leaf: " + ruleName + " not found in courseMapping");
		}
		
		List<Course> real_list = realCourseFilter(courseList);
		AuditReportingNode reportingNode = new AuditReportingLeaf(ruleName, real_list);
		this.courseTreeForest.add(reportingNode);
	}
	
	private List<Course> realCourseFilter(List<Course> courseList){
		List<Course> real_course = new ArrayList<Course>();
		List<Course> trans_course = transReport.getTranscript().getCourses();
		
		Map<Integer, Course> idMap = new HashMap<Integer, Course>();
		for (Course c : trans_course) {
			idMap.put(c.getId(), c);
		}
		for (Course c : courseList) {
			real_course.add(idMap.get(c.getId()));
		}
		
		return real_course;
	}

}
