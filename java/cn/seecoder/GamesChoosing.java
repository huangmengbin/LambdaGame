package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class GamesChoosing {
    JPanel panel=new JPanel(null);
    JPanel cards;
    JButton returnButton=new JButton("返回");
    private JPanel smallpanel;

    static class SetGame{
        String source;
        private int twoStar;
        private int threeStar;
        private String destination;

        SetGame(String source,int twoStar,int threeStar,String destination){
            this.source=source;
            this.twoStar=twoStar;
            this.threeStar=threeStar;
            this.destination=destination;
        }
    }
    static ArrayList<SetGame>gameList=new ArrayList<>();
    static {
        gameList.add(new SetGame("SIXSEVEN",3,2,"67_NB"));//1
        gameList.add(new SetGame("SUCC THREE",10,8,"FOUR"));//2
        gameList.add(new SetGame("PLUS ZERO ONE",10,8,"ONE"));//3
        gameList.add(new SetGame("PLUS TWO THREE",20,18,"FIVE"));//4
        gameList.add(new SetGame("POW THREE TWO",20,18,"NINE"));//5
        gameList.add(new SetGame("PRED ZERO",12,9,"ZERO"));//6
        gameList.add(new SetGame("PRED FOUR",60,50,"THREE"));//7
        gameList.add(new SetGame("SUB FIVE THREE",60,52,"TWO"));//8
        gameList.add(new SetGame("MULT FOUR ZERO",15,13,"ZERO"));//9
        gameList.add(new SetGame("AND TRUE TRUE",10,8,"TRUE"));//10
        gameList.add(new SetGame("AND TRUE FALSE",10,8,"FALSE"));//
        gameList.add(new SetGame("AND FALSE FALSE",10,8,"FALSE"));//
        gameList.add(new SetGame("OR TRUE TRUE",10,8,"TRUE"));//
        gameList.add(new SetGame("OR FALSE TRUE",10,8,"TRUE"));//
        gameList.add(new SetGame("OR FALSE FALSE",10,8,"FALSE"));//
        gameList.add(new SetGame("NOT TRUE",10,8,"FALSE"));//
        gameList.add(new SetGame("NOT FALSE",10,8,"TRUE"));//
        gameList.add(new SetGame("IF TRUE",10,8,"TRUE"));//
        gameList.add(new SetGame("IF TRUE SIX SEVEN",10,8,"SIX"));//
        gameList.add(new SetGame("IF FALSE SIX SEVEN",10,8,"SEVEN"));//
        gameList.add(new SetGame("ISZERO ZERO",10,6,"TRUE"));//
        gameList.add(new SetGame("ISZERO TWO",10,8,"FALSE"));//
        gameList.add(new SetGame("LEQ THREE TWO",45,40,"FALSE"));//
        gameList.add(new SetGame("LEQ TWO THREE",45,40,"TRUE"));//
        gameList.add(new SetGame("EQ ONE TWO",65,62,"FALSE"));//
        gameList.add(new SetGame("EQ TWO ONE",65,62,"FALSE"));//
        gameList.add(new SetGame("EQ ONE ONE",60,55,"TRUE"));//
        gameList.add(new SetGame("MIN ONE TWO",45,40,"ONE"));//
        gameList.add(new SetGame("MAX THREE FOUR",70,60,"FOUR"));//
        gameList.add(new SetGame("FACT ZERO",15,13,"ONE"));//30
        gameList.add(new SetGame("FACT ONE",45,42,"ONE"));//
        gameList.add(new SetGame("FACT TWO",80,75,"TWO"));//
        gameList.add(new SetGame("FACTY ZERO",18,16,"ONE"));//
        gameList.add(new SetGame("FACTY ONE",50,47,"ONE"));//
        gameList.add(new SetGame("CAR(CONS SIXSEVEN(CONS is666 NIL))",12,11,"67_NB"));//
        gameList.add(new SetGame("CDR(CONS SIXSEVEN(CONS is666 NIL))",12,10,"CONS is666 NIL"));//
        gameList.add(new SetGame("NULL NIL",6,5,"TRUE"));//
        gameList.add(new SetGame("NULL (CONS SIX(CONS SEVEN NIL))",12,9,"FALSE"));//
        gameList.add(new SetGame("NULL (CDR(CONS SIXSEVEN NIL))",16,14,"TRUE"));//
        gameList.add(new SetGame("LENGTH NIL",18,16,"ZERO"));//
        gameList.add(new SetGame("LENGTH (CONS SIXSEVEN NIL)",51,48,"ONE"));//
        gameList.add(new SetGame("LENGTH (CDR (CONS 67NB NIL))",28,25,"ZERO"));//

        //gameList.add(new SetGame("",,,""));
    }

    GamesChoosing(JPanel cards){

        this.cards=cards;
        returnButton.setBounds(0,5,60,30);
        returnButton.addActionListener(new ret());
        panel.add(returnButton);
        smallpanel=new JPanel();
        JScrollPane jScrollPane=new JScrollPane(smallpanel);
        jScrollPane.createVerticalScrollBar();
        jScrollPane.createHorizontalScrollBar();
        jScrollPane.setBounds(10,60,1880,900);
        panel.add(jScrollPane);
        int number=gameList.size();
        smallpanel.setLayout(new GridLayout(14,4,50,40));

        updateMessage();


    }

    void updateMessage(){
        smallpanel.removeAll();
        int i=0;
        for(SetGame setGame:gameList) {
            i++;

            int history=999999999;
            try{
                File file=new File(setGame.source);
                Scanner in =new Scanner(file);
                history=in.nextInt();
            }
            catch (Exception e){
                try {
                    e.printStackTrace();
                    FileWriter writer = new FileWriter(setGame.source);
                    writer.write(Integer.valueOf(history).toString());
                    writer.close();
                }
                catch (Exception f){
                    f.printStackTrace();
                }
            }

            JButton button=new JButton();
            String message="";
            if(history==999999999){
                message="未通关";
            }
            else if(history<999999999&&history>setGame.twoStar){
                message="一星通关";
            }
            else if(history<=setGame.twoStar&&history>setGame.threeStar){
                message="两星通关";
            }
            else if(history<=setGame.threeStar){
                message="三星通关";
            }

            String historyString;
            if(history!=999999999){
                historyString="历史记录:"+history+"步";
            }
            else {
                historyString="\n";
            }
            button.setText("<html><center><br><br>"+i+". "+setGame.source+"</center><center>"+message+"</center><center>"+historyString+"<br><br></center></html>");
            button.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));
            button.addActionListener(new choose(setGame.source,setGame.twoStar,setGame.threeStar,setGame.destination));
            smallpanel.add(button);
        }
        this.panel.updateUI();
    }

    class ret implements ActionListener {//返回键
        public void actionPerformed(ActionEvent e) {
            cards.remove(GamesChoosing.this.panel);//真的不会出事吗？？？
            cards.updateUI();
        }
    }

    class choose implements ActionListener{//选关
        String source;
        private int twoStar;
        private int threeStar;
        private String destination;

        choose(String source,int twoStar,int threeStar,String destination){
            this.source=source;
            this.twoStar=twoStar;
            this.threeStar=threeStar;
            this.destination=destination;
        }
        public void actionPerformed(ActionEvent e){
            GameAdventure game=new GameAdventure(source,cards,twoStar,threeStar,destination,GamesChoosing.this);
            cards.add(game.panel);
            CardLayout cl=(CardLayout)(cards.getLayout());
            cl.last(cards);
        }
    }
}
