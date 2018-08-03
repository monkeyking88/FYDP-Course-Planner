package org.common.exception.handling;

import org.common.audit.AuditResult.AuditSingleResult;
import org.common.exception.ReportableException;

public interface ExceptionHandler {
	
	// Take the exception and turn it into readable result for reporting of error, will log if shouldLog is set to to true
	AuditSingleResult handle(ReportableException e);
	
	// Silence any exception, will always log, produce result containing minimal information
	AuditSingleResult handle(Exception e);
	
}
