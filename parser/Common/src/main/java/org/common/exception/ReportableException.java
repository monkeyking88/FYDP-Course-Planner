package org.common.exception;

import org.common.audit.AuditResult.AuditOutcome;
import org.common.audit.AuditResult.AuditSingleResult;

/**
 * @author mattyang
 * 
 *         Runtime exception specially designed so when caught by upper layer,
 *         minimum reporting information can be generated
 *
 */
public class ReportableException extends CommonException {

	private static final long serialVersionUID = 5048899709315577723L;

	protected AuditOutcome outcome = AuditOutcome.ERROR; // what audit outcome
															// to show user
	protected String explanation = ""; // optional detailed explanation of the
										// situation
	protected boolean shouldLog = true; // should we log this exception

	@SuppressWarnings("unused")
	private ReportableException() {
		// forbid use, used for serialization if needed only
	}

	public ReportableException(String message) {
		super(message);
	}

	public ReportableException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuditOutcome getOutcome() {
		return outcome;
	}

	public String getExplanation() {
		return explanation;
	}

	public boolean isShouldLog() {
		return shouldLog;
	}

	// builder should make life easier, we encourage adding more information to exceptions whenever possible
	public ReportableException setOutcome(AuditOutcome outcome) {
		this.outcome = outcome;
		return this;
	}

	public ReportableException setExplanation(String explanation) {
		this.explanation = explanation;
		return this;
	}

	public ReportableException setShouldLog(boolean shouldLog) {
		this.shouldLog = shouldLog;
		return this;
	}

	public AuditSingleResult report() {
		String msg = (this.getMessage() == null || this.getMessage().length() == 0) ? "ERROR" : this.getMessage();
		String explanation = msg + "\n" + this.getExplanation();
		return new AuditSingleResult(this.outcome, explanation, null);
	}
}
