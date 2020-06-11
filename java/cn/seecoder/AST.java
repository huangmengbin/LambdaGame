package cn.seecoder;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.lang.*;

public abstract class AST {

    final static int basicSize=35*1920/Global.ScreenWidth;
    final static int wordSize =16;

    final static int DEBRUIN=0;
    final static int SIMPLIFY=1;
    final static int MEDIUM=2;
    final static int FULL=3;

    final static int VERTICAL=0;
    final static int OBLIQUE=1;


    static int treeMode =GameSetting.getTreeMode();
    static int printMode=GameSetting.getPrintMode();


    AST father;
    int left_distance;
    int right_distance;
    int width;
    GameFreelyExplore card;////还有什么别的方法呢
    JButton button=new JButton();//还有什么别的方法呢

    AST(){
        button.setFont(new Font("黑体",Font.BOLD,16*Global.ScreenWidth/1920));
    }

    static void replaceAST(AST oldAST,AST newAST){

        if(oldAST.father==null){
            System.out.println("replaceAST : father is null");
        }
        else if(oldAST.father instanceof AST_Identifier){
            System.out.println("replaceAST : father is Identify");
        }
        else if(oldAST.father instanceof AST_Application){
            if(((AST_Application) oldAST.father).lhs==oldAST){
                ((AST_Application) oldAST.father).lhs=newAST;
            }
            else if(((AST_Application) oldAST.father).rhs==oldAST){
                ((AST_Application) oldAST.father).rhs=newAST;
            }
            newAST.father=oldAST.father;
        }
        else if(oldAST.father instanceof AST_Abstraction){
            if(((AST_Abstraction) oldAST.father).param==oldAST){
                ((AST_Abstraction) oldAST.father).param=(AST_Identifier) newAST;
            }
            else if(((AST_Abstraction) oldAST.father).body==oldAST){
                ((AST_Abstraction) oldAST.father).body=newAST;
            }
            newAST.father=oldAST.father;
        }
        else if(oldAST.father instanceof AST_RootSentry){
            ((AST_RootSentry) oldAST.father).ast=newAST;
            newAST.father=oldAST.father;
        }
    }

    public abstract String toString0();

    public String toString(){return toString(3);}

    public abstract String toString(int mode);

    protected abstract AST clone();//不知怎么的突然想写这个，感觉比之前的快多了

    abstract AST find_and_B_change(Bool have_changed);
    
    protected abstract void a_change (String oldString , String newString);

    void B_replace_1(AST ast, String label){
        //我们要把很多东西替成传进来的那个AST
        if (this instanceof AST_Application){//1.
            if(((AST_Application) this).lhs instanceof AST_Identifier){
                if(((AST_Identifier) ((AST_Application) this).lhs).getName()  .equals(label) ){
                    ((AST_Application) this).lhs =ast.clone();
                    ((AST_Application) this).lhs.father=this;//关键
                }
            }
            else {
                ((AST_Application) this).lhs.B_replace_1(ast, label);
            }
            if(((AST_Application) this).rhs instanceof AST_Identifier){
                if(((AST_Identifier) ((AST_Application) this).rhs).getName()  .equals(label)){
                    ((AST_Application) this).rhs =ast.clone();
                    ((AST_Application) this).rhs.father=this;//关键
                }
            }
            else {
                ((AST_Application) this).rhs.B_replace_1(ast, label);
            }
        }

        else if(this instanceof AST_Abstraction){//2.
            if ( ! ((AST_Abstraction) this).param.getName() .equals(label)){
                //就是说如果abstraction左边等于label,那么右边的一大串都不需要搞了

                if(((AST_Abstraction) this).body instanceof AST_Identifier){
                    if(((AST_Identifier) ((AST_Abstraction) this).body).getName()   .equals(label)){
                        ((AST_Abstraction) this).body=ast.clone();
                        ((AST_Abstraction) this).body.father=this;////关键
                    }
                }
                else {
                    ((AST_Abstraction) this).body.B_replace_1(ast, label);
                }
            }
        }
        else if(this instanceof AST_RootSentry){//3
            System.out.println("bug of root_sentry replace");
        }
        else if(this instanceof AST_Identifier){//4
            System.out.println("bug of identify replace");
           //卧槽，好烦啊
        }

    }

    abstract void change_to_DeBruin(Map<String,Integer> map);




    //------------------------------打印一棵好看的树，怎么做到呢？？？

