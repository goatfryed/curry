
grammar Curry;

// parser
@header {
package de.goatfryed.curry.language.parser;

import de.goatfryed.curry.language.ast.*;
}

@parser::members {
    private Builder factory;

    public void setNodeFactory(Builder factory) {
        this.factory = factory;
    }
}

@lexer::members {
    public static final int WHITESPACE = 1;
    public static final int COMMENTS = 2;
}

curry returns [ExpressionNode main, CurryFunction[] functions]
:
(
    expression { $main = $expression.result; }
    | module {
    $main = $module.main;
    $functions = $module.functions;
}
) EOF
;

module returns [ExpressionNode main, CurryFunction[] functions]
@init { var functions = new ArrayList<CurryFunction>(); }
@after { $functions = functions.toArray(CurryFunction[]::new); }
:
    block { $main = $block.node; }
    (function_definition { functions.add($function_definition.it); })*
;

function_definition returns [CurryFunction it]:
    'let' IDENTIFIER 'be'? block { $it = factory.createFunction($IDENTIFIER, $block.node); }
;

block returns [CurryBlockNode node]
@init {
    var body = factory.startBlock();
}
@after { $node = body.build(); }
:
'{'
    (
        statement { body.add($statement.node); }
        EOS
    )*
'}';

statement returns [StatementNode node]:
    expression { $node = $expression.result;}
    | assignment { $node = $assignment.node; }
    | 'return' expression { $node = $expression.result; }
;

assignment returns [StatementNode node]:
    IDENTIFIER '=' expression { $node = factory.createAssignment($IDENTIFIER, $expression.result); }
;

expression returns [ExpressionNode result]:
    safe_expression { $result = $safe_expression.node; }
    | unsafe_expression { $result = $unsafe_expression.node; }
;

safe_expression returns [ExpressionNode node]:
    function_call {$node = $function_call.result; }
    | literal { $node = $literal.result; }
    | IDENTIFIER { $node = factory.createRead($IDENTIFIER); }
    | '(' expression ')' { $node = $expression.result; }
;

unsafe_expression returns [ExpressionNode node]:
    addition { $node = $addition.node; }
;

function_call returns [ExpressionNode result]:
    'hello' { $result = HelloWorldNodeFactory.create(); }
    | IDENTIFIER '(' function_arguments ')' { $result = factory.createInvocation($IDENTIFIER); }
;

function_arguments returns [ExpressionNode[] result]:
;

addition returns [ExpressionNode node]:
    safe_expression '+' expression { $node = AdditionNodeGen.create($safe_expression.node, $expression.result); }
;

literal returns [ExpressionNode result]:
    STRING_LITERAL { $result = factory.createStringLiteral($STRING_LITERAL); }
;


// lexer

WS : [ \t\r\n\u000C]+ -> skip;
COMMENT : '/*' .*? '*/' -> skip;
LINE_COMMENT : '//' ~[\r\n]* -> skip;

fragment LETTER : [A-Z] | [a-z] | '_';
fragment DIGIT : [0-9];
fragment STRING_CHAR : ~('"' | '\\' | '\r' | '\n');
// see how a line break could also be a valid end of statement
// currently, line breaks are skipped and we want this behavior inside of statements
EOS : ';' ;

STRING_LITERAL : '"' STRING_CHAR* '"';

IDENTIFIER : LETTER (LETTER | DIGIT)*;