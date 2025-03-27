lexer grammar testerLexer;

TEST : 'test' ;
GET : 'GET' ;
POST : 'POST' ;
PUT : 'PUT' ;
DELETE : 'DELETE' ;
HEAD: 'HEAD';

OPTIONS : 'options' ;

HEADERS : 'headers' ;
BODY : 'body' ;

EXPECT : 'expect' ;
ASSIGN : '=' ;

STATUS : 'status' ;

TRUE : 'true' ;
FALSE : 'false' ;
EQ : '==' ;
NEQ : '!=' ;
LT : '<' ;
GT : '>' ;
LTE : '<=' ;
GTE : '>=' ;

ENV : 'ENV';
VAR: 'var';
ENV_REF : '"@{' ID '}"' ;
VAR_REF : '"${' ID '}"' ;


JSON : 'json' ;
DOT : '.' ;

LCURLY : '{' ;
RCURLY : '}' ;
LPAREN : '(' ;
RPAREN : ')' ;
LBRACK : '[' ;
RBRACK : ']' ;
COLON : ':' ;
COMMA : ',' ;

ID : [a-zA-Z_][a-zA-Z0-9_]* ;
STRING : '"' (~["\r\n])* '"' ;
NUMBER : [0-9]+ ('.' [0-9]+)? ;
WS : [ \t\r\n]+ -> skip ;