    private static boolean is_space(AST ast){//指一个Identifier,里面的name是一串空格
        if(ast instanceof AST_Identifier){
            if(((AST_Identifier) ast).getName().trim().equals("")){
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

    //-----------------------------打印

    int print_lines(){
        return print_lines(true);
    }

    int print_lines(boolean enable){
        ArrayList<AST> ast_list=new ArrayList<>();
        if(this instanceof AST_RootSentry){
            ast_list.add(((AST_RootSentry) this).ast);
        }
        else {
            ast_list.add(this);
        }
        return print_lines(ast_list,0,enable,this.card);
    }

    private static int print_lines(ArrayList<AST> ast_list ,int y,boolean enable,GameFreelyExplore card){




        final int line_height=25,button_height=30;//每行高度=用来画线高度+用来放按钮高度
        final int height=line_height+button_height;

        JPanel panel=new JPanel();
        panel.setLayout(null);
        panel.setBounds(4,y*Global.ScreenHeight/1080,999999999,height*Global.ScreenHeight/1080);//离最左距离为4
        y+=height;

        int x=0;
        int number=0;

        for (AST ast:ast_list){

            x+=ast.left_distance;

            ast.button.setBounds(x*Global.ScreenWidth/1920,line_height*Global.ScreenHeight/1080,ast.width*Global.ScreenWidth/1920,button_height*Global.ScreenHeight/1080);
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

                    Color color=Color.black;
                    if(ast instanceof AST_Identifier && x2>x1 && ast.father instanceof AST_Abstraction){
                        color=Color.RED.brighter();
                    }
                    if(treeMode==OBLIQUE){
                        panel.add(new Line(x1*Global.ScreenWidth/1920,y1*Global.ScreenHeight/1080,x2*Global.ScreenWidth/1920,y2*Global.ScreenHeight/1080,color));//斜模式
                    }
                    else if(treeMode==VERTICAL){
                        panel.add(new Line(x2*Global.ScreenWidth/1920,y2*Global.ScreenHeight/1080,x2*Global.ScreenWidth/1920,y1/2*Global.ScreenHeight/1080,Color.black));//看你想根部那条是什么色
                        panel.add(new Line(x1*Global.ScreenWidth/1920,y1*Global.ScreenHeight/1080,x1*Global.ScreenWidth/1920,y1/2*Global.ScreenHeight/1080,color));
                        panel.add(new Line(x1*Global.ScreenWidth/1920,y1/2*Global.ScreenHeight/1080,x2*Global.ScreenWidth/1920,y1/2*Global.ScreenHeight/1080,color));
                    }

                }

            }

            x+=ast.width;
            x+=ast.right_distance;
            x+=(wordSize*3+basicSize);


            if(ast.father==null){//检测一下树的指针是否都正确了
                ast.button.setBackground(Color.blue);
            }////////////////////////////////////////妈的
            if(ast.father instanceof AST_Application){
                if(((AST_Application) ast.father).lhs!=ast && ((AST_Application) ast.father).rhs!=ast){
                    ast.button.setBackground(Color.BLUE);
                }
            }
            if(ast.father instanceof AST_Abstraction){
                if(((AST_Abstraction) ast.father).param!=ast && ((AST_Abstraction) ast.father).body!=ast){
                    ast.button.setBackground(Color.RED);
                }
            }

        }

        card.verticalPanel.add(panel);

        boolean to_continue=false;
        ArrayList<AST> next_asts=new ArrayList<>();

        for(AST ast : ast_list){
            if(ast instanceof AST_Application){
                to_continue=true;
                next_asts.add(((AST_Application) ast).lhs);
                next_asts.add(((AST_Application) ast).rhs);
            }
            else if(ast instanceof AST_Abstraction){
                to_continue=true;
                next_asts.add(((AST_Abstraction) ast).param);
                next_asts.add(((AST_Abstraction) ast).body);
            }
            else if(ast instanceof AST_Identifier){//这个暂时还是得保留啊，以后应该有别的解决方案
                String new_name= to_space(((AST_Identifier) ast).getName());
                AST_Identifier empty=new AST_Identifier(new_name,((AST_Identifier) ast).card);
                empty.left_distance=ast.left_distance;
                empty.right_distance=ast.right_distance;
                empty.width =ast.width;
                next_asts.add(empty);
            }
        }

        if(to_continue){
            return print_lines(next_asts,y,enable,card);
        }
        else {
            return y;
        }
    }
}