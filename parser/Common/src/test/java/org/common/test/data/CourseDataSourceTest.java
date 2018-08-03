package org.common.test.data;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.common.course.Course;
import org.common.course.CourseDataSource;
import org.common.exception.UnSupportedException;
import org.junit.Test;

public class CourseDataSourceTest {

	@Test
	public void basicTest() {
		Course ece124 = CourseDataSource.Defs.getDefByCourseName("ECE124");
		assertTrue(ece124.getId() == 13168);
		
		Course cs343 = CourseDataSource.Defs.getDefById(11417);
		assertTrue(cs343.getName().equals("CS343"));
	}
	
	@Test
	public void rangeTest(){
		List<Course> result = CourseDataSource.Defs.getDefByCatalogRange("CS", "135", "138");
		assertTrue(result.size() == 4);
		
		result = CourseDataSource.Defs.getDefByCatalogRange("CS", "444", "499R");
		List<Course> another = CourseDataSource.Defs.getDefByCatalogRange("CS", "444", "499T");
		assertTrue(another.size() - result.size() == 1);		
	}
	
	@Test
	public void wildCardTest(){
		List<Course> result = CourseDataSource.Defs.getDefByCatalogWildCard("CS", "13");
		
		//will be 135, 136, 137, 138
		assertTrue(result.size() == 4);
		
		try{
			result = CourseDataSource.Defs.getDefByTopicWildCard("CS", "137", "");
			
			//should have thrown exception
			fail();
		} catch (UnSupportedException e){
			//expected, do nothing
		}
		
		result = CourseDataSource.Defs.getDefByCatalogWildCard("CS", null);
		List<Course> another = CourseDataSource.Defs.getDefByCatalogWildCard("CS", "");
		assertTrue(result.equals(another));
		
		
		result = CourseDataSource.Defs.getDefByCatalogWildCard("CS", "1");
		assertTrue(result.size() == 9);
		
		List<Course> mask = new ArrayList<Course>();
		mask.addAll(result);
		mask.remove(6);
		mask.remove(3);
		mask.remove(0);
		Set<Course> maskSet = new TreeSet<Course>(mask);
		
		assertTrue(CourseDataSource.Defs.getDefByCatalogWildCard(maskSet, "CS", "").size() == 6);
		
		List<Integer> idMask = new ArrayList<Integer>();
		idMask.add(mask.get(0).getId());
		idMask.add(mask.get(2).getId());
		idMask.add(mask.get(4).getId());
		
		result = CourseDataSource.Defs.getDefByCatalogWildCard(idMask, "CS", "2");
		assertTrue(result.size() == 0);
		
		result = CourseDataSource.Defs.getDefByCatalogWildCard(idMask, "CS", "1");
		assertTrue(result.size() == 3);
		assertTrue(result.get(0).getId().equals(mask.get(0).getId()));
		assertTrue(result.get(1).getId().equals(mask.get(2).getId()));
		assertTrue(result.get(2).getId().equals(mask.get(4).getId()));
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void wildCardTest2(){
		List<Course> result = CourseDataSource.Defs.getDefByCatalogWildCard("ARTS", "");
	}

}
