package cn.seecoder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.lang.*;


abstract class Function {

    static void func (String function_place)throws Exception{
        System.out.print("function: ");
        Scanner in=new Scanner(System.in);
        String string = in.nextLine().trim().toLowerCase();

        switch (string){
            case "initial":
            case "init":
                key_value=init_key_value;
                System.out.println("Success!");
                write(function_place);
                break;
            case "add":
            case "define":
                define();
                write(function_place);
                break;
            case "change":
                change();
                write(function_place);
                break;
            case "check":
                print();
                break;
            case "delete":
            case "del":
                delete();
                write(function_place);
                break;
            default:
                System.out.println("unrecognized");
        }

    }

    static AST FunctionString_to_AST(String source, GameFreelyExplore card, AST father){
        AST ast=null;
        Lexer lexer = null;
        for(int i=key_value.size()-2;i>=0;i-=2){
            if(source.equals(key_value.get(i))){
                lexer=new Lexer(key_value.get(i+1));
                Parser parser=new Parser(lexer);
                ast=parser.parse(card);
                break;
            }
        }

        if(lexer==null && source.startsWith("INT")){
            StringBuilder sb1=new StringBuilder();
            StringBuilder sb2=new StringBuilder();
            try{
                int number=Integer.parseInt(source.substring(3));
                for(int ii=0;ii<number;++ii){
                    sb1.append("(f");
                    sb2.append(')');
                }
            }
            catch (NumberFormatException e){
                return null;
            }
            lexer=new Lexer("(\\f.(\\x."+sb1.toString()+" x))"+sb2.toString());
            Parser parser=new Parser(lexer);
            ast=parser.parse(card);
        }
        return ast;
    }

    static AST_Identifier AST_to_FunctionIdentify(AST oldAST, String FunctionName, GameFreelyExplore card, AST father){

        for(int i=key_value.size()-2;i>=0;i-=2){
            if(FunctionName.equalsIgnoreCase(key_value.get(i))){//???
                Lexer lexer = new Lexer(key_value.get(i + 1));
                Parser parser = new Parser(lexer);
                AST temp=parser.parse(null);
                if(temp.toString(0).equals(oldAST.toString(0))){
                    return new AST_Identifier(key_value.get(i),card);
                }
            }
        }

        if(FunctionName.startsWith("INT")){
            StringBuilder sb1=new StringBuilder();
            StringBuilder sb2=new StringBuilder();
            try{
                int number=Integer.parseInt(FunctionName.substring(3));
                for(int ii=0;ii<number;++ii){
                    sb1.append("(f");
                    sb2.append(')');
                }
            }
            catch (NumberFormatException e){
                return null;
            }
            Lexer lexer=new Lexer("(\\f.(\\x."+sb1.toString()+" x))"+sb2.toString());
            Parser parser=new Parser(lexer);
            AST temp=parser.parse(null);
            if(temp.toString(0).equals(oldAST.toString(0))){
                return new AST_Identifier(FunctionName,card);
            }
        }
        return null;
    }


    static void read(String function_place){
        try {
            File file=new File(function_place);
            Scanner in=new Scanner(file);
            String[] all = in.nextLine().split(",");
            Collections.addAll(key_value,all);
        }catch (Exception e){
            e.printStackTrace();
            key_value=init_key_value;
            write(function_place);
            System.out.println("尝试读取function文件时发生错误\n已恢复至默认模式");
        }
    }

    static void write(String function_place){
        try {
            FileWriter writer=new FileWriter(function_place);
            StringBuilder result =new StringBuilder();
            for(String i:key_value){
                result.append(i);
                result.append(",");
            }
            writer.write(result.toString());
            System.out.println("Function file saved！");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("尝试写function文件失败");
        }
    }

    static private void define()throws Exception{
        Scanner in = new Scanner(System.in);
        System.out.print("number= ");
        int number1;
        try {
            number1 = in.nextInt();
            in.nextLine();
            if(number1>key_value.size()/2-1){
                System.out.println("默认：添加至末尾");
                number1=key_value.size()/2-1;
            }
        }
        catch (Exception e){
            number1=key_value.size()/2-1;
            System.out.println("默认：添加至末尾");
        }


        System.out.print("Your function name is: ");
        String function_name;
        do {
            function_name = in.nextLine().trim();
            if(function_name.length()>20){
                throw new  Exception("函数名字不要超过20个字符");
            }
        }while (function_name.isEmpty());

        System.out.print("Your function define is: ");
        String function_define;
        do {
            function_define = in.nextLine().trim();
        }while (function_define.isEmpty());

        key_value.add(number1*2+2,function_define);
        key_value.add(number1*2+2,function_name);
        System.out.println("Success!");
    }

    static private void delete()throws Exception{
        Scanner in = new Scanner(System.in);
        System.out.print("number=");
        try {
            int number2 = in.nextInt();
            key_value.remove(number2 * 2);
            key_value.remove(number2 * 2);
            System.out.println("Success!");
        }
        catch (Exception e){
            throw new Exception("无法删除");
        }

    }

