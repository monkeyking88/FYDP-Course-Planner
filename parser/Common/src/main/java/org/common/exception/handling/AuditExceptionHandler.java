package org.common.exception.handling;

import org.apache.log4j.Logger;
import org.common.audit.AuditResult.AuditOutcome;
import org.common.audit.AuditResult.AuditSingleResult;
import org.common.exception.ReportableException;

public class AuditExceptionHandler implements ExceptionHandler {
	
	private Logger logger = Logger.getLogger(AuditExceptionHandler.class);
	
	@Override
	public AuditSingleResult handle(ReportableException e) {
		if (e.isShouldLog()) {
			logger.warn("ReportableException", e);
		}
		return e.report();
	}

	@Override
	public AuditSingleResult handle(Exception e) {
		if (e instanceof ReportableException) {
			ReportableException rE = (ReportableException)e;
			return handle(rE);
		}
		logger.error("GeneralException", e);
		return new AuditSingleResult(AuditOutcome.ERROR, "Error occurred during execution", null);
	}

}
