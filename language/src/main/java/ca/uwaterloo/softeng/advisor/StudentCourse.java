package ca.uwaterloo.softeng.advisor;

/* copyright 2010 Andrew Morton */


class StudentCourse implements Comparable {
	public Student student;
	public Course course;

	public StudentCourse(Student student, Course course) {
		this.student = student;
		this.course = course;
	}

	public int compareTo(Object obj) throws ClassCastException {
		if(!(obj instanceof StudentCourse)) {
			throw new ClassCastException("A StudentCourse object expected.");
		}

		StudentCourse studentCourse = (StudentCourse)obj;

		int grade1 = this.course.getIntGrade();
		int grade2 = studentCourse.course.getIntGrade();

		if(grade1 > grade2) {
			return -1;
		} else if(grade2 > grade1) {
			return 1;
		} else {
			return this.student.getUw_id() - studentCourse.student.getUw_id();
		}
	}
}
