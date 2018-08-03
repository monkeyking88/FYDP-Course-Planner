open courseReq_SE_2016

//Hard coded transcript of a student
fun transcript_20310541 [] : set CourseReq_SE_2016 {
    CoreCourse +
    CS492 + //no need list A
    ENGL109 +
    CS360 +     //extra ate
//	ECE327 +   //extra ate
//   CS462 +  // moreatm
	CO487 +   // moreatm 
    ECE409 +  //ece ate
    CS442 + // cs ate
 //   ECON102 + //list C
   ECON202 + //list C
    PHIL315 +  //list C Phil or gene
//	GENE412 + //list C Phil or gene
	SCI238  +
    //SCI215 +
	EARTH121 +
	EARTH121L
}

//Making an exception for course substitution ECE100 for SE101
/*
fun substitutions [] : List -> Course -> Course {
	Core -> ECE100 -> SE101
}
*/
