package org.common.reporting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.common.course.Course;
import org.common.transcript.Transcript;

public class TranscriptReport implements Serializable {

	private static final long serialVersionUID = -3451345947879657009L;

	@JsonProperty("notFoundList")
	private List<String> notFoundList = new ArrayList<String>();
	
	@JsonProperty("discardedList")
	private Map<String, String> discardedList = new LinkedHashMap<String, String>();
	
	@JsonProperty("antiReqMap")
	private Map<Course, List<Course>> antiReqMap = new LinkedHashMap<Course, List<Course>>();
	
	@JsonProperty("transcript")
	private Transcript transcript;

	public TranscriptReport() {
		super();
	}

	public TranscriptReport(List<String> notFoundList, Map<String, String> discardedList, Map<Course, List<Course>> antiReqMap) {
		super();
		this.notFoundList = notFoundList;
		this.discardedList = discardedList;
		this.antiReqMap = antiReqMap;
	}
	
	public TranscriptReport(List<String> notFoundList, Map<String, String> discardedList, Map<Course, List<Course>> antiReqMap, Transcript transcript) {
		super();
		this.notFoundList = notFoundList;
		this.discardedList = discardedList;
		this.antiReqMap = antiReqMap;
		this.transcript = transcript;
	}

	public List<String> getNotFoundList() {
		return notFoundList;
	}

	public TranscriptReport setNotFoundList(List<String> notFoundList) {
		this.notFoundList = notFoundList;
		return this;
	}

	public Map<String, String> getDiscardedList() {
		return discardedList;
	}

	public TranscriptReport setDiscardedList(Map<String, String> discardedList) {
		this.discardedList = discardedList;
		return this;
	}

	public Map<Course, List<Course>> getAntiReqMap() {
		return antiReqMap;
	}

	public TranscriptReport setAntiReqMap(Map<Course, List<Course>> antiReqMap) {
		this.antiReqMap = antiReqMap;
		return this;
	}
	
	public Transcript getTranscript() {
		return transcript;
	}

	public TranscriptReport setTranscript(Transcript transcript) {
		this.transcript = transcript;
		return this;
	}

	@Override
	public String toString() {
		String str = "Transcript parsing finished\n";
		
		if (this.notFoundList.size() > 0) {
			str += "[WARNING!] Courses not found in our definition:\n";
			for (String s : this.notFoundList) {
				str += "    " + s + "\n";
			}
		}
		if (this.antiReqMap.size() > 0) {
			str += "[WARNING!] Transcript contains potential antirequisites:\n";
			for (Course course : this.antiReqMap.keySet()) {
				str += "    " + course.getName() + " has anti requisites: ";
				List<Course> cList = this.antiReqMap.get(course);
				for (Course antiReqC : cList) {
					str += antiReqC.getName() + " ";
				}
				str += "\n";
			}
		}
		if (this.discardedList.size() > 0) {
			str += "Courses discarded:\n";
			for (Map.Entry<String, String> e: discardedList.entrySet()) {
				str += "    " + e.getKey() + " : " + e.getValue() + "\n";
			}
		}
		str += this.transcript.toString();
		
		return str;
	}

}
