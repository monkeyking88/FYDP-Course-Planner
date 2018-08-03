open courseReq_SE_2016
open sampleSE2016Transcript

//Requests a result
one sig Result {
    rawresult : set CourseReq_SE_2016
}
fun result : set CourseReq_SE_2016 { Result.rawresult }

pred SEPlan2016 {
	upperbound{	result in  transcript_20310542}
	//Here is list of minimum requirments 
	-- all core courses (or substitutions)
    allOf[CoreCourse]{ all c : CoreCourse| c in result}

	-- ATE
    oneOf[ATECourseCS]{some c : ATECourseCS | c in result}
    oneOf[ATECourseECE]{some c : ATECourseECE | c in result}
    threeOf[ATECourseCS + ATECourseECE + ATECourseMore] {some disjoint w, x, y: (ATECourseCS + ATECourseECE + ATECourseMore) & result | x != y and x!= w}
    fourOf[ATECourseCS + ATECourseECE + ATECourseMore + ATECourseExtra] {some disjoint w, x, y, z: (ATECourseCS + ATECourseECE + ATECourseMore + ATECourseExtra) & result | x != y and x != z and x != w}
    // having 2 ATECourseExtra and 1 CS and 1 ECE does not pass
    no disjoint x, y : ATECourseExtra & result | x != y


    -- Science -- need to make more efficient to have labs
   {twoOf[ScienceCourse]{ some disjoint x, y : ScienceCourse& result | x != y} } or  {twoOf[Earth121andLab]{some disjoint x, y : Earth121andLab & result | x != y} and  oneOf[ScienceCourse] {some c : ScienceCourse | c in result}}
										or {twoOf[Chem262andLab]{ some disjoint x, y : Chem262andLab& result | x != y} and  oneOf[ScienceCourse]{some c : ScienceCourse | c in result}}
										or {twoOf[Bio130andLab]{ some disjoint x, y : Bio130andLab& result | x != y} and oneOf[ScienceCourse]{some c : ScienceCourse | c in result}}
										//do not need earth 121 and lab only need earth 121
										//or {twoOf[Earth121andLab]{some disjoint x, y : Earth121andLab& result | x != y} and  twoOf[Chem262andLab]{some disjoint x, y : Chem262andLab& result | x != y}}
										//or {twoOf[Earth121andLab]{some disjoint x, y : Earth121andLab& result | x != y} and  twoOf[Bio130andLab]{some disjoint x, y : Bio130andLab& result | x != y}}
										or {twoOf[Chem262andLab]{some disjoint x, y : Chem262andLab& result | x != y} and  twoOf[Bio130andLab]{some disjoint x, y : Bio130andLab& result | x != y}}
										//course substitution
										or {oneOf[ScienceCourse]{some c : ScienceCourse | c in result} and  oneOf[Bio139orBio239]{some c : Bio139orBio239 | c in result}}
										or {twoOf[Earth121andLab]{some disjoint x, y : Earth121andLab& result | x != y} and  oneOf[Bio139orBio239]{some c : Bio139orBio239 | c in result}}
										or {twoOf[Chem262andLab]{some disjoint x, y : Chem262andLab& result | x != y} and  oneOf[Bio139orBio239]{some c : Bio139orBio239 | c in result}}
										or {twoOf[Bio130andLab]{some disjoint x, y : Bio130andLab& result | x != y} and oneOf[Bio139orBio239]{some c : Bio139orBio239 | c in result}}

										or {oneOf[Bio139orBio239]{some c : Bio139orBio239 | c in result} and  oneOf[Bio140orBio240]{some c : Bio140orBio240 | c in result}}
										//course substitution
										or {oneOf[ScienceCourse] {some c : ScienceCourse | c in result} and  oneOf[Bio140orBio240] {some c : Bio140orBio240 | c in result}}
										//or {twoOf[Earth121andLab]{some disjoint x, y : Earth121andLab& result | x != y} and  oneOf[Bio140orBio240]{some c : Bio140orBio240 | c in result}}
										or {twoOf[Bio130andLab]{some disjoint x, y : Bio130andLab& result | x != y} and  oneOf[Bio140orBio240] {some c : Bio140orBio240 | c in result}}
										or {twoOf[Chem262andLab]{some disjoint x, y : Chem262andLab& result | x != y}  and  oneOf[Bio140orBio240] {some c : Bio140orBio240 | c in result}}
   
										
	 -- Linkage Electives
    oneOf[CS492 + ComplementaryCourseA] {some c : (CS492 + ComplementaryCourseA )| c in result}
    oneOf[CommunicationCourse]{some c : CommunicationCourse | c in result}

    {twoOf[ComplementaryCourseC]{some disjoint x, y : ComplementaryCourseC& result | x != y}} or {oneOf[ComplementaryCourseC_PhilorGene]{some c : ComplementaryCourseC_PhilorGene | c in result}
													    and oneOf[ComplementaryCourseC]{some c : ComplementaryCourseC | c in result}}

}

pred allOf[ s : set CourseReq_SE_2016 ] {
 //   s in result
}

pred oneOf[ s : set CourseReq_SE_2016 ] {
   // some c : s | c in result
}

pred twoOf[ s : set CourseReq_SE_2016 ] {
   // some disjoint x, y : s & result | x != y
}

pred threeOf[ s : set CourseReq_SE_2016 ] {
   // some disjoint x, y, z : s & result | x != y and x != z
}

pred fourOf[ s : set CourseReq_SE_2016 ] {
//    some disjoint w, x, y, z : s & result | x != y and x != z and x != w
}



pred upperbound {
	-- result constrained by transcript
	//result in  transcript_20310541
}

-- run the SE 2016 Plan
run SEPlan2016

