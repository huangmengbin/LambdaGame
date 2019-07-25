package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

class GameAdventure extends GameExploration {

    private String source;
    private String destination;
    private int twoStar;
    private int threeStar;

    GameAdventure(String source, JPanel cards, int twoStar, int threeStar,String destination){
        super(cards);

        this.source=source;
        this.twoStar=twoStar;
        this.threeStar=threeStar;
        this.destination=destination;

        lexer=new Lexer(source);//要不要replace
        parser=new Parser(lexer);
        AST_Identifier param=new AST_Identifier("abstraction",this);
        AST body=parser.parse(this);
        abstraction =new AST_Abstraction(param,body,this);
        overTextArea.setText("");//清空

        inputButton.setText("重玩");




        inputTextField.setText(source);
        inputTextField.setEditable(false);

        updateMessage();
    }

    void updateMessage() {
        super.updateMessage();
        if(abstraction.body.toString(1).equals(destination)){//通关了


            int history;

            try{
                File file=new File(source);
                Scanner in =new Scanner(file);
                history=in.nextInt();

                String starMessage="？星通关";
                String newRecordOrNot="成功";

                if(step<history){//更新历史纪录
                    newRecordOrNot="新记录  ";
                    history=step;
                    FileWriter writer = new FileWriter(source);
                    writer.write(Integer.valueOf(history).toString());
                    writer.close();
                }


                if(step>twoStar){
                    starMessage="一星通关";
                }
                else if(step<=threeStar){
                    starMessage="三星通关";
                }
                else {
                    starMessage="两星通关";
                }

                //JButton[] bs={new JButton("确定")};//这个怎么搞？？？得修改。。。。
                JOptionPane.showOptionDialog(cards,null,newRecordOrNot+starMessage,JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,null,null);

                JFrame frame=new JFrame("通关信息：");
                frame.setBounds(50,50,1800,800);
                frame.setVisible(true);
                frame.add(overScrollpane);

            }
            catch (Exception e){
                e.printStackTrace();
            }

            panel.removeAll();
            cards.removeAll();
            GameWelcome welcome=new GameWelcome(cards);
            cards.add(welcome.panel);
            GamesChoosing chooseGames=new GamesChoosing(cards);
            cards.add(chooseGames.panel);
            CardLayout cl=(CardLayout)(cards.getLayout());
            cl.last(cards);

            cards.updateUI();
        }
    }
}
