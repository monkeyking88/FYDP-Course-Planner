package org.common.course.utilities;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class CourseUtilities {
	
	public static final String CourseMetaDataPrefix = "//[COURSEMETADATASECTION]";

	//Jackson's mapper is thread safe
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static String IdsToJson(List<Integer> ids) throws JsonGenerationException, JsonMappingException, IOException{
		String json = mapper.writeValueAsString(ids);
		return json;
	}
	
	public static List<Integer> JsonToIds(String jsonIds) throws JsonParseException, JsonMappingException, IOException{
		List<Integer> ids = Arrays.asList(mapper.readValue(jsonIds, Integer[].class));
		return ids;
	}
	
	public static String IdsToCourseMeta(List<Integer> ids) throws JsonGenerationException, JsonMappingException, IOException {
		return CourseMetaDataPrefix + IdsToJson(ids);
	}
	
	public static List<Integer> CourseMetaToIds(String meta) throws JsonParseException, JsonMappingException, IOException{
		String jsonIds = meta.substring(CourseMetaDataPrefix.length());
		return JsonToIds(jsonIds);
	}
	
	
}
