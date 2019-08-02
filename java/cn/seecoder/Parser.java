package cn.seecoder;

import java.lang.*;
import java.util.HashMap;
import java.util.Map;

public class Parser {


    static final private String left_parenthesis= Global.left_parenthesis;
    static final private String right_parenthesis= Global.right_parenthesis;
    static final private String lambda= Global.lambda;
    static final private String dot= Global.dot;

    Lexer lexer;

    public Parser(Lexer lexer){
        this.lexer = lexer;
    }

    AST parse(GameFreelyExplore card){//来构建树了
        return term(lexer,card);
    }

    private AST term(Lexer lexer, GameFreelyExplore card){

        switch (lexer.getValue(0)) {
            case left_parenthesis:          //这个效率很低的，复杂度为O(n^2)，好在调用次数非常少

//-------------------------------------------------------------------------------------
                        switch (lexer.getValue(1)) {
                            case lambda:
                                return new AST_Abstraction
                                        (new AST_Identifier(lexer.getValue(2),card),
                                                term(lexer.subLexer(4, lexer.match(4) + 1),card),card);
                            case left_parenthesis:
                                int temp1 = lexer.match(1) + 1;
                                int temp2 = lexer.match(temp1) + 1;
                                return new AST_Application(
                                        term(lexer.subLexer(1, temp1),card),
                                        term(lexer.subLexer(temp1, temp2),card),
                                        card);
                            default:
                                return new AST_Application(
                                        new AST_Identifier(lexer.getValue(1),card),
                                        term(lexer.subLexer(2, lexer.match(2) + 1),card),card);
                            }
//------------------------------------------------------------------------------------

            default://普通的string
                return new AST_Identifier(lexer.getValue(),card);
            }

    }
}