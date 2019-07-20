package cn.seecoder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Abstraction extends AST {
    Identifier param;//变量
    AST body;//表达式

    class TreeListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            button.setText("ABS");
        }
    }

    Abstraction(Identifier p, AST b, GameCreating cards){
        param = p;
        body = b;
        this.card =cards;
        this.father=father;
        button.setText("Abs");
        button.addActionListener(new TreeListener());
    }

    final public String toString(){
            return "\\."+body.toString();
    }

    final public String toString(int mode){
        switch (mode){
            case 1:
                if(body instanceof Application){
                    return "\\"+param.toString(mode)+"."+body.toString(mode)+"";
                }
            case 2:
                return "\\"+param.toString(mode)+"."+body.toString(mode)+"";      //不太完整的括号
            case 3:
                return "(\\"+param.toString(mode)+"."+body.toString(mode)+")";//完完整整的括号
            default:
                this.change_to_seecoder(new HashMap<>());
                return "\\."+body.toString();
        }
    }

    public Lexer toLexer(){
        ArrayList<String>arrayList=new ArrayList<>();
        arrayList.add("\\");
        arrayList.add(param.name);
        arrayList.add(".");
        Lexer lexer=new Lexer(arrayList);
        lexer.connect(body.toLexer());
        lexer.add_a_parenthesis();
        return lexer;
    }


    protected AST clone(){
        return new Abstraction(param.clone(),body.clone(), card);
    }

    public void change_to_seecoder(Map<String,Integer> map){
        Map<String,Integer>map1=new HashMap<>();
        for (String key :map.keySet()) {
            map1.put(key,map.get(key)+1);
        }
        map1.put(param.name,0);
        body.change_to_seecoder(map1);
    }

    //以下为规约---------------------------------

    public AST find_and_B_change(Bool have_changed){
        body=body.find_and_B_change(have_changed);
        return this;
    }

    protected void a_change(String oldString , String newString, boolean should_replace){
        if(   param.name.equals(oldString)  ){
            param.name=newString;
            body.a_change(oldString,newString,true);
        }
        body.a_change(oldString,newString,should_replace);
    }


    int calculate_node_distance(){
        left_distance=param.calculate_node_distance();
        right_distance=body.calculate_node_distance();
        width =3*wordSize+basicSize;
        return left_distance+right_distance+ width;//"abs"的长度
    }
}