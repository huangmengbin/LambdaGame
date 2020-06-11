package cn.seecoder;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class AST_Identifier extends AST {

    private final static int empty_distance =4;

    private String name; //名字
    private String value;//没什么用的东西

    void setName(String name){
        this.name=name;
        if(isFunction()){
            button.setBackground(Color.CYAN);
        }
        else {
            button.setBackground(new Color(120,255,120));
        }
        button.setText(name);
    }
    String getName(){
        return name;
    }


    boolean isFunction(){
        return name.charAt(0)>='A' && name.charAt(0)<='Z';//首字母大写 视为抽象函数
    }
    private boolean isFunction(String name){
        return name.charAt(0)>='A' && name.charAt(0)<='Z';//首字母大写 视为抽象函数
    }

    class TreeListener implements ActionListener {

        public void actionPerformed(ActionEvent e){
            if(isFunction()){//视为抽象函数,展开它
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
                        String newName = JOptionPane.showInputDialog(null, "请输入新的变量名", "a变换", JOptionPane.PLAIN_MESSAGE).trim();
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

    AST_Identifier(String name, GameFreelyExplore cards){
        setName(name);
        this.card =cards;
        button.addActionListener(new TreeListener());
    }

    AST find_and_B_change(Bool have_changed){return this;}

    protected void a_change(String oldString , String newString){
        if(name.equals(oldString)){
            setName(newString);//封装？？
        }
    }

    public String toString0(){
            return value;
    }

    public String toString(int mode){
        if(mode==DEBRUIN){
            return toString0();
        }
        else {
            return name;
        }
    }



    void change_to_DeBruin(Map<String,Integer> map){
        if (!map.containsKey(name)){
            value="free";//大概是这个意思吧
        }
        else {
            value = map.get(name).toString();
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