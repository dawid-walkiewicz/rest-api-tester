parser grammar testerParser;
options { tokenVocab=testerLexer; }

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

endpoint : STRING | VAR_REF | ENV_REF;

obj
    : '{' pair (',' pair)* '}'
    | '{' '}'
    ;

pair
    : STRING ':' value
    ;

key
    : STRING
    | NUMBER
    | ID
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
    | VAR_REF
    | ENV_REF
    ;

assertion : EXPECT assertionExpr ;

assertionExpr
    : STATUS comparison NUMBER
    | path comparison value
    ;

comparison : EQ | NEQ | LT | GT | LTE | GTE ;

rootElement : RESPONSE | BODY | HEADERS | STATUS | TYPE | ID ;

bracketAccess
    : LBRACK property RBRACK
    ;

property : ID | STRING | NUMBER ;

path : rootElement bracketAccess* ;