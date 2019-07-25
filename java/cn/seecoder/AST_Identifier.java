package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class AST_Identifier extends AST {

    private final static int empty_distance =4;

    private String name; //名字
    private String value;//没什么用的东西

    private void setName(String name){
        this.name=name;
        if(isFunction(name)){
            button.setBackground(Color.CYAN);
        }
        button.setText(name);
    }
    String getName(){
        return name;
    }


    private boolean isFunction(String name){
        return name.charAt(0)>='A' && name.charAt(0)<='Z';//首字母大写 视为抽象函数
    }
    class TreeListener implements ActionListener {

        public void actionPerformed(ActionEvent e){
            if(isFunction(name)){//视为抽象函数,展开它
                AST ast=Function.FunctionString_to_AST(name,AST_Identifier.this.card,AST_Identifier.this.father);
                if(ast!=null) {
                    replaceAST(AST_Identifier.this, ast);
                    card.updateMessage();
                }
            }
            else {//视为变量名，a规约
                if(father instanceof AST_Abstraction
                &&((AST_Abstraction) father).param==AST_Identifier.this) {
                    try {
                        String newName = JOptionPane.showInputDialog(null, "请输入新的变量名", "a规约", 1).trim();
                        CheckLegal.check_IdentifyName(newName);
                        if (!isFunction(newName) && !newName.equals(name)) {
                            ((AST_Abstraction) father).body.a_change(name, newName);
                            AST_Identifier.this.setName(newName);
                            card.updateMessage();
                            }
                        }
                    catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    public AST_Identifier(String name, GameExploration cards){
        setName(name);
        this.card =cards;
        button.addActionListener(new TreeListener());
    }

    public AST find_and_B_change(Bool have_changed){return this;}

    protected void a_change(String oldString , String newString){
        if(name.equals(oldString)){
            setName(newString);//封装？？
        }
    }

    public String toString0(){
            return value;
    }

    public String toString(int mode){
        if(mode==0){
            return toString0();
        }
        else {
            return name;
        }
    }

    public Lexer toLexer(){
        ArrayList<String>arrayList=new ArrayList<>();
        arrayList.add(name);
        return new Lexer(arrayList);
    }

    public void change_to_seecoder(Map<String,Integer> map){
        if (!map.containsKey(name)){
            value="free";//大概是这个意思吧
        }
        else {
            value = Integer.valueOf(map.get(name)).toString();//所以要怎么改？？
        }
    }



    int calculate_node_distance(){
        left_distance= empty_distance;
        right_distance= empty_distance;
        width =this.name.length()*wordSize+basicSize;
        return left_distance+right_distance+ width;
    }

    protected AST_Identifier clone(){
        return new AST_Identifier(this.name, card);}
}