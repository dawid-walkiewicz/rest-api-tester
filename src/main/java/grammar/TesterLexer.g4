lexer grammar TesterLexer;

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

AND : '&&' ;
OR : '||' ;
NOT : '!' ;

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

ID : [a-zA-Z_][a-zA-Z0-9_]* ;

STRING:
    '"' ( '\\' .
    | ~["\\\r\n]
    )* '"'
    ;

NUMBER : [0-9]+ ('.' [0-9]+)? ;

COMMENT : '//' ~[\r\n]* -> skip ;
WS : [ \t\r\n]+ -> skip ;
