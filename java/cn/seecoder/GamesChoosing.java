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
        gameList.add(new SetGame("SUCC THREE",8,4,"FOUR"));
        gameList.add(new SetGame("POW THREE TWO",11,9,"NINE"));
        gameList.add(new SetGame("SUB FIVE THREE",8,4,"TWO"));
        gameList.add(new SetGame("MAX THREE FOUR",80,50,"FOUR"));

    }

    GamesChoosing(JPanel cards){

        this.cards=cards;
        returnButton.setBounds(0,5,60,30);
        returnButton.addActionListener(new ret());
        panel.add(returnButton);
        JPanel smallpanel=new JPanel();
        JScrollPane jScrollPane=new JScrollPane(smallpanel);
        jScrollPane.createVerticalScrollBar();
        jScrollPane.createHorizontalScrollBar();
        jScrollPane.setBounds(10,60,1880,900);
        panel.add(jScrollPane);
        int number=gameList.size();
        smallpanel.setLayout(new GridLayout(number/6,4,50,40));
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
                    writer.write("999999999");
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

            button.setText("<html><center>"+i+".</center>"+setGame.source+"<center>"+message+"</center></html>");
            button.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));
            button.addActionListener(new choose(setGame.source,setGame.twoStar,setGame.threeStar,setGame.destination));
            smallpanel.add(button);
        }
        panel.updateUI();
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
            GameAdventure game=new GameAdventure(source,cards,twoStar,threeStar,destination);
            cards.add(game.panel);
            CardLayout cl=(CardLayout)(cards.getLayout());
            cl.last(cards);
            JOptionPane.showMessageDialog(null,"目的："+destination,"开始游戏",JOptionPane.PLAIN_MESSAGE);
        }
    }
}
