open courseReq_SE_2016
open sampleSE2016Transcript

//Requests a result
one sig Result {
    rawresult : set CourseReq_SE_2016
}
fun result : set CourseReq_SE_2016 { Result.rawresult }

pred SEPlan2016 {
	upperbound
	//Here is list of minimum requirments 
	-- all core courses (or substitutions)
    allOf[CoreCourse]

	-- ATE
    oneOf[ATECourseCS]
    oneOf[ATECourseECE]
    threeOf[ATECourseCS + ATECourseECE + ATECourseMore]
    fourOf[ATECourseCS + ATECourseECE + ATECourseMore + ATECourseExtra]
    // having 2 ATECourseExtra and 1 CS and 1 ECE does not pass
    no disjoint x, y : ATECourseExtra & result | x != y


    -- Science -- need to make more efficient to have labs
    twoOf[ScienceCourse] or (twoOf[Earth121andLab] and  oneOf[ScienceCourse]) 
										or (twoOf[Chem262andLab] and  oneOf[ScienceCourse]) 
										or (twoOf[Bio130andLab] and oneOf[ScienceCourse])
										or (twoOf[Earth121andLab] and  twoOf[Chem262andLab])
										or (twoOf[Earth121andLab] and  twoOf[Bio130andLab])
										or (twoOf[Chem262andLab] and  twoOf[Bio130andLab])
   
	 -- Linkage Electives
    oneOf[CS492 + ComplementaryCourseA]
    oneOf[CommunicationCourse] 
    twoOf[ComplementaryCourseC] or (oneOf[ComplementaryCourseC_PhilorGene] 
													   and oneOf[ComplementaryCourseC])

}

pred allOf[ s : set CourseReq_SE_2016 ] {
    s in result
}

pred oneOf[ s : set CourseReq_SE_2016 ] {
    some c : s | c in result
}

pred twoOf[ s : set CourseReq_SE_2016 ] {
    some disjoint x, y : s & result | x != y
}

pred threeOf[ s : set CourseReq_SE_2016 ] {
    some disjoint x, y, z : s & result | x != y and x != z
}

pred fourOf[ s : set CourseReq_SE_2016 ] {
    some disjoint w, x, y, z : s & result | x != y and x != z and x != w
}



pred upperbound {
	-- result constrained by transcript
	result in  transcript_20310541
}

-- run the SE 2016 Plan
run SEPlan2016

