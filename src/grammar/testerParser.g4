parser grammar testerParser;
options { tokenVocab=testerLexer; }

program : (testCase | envDeclaration)* ;

envDeclaration : ENV ID '=' value ;

testCase : TEST STRING (optionsBlock)? '{' (statement)* '}' ;

optionsBlock : 'options' optionsList ;
optionsList : '{' option (',' option)* '}' ;
option : ID ':' optionValue ;
optionValue : NUMBER | STRING ;

statement
    : request
    | assertion
    | assignment
    ;

request
    : method endpoint (block)? ;

method : GET | POST | PUT | DELETE | HEAD ;

endpoint : STRING | VAR_REF ;

block : '{' (blockItem)* '}' ;
blockItem
    : headersBlock
    | bodyBlock
    ;

headersBlock : HEADERS '{' (header)* '}' ;
header : STRING ':' (STRING | VAR_REF) ;

bodyBlock : BODY obj ;

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
    : STRING
    | NUMBER
    | obj
    | arr
    | TRUE
    | FALSE
    | VAR_REF
    ;

assertion : EXPECT assertionExpr ;
assertionExpr
    : STATUS comparison NUMBER
    | jsonPath comparison value
    ;

comparison : EQ | NEQ | LT | GT | LTE | GTE ;

jsonPath : JSON ('{' (STRING | NUMBER) '}' | DOT ID)+ ;

assignment : ID '=' value ;