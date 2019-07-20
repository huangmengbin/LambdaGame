package cn.seecoder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

class Application extends AST{
    AST lhs;//左树
    AST rhs;//右树

    class TreeListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(lhs instanceof Abstraction){

            }
        }
    }


    Application(AST left, AST right, GameCreating cards){
        lhs = left;
        rhs = right;
        this.card =cards;
        this.father=father;
        button.setText("APP");
        button.addActionListener(new TreeListener());
    }



    public String toString(){
        return "("+ lhs.toString()+" "+ rhs.toString()+")";
    }

    public String toString(int mode){
        switch (mode){
            case 1:
                StringBuilder temp1=new StringBuilder(lhs.toString(mode));
                StringBuilder temp2=new StringBuilder(rhs.toString(mode));
                if(lhs instanceof Abstraction){
                    temp1.insert(0,"(");
                    temp1.append(")");
                }
                if( ! (rhs instanceof Identifier) ){
                    temp2.insert(0,"(");
                    temp2.append(")");
                }
                return temp1.toString()+" "+temp2.toString();

            case 2:
                StringBuilder temp3=new StringBuilder(rhs.toString(mode));
                    if(rhs instanceof Abstraction){
                        temp3.insert(0,"(");
                        temp3.append(")");
                    }
                return "("+ lhs.toString(mode)+" "+ temp3.toString()+")";
            case 3:
                return "("+ lhs.toString(mode)+" "+ rhs.toString(mode)+")";
            default:
                this.change_to_seecoder(new HashMap<>());
                return this.toString();
        }
    }

    public Lexer toLexer(){
        Lexer a= lhs.toLexer();
        Lexer b= rhs.toLexer();
        a.connect(b);
        a.add_a_parenthesis();
        return a;
    }

    protected AST clone(){
        return new Application(lhs.clone(), rhs.clone(), card);
    }

    public void change_to_seecoder(Map<String, Integer> map){
        lhs.change_to_seecoder(map);
        rhs.change_to_seecoder(map);
    }

    //以下为规约------------------------------------------

    public AST find_and_B_change(Bool have_changed){

        if(!have_changed.isTrue() && lhs instanceof Abstraction){
            have_changed.setValue(true);//找到了hhh
            return this.B_replace();//它好像不给写this=this.B_replace();   hhh
        }
        else {
            lhs=lhs.find_and_B_change(have_changed);
            rhs=rhs.find_and_B_change(have_changed);
            return this;
        }
    }

    private AST B_replace(){//B替换第0步，即自己是APP，左边是Abs 的时候，开始替代

        if(! (lhs instanceof Abstraction) ) {
            System.out.println("B规约出现bug");
            return this;
        }

        String label =((Abstraction) lhs).param.name;

        if(((Abstraction) lhs).body instanceof Identifier){
            if(((Identifier) ((Abstraction) lhs).body).name.equals(label)){
                return rhs;
            }
            else {
                return ((Abstraction) lhs).body;
            }
        }

        else {
            Lexer lexer = rhs.toLexer();
            Lexer bodyLexer=((Abstraction) lhs).body.toLexer();
            ((Abstraction) lhs).body.B_replace1(lexer,bodyLexer, rhs.clone(),label);//进入了B替换第1步
            return ((Abstraction) lhs).body;
        }

    }

    protected void a_change(String oldString , String newString, boolean should_replace){
        lhs.a_change(oldString,newString,should_replace);
        rhs.a_change(oldString,newString,should_replace);
    }

    int calculate_node_distance(){
        left_distance= lhs.calculate_node_distance();
        right_distance= rhs.calculate_node_distance();
        width =3*wordSize+basicSize;
        return left_distance+right_distance+ width;//长度
    }


}