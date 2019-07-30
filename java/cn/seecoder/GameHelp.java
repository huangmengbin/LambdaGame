package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



class GameHelp {

    JPanel panel=new JPanel(null);
    JPanel cards;
    GameHelp(JPanel cards){

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

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("黑体",Font.BOLD,16));
        textArea.setText(helpString);
        //textArea.setText(Function.DictionaryToString());
        JScrollPane scrollPane =new JScrollPane(textArea);
        scrollPane.setBounds(180,75,750,880);
        scrollPane.createVerticalScrollBar();
        scrollPane.createHorizontalScrollBar();

        panel.add(scrollPane);

        JButton helpButton=new JButton("帮助");
        helpButton.setBounds(1200,300,80,30);
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(helpString);
            }
        });
        panel.add(helpButton);

        JButton functionButton=new JButton("组合子");
        functionButton.setBounds(1200,350,80,30);
        functionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(Function.DictionaryToString());
            }
        });
        panel.add(functionButton);
    }




















    private String helpString="\n" +
            "B规约：\n" +
            "在能规约的Application按钮那里点击一下\n" +
            "要是找不到就点一下“下一步”来自动寻找并规约\n" +
            "B规约之前有时候要进行一次a规约，否则可能会乱\n" +
            "\n" +
            "a变换：\n" +
            "点击一下那个变量名，输入新的变量名（首字符不允许为大写字母）\n" +
            "\n" +
            "绑定的组合子名称展开：\n" +
            "点击一下那个东西，就是青蓝色那种，就展开了\n" +
            "\n" +
            "组合子简写成它的名称：\n" +
            "点击一下根部的的那个Abstraction，输入正确的函数名称（首字母必须为大写字母）\n" +
            "\n" +
            "查看之前生成的树：\n" +
            "就是屏幕中间的那个框框，里面有一列按钮，点它们就可以了\n" +
            "\n" +
            "查看绑定的组合子及其对应名称：\n" +
            "可以去控制台看，或者在这里看\n" +
            "\n" +
            "修改组合子及其对应名称：\n" +
            "去控制台吧\n" +
            "\n" +
            "删除存档：\n" +
            "去控制台吧\n" +
            "\n" +
            "退出游戏：\n" +
            "返回到不能返回的时候，右上方的“退出游戏”按钮，旁边有一个叉叉\n"
            ;
}
