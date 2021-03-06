package cn.seecoder;

import java.io.*;
import java.util.*;


public class Main {

    private final static String function_place="file/function";

    public static void main(String[] args) {

        System.out.print("\n输入 help 可查看帮助\n\n");
        File filePath = new File("file");
        if ( !filePath.exists()){//若此目录不存在，则创建之// 这个东西只能简历一级文件夹，两级是无法建立的。。。。。
            boolean b = filePath.mkdir();
        }
        Function.read(function_place);
//-----------------------------------------分割线------------------------------


        new GameStartNewGame();












        String source;
        Scanner in = new Scanner(System.in);

        while(true){

            try {

                System.out.print("Lambda-> ");

                source = in.nextLine().trim();

                if (source.equalsIgnoreCase("function") || source.equalsIgnoreCase("func")) {
                    Function.func(function_place);
                } else if (source.equalsIgnoreCase("help")) {
                    System.out.println(Global.help);
                }  else if (source.equalsIgnoreCase("quit") || source.equalsIgnoreCase("out")) {
                    break;
                } else if(source.equals("DELETE-GAME")){//重置
                    GamesChoosing.deleteGame();
                } else if (source.isEmpty()) {
                    continue;
                } else {
                    System.out.println("unrecognized");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Function.write(function_place);

        System.out.print("\nThanks for your use.\n");



    }

//------------------------------------完了---------------------------------

    static String ZERO = "(\\f.\\x.x)";
    static String SUCC = "(\\n.\\f.\\x.f (n f x))";
    static String ONE = app(SUCC, ZERO);
    static String TWO = app(SUCC, ONE);
    static String THREE = app(SUCC, TWO);
    static String FOUR = app(SUCC, THREE);
    static String FIVE = app(SUCC, FOUR);
    static String PLUS = "(\\m.\\n.((m "+SUCC+") n))";
    static String POW = "(\\b.\\e.e b)";       // POW not ready
    static String PRED = "(\\n.\\f.\\x.n(\\g.\\h.h(g f))(\\u.x)(\\u.u))";
    static String SUB = "(\\m.\\n.n"+PRED+"m)";
    static String TRUE = "(\\x.\\y.x)";
    static String FALSE = "(\\x.\\y.y)";
    static String AND = "(\\p.\\q.p q p)";
    static String OR = "(\\p.\\q.p p q)";
    static String NOT = "(\\p.\\a.\\b.p b a)";
    static String IF = "(\\p.\\a.\\b.p a b)";
    static String ISZERO = "(\\n.n(\\x."+FALSE+")"+TRUE+")";
    static String LEQ = "(\\m.\\n."+ISZERO+"("+SUB+"m n))";
    static String EQ = "(\\m.\\n."+AND+"("+LEQ+"m n)("+LEQ+"n m))";
    static String MAX = "(\\m.\\n."+IF+"("+LEQ+" m n)n m)";
    static String MIN = "(\\m.\\n."+IF+"("+LEQ+" m n)m n)";

    private static String app(String func, String x){
        return "(" + func + x + ")";
    }
    private static String app(String func, String x, String y){
        return "(" +  "(" + func + x +")"+ y + ")";
    }
    private static String app(String func, String cond, String x, String y){
        return "(" + func + cond + x + y + ")";
    }


//-----------------------------------------------------------------------------------------
    private static String[] sources = {
            ZERO,//0
            ONE,//1
            TWO,//2
            THREE,//3
            app(PLUS, ZERO, ONE),//4
            app(PLUS, TWO, THREE),//5
            app(POW, TWO, TWO),//6
            app(PRED, ONE),//7
            app(PRED, TWO),//8
            app(SUB, FOUR, TWO),//9
            app(AND, TRUE, TRUE),//10
            app(AND, TRUE, FALSE),//11
            app(AND, FALSE, FALSE),//12
            app(OR, TRUE, TRUE),//13
            app(OR, TRUE, FALSE),//14
            app(OR, FALSE, FALSE),//15
            app(NOT, TRUE),//16
            app(NOT, FALSE),//17
            app(IF, TRUE, TRUE, FALSE),//18
            app(IF, FALSE, TRUE, FALSE),//19
            app(IF, app(OR, TRUE, FALSE), ONE, ZERO),//20
            app(IF, app(AND, TRUE, FALSE), FOUR, THREE),//21
            app(ISZERO, ZERO),//22
            app(ISZERO, ONE),//23
            app(LEQ, THREE, TWO),//24
            app(LEQ, TWO, THREE),//25
            app(EQ, TWO, FOUR),//26
            app(EQ, FIVE, FIVE),//27
            app(MAX, ONE, TWO),//28
            app(MAX, FOUR, TWO),//29
            app(MIN, ONE, TWO),//30
            app(MIN, FOUR, TWO),//31
     };

}