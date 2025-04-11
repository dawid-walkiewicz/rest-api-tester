parser grammar TesterParser;
options { tokenVocab=TesterLexer; }

program : (testCase | envDeclaration)* ;

envDeclaration : ENV ID '=' value ;

testCase : TEST STRING (optionsBlock)? '{' statement* '}' ;

optionsBlock : 'options' optionsList ;
optionsList : '{' option (',' option)* '}' ;
option : ID ':' optionValue ;
optionValue : NUMBER | STRING ;

statement
    : request
    | assertion
    | varDeclaration
    | varReassignment
    ;

varDeclaration : VAR ID '=' value ;

varReassignment : ID '=' value ;

request
    : method endpoint (obj)? ;

method : GET | POST | PUT | DELETE | HEAD ;

endpoint : STRING ;

obj
    : '{' pair (',' pair)* '}'
    | '{' '}'
    ;

pair
    : STRING ':' value
    ;

arr
    : '[' value (',' value)* ']'
    | '[' ']'
    ;

value
    : path
    | STRING
    | NUMBER
    | obj
    | arr
    | TRUE
    | FALSE
    | ID
    ;

assertion : EXPECT boolExpr ;

boolExpr
    : lval=boolExpr (AND | OR) rval=boolExpr
    | NOT boolExpr
    | LPAREN boolExpr RPAREN
    | assertionExpr
    ;

assertionExpr
    : lval=value comparison rval=value
    ;

comparison : EQ | NEQ | LT | GT | LTE | GTE ;

bracketAccess
    : LBRACK property RBRACK
    ;

property : ID | STRING | NUMBER ;

path : ID bracketAccess* ;