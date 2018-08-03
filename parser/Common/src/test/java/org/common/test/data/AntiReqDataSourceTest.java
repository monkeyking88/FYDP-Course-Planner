package org.common.test.data;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.common.course.AntiRequisiteDataSource;
import org.common.course.Course;
import org.common.course.CourseDataSource;
import org.junit.Test;

public class AntiReqDataSourceTest {

	@Test
	public void test() {
		AntiRequisiteDataSource.testPrint();
	}

	@Test
	public void testCrosslist() {
		List<Course> trans = new ArrayList<Course>();
		trans.add(CourseDataSource.Defs.getDefByCourseName("PHYS365"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("AMATH332"));
		assertTrue(AntiRequisiteDataSource.hasAntiReq(trans).entrySet().size() > 0);
		try {
			AntiRequisiteDataSource.forceAntiReqFail(trans);
			fail();
		} catch (RuntimeException e) {}
	}

	@Test
	public void testMore() {
		List<Course> trans = new ArrayList<Course>();
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS135"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS136"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS251"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS240"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS241"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS245"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS246"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS350"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS343"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS341"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS349"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS444"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS452"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS448"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("MATH239"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("MATH135"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("MATH136"));
		assertTrue(AntiRequisiteDataSource.hasAntiReq(trans).entrySet().size() == 0);
		AntiRequisiteDataSource.forceAntiReqFail(trans);
		
		
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS115"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS121")); //does not exist in courseDef yet
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS251"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS240"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("GENE344"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS246"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("MATH145"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("ECE250"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("SE240"));
		assertTrue(AntiRequisiteDataSource.hasAntiReq(trans).entrySet().size() > 0);
		try {
			AntiRequisiteDataSource.forceAntiReqFail(trans);
			fail();
		} catch (RuntimeException e) {}
		
		trans = new ArrayList<Course>();
		Course c100 = CourseDataSource.Defs.getDefByCourseName("CS100");
		trans.add(c100);
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS136"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS251"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS240"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS241"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS245"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS246"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS350"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("CS448"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("MATH239"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("MATH135"));
		trans.add(CourseDataSource.Defs.getDefByCourseName("MATH136"));
		assertTrue(AntiRequisiteDataSource.hasAntiReq(trans).entrySet().size() > 0);
		try {
			AntiRequisiteDataSource.forceAntiReqFail(trans);
			fail();
		} catch (RuntimeException e) {}
		
		
		trans.remove(c100);
		assertTrue(AntiRequisiteDataSource.hasAntiReq(trans).entrySet().size() == 0);
		AntiRequisiteDataSource.forceAntiReqFail(trans);
		
	}

}
