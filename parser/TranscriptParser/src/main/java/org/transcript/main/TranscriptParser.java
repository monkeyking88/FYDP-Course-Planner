package org.transcript.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.NumberFormatException;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.JSONException;
import org.common.course.AntiRequisiteDataSource;
import org.common.course.Course;
import org.common.course.CourseDataSource;
import org.common.course.utilities.CourseUtilities;
import org.common.helper.Helper;
import org.common.reporting.TranscriptReport;
// import org.common.reporting.TranscriptReport.TranscriptOutcome;
import org.common.transcript.Plan;
import org.common.transcript.Transcript;

public class TranscriptParser {

	private static final Map<String, String> milestoneCourseMap;
    static {
    	HashMap<String, String> temp = new HashMap<String, String>();
    	temp.put("UENGLPROF", "ELPE100");
    	temp.put("WHMIS", "WHMIS1X000");
    	temp.put("WKRPT1", "WKRPT100");
    	temp.put("WKRPT2", "WKRPT200");
    	temp.put("WKRPT3", "WKRPT300");
    	temp.put("WKRPT4", "WKRPT400");
    	temp.put("WKTRM1", "COOP1");
    	temp.put("WKTRM2", "COOP2");
    	temp.put("WKTRM3", "COOP3");
    	temp.put("WKTRM4", "COOP4");
    	temp.put("WKTRM5", "COOP5");
    	temp.put("WKTRM6", "COOP6");
    	milestoneCourseMap = Collections.unmodifiableMap(temp);
    }
    
	private static boolean valid_pass(JSONObject courseInfo) throws JSONException {
		if (courseInfo.get("course_grade") != null && courseInfo.get("details") != null) {
			JSONObject details = (JSONObject) courseInfo.get("details");
			String grading_basis = details.get("grading_basis").toString();
			String course_grade = courseInfo.get("course_grade").toString();
			if (grading_basis.equals("NUM") || grading_basis.equals("DRN") || grading_basis.equals("CLR")) {
				try {
					int grade;
					try {
						grade = Integer.parseInt(course_grade);
					} catch (NumberFormatException e) {
						grade = 0;
					}
					if (grade >= 50) {
						return true;
					}
				} catch (Exception e) {
					return false;
				}
			} else if (grading_basis.equals("CNC") || grading_basis.equals("CNP") || grading_basis.equals("CNW") || grading_basis.equals("NGP")) {
				if (course_grade.equals("CR")) {
					return true;
				}
			} else if (grading_basis.equals("TRN") || grading_basis.equals("CLF")) {
				return true;
			} else if (course_grade.startsWith("A") || course_grade.startsWith("B") || course_grade.startsWith("C") || course_grade.startsWith("D")) {
				return true;
			}
		}
		return false;
	}
	
	private static String failReason(JSONObject courseInfo) throws JSONException {
		if (valid_pass(courseInfo)) {
			return "This is a passed course";
		}
		
		if (courseInfo.get("subject_code").toString().equals("SEQ")) {
			return "Sequence information, not an actual course";
		}
		
		if (courseInfo.get("course_grade") != null && courseInfo.get("details") != null) {
			JSONObject details = (JSONObject) courseInfo.get("details");
			String grading_basis = details.get("grading_basis").toString();
			String course_grade = courseInfo.get("course_grade").toString();
			if (grading_basis.equals("NUM") || grading_basis.equals("DRN") || grading_basis.equals("MEX")) {
				return "Course grade is " + course_grade;
			} else if (grading_basis.equals("CNC") || grading_basis.equals("CNP") || grading_basis.equals("CNW") || grading_basis.equals("NGP")) {
				if (!course_grade.equals("CR")) {
					return "This " + grading_basis + " course is not credited";
				}
			} else if (grading_basis.equals("ABC")) {
				if (!course_grade.startsWith("A") && !course_grade.startsWith("B") && !course_grade.startsWith("C") && !course_grade.startsWith("D")) {
					return "This course has grade \"" + course_grade + "\" and doesn't considered as PASS in ABC grading basis";
				}
			} else if (grading_basis.equals("CLC") || grading_basis.equals("CLR")) {
				return "This course is " + grading_basis;
			}
		}
		if (courseInfo.get("course_grade") == null){
			return "Course grade missing (maybe because of incomplete)";
		}
		return "Course data corrupted";
	}

