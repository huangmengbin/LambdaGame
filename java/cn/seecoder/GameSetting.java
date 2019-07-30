package cn.seecoder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

class GameSetting {
    //mode用了一堆位运算符。。。
    //具体含义可以看看print
    JPanel panel=new JPanel(null);
    JPanel cards;

    GameSetting(JPanel cards){
        this.cards=cards;

        JButton returnButton=new JButton("返回");
        returnButton.setBounds(0,5,60,30);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cards.remove(panel);//真的不会出事吗？？？
                cards.updateUI();
            }
        });
        panel.add(returnButton);

        JButton treeButton=new JButton("树绘制模式");
        treeButton.setBounds(300,400,130,80);
        treeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] str={"垂直线","斜线"};
                Object object = JOptionPane.showInputDialog(panel,"","树的绘制模式",JOptionPane.PLAIN_MESSAGE,null,str,str[AST.treeMode]);
                if(object instanceof String){
                    if(object.equals("垂直线")){
                        AST.treeMode=AST.VERTICAL;
                    }
                    else if(object.equals("斜线")){
                        AST.treeMode=AST.OBLIQUE;
                    }
                    set("treeMode",AST.treeMode);////
                }
            }
        });
        panel.add(treeButton);


        JButton printButton=new JButton("打印模式");
        printButton.setBounds(1100,400,130,80);
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] str={"比较精简的括号","有点完整的括号","十分完整的括号"};//不想要德布鲁因模式了
                Object object = JOptionPane.showInputDialog(panel,"","括号的打印模式",JOptionPane.PLAIN_MESSAGE,null,str,str[AST.printMode-1]);
                if(object instanceof String){
                    if(object.equals("比较精简的括号")){
                        AST.printMode=AST.SIMPLIFY;
                    }
                    else if(object.equals("有点完整的括号")){
                        AST.printMode=AST.MEDIUM;
                    }
                    else if(object.equals("十分完整的括号")){
                        AST.printMode=AST.FULL;
                    }
                    set("printMode",AST.printMode);////
                }
            }
        });

        panel.add(printButton);
    }



    static void set( String mode_file_place , int new_mode) {
        try {
            FileWriter writer = new FileWriter(mode_file_place);
            writer.write(Integer.valueOf(new_mode).toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("尝试写mode文件失败");
        }

    }

    static int getTreeMode(){
        return get("treeMode");
    }
    static int getPrintMode(){
        return get("printMode");
    }

    private static int get(String string) {
        try {
            File file = new File(string) ;
            Scanner scanner = new Scanner(file);
            return scanner.nextInt();
        }
        catch (Exception e){
            e.printStackTrace();
            return 1;
        }
    }
}
