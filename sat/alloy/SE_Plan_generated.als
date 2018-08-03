//* source language */
/*-------------------------------------*/
/*

core_course is all of {CS115, CS116}
advanced courses is all of {CS136}

*/
/*-------------------------------------*/

// for file named SE_2016
abstract sig SE_2016{}
abstract sig all_courses extends SE_2016{}

// source:
// core_course is {CS115, CS116}
//
// pick up courses as first seen
one sig CS115, CS116 extends all_courses{}
sig core_courses in SE_2016{}
// to put courses in list
fact {
	CS115 in core_courses{}
	CS116 in core_courses{}
}
// source:
// advanced courses is {CS136}
//
// pick up courses as first seen
one sig CS136 extends all_courses{}
sig advanced_courses in SE_2016{}
// to put courses in list
fact {
	CS136 in advanced_courses{}
}


///////////////////////////////////////////////////

abstract sig _BASE_SE_ENG {}
abstract sig ALL_COURSES extends SE_2016{}

// required courses
one sig CS137, ECE105, ECE140, MATH115, MATH117, CS138, ECE106, ECE124, MATH119, MATH135, CHE102, CS241, ECE222, SE212, STAT206, CS240, CS247, MSCI261, MATH213, MATH239, CS341, CS349, SE350, SE380, CS343, CS348, SE390, SE463, ECE358, SE464, SE490, SE465, SE491 extends ALL_COURSES{}
sig _REQUIRED_COURSES in SE_2016{}
fact {
    CS137 in _REQUIRED_COURSES{}
    ECE105 in _REQUIRED_COURSES{}
    ECE140 in _REQUIRED_COURSES{}
    MATH115 in _REQUIRED_COURSES{}
    MATH117 in _REQUIRED_COURSES{}
    CS138 in _REQUIRED_COURSES{}
    ECE106 in _REQUIRED_COURSES{}
    ECE124 in _REQUIRED_COURSES{}
    MATH119 in _REQUIRED_COURSES{}
    MATH135 in _REQUIRED_COURSES{}
    CHE102 in _REQUIRED_COURSES{}
    CS241 in _REQUIRED_COURSES{}
    ECE222 in _REQUIRED_COURSES{}
    SE212 in _REQUIRED_COURSES{}
    STAT206 in _REQUIRED_COURSES{}
    CS240 in _REQUIRED_COURSES{}
    CS247 in _REQUIRED_COURSES{}
    MSCI261 in _REQUIRED_COURSES{}
    MATH213 in _REQUIRED_COURSES{}
    MATH239 in _REQUIRED_COURSES{}
    CS341 in _REQUIRED_COURSES{}
    CS349 in _REQUIRED_COURSES{}
    SE350 in _REQUIRED_COURSES{}
    SE380 in _REQUIRED_COURSES{}
    CS343 in _REQUIRED_COURSES{}
    CS348 in _REQUIRED_COURSES{}
    SE390 in _REQUIRED_COURSES{}
    SE463 in _REQUIRED_COURSES{}
    ECE358 in _REQUIRED_COURSES{}
    SE464 in _REQUIRED_COURSES{}
    SE490 in _REQUIRED_COURSES{}
    SE465 in _REQUIRED_COURSES{}
    SE491 in _REQUIRED_COURSES{}
}

// ATEs, not sure how to deal with the looped predicates
// 1-3 of {CS442, CS444, CS448, CS449, CS450, CS452, CS454, CS457, CS458, CS473, CS484, CS485, CS486, CS488}
// 1-3 of {ECE409, ECE416, ECE417, ECE418, ECE419, ECE423, ECE454, ECE455, ECE457A, ECE457B, ECE458, ECE459, ECE481, ECE486, ECE488}
// 0-2 of {CO487, CS462, CS466, CS467, CS475, CS487, CS489, CS490, ECE406, SE498, SE499, SYDE433, SYDE475, SYDE558}
// 0-1 of {CS360, CS365,CS370,CS371,ECE207,ECE224, ECE327, SYDE348}
one sig () extends ALL_COURSES{}
sig _ATE in SE_2016{}
fact {
    CS442 in _ATE{}
    CS444 in _ATE{}
    CS448 in _ATE{}
    CS449 in _ATE{}
    CS450 in _ATE{}
    CS452 in _ATE{}
    CS454 in _ATE{}
    CS457 in _ATE{}
    CS458 in _ATE{}
    CS473 in _ATE{}
    CS484 in _ATE{}
    CS485 in _ATE{}
    CS486 in _ATE{}
    CS488 in _ATE{}
    ECE409 in _ATE{}
    ECE416 in _ATE{}
    ECE417 in _ATE{}
    ECE418 in _ATE{}
    ECE419 in _ATE{}
    ECE423 in _ATE{}
    ECE454 in _ATE{}
    ECE455 in _ATE{}
    ECE457A in _ATE{}
    ECE457B in _ATE{}
    ECE458 in _ATE{}
    ECE459 in _ATE{}
    ECE481 in _ATE{}
    ECE486 in _ATE{}
    ECE488 in _ATE{}
    CO487 in _ATE{}
    CS462 in _ATE{}
    CS466 in _ATE{}
    CS467 in _ATE{}
    CS475 in _ATE{}
    CS487 in _ATE{}
    CS489 in _ATE{}
    CS490 in _ATE{}
    ECE406 in _ATE{}
    SE498 in _ATE{}
    SE499 in _ATE{}
    SYDE433 in _ATE{}
    SYDE475 in _ATE{}
    SYDE558 in _ATE{}
    CS360 in _ATE{}
    CS365 in _ATE{}
    CS370 in _ATE{}
    CS371 in _ATE{}
    ECE207 in _ATE{}
    ECE224 in _ATE{}
    ECE327 in _ATE{}
    SYDE348 in _ATE{}
}

