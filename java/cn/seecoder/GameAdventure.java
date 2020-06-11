package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

class GameAdventure extends GameFreelyExplore {

    private String source;
    private String destination;
    private int twoStar;
    private int threeStar;
    private GamesChoosing gamesChoosing;

    GameAdventure(String source, JPanel cards, int twoStar, int threeStar, String destination, GamesChoosing gamesChoosing){
        super(cards);

        JButton proveButton = new JButton("证明");
        inputBox.add(Box.createHorizontalStrut(60*Global.ScreenWidth/1920));
        inputBox.add(proveButton);
        proveButton.addActionListener(e -> {
            GameProved game = new GameProved(cards);
            cards.add(game.panel);
            CardLayout cl=(CardLayout)(cards.getLayout());
            cl.last(cards);
        });

        this.source=source;
        this.twoStar=twoStar;
        this.threeStar=threeStar;
        this.destination=destination;
        this.gamesChoosing=gamesChoosing;

        lexer=new Lexer(source);//要不要replace
        parser=new Parser(lexer);
        AST temp = parser.parse(this);
        ast=new AST_RootSentry(temp,this);
        overTextArea.setText("");//清空？？？

        inputButton.setText("重玩");


        JButton button=new JButton("目的 ： "+destination);
        button.setEnabled(false);
        button.setBounds(780*Global.ScreenWidth/1920,3,350*Global.ScreenWidth/1920,24*Global.ScreenHeight/1080);
        panel.add(button);


        inputTextField.setText(source);
        inputTextField.setEditable(false);

        updateMessage();
    }

    void updateMessage() {
        super.updateMessage();
        if(ast.toString(1).equals(destination)){//通关了,无需abstraction.body instanceof AST_Identifier &&

            if(GameProved.could_be_proved(source)){
                int prove = JOptionPane.showConfirmDialog(panel,"是否要将其加入已证集合中，以后可以直接使用","完成了一个新的证明",JOptionPane.YES_NO_OPTION);
                if(prove==0){
                    GameProved.adder(source,destination,step);
                }
            }

            final String file_place = "file/"+source;

            passSuccessfully=true;
            checkHistory(ast,false);


            String starMessage="？星通关";
            String newRecordOrNot="成功";

            try{
                int history = GamesChoosing.getHistory(source);

                if(step<history){//更新历史纪录
                    newRecordOrNot="新记录  ";
                    GamesChoosing.setHistory(source,step);
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

            }
            catch (Exception e){
                e.printStackTrace();
            }



            Object[] bs=new Object[]{"重玩","返回","查看通关信息"};//这个怎么搞？？？得修改。。。。
            int chooseMessage = JOptionPane.showOptionDialog(panel,newRecordOrNot+starMessage,step+"步"  ,JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,bs,bs[1]);

            if(chooseMessage==0){
                inputSure();
            }
            else if(chooseMessage==1) {//返回
                ret();
            }
            else if(chooseMessage==2){
                printMessage();
            }

        }
    }

    void ret(){
        gamesChoosing.updateMessage();///
        super.ret();
    }
}
