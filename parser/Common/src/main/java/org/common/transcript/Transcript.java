package org.common.transcript;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.common.course.Course;

public class Transcript implements Serializable {

	private static final long serialVersionUID = -6999282125296771891L;
	
	public static String toJsonStr(Transcript transcript) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(transcript);
	}
	
	public static Transcript fromJsonStr(String str) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(str, Transcript.class);
	}
	
	@JsonProperty("courseIds")
	private List<Integer> courseIds;
	
	@JsonProperty("plans")
	private List<Plan> plans;
	
	@JsonProperty("courses")
	private List<Course> courses;
	
	@JsonProperty("id")
	private long id;
	
	public Transcript() {
		super();
	}

	public Transcript(List<Integer> courseIds, List<Plan> plans, List<Course> courses, long id) {
		super();
		this.courseIds = courseIds;
		this.plans = plans;
		this.courses = courses;
		this.id = id;
	}

	public List<Integer> getCourseIds() {
		return courseIds;
	}

	public Transcript setCourseIds(List<Integer> courseIds) {
		this.courseIds = courseIds;
		return this;
	}

	public List<Plan> getPlans() {
		return plans;
	}

	public Transcript setPlans(List<Plan> plans) {
		this.plans = plans;
		return this;
	}

	public long getId() {
		return id;
	}

	public Transcript setId(long id) {
		this.id = id;
		return this;
	}
	
	public List<Course> getCourses() {
		return courses;
	}

	public Transcript setCourses(List<Course> courses) {
		this.courses = courses;
		return this;
	}

	@Override
	public String toString() {
		String str = "Following plans are found in the transcript for student id [" + this.id + "]:\n";
		for (Plan plan : this.plans) {
			str += "    " + plan.toString() + "\n";
		}
		return str;
	}
	
}
