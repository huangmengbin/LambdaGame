package cn.seecoder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

public class Identifier extends AST {

    private final static int empty_distance =4;

    String name; //名字
    private String value;//没什么用的东西

    class TreeListener implements ActionListener {
        public void actionPerformed(ActionEvent e){

        }
    }

    public Identifier(String n, GameCreating cards){
        name = n;
        this.card =cards;
        this.father=father;
        button.setText(n);
        button.addActionListener(new TreeListener());
    }

    public AST find_and_B_change(Bool have_changed){return this;}

    protected void a_change(String oldString , String newString, boolean should_replace){
        if(name.equals(oldString)&&should_replace){
            name=newString;
        }
    }

    public String toString(){
            return value;
    }

    public String toString(int mode){
        if(mode==0){
            return "free";
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

    protected Identifier clone(){
        return new Identifier(this.name, card);}
}