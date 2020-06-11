package cn.seecoder;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

class GameSetting {
    //mode用了一堆位运算符。。。
    //具体含义可以看看print
    private final static String TREEMODE_PLACE = "file/treeMode";
    private static final String PRINTMODE_PLACE = "file/printMode";
    JPanel panel=new JPanel(null);
    JPanel cards;

    GameSetting(JPanel cards){
        this.cards=cards;

        JButton returnButton=new JButton("返回");
        returnButton.setBounds(0,5,60*Global.ScreenWidth/1920,30*Global.ScreenHeight/1080);
        returnButton.addActionListener(e -> {
            cards.remove(panel);//真的不会出事吗？？？
            cards.updateUI();
        });
        panel.add(returnButton);
        Font font = new Font("黑体",Font.BOLD,40*Global.ScreenWidth/1920);

        JButton treeButton=new JButton("树绘制模式");
        treeButton.setFont(font);
        treeButton.setBounds(300*Global.ScreenWidth/1920,400*Global.ScreenHeight/1080,450*Global.ScreenWidth/1920,80*Global.ScreenHeight/1080);
        treeButton.addActionListener(e -> {
            String[] str={"垂直线","斜线"};
            Object object = JOptionPane.showInputDialog(panel,"","树的绘制模式",JOptionPane.PLAIN_MESSAGE,null,str,str[AST.treeMode]);
            if(object instanceof String){
                if(object.equals("垂直线")){
                    AST.treeMode=AST.VERTICAL;
                }
                else if(object.equals("斜线")){
                    AST.treeMode=AST.OBLIQUE;
                }
                set(TREEMODE_PLACE,AST.treeMode);////
            }
        });
        panel.add(treeButton);


        JButton printButton=new JButton("打印模式");
        printButton.setFont(font);
        printButton.setBounds(1100*Global.ScreenWidth/1920,400*Global.ScreenHeight/1080,360*Global.ScreenWidth/1920,80*Global.ScreenHeight/1080);
        printButton.addActionListener(e -> {
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
                set(PRINTMODE_PLACE,AST.printMode);////
            }
        });

        panel.add(printButton);
    }



    private static void set( String mode_file_place , int new_mode) {
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
        return get(TREEMODE_PLACE);
    }
    static int getPrintMode(){
        return get(PRINTMODE_PLACE);
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
