//* source language */
/*-------------------------------------*/
/*

_REQUIRED_MATH is {MATH135, MATH136, MATH137, MATH138, MATH235, MATH237, MATH239, STAT230, STAT231, STAT371, STAT372, CS115, CS116, CS330, C0250, C0370, ACTSC231}
_REQUIRED_BUSINESS is {BU111, BU121, BU227, BU231, BU247, BU288, BU352, BU354, BU362, BU383, BU385, BU393, BU395, BU398, BU481, BU491, ECON120W, ECON140W}

_BUSINESS_ADMINISTRATION_AND_MATHEMATICS_DOUBLE_DEGREE is all of {
    _REQUIRED_COURSES is all of {
        3-5 of {_REQUIRED_MATH}
        1 of {ECON250W, ECON260W}
    }
}


0.5
0.25
*/
/*-------------------------------------*/

open MathDD_DB
sig result in Course{}

sig _REQUIRED_MATH in Course{}
fact {

_REQUIRED_MATH = 
MATH135 +
MATH136 + 
MATH137 + 
MATH138 + 
MATH235 + 
MATH237 + 
MATH239 + 
STAT230 + 
STAT231 + 
STAT371 + 
STAT372 +
CS115 + 
CS116 + 
CS330 + 
C0250 + 
C0370 + 
ACTSC231

}
fact {
	all c : _REQUIRED_MATH | c.unit = 20
}

sig  _REQUIRED_BUSINESS in Course{}
fact {
	_REQUIRED_BUSINESS = 
	BU111 + 
	BU121 + 
	BU227 + 
	BU231 + 
	BU247 + 
	BU288 +
	BU352 + 
	BU354 + 
	BU362 + 
	BU383 + 
	BU385 +
	BU393 +
	BU395 +
	BU398 + 
	BU481 + 
	BU491 + 
	ECON120W +
	ECON140W
}
fact {
	all c : _REQUIRED_BUSINESS| c.unit = 2
}

sig Block1 in Course{}
pred Block1_pred {
	Block1 = ( _REQUIRED_MATH + _REQUIRED_BUSINESS) & result
	// 4-5 units of {_REQUIRED_MATH, _REQUIRED_BUSINESS} 
	// some disjoint c1, c2, c3, c4 : ( _REQUIRED_MATH) | c1 in result and c2 in result and c3 in result and c4 in result
	// not (some disjoint c1, c2, c3, c4, c5, c6 :( _REQUIRED_MATH) | c1 in result and c2 in result and c3 in result and c4 in result and c5 in result and c6 in result)
	#Block1 >=4 and #Block1 <=5
}

sig Block2 in Course{}
pred Block2_pred {
	Block2 = (ECON250W + ECON260W) & result
	// 1 of {ECON250W, ECON260W}
	some c : (ECON250W + ECON260W) | c in result
}

fact {
	all c : (ECON250W + ECON260W) | c.unit = 2
}

sig _REQUIRED_COURSES in Course{}
pred _REQUIRED_COURSES_pred {
	_REQUIRED_COURSES = (Block1 + Block2) & result
	// _REQUIRED_COURSES is all of {...}
	let x = Block1_pred | let y = Block2_pred | x and y
}
/*
sig _BUSINESS_ADMINISTRATION_AND_MATHEMATICS_DOUBLE_DEGREE in Course{}
pred _BUSINESS_ADMINISTRATION_AND_MATHEMATICS_DOUBLE_DEGREE_pred {
	_BUSINESS_ADMINISTRATION_AND_MATHEMATICS_DOUBLE_DEGREE in _REQUIRED_COURSES
	// _BUSINESS_ADMINISTRATION_AND_MATHEMATICS_DOUBLE_DEGREE is all of
	
}
*/
pred main {
	_REQUIRED_COURSES_pred 
	result in transcript
}

run main for 0 but 9 Int
