lexer grammar testerLexer;

TEST : 'test' ;
GET : 'GET' ;
POST : 'POST' ;
PUT : 'PUT' ;
DELETE : 'DELETE' ;
HEAD: 'HEAD';

OPTIONS : 'options' ;

EXPECT : 'expect' ;
ENV : 'ENV' ;
VAR : 'var' ;

ASSIGN : '=' ;
EQ : '==' ;
NEQ : '!=' ;
LT : '<' ;
GT : '>' ;
LTE : '<=' ;
GTE : '>=' ;

LCURLY : '{' ;
RCURLY : '}' ;
LPAREN : '(' ;
RPAREN : ')' ;
LBRACK : '[' ;
RBRACK : ']' ;
COLON : ':' ;
COMMA : ',' ;
DOT : '.' ;

TRUE : 'true' ;
FALSE : 'false' ;

ENV_REF : '"@{' ID '}"' ;
VAR_REF : '"${' ID '}"' ;

RESPONSE : 'response' ;
BODY : 'body' ;
HEADERS : 'headers' ;
STATUS : 'status' ;
TYPE : 'type' ;

ID : [a-zA-Z_][a-zA-Z0-9_]* ;
STRING : '"' (~["\r\n])* '"' ;
NUMBER : [0-9]+ ('.' [0-9]+)? ;
COMMENT : '//' ~[\r\n]* -> skip ;
WS : [ \t\r\n]+ -> skip ;
