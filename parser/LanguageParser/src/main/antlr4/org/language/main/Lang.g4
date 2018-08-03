/** Grammars always start with a grammar header. This grammar is called
 *  ArrayInit and must match the filename: ArrayInit.g4
 */
grammar Lang;

//tell parser to throw exception to indicate parsing failure instead of trying to recover
@parser::members {
	@Override
	public void notifyErrorListeners(Token offendingToken, String msg, RecognitionException ex) {
		throw new RuntimeException(msg); 
	}
}

//tell lexer to throw exception to indicate parsing failure instead of trying to recover
@lexer::members {
	@Override
	public void recover(RecognitionException ex) {
		throw new RuntimeException(ex.getMessage()); 
	}
}


INT 	: [0-9]+ ;             	// Define token INT as one or more digits
DEC 	: INT '.' INT ; 		// Decimal 
WS  	: (' '| '\t' | '\r' | '\n' | '\f' | SingleLineComment | MultiLineComment )+ -> skip ; 	// Define whitespace rule, toss it out
SingleLineComment	: '//' ~('\r' | '\n')*;
MultiLineComment	: '/*' .*? '*/' ;


ALL 	: 'all';
ANY		: 'any';	
UNIQUE 	: 'unique';
EXCEPT 	: 'except';
IS		: 'is';
OF		: 'of';
LBRACE	: '{';
RBRACE	: '}';
COMMA	: ',';
UNITS	: 'units';
COURSES : 'courses';
DASH	: '-';
STAR 	: '*';

ID		: '_'('A'..'Z' | 'a'..'z' | '_' | [0-9])+ ;

COURSE		: ('A'..'Z' | 'a'..'z')+ [0-9]* ('A'..'Z' | 'a'..'z' )*  (('.')('A'..'Z' | 'a'..'z' | [0-9])+)? ;
COURSERANGE	: ('A'..'Z' | 'a'..'z')+ [0-9]+ DASH [0-9]+ ;

COURSESTAR	: ('A'..'Z' | 'a'..'z')+ [0-9]* ('A'..'Z' | 'a'..'z' )* STAR |  ('A'..'Z' | 'a'..'z')+ [0-9]* ('A'..'Z' | 'a'..'z' )* '.' STAR ; 

init  : degree ;  // must match at least one value

/** A rule called init that matches comma-separated values between {...}. */
degree  :  list_defs reqs ;  // must match at least one value


course_id 	: COURSE
			| COURSESTAR
			| COURSERANGE
			| EXCEPT course_id
			| ID
			| ANY;
course_id_list	: course_id
				| course_id COMMA course_id_list;


// disallow empty array to avoid ambiguity with an empty req
// e.g.: course_id, course_id, course_id
array : LBRACE course_id_list RBRACE;


list_defs 	: 		//can be empty
		 	| list_def list_defs;	// can be infinitely long
list_def	: ID IS array;	

req : ID IS expr 	// Computer_Science is all of { 1 of {BIO110} 1 of {MATH135,MATH145}}
	| expr
	| ID IS unit_expr
	| unit_expr  	// 5.00 units of {2.00 units of {...} 3.00 units of {...}} look below
	| ID IS course_expr
	| course_expr; 	// 5 courses of {..., 0-3 of {...}} look below
	
reqs	: 
		| req reqs;

discriminator	: 
				| UNIQUE;
				
quantifier	: INT
			| ALL
			| INT DASH INT;
			
dec_quantifier	: DEC
				| DEC DASH DEC;


expr	: discriminator quantifier OF LBRACE reqs RBRACE
		| discriminator quantifier OF array;
unit_expr	: discriminator dec_quantifier UNITS OF LBRACE reqs RBRACE
			| discriminator dec_quantifier UNITS OF array;
course_expr	: discriminator quantifier COURSES OF LBRACE reqs RBRACE
			| discriminator quantifier COURSES OF array;