	int getYearFromTermId(int termId) {
		return (termId - 8) / 10 - 100 + 2000;
	}

	public TranscriptReport handle(InputStream in, String jsonStr, OutputStream out) throws Exception {
		if (in != null) {
			return handle(in, out);
		} else {
			return handle(jsonStr, out);
		}
	}

	public TranscriptReport handle(InputStream in, OutputStream out) throws Exception {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(in));
			String jsonStr = "";
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				jsonStr += line + "\n";
			}
			return handle(jsonStr, out);
		} catch (Exception e) {
			throw e;
		}
		//no need to close the reader here
	}

	// hacky version that takes a transcript in JSON string
	public TranscriptReport handle(String jsonStr, OutputStream out) throws Exception {
		BufferedWriter bufferedWriter = null;

		try {
			List<Integer> course_ids = new ArrayList<Integer>();
			List<Course> course_real = new ArrayList<Course>();
			String alloy_code = "\n\nfun transcript [] : set Course {\n    CoreCourse";
			String course_name = null;
			TranscriptReport report = new TranscriptReport();

			bufferedWriter = new BufferedWriter(new OutputStreamWriter(out));
			JSONObject json = (JSONObject) new JSONParser().parse(jsonStr);
			JSONArray courses = (JSONArray) json.get("courses");
			JSONArray milestones = (JSONArray) json.get("milestones");
			JSONArray plans_raw = (JSONArray) json.get("plans");
			Long uw_id = (Long) json.get("uw_id");
			List<Plan> plans = new ArrayList<Plan>();
			
			//read the plans from transcript
			JSONObject obj;
			for (int i = 0; i < plans_raw.size(); ++i) {
				obj = (JSONObject) plans_raw.get(i);
				Plan plan = new Plan(getYearFromTermId(Helper.toInt(obj.get("requirement_term_id"))), // year, hacked of course
								Helper.toInt(obj.get("plan_position")), // position
								obj.get("plan_code").toString());
				plans.add(plan);
			}

			//read the courses from transcript
			JSONObject courseInfo;
			for (int i = 0; i < courses.size(); ++i) {
				courseInfo = (JSONObject) courses.get(i);
				course_name = courseInfo.get("subject_code").toString() + courseInfo.get("catalog").toString();
				if (valid_pass(courseInfo)) {
					Course target_course = CourseDataSource.Defs.getDefByCourseName(course_name);
					if (target_course != null) {
						course_ids.add(target_course.getId());
						course_real.add(target_course);
						alloy_code += " +\n    " + course_name;
					} else {
						report.getNotFoundList().add(course_name);
						// report.setOutcome(TranscriptOutcome.FAIL);
					}
				} else {
					report.getDiscardedList().put(course_name, failReason(courseInfo));
				}
			}
			JSONObject milestoneInfo;
			String milestoneName;
			for (int i = 0; milestones != null && i < milestones.size(); ++i) {
				milestoneInfo = (JSONObject) milestones.get(i);
				milestoneName = milestoneInfo.get("milestone_code").toString();
				course_name = milestoneCourseMap.get(milestoneName);
				if (course_name != null && milestoneInfo.get("milestone_complete").toString().equals("Y")) {
					Course target_course = CourseDataSource.Defs.getDefByCourseName(course_name);
					if (target_course != null) {
						course_ids.add(target_course.getId());
						course_real.add(target_course);
						alloy_code += " +\n    " + course_name;
					} else {
						report.getNotFoundList().add(course_name);
						// report.setOutcome(TranscriptOutcome.FAIL);
					}
				}
			}
			alloy_code += "\n}\n";
			//check for anti reqs
			Map<Course, List<Course>> antiReqMap = AntiRequisiteDataSource.hasAntiReqWithId(course_ids);
			if (antiReqMap != null && antiReqMap.size() > 0) {
				report.setAntiReqMap(antiReqMap);
			}

			bufferedWriter.write(CourseUtilities.IdsToCourseMeta(course_ids));
			bufferedWriter.write(alloy_code);

			Transcript transcript = new Transcript();
			transcript.setPlans(plans).setCourseIds(course_ids).setCourses(course_real).setId(uw_id);
			return report.setTranscript(transcript);
		} catch (IOException | ParseException | JSONException e) {
			throw e;
		} finally {
			try {
				bufferedWriter.flush();
			} catch (IOException e) {
				throw e;
			}
		}
	}
}
