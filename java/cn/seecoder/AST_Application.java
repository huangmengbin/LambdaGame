package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

class AST_Application extends AST{
    AST lhs;//左树
    AST rhs;//右树

    class TreeListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(lhs instanceof AST_Abstraction)//可约
            {
                replaceAST(AST_Application.this,find_and_B_change(new Bool(false)));
                card.updateMessage();
            }
            else{
                AST_Identifier identifier = new AST_Identifier("5467",card);
                int step1 = GameProved.provedReplace(AST_Application.this,identifier);
                if(step1>0){
                    card.step+=step1-1;
                    replaceAST(AST_Application.this,identifier);
                    card.updateMessage();
                }
                else if(step1 <= -2) {
                    int confirm = JOptionPane.showConfirmDialog(null,"现在要证明它吗？","这个东西还没被证明",JOptionPane.YES_NO_OPTION);
                    if(confirm==0){
                        String source = ((AST_Identifier)lhs).getName()+" "+((AST_Identifier)rhs).getName();
                        String destination = JOptionPane.showInputDialog(null,source+"  =>","欲证：",JOptionPane.PLAIN_MESSAGE);
                        if(destination!=null && destination.length()>=1 && Character.isUpperCase(destination.charAt(0))){
                            GameProving game = new GameProving(card.cards, source, destination,null);
                            card.cards.add(game.panel);
                            CardLayout cl = (CardLayout) (card.cards.getLayout());
                            cl.last(card.cards);
                        }
                    }
                }
            }
        }
    }


    AST_Application(AST left, AST right, GameFreelyExplore cards){
        lhs = left;
        rhs = right;
        this.button.setBackground(Color.pink);
        this.card =cards;
        lhs.father=this;
        rhs.father=this;
        button.setText("APP");
        button.addActionListener(new TreeListener());
    }



    public String toString0(){
        return "("+ lhs.toString0()+" "+ rhs.toString0()+")";
    }

    public String toString(int mode){
        switch (mode){
            case SIMPLIFY:
                StringBuilder temp1=new StringBuilder(lhs.toString(mode));
                StringBuilder temp2=new StringBuilder(rhs.toString(mode));
                if(lhs instanceof AST_Abstraction){
                    temp1.insert(0,"(");
                    temp1.append(")");
                }
                if( ! (rhs instanceof AST_Identifier) ){
                    temp2.insert(0,"(");
                    temp2.append(")");
                }
                return temp1.toString()+" "+temp2.toString();

            case MEDIUM:
                StringBuilder temp3=new StringBuilder(rhs.toString(mode));
                    if(rhs instanceof AST_Abstraction){
                        temp3.insert(0,"(");
                        temp3.append(")");
                    }
                return "("+ lhs.toString(mode)+" "+ temp3.toString()+")";
            case FULL:
                return "("+ lhs.toString(mode)+" "+ rhs.toString(mode)+")";
            default:
                this.change_to_DeBruin(new HashMap<>());
                return this.toString0();
        }
    }


    protected AST clone(){
        return new AST_Application(lhs.clone(), rhs.clone(), card);
    }

    void change_to_DeBruin(Map<String, Integer> map){
        lhs.change_to_DeBruin(map);
        rhs.change_to_DeBruin(map);
    }

    //以下为规约------------------------------------------

    AST find_and_B_change(Bool have_changed){

        if(!have_changed.isTrue() && lhs instanceof AST_Abstraction){
            have_changed.setValue(true);//找到了hhh
            AST temp = this.B_replace_0();//它好像不给写this=this.B_replace_0();   hhh
            temp.father=this.father; ///???
            return temp;
        }
        else {
            lhs=lhs.find_and_B_change(have_changed);
            rhs=rhs.find_and_B_change(have_changed);
            return this;
        }
    }

    private AST B_replace_0(){//B替换第0步，即自己是APP，左边是Abs 的时候，开始替代


        String label =((AST_Abstraction) lhs).param.getName();

        if(((AST_Abstraction) lhs).body instanceof AST_Identifier){
            if(((AST_Identifier) ((AST_Abstraction) lhs).body).getName().equals(label)){
                return rhs;
            }
            else {
                return ((AST_Abstraction) lhs).body;
            }
        }

        else {
            ((AST_Abstraction) lhs).body.B_replace_1(rhs.clone(),label);
            return ((AST_Abstraction) lhs).body;
        }

    }

    protected void a_change(String oldString , String newString){
        lhs.a_change(oldString,newString);
        rhs.a_change(oldString,newString);
    }

    int calculate_node_distance(){
        left_distance= lhs.calculate_node_distance();
        right_distance= rhs.calculate_node_distance();
        width =3*wordSize+basicSize;
        return left_distance+right_distance+ width;//长度
    }


}