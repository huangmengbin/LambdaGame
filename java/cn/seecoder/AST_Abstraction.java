package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class AST_Abstraction extends AST {
    AST_Identifier param;//变量
    AST body;//表达式

    class TreeListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            String newName = JOptionPane.showInputDialog(null, "请输入函数名称", "", 1);
            if ( newName!=null  ) {//不考虑是否首字母大写
                AST_Identifier identifier=Function.AST_to_FunctionIdentify(AST_Abstraction.this,newName.trim(),card,father);
                if(identifier!=null){
                    replaceAST(AST_Abstraction.this,identifier);
                    card.updateMessage();
                }
            }
        }
    }

    AST_Abstraction(AST_Identifier p, AST b, GameExploration cards){
        param = p;
        body = b;
        this.card =cards;
        param.father=this;
        body.father=this;
        button.setText("Abs");
        button.setBackground(Color.yellow.brighter());
        button.addActionListener(new TreeListener());
    }

    final public String toString0(){
            return "\\."+body.toString0();
    }

    final public String toString(int mode){
        switch (mode){
            case 1:
                if(body instanceof AST_Application){
                    return "\\"+param.toString(mode)+"."+body.toString(mode)+"";
                }
            case 2:
                return "\\"+param.toString(mode)+"."+body.toString(mode)+"";      //不太完整的括号
            case 3:
                return "(\\"+param.toString(mode)+"."+body.toString(mode)+")";//完完整整的括号
            default:
                this.change_to_seecoder(new HashMap<>());
                return "\\."+body.toString0();
        }
    }

    public Lexer toLexer(){
        ArrayList<String>arrayList=new ArrayList<>();
        arrayList.add("\\");
        arrayList.add(param.getName());
        arrayList.add(".");
        Lexer lexer=new Lexer(arrayList);
        lexer.connect(body.toLexer());
        lexer.add_a_parenthesis();
        return lexer;
    }


    protected AST clone(){
        return new AST_Abstraction(param.clone(),body.clone(), card);
    }

    public void change_to_seecoder(Map<String,Integer> map){
        Map<String,Integer>map1=new HashMap<>();
        for (String key :map.keySet()) {
            map1.put(key,map.get(key)+1);
        }
        map1.put(param.getName(),0);
        body.change_to_seecoder(map1);
    }

    //以下为规约---------------------------------

    public AST find_and_B_change(Bool have_changed){
        body=body.find_and_B_change(have_changed);
        return this;
    }

    protected void a_change(String oldString , String newString){
        if(   ! param.getName().equals(oldString)  ){//相等则不做处理
            body.a_change(oldString, newString);
        }
    }


    int calculate_node_distance(){
        left_distance=param.calculate_node_distance();
        right_distance=body.calculate_node_distance();
        width =3*wordSize+basicSize;
        return left_distance+right_distance+ width;//"abs"的长度
    }
}