    static private void change()throws Exception{
        Scanner in = new Scanner(System.in);
        System.out.print("number= ");
        int number1;
        try {
            number1 = in.nextInt();
            in.nextLine();
            if(number1>key_value.size()/2-1  ||  number1 <0){
                throw new ArrayIndexOutOfBoundsException("这样是会越界的！");
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            throw e;
        }
        catch (Exception e){
            throw new Exception("无法修改");
        }

        System.out.print("Your function define is: ");
        String function_define;
        do {
            function_define = in.nextLine().trim();
        }while (function_define.isEmpty());

        key_value.remove(number1*2+1);
        key_value.add(number1*2+1,function_define);

    }

    static private void print(){
        for (int i=0;i<key_value.size();i+=2){
            System.out.println((i/2)+":\t"+
                    key_value.get(i)
                    +print_space(21-key_value.get(i).length())+
                    key_value.get(i+1));
        }
    }

    static String DictionaryToString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<key_value.size();i+=2){
            stringBuilder.append('\n');
            stringBuilder.append(Integer.valueOf(i/2).toString());
            stringBuilder.append(":\t");
            stringBuilder.append(key_value.get(i) );
            stringBuilder.append(print_space(21-key_value.get(i).length()));
            stringBuilder.append(key_value.get(i+1));
        }
        return stringBuilder.toString();
    }

    static private String print_space(int len){
        StringBuilder res=new StringBuilder();
        for(int i=0;i<len;i++){
            res.append(' ');
        }
        return res.toString();
    }


    private static ArrayList<String> init_key_value= new ArrayList<>(//要是想改这个，改完记得FUNC+INIT,才能将其应用
            Arrays.asList(
                    "ZERO", "(\\f.(\\x.x))",
                    "ONE", "(\\f.(\\x.(f x)))",
                    "TWO", "(\\f.(\\x.(f(f x))))",
                    "THREE", "(\\f.(\\x.(f(f(f x)))))",
                    "FOUR", "(\\f.(\\x.(f(f(f(f x))))))",
                    "FIVE", "(\\f.(\\x.(f(f(f(f(f x)))))))",
                    "SIX", "(\\f.(\\x.(f(f(f(f(f(f x))))))))",
                    "SIXSEVEN", "67_NB",
                    "SEVEN", "(\\f.(\\x.(f(f(f(f(f(f(f x)))))))))",
                    "EIGHT", "(\\f.(\\x.(f(f(f(f(f(f(f(f x))))))))))",
                    "NINE", "(\\f.(\\x.(f(f(f(f(f(f(f(f(f x)))))))))))",
                    "TEN", "(\\f.(\\x.(f(f(f(f(f(f(f(f(f(f x))))))))))))",
                    "ELEVEN", "(\\f.(\\x.(f(f(f(f(f(f(f(f(f(f(f x)))))))))))))",
                    "TWELVE", "(\\f.(\\x.(f(f(f(f(f(f(f(f(f(f(f(f x))))))))))))))",
                    "SUCC", "(\\n.(\\f.(\\x.(f((n f)x)))))",
                    "PLUS", "(\\m.(\\n.((m SUCC)n)))",
                    "MULT", "(\\m.(\\n.(\\f.(m(n f)))))",
                    "POW", "(\\b.(\\e.(e b)))",
                    "PRED", "(\\n.(\\f.(\\x.(((n(\\g.(\\h.(h(g f)))))(\\u.x))(\\v.v)))))",
                    "SUB", "(\\m.(\\n.((n PRED)m)))",
                    "TRUE", "(\\u.(\\v.u))",
                    "FALSE", "(\\u.(\\v.v))",
                    "AND", "(\\p.(\\q.((p q)p)))",
                    "OR", "(\\p.(\\q.((p p)q)))",
                    "NOT", "(\\p.(\\a.(\\b.((p b)a))))",
                    "IF", "(\\p.(\\a.(\\b.((p a)b))))",
                    "ISZERO", "(\\h.((h(\\t.FALSE))TRUE))",
                    "LEQ", "(\\m.(\\n.(ISZERO((SUB m)n))))",
                    "EQ", "(\\m.(\\n.((AND((LEQ m)n))((LEQ n)m))))",
                    "FACT", "(FACT1 FACT1)",
                    "FACT1", "(\\f.(\\n.(((ISZERO n)ONE)((MULT n)((f f)(PRED n))))))",
                    "Y","(\\g.((\\x.g(x x))(\\x.g(x x))))",
                    "FACTY","(Y FACT2)",
                    "FACT2","(\\f.(\\n.(((ISZERO n)ONE)((MULT n)(f(PRED n))))))",
                    "MAX", "(\\x.(\\y.(((IF((LEQ x)y))y)x)))",
                    "MIN", "(\\x.(\\y.(((IF((LEQ x)y))x)y)))",
                    "CDR", "(\\p.(p FALSE))",
                    "CAR", "(\\p.(p TRUE))",
                    "CONS", "(\\x.(\\y.(\\f.((f x)y))))",
                    "NULL", "(\\p.(p(\\x.(\\y.FALSE))))",
                    "NIL", "(\\x.TRUE)",
                    "LENGTH1","(\\g.\\c.\\x.NULL x c (g (SUCC c)(CDR x)))",
                    "LENGTH","(Y LENGTH1 ZERO)"

                    ));

    private static ArrayList<String> key_value=new ArrayList<>();
}