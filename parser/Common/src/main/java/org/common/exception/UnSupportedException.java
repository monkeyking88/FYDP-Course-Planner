package org.common.exception;

public class UnSupportedException extends ReportableException {

	private static final long serialVersionUID = -743486875771013364L;
	
	public UnSupportedException(String feature){
		super("Feature: [" + feature + "] is not yet supported");
	}

}
