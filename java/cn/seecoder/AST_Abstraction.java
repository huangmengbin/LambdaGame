package cn.seecoder;


import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

class AST_Abstraction extends AST {
    AST_Identifier param;//变量
    AST body;//表达式

    class TreeListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            String newName = JOptionPane.showInputDialog(null, "请输入函数名称", "抽象", JOptionPane.PLAIN_MESSAGE);
            if ( newName!=null  ) {//不考虑是否首字母大写
                AST_Identifier identifier=Function.AST_to_FunctionIdentify(AST_Abstraction.this,newName.trim(),card,father);
                if(identifier!=null){
                    replaceAST(AST_Abstraction.this,identifier);
                    card.updateMessage();
                }
            }
        }
    }

    AST_Abstraction(AST_Identifier p, AST b, GameFreelyExplore cards){
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
            case SIMPLIFY:
                if(body instanceof AST_Application){
                    return "\\"+param.toString(mode)+"."+body.toString(mode)+"";
                }
            case MEDIUM:
                return "\\"+param.toString(mode)+"."+body.toString(mode)+"";      //不太完整的括号
            case FULL:
                return "(\\"+param.toString(mode)+"."+body.toString(mode)+")";//完完整整的括号
            default:
                this.change_to_DeBruin(new HashMap<>());
                return this.toString0();
        }
    }


    protected AST clone(){
        return new AST_Abstraction(param.clone(),body.clone(), card);
    }

    void change_to_DeBruin(Map<String,Integer> map){
        Map<String,Integer>map1=new HashMap<>();
        for (String key :map.keySet()) {
            map1.put(key,map.get(key)+1);
        }
        map1.put(param.getName(),0);
        body.change_to_DeBruin(map1);
    }

    //以下为规约---------------------------------

    AST find_and_B_change(Bool have_changed){
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