package org.common.test.data;

import static org.junit.Assert.*;

import org.common.course.Course;
import org.common.course.utilities.CourseUtilities;
import org.common.helper.Helper;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class CourseRepresentationTest {
	
	final ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void testJSON() throws JsonGenerationException, JsonMappingException, IOException {
		final InputStream jsonDefFileIs = getClass().getClassLoader().getResourceAsStream("courseDef/courseDef.json");
		
		//reading all the course definitions
		List<Course> courseDefs = Arrays.asList(mapper.readValue(jsonDefFileIs, Course[].class));
		Helper.close(jsonDefFileIs);
		
		//generating list of all the course ids
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < courseDefs.size(); i++) {
			ids.add(courseDefs.get(i).getId());
		}
		
		//System.out.println(mapper.writeValueAsString(courseDefs));
		
		//parse the ids to JSON
		String jsonIds = CourseUtilities.IdsToJson(ids);
		System.out.println(jsonIds);
		
		//parse the JSON back to ids and compare with the original ids
		List<Integer> processedIds = CourseUtilities.JsonToIds(jsonIds);
		assertTrue(processedIds.equals(ids));
		
		//test the meta to/from functions
		String metaData = CourseUtilities.IdsToCourseMeta(ids);
		System.out.println(metaData);
		List<Integer> metaIds = CourseUtilities.CourseMetaToIds(metaData);
		assertTrue(metaIds.equals(ids));
		
	}
	
	@Test
	public void testSpecial(){
		assertTrue(Course.ANY.getName().equals(Course.NAME_ANY));
		assertTrue(Course.NONE.getName().equals(Course.NAME_NONE));
	}

}
