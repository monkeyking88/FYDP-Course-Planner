package org.language.codegenerator;

import java.util.Collection;
import java.util.LinkedList;

public class CourseIdArray {
	
	private Collection<CourseId> plusArray = new LinkedList<CourseId>();
	private Collection<CourseId> minusArray = new LinkedList<CourseId>();
	private String id;
	
	// private boolean hasPattern = false;
	
	public CourseIdArray(){}
	
	public CourseIdArray(String id){
		this.id = id;
	}
	
	public Collection<CourseId> getPlusArray() {
		return plusArray;
	}

	public Collection<CourseId> getMinusArray() {
		return minusArray;
	}
	
	public boolean removePlus(CourseId courseId){
		return plusArray.remove(courseId);
	}
	
	public boolean removeMinus(CourseId courseId){
		return minusArray.remove(courseId);
	}

	public boolean addPlus(CourseId courseId){
		return plusArray.add(courseId);
	}
	
	public boolean addAllPlus(Collection<CourseId> courseIds){
		return plusArray.addAll(courseIds);
	}
	
	public boolean addMinus(CourseId courseId){
		return minusArray.add(courseId);
	}
	
	public boolean addAllMinus(Collection<CourseId> courseIds){
		return minusArray.addAll(courseIds);
	}
	
	public void clearPlus(){
		plusArray.clear();
	}
	
	public void clearMinus(){
		minusArray.clear();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}