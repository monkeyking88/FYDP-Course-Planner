package ca.uwaterloo.softeng.advisor;

import java.util.Calendar;
import java.util.List;

import ca.uwaterloo.softeng.advisor.Audit.Status;

public class Db {

	public Status planAuditStatus(Student s, String str){
		return null;
	}
	
	public Calendar planAuditDate(Student s, String str){
		return null;
	}
	
	public Calendar lastUploadDate(Student s){
		return null;
	}
	
	public Calendar lastApprovalDate(Student s){
		return null;
	}
	
	public void auditCommit(Audit a){
		return;
	}
	
	public List<String> listMatchList(int cal, String str_1, String str_2){
		return null;
	}
	
	public List<Course> courseList(Student s){
		return null;
	}
	
	public List<TransferCourse> transferCourseList(Student s){
		return null;
	}
	
	public List<Approval> stdntApprovalList(int id){
		return null;
	}
	
	
	public List<String> listList(int cal){
		return null;
	}
	
	public List<ListItem> listItemList(String listName, int cal){
		return null;
	}
	
	
	public List<Plan> planList(Student s){
		return null;
	}
	
	
	public  List<Course> courseList(Student S, Rqmt rqmt){
		return null;
	}
	
}
