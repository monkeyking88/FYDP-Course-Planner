package org.common.exception;

public class CourseNotFoundException extends ReportableException {

	private static final long serialVersionUID = -5523797037975454132L;

	public CourseNotFoundException() {
		super("Course not defined");
	}
	
	public CourseNotFoundException(String courseName) {
		super("Course: [" + courseName + "] is not defined");
	}
	
	public CourseNotFoundException(String subject, String catalogue) {
		super(subject + catalogue);
	}
	
	public CourseNotFoundException(int courseId) {
		super("Course with id: [" + courseId + "] is not defined");
	}
	
}
