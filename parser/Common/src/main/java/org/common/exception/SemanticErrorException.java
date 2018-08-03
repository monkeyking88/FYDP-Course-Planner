package org.common.exception;

public class SemanticErrorException extends ReportableException {

	private static final long serialVersionUID = -3512622169706963377L;

	public SemanticErrorException(String message) {
		super(message);
	}
	
	public SemanticErrorException(String message, Throwable cause) {
		super(message, cause);
	}

}