// LEs, not sure how to deal with the looped predicates, wildcards and set subtraction

// _LIST_C_NONPRESCHEDULED is {ANTH*, CLAS*, ECON*, ENGL*, HIST*, HRM*, PHIL*, PSCI*, PSYCH*, RS*, SMF*, SDS*, SOCWK*, STV*, SOC*, WS*}
// _LIST_C_EXCEPTION is {ECON211, ECON221, ECON311, ECON321, ECON371, ECON404, ECON405, ECON411, ECON412, ECON421, ECON422, ECON471, ENGL109, ENGL129R, ENGL140R, ENGL141R, ENGL210E, ENGL210F, HIST4.*, PHIL145, PHIL200J, PHIL216, PHIL240, PHIL256, PHIL359, PHIL441, PSCI314, PSCI315, PSYCH256, PSYCH261, PSYCH291, PSYCH292, PSYCH307, PSYCH312, PSYCH317, PSYCH391, PSYCH4.*, RS131, RS132, RS133, RS134, RS233, RS234, RS331, RS332, SDS150R, SDS250R, SDS251R, SDS350R, SDS398R, SDS399R, SOCWK390A, SOCWK390B, SOCWK398R, SOCWK399R, SOC280, SOC321, SOC322, SOC382, SOC410, SOC421, SOC498, SOC499A, SOC499B, WS365, WS475}
// _LIST_A_SE is {ECE390, ENVS105, ERS215, ERS315, GENE22A, GEOG203, GEOG368, MSCI422, MSCI442, PHIL226, SOC232, STV100, STV202, STV203, STV205, STV210, STV302, STV404, SYDE261, WS205}
// 1 of {
//     0-1 of {CS491}
//     0-1 of {_LIST_A_SE}
// }
// 
// 1 of {ENGL109, ENGL119, ENGL140R, ENGL210E, ENGL210F, ENGL210G, SPCOM100, SPCOM223, SPCOM324, SPCOM432}
// 2 of {
//     1-2 of {ECON102, ECON202, MSCI211, MSCI311, PSCI260, PSYCH101, SOC101, HIST253, HIST254, HIST275, PHIL250A, PHIL250B, DRAMA101A, DRAMA101B, EASIA201R, ENVS195, FR197, FR297, GENE22C, GENE412, GEOG101, GEOG202, GEOG203, GEOG368, GERON201, HLTH220, HUMSC101, JUMSC102, INTST101, KIN352, KIN354, LS101, LS102, MSCI211, MSCI263, MSCI311, MSCI411, MUSIC140, MUSIC245, MUSIC253, MUSIC256, MUSIC334, MUSIC355, MUSIC363, PLAN100, REC205, REC230, REC304, REC425, _LIST_C_NONPRESCHEDULED, except _LIST_C_EXCEPTION}
//     0-1 of {PHIL315, GENE412}
// }
one sig () extends ALL_COURSES{}
sig _LE in SE_2016{}
fact {
    // ...
}

// CSEs, not sure how to deal with loop predicates, and course predicates
// 2 of {
//     0-1 of {
//         all of {BIOL130, BIOL130L}
//         all of {BIOL110, BIOL110L}
//         all of {CHEM262, CHEM262L}
//     }
//     0-2 of {BIO165, BIO239, BIO240, BIO273,CHE161, EARTH212, EARTH122, PHYS234, PHYS246,PHYS263, PHYS275,PHYS334,PHYS375,SCI238,SCI250}
// }
one sig () extends ALL_COURSES{}
sig _CSE in SE_2016{}
fact {
    BIOL130 in _CSE{}
    BIOL130L in _CSE{}
    BIOL110 in _CSE{}
    BIOL110L in _CSE{}
    CHEM262 in _CSE{}
    CHEM262L in _CSE{}
    BIO165 in _CSE{}
    BIO239 in _CSE{}
    BIO240 in _CSE{}
    BIO273 in _CSE{}
    CHE161 in _CSE{}
    EARTH212 in _CSE{}
    EARTH122 in _CSE{}
    PHYS234 in _CSE{}
    PHYS246 in _CSE{}
    PHYS263 in _CSE{}
    PHYS275 in _CSE{}
    PHYS334 in _CSE{}
    PHYS375 in _CSE{}
    SCI238 in _CSE{}
    SCI250 in _CSE{}
}

// WKRPT
one sig WKRPT200, WKRPT300, WKRPT400 extends ALL_COURSES{}
sig _WKRPT in SE_2016{}
fact {
    WKRPT200 in _WKRPT{}
    WKRPT300 in _WKRPT{}
    WKRPT400 in _WKRPT{}
}

// COOP, not sure how to deal with wildcard
one sig () extends ALL_COURSES{}
sig _COOP in SE_2016{}
fact {
    // ...
}

// PD
one sig PD20, PD30, threeOf[PD3, PD4, PD5, PD6, PD7, PD8, PD9] extends ALL_COURSES{}
sig _COOP in SE_2016{}
fact {
    PD20 in _COOP{}
    PD21 in _COOP{}
    PD3 in _COOP{}
    PD4 in _COOP{}
    PD5 in _COOP{}
    PD6 in _COOP{}
    PD7 in _COOP{}
    PD8 in _COOP{}
    PD9 in _COOP{}
}

// MILESTONE
one sig ELPE, WHMIS, TP extends ALL_COURSES{}
sig _MILESTONE in SE_2016{}
fact {
    ELPE in _MILESTONE{}
    WHMIS in _MILESTONE{}
    TP in _MILESTONE{}
}

run {}
