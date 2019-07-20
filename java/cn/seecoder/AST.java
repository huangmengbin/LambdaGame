package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.lang.*;

public abstract class AST {

    final static int basicSize=35;
    final static int wordSize =14;


    AST father;
    int left_distance;
    int right_distance;
    int width;
    GameCreating card;////还有什么别的方法呢
    JButton button=new JButton();//还有什么别的方法呢

    private final String[] alphabet= Global.alphabet;

    public abstract String toString();

    public abstract String toString(int mode);

    protected abstract AST clone();//不知怎么的突然想写这个，感觉比之前的快多了

    //protected abstract boolean equals(AST that);

    public abstract AST find_and_B_change(Bool have_changed);

    public abstract Lexer toLexer();

    protected abstract void a_change (String oldString , String newString, boolean shold_replace);

    void B_replace1(Lexer rhs_lexer, Lexer bodylexer, AST ast, String label){
        //功能是
        //先发现需要a替换的位置
        ArrayList<String>temp1=new ArrayList<>();
        ArrayList<String>conflict=new ArrayList<>();
        ArrayList<String>allString=new ArrayList<>();

        for(String i: rhs_lexer.my_token){
            if(!temp1.contains(i) &&  Lexer.isLCID(i)  &&  !i.equals(label)){
                temp1.add(i);
            }
            if(!allString.contains(i) && Lexer.isLCID(i)){
                allString.add(i);
            }
        }

        for(String i : bodylexer.my_token){
            if(!allString.contains(i) && Lexer.isLCID(i)){
                allString.add(i);
            }
        }

        for (String i : temp1){
            if(!conflict.contains(i) && bodylexer.my_token.contains(i)){//发现了字母冲突的地方
                conflict.add(i);//那就把它加进去
            }
        }

        int alphabetPtr=0;//记录上次找到的位置，可以提高一点点效率
        String new_one;
        for (String old_one : conflict ){
            for(int i=alphabetPtr;i<alphabet.length;i++){
                new_one=alphabet[i];
                if(!allString.contains(new_one)){
                    allString.add(new_one);
                    this.a_change(old_one,new_one,false);//a等价替换
                    alphabetPtr=0;
                    break;
                }
            }
        }

        this.B_replace2(ast,label);//开始B替换第二步
    }
    //实际上B_replace1,B_replace2可以写在interpreter里头，会更清晰

    private void B_replace2(AST ast, String label){
        //我们要把很多东西替成传进来的那个AST
        if (this instanceof Application){//1.
            if(((Application) this).lhs instanceof Identifier){
                if(((Identifier) ((Application) this).lhs).name  .equals(label) ){
                    ((Application) this).lhs =ast.clone();
                }
            }
            else {
                ((Application) this).lhs.B_replace2(ast, label);
            }
            if(((Application) this).rhs instanceof Identifier){
                if(((Identifier) ((Application) this).rhs).name  .equals(label)){
                    ((Application) this).rhs =ast.clone();
                }
            }
            else {
                ((Application) this).rhs.B_replace2(ast, label);
            }
        }

        else if(this instanceof Abstraction){//2.
            if ( ! ((Abstraction) this).param.name  .equals(label)){
                //就是说如果abstraction左边等于label,那么右边的一大串都不需要搞了

                if(((Abstraction) this).body instanceof Identifier){
                    if(((Identifier) ((Abstraction) this).body).name   .equals(label)){
                        ((Abstraction) this).body=ast.clone();
                    }
                }
                else {
                    ((Abstraction) this).body.B_replace2(ast, label);
                }
            }
        }
        else if(this instanceof Identifier){//3
            System.out.println("bug of identify replace");
           //卧槽，好烦啊
        }

    }

    public abstract void change_to_seecoder(Map<String,Integer> map);




    private static boolean is_space(AST ast){//指一个Identifier,里面的name是一串空格
        if(ast instanceof Identifier){
            if(((Identifier) ast).name.trim().equals("")){
                return true;
            }
        }
        return false;
    }

    private static String to_space(String string){//返回一个相同长度全为空格的字符串
        StringBuilder result = new StringBuilder();
        for (int i=0;i<string.length();i++){
            result.append(' ');
        }
        return result.toString();
    }

    abstract int calculate_node_distance();



    //------------------------------打印一棵好看的树，怎么做到呢？？？

    int print_lines(){
        return print_lines(true);
    }

    int print_lines(boolean enable){
        ArrayList<AST> ast_list=new ArrayList<>();
        ast_list.add(this);
        return print_lines(ast_list,0,enable);
    }

    int print_lines(ArrayList<AST> ast_list ,int y,boolean enable){

        final int line_height=20,button_height=20;//每行高度40，20用来画线，20用来放按钮
        final int height=line_height+button_height;

        JPanel panel=new JPanel();
        panel.setLayout(null);
        panel.setBounds(4,y,99999999,height);//离最左距离为4
        y+=height;

        int x=0;
        int number=0;

        for (AST ast:ast_list){

            x+=ast.left_distance;

            ast.button.setBounds(x,line_height,ast.width,button_height);
            ast.button.setEnabled(enable);

            if(!is_space(ast)) {//空白无需处理

                panel.add(ast.button);//画按钮
                number++;

                if(ast_list.size()!=1 ) {//画线

                    int x1=x + ast.width / 2;
                    int y1=line_height;
                    int y2=0;
                    int x2;

                    if (number % 2 == 1) {
                        x2=x + ast.width + ast.right_distance + (wordSize * 3 + basicSize) / 2;
                    } else {
                        x2=x - ast.left_distance - (wordSize * 3 + basicSize) / 2;
                    }

                    if(false){
                        panel.add(new Line(x1,y1,x2,y2));//斜模式
                    }
                    else {
                        Color color=Color.black;
                        if(ast instanceof Identifier && (x2>x1) ){//以后改成 father instanceof abs
                            color=Color.RED;
                        }
                        panel.add(new Line(x2,y2,x2,y1/2,Color.black));//看你想根部那条是什么色
                        panel.add(new Line(x1,y1,x1,y1/2,color));
                        panel.add(new Line(x1,y1/2,x2,y1/2,color));
                    }

                }

            }

            x+=ast.width;
            x+=ast.right_distance;
            x+=(wordSize*3+basicSize);


        }

        this.card.verticalPanel.add(panel);

        boolean to_continue=false;
        ArrayList<AST> next_asts=new ArrayList<>();

        for(AST ast : ast_list){
            if(ast instanceof Application){
                to_continue=true;
                next_asts.add(((Application) ast).lhs);
                next_asts.add(((Application) ast).rhs);
            }
            else if(ast instanceof Abstraction){
                to_continue=true;
                next_asts.add(((Abstraction) ast).param);
                next_asts.add(((Abstraction) ast).body);
            }
            else if(ast instanceof Identifier){
                String new_name= to_space(((Identifier) ast).name);
                Identifier empty=new Identifier(new_name,((Identifier) ast).card);
                empty.left_distance=ast.left_distance;
                empty.right_distance=ast.right_distance;
                empty.width =ast.width;
                next_asts.add(empty);
            }
        }

        if(to_continue){
            return print_lines(next_asts,y,enable);
        }
        else {
            return y;
        }
    }
}