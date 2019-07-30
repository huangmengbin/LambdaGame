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
    GamesChoosing gamesChoosing;

    GameAdventure(String source, JPanel cards, int twoStar, int threeStar,String destination,GamesChoosing gamesChoosing){
        super(cards);

        this.source=source;
        this.twoStar=twoStar;
        this.threeStar=threeStar;
        this.destination=destination;
        this.gamesChoosing=gamesChoosing;
        returnButton.addActionListener(new Ret());

        lexer=new Lexer(source);//要不要replace
        parser=new Parser(lexer);
        AST_Identifier param=new AST_Identifier("abstraction",this);
        AST body=parser.parse(this);
        abstraction =new AST_Abstraction(param,body,this);
        overTextArea.setText("");//清空

        inputButton.setText("重玩");


        JButton button=new JButton("目的 ： "+destination);
        button.setEnabled(false);
        button.setBounds(780,3,350,24);
        panel.add(button);


        inputTextField.setText(source);
        inputTextField.setEditable(false);

        updateMessage();
    }

    void updateMessage() {
        super.updateMessage();
        if(abstraction.body.toString(1).equals(destination)){//通关了,无需abstraction.body instanceof AST_Identifier &&

            passSuccessfully=true;
            checkHistory(abstraction.body,false);
            int history;

            String starMessage="？星通关";
            String newRecordOrNot="成功";

            try{
                File file=new File(source);
                Scanner in =new Scanner(file);
                history=in.nextInt();

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
}
