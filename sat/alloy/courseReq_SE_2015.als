//List of all courses that we concern
abstract sig CourseReq_SE_2015{}

abstract sig 
    CoreCourse, 
	ATECourseCS300,
	ATECourseCS400,
	ATECourseECE300,
	ATECourseECE400,
	ATECourseExtra,

    LinkageCourse, 

    CommunicationCourse, 
    ComplementaryCourseA,
    ComplementaryCourseC,
	ComplementaryCourseC_PhilorGene,

    ScienceCourse,
    Bio130andLab,
    Chem262andLab,
    Earth121andLab,
    Bio139orBio239,
	Bio140orBio240
    extends CourseReq_SE_2015 {}

//core:
one sig
//1A
CS137,
ECE105,
ECE140,
MATH115,
MATH117,
SE101, 
//1B
CS138,
ECE106,
ECE124,
MATH119,
MATH135,
//2A
CHE102,
CS241,
ECE222,
SE212,
STAT206,
//2B
CS240,
CS247,
MSCI261,
MATH213,
MATH239,
//3A
CS341,
CS349,
SE350,
SE380,
//3B
CS343,
CS348,
SE390,
SE463,
//4A
ECE358,
SE464,
SE490,
//4B
SE465,
SE491 
extends CoreCourse {}

//ATE
//CS
one sig 
CS360,
CS370
extends ATECourseCS300 {}

one sig
CS442,
CS444,
CS448,
CS449,
CS450,
CS452,
CS454,
CS457,
CS458,
CS462,
CS466,
CS473,
CS475,
CS484,
CS485,
CS486,
CS487,
CS488,
SE498
extends ATECourseCS400 {}

//ECE
one sig
ECE318,
ECE342
extends ATECourseECE300 {}

one sig
ECE409,
ECE411,
ECE412,
ECE414,
ECE418,
ECE427,
ECE429,
ECE454,
ECE455,
ECE457,
ECE457A,
ECE457B,
ECE458,
ECE459,
ECE484,
ECE486,
ECE488
extends ATECourseECE400 {}

one sig
CO487,
SYDE524,
SYDE433,
SYDE558,
SYDE475,
SE499
extends ATECourseExtra {}



/*
Linkages
*/
//Social Issue
one sig CS492 extends LinkageCourse {}

//Science
one sig 
BIOL273, 
CHE161,
PHYS122,
PHYS125,
PHYS234,
PHYS246,
PHYS275,
PHYS334,
PHYS375,
ECE209
extends ScienceCourse {}

one sig
BIO139,
BIO239
extends Bio139orBio239 {}

one sig
BIO140,
BIO240
extends Bio140orBio240 {}

one sig
BIOL130, BIOL130L
extends Bio130andLab {}

one sig
EARTH121, EARTH121L
extends Earth121andLab {}

one sig
CHEM262, CHE262L
extends Chem262andLab {}

//Communications
one sig 
ENGL109,
ENGL119, 
ENGL140R, 
ENGL210F, 
ENGL210G, 
SPCOM100, 
SPCOM223, 
SPCOM324, 
SPCOM432 
extends CommunicationCourse {}

//Complementary Studies List A
one sig 
ECE390,
ENVS105,
ERS215,
ERS315,
GENE22A,
GEOG203,
GEOG368,
MSCI422,
MSCI442,
PHIL226,
SOC232,
STV100,
STV202,
STV203,
STV205,
STV210,
STV302,
STV404,
SYDE261,
WS205
extends ComplementaryCourseA {}


//List C
one sig
ECON102,
ECON202,
MSCI211,
MSCI311,
PSCI260,
PSYCH101,
SOC101,
HIST253,
HIST254,
HIST275,
PHIL250A,
PHIL250B
extends ComplementaryCourseC {}

one sig
PHIL315,
GENE412
extends ComplementaryCourseC_PhilorGene {}

