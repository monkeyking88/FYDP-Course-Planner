open courseReq_SE_2015
open sampleSE2015Transcript

//Requests a result
one sig Result {
    rawresult : set CourseReq_SE_2015
}
fun result : set CourseReq_SE_2015 { Result.rawresult }

pred SEPlan2015 {
	upperbound{	result in  transcript_20310542}
	//Here is list of minimum requirments 
	-- all core courses (or substitutions)
    allOf[CoreCourse]{ all c : CoreCourse| c in result}

	-- ATE
    oneOf[ATECourseCS300 + ATECourseCS400] {some c : (ATECourseCS300 + ATECourseCS400) | c in result}
    oneOf[ATECourseECE300 + ATECourseECE400]  {some c : (ATECourseECE300 + ATECourseECE400)| c in result}
    twoOf[ATECourseECE400 + ATECourseCS400] { some disjoint x, y : (ATECourseECE400 + ATECourseCS400 ) & result | x != y } or  
			{oneOf[ATECourseECE400 + ATECourseCS400] {some c : (ATECourseECE400 + ATECourseCS400)| c in result} and oneOf[ATECourseExtra]{some c : ATECourseExtra| c in result}}
  	fourOf[ATECourseCS300 + ATECourseCS400 + ATECourseECE300 + ATECourseECE400 + ATECourseExtra] {  some disjoint w, x, y, z : (ATECourseCS300 + ATECourseCS400 + ATECourseECE300 + ATECourseECE400 + ATECourseExtra) & result | x != y and x != z and x != w}
   
 // having 2 ATECourseExtra and 1 CS and 1 ECE does not pass
    no disjoint x, y : ATECourseExtra & result | x != y


    -- Science -- need to make more efficient to have labs
   {twoOf[ScienceCourse]{ some disjoint x, y : ScienceCourse& result | x != y} } or  {twoOf[Earth121andLab]{some disjoint x, y : Earth121andLab & result | x != y} and  oneOf[ScienceCourse] {some c : ScienceCourse | c in result}}
										or {twoOf[Chem262andLab]{ some disjoint x, y : Chem262andLab& result | x != y} and  oneOf[ScienceCourse]{some c : ScienceCourse | c in result}}
										or {twoOf[Bio130andLab]{ some disjoint x, y : Bio130andLab& result | x != y} and oneOf[ScienceCourse]{some c : ScienceCourse | c in result}}
										or {twoOf[Earth121andLab]{some disjoint x, y : Earth121andLab& result | x != y} and  twoOf[Chem262andLab]{some disjoint x, y : Chem262andLab& result | x != y}}
										or {twoOf[Earth121andLab]{some disjoint x, y : Earth121andLab& result | x != y} and  twoOf[Bio130andLab]{some disjoint x, y : Bio130andLab& result | x != y}}
										or {twoOf[Chem262andLab]{some disjoint x, y : Chem262andLab& result | x != y} and  twoOf[Bio130andLab]{some disjoint x, y : Bio130andLab& result | x != y}}
										//course substitution
										or {oneOf[ScienceCourse]{some c : ScienceCourse | c in result} and  oneOf[Bio139orBio239]{some c : Bio139orBio239 | c in result}}
										or {twoOf[Earth121andLab]{some disjoint x, y : Earth121andLab& result | x != y} and  oneOf[Bio139orBio239]{some c : Bio139orBio239 | c in result}}
										or {twoOf[Chem262andLab]{some disjoint x, y : Chem262andLab& result | x != y} and  oneOf[Bio139orBio239]{some c : Bio139orBio239 | c in result}}
										or {twoOf[Bio130andLab]{some disjoint x, y : Bio130andLab& result | x != y} and oneOf[Bio139orBio239]{some c : Bio139orBio239 | c in result}}

										or {oneOf[Bio139orBio239]{some c : Bio139orBio239 | c in result} and  oneOf[Bio140orBio240]{some c : Bio140orBio240 | c in result}}
										//course substitution
										or {oneOf[ScienceCourse] {some c : ScienceCourse | c in result} and  oneOf[Bio140orBio240] {some c : Bio140orBio240 | c in result}}
										or {twoOf[Earth121andLab]{some disjoint x, y : Earth121andLab& result | x != y} and  oneOf[Bio140orBio240]{some c : Bio140orBio240 | c in result}}
										or {twoOf[Bio130andLab]{some disjoint x, y : Bio130andLab& result | x != y} and  oneOf[Bio140orBio240] {some c : Bio140orBio240 | c in result}}
										or {twoOf[Chem262andLab]{some disjoint x, y : Chem262andLab& result | x != y}  and  oneOf[Bio140orBio240] {some c : Bio140orBio240 | c in result}}
   
	 -- Linkage Electives
    oneOf[CS492 + ComplementaryCourseA] {some c : (CS492 + ComplementaryCourseA )| c in result}
    oneOf[CommunicationCourse]{some c : CommunicationCourse | c in result}

    {twoOf[ComplementaryCourseC]{some disjoint x, y : ComplementaryCourseC& result | x != y}} or {oneOf[ComplementaryCourseC_PhilorGene]{some c : ComplementaryCourseC_PhilorGene | c in result}
													    and oneOf[ComplementaryCourseC]{some c : ComplementaryCourseC | c in result}}

}

pred allOf[ s : set CourseReq_SE_2015 ] {
// s in result
}

pred oneOf[ s : set CourseReq_SE_2015 ] {
   // some c : s | c in result
}

pred twoOf[ s : set CourseReq_SE_2015 ] {
   // some disjoint x, y : s & result | x != y
}

pred threeOf[ s : set CourseReq_SE_2015 ] {
   // some disjoint x, y, z : s & result | x != y and x != z
}

pred fourOf[ s : set CourseReq_SE_2015 ] {
   // some disjoint w, x, y, z : s & result | x != y and x != z and x != w
}



pred upperbound {
	-- result constrained by transcript
	//result in  transcript_20310542
}

-- run the SE 2016 Plan
run SEPlan2015

