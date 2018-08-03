package org.common.exception.handling;

import org.common.audit.AuditResult.AuditSingleResult;
import org.common.exception.ReportableException;

public class DebugExceptionHandler implements ExceptionHandler {

	@Override
	public AuditSingleResult handle(ReportableException e) {
		e.printStackTrace();
		throw e;
	}

	@Override
	public AuditSingleResult handle(Exception e) {
		e.printStackTrace();
		throw new RuntimeException(getClass().getSimpleName(), e);
	}

}
