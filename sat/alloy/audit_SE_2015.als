open courseReq_SE_2015
open sampleSE2015Transcript

//Requests a result
one sig Result {
    rawresult : set CourseReq_SE_2015
}
fun result : set CourseReq_SE_2015 { Result.rawresult }

pred SEPlan2015 {
	upperbound
	//Here is list of minimum requirments 
	-- all core courses (or substitutions)
    allOf[CoreCourse]

	-- ATE
    oneOf[ATECourseCS300 + ATECourseCS400]
    oneOf[ATECourseECE300 + ATECourseECE400]
    twoOf[ATECourseECE400 + ATECourseCS400] or  
			(oneOf[ATECourseECE400 + ATECourseCS400] and oneOf[ATECourseExtra])
    fourOf[ATECourseCS300 + ATECourseCS400 + ATECourseECE300 + ATECourseECE400 + ATECourseExtra]
    // having 2 ATECourseExtra and 1 CS and 1 ECE does not pass
    no disjoint x, y : ATECourseExtra & result | x != y


    -- Science -- need to make more efficient to have labs
    twoOf[ScienceCourse] or (twoOf[Earth121andLab] and  oneOf[ScienceCourse]) 
										or (twoOf[Chem262andLab] and  oneOf[ScienceCourse]) 
										or (twoOf[Bio130andLab] and oneOf[ScienceCourse])
										or (twoOf[Earth121andLab] and  twoOf[Chem262andLab])
										or (twoOf[Earth121andLab] and  twoOf[Bio130andLab])
										or (twoOf[Chem262andLab] and  twoOf[Bio130andLab])
										//course substitution
										or (oneOf[ScienceCourse] and  oneOf[Bio139orBio239]) 
										or (twoOf[Earth121andLab] and  oneOf[Bio139orBio239]) 
										or (twoOf[Chem262andLab] and  oneOf[Bio139orBio239]) 
										or (twoOf[Bio130andLab] and oneOf[Bio139orBio239])

										or (oneOf[Bio139orBio239] and  oneOf[Bio140orBio240]) 
										//course substitution
										or (oneOf[ScienceCourse] and  oneOf[Bio140orBio240]) 
										or (twoOf[Earth121andLab] and  oneOf[Bio140orBio240])
										or (twoOf[Bio130andLab] and  oneOf[Bio140orBio240])
										or (twoOf[Chem262andLab] and   oneOf[Bio140orBio240])
   
	 -- Linkage Electives
    oneOf[CS492 + ComplementaryCourseA]
    oneOf[CommunicationCourse]
    twoOf[ComplementaryCourseC] or (oneOf[ComplementaryCourseC_PhilorGene]
													    and oneOf[ComplementaryCourseC])

}

pred allOf[ s : set CourseReq_SE_2015 ] {
    s in result
}

pred oneOf[ s : set CourseReq_SE_2015 ] {
    some c : s | c in result
}

pred twoOf[ s : set CourseReq_SE_2015 ] {
    some disjoint x, y : s & result | x != y
}

pred threeOf[ s : set CourseReq_SE_2015 ] {
    some disjoint x, y, z : s & result | x != y and x != z
}

pred fourOf[ s : set CourseReq_SE_2015 ] {
    some disjoint w, x, y, z : s & result | x != y and x != z and x != w
}



pred upperbound {
	-- result constrained by transcript
	result in  transcript_20310542
}

-- run the SE 2016 Plan
run SEPlan2015

