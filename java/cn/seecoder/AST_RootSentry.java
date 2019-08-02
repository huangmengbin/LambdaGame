package cn.seecoder;

import java.util.Map;

public class AST_RootSentry extends AST {//这是一个哨兵
    AST ast;
    AST_RootSentry(AST ast, GameFreelyExplore card){
        this.ast=ast;
        this.ast.father=this;
        this.card=card;
    }

    int calculate_node_distance(){
        int temp = ast.calculate_node_distance();
        left_distance=ast.left_distance;
        right_distance=ast.right_distance;
        width=ast.width;
        return temp;
    }
    void change_to_DeBruin(Map<String,Integer> map){
        ast.change_to_DeBruin(map);
    }
    protected void a_change(String oldString , String newString){
        System.out.println("AST_RootSentry can't be a change. ");
    }
    AST find_and_B_change(Bool have_changed){
        ast=ast.find_and_B_change(have_changed);
        return this;
    }
    protected AST clone(){
        return new AST_RootSentry(ast.clone(),card);
    }
    public String toString(int mode){
        return ast.toString(mode);
    }
    public String toString0(){
        System.out.println("root_sentry toString0?");
        return null;//应该不会发生
    }

}
