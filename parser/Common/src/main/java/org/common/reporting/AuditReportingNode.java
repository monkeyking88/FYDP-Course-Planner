package org.common.reporting;

public interface AuditReportingNode {
	
	int getCount();
	
	String toStringWithIndent(int depth);
	
}
