parser grammar testerParser;
options { tokenVocab=testerLexer; }

program : (testCase)+ ;

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

endpoint : STRING ;

block : '{' (blockItem)* '}' ;
blockItem
    : headersBlock
    | bodyBlock
    ;

headersBlock : HEADERS '{' (header)* '}' ;
header : STRING ':' STRING ;

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
    | NULL
    ;

assertion : EXPECT assertionExpr ;
assertionExpr
    : STATUS NUMBER
    | JSON jsonPath ('==' | '!=') value
    ;
jsonPath : STRING ;

assignment : ID '=' value ;