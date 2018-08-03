package org.common.exception;

/**
 * @author mattyang
 * 
 *         Common type exception intended to be root of all custom exceptions
 *         This exception draws border line between exceptions we designed and
 *         exceptions occurred due to error we did not account for
 * 
 */
public abstract class CommonException extends RuntimeException {

	private static final long serialVersionUID = 1025896247121885922L;
	
	public CommonException(){
		super();
	}
	
	public CommonException(String message){
		super(message);
	}
	
	public CommonException(String message, Throwable cause){
		super(message, cause);
	}

}
