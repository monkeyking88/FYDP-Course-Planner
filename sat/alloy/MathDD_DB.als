abstract sig Course{
	unit : one Int
}

one sig MATH135, MATH136, MATH137, MATH138, MATH235, MATH237, MATH239, STAT230, STAT231, STAT371, STAT372, CS115, CS116, CS330, C0250, C0370, ACTSC231 extends Course{}
one sig BU111, BU121, BU227, BU231, BU247, BU288, BU352, BU354, BU362, BU383, BU385, BU393, BU395, BU398, BU481, BU491, ECON120W, ECON140W extends Course{} 
one sig BU205 extends Course{}
one sig ECON250W, ECON260W extends Course{}
sig BU in Course{}
fact {
	BU =
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
	BU205
}

fun transcript : set Course {
MATH137 + 
MATH237 +
BU111 + 
BU121 +

ECON250W

}

run {}
