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

    AST parse(GameCreating card){//来构建树了

        try {
            AST ast = term(lexer,card);

            if (Global.to_seecoder) {//为了 给输出德布鲁因式 作准备
                Map<String, Integer> map = new HashMap<>();
                ast.change_to_seecoder(map);
            }
            return ast;

        }catch (Exception e){
            System.out.println("构建树时出错");
            e.printStackTrace();
            return null;
        }

    }

    private AST term(Lexer lexer, GameCreating card)throws Exception{

        switch (lexer.getValue(0)) {
            case left_parenthesis:          //这个效率很低的，复杂度为O(n^2)，好在调用次数非常少

//-------------------------------------------------------------------------------------
                        switch (lexer.getValue(1)) {
                            case lambda:
                                return new Abstraction
                                        (new Identifier(lexer.getValue(2),card),
                                                term(lexer.subLexer(4, lexer.match(4) + 1),card),card);
                            case left_parenthesis:
                                int temp1 = lexer.match(1) + 1;
                                int temp2 = lexer.match(temp1) + 1;
                                return new Application(
                                        term(lexer.subLexer(1, temp1),card),
                                        term(lexer.subLexer(temp1, temp2),card),
                                        card);
                            default:
                                return new Application(
                                        new Identifier(lexer.getValue(1),card),
                                        term(lexer.subLexer(2, lexer.match(2) + 1),card),card);
                            }
//------------------------------------------------------------------------------------
            case right_parenthesis://理论上是不会遇到的
                throw new Exception("))");
            case lambda://理论上是不会遇到的
                throw new Exception("\\\\");
            case dot://理论上是不会遇到的
                throw new Exception("...");
            default://普通的string
                return new Identifier(lexer.getValue(),card);
            }

    }
}