package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWelcome {

    JPanel panel=new JPanel();
    private JPanel cards;
    private JButton startGame=new JButton("开始冒险");
    private JButton freeGame=new JButton("自由探索");
    private JButton proveGame=new JButton("证明");
    private JButton helpButton=new JButton("帮助");
    private JButton setButton=new JButton("设置");
    GameWelcome(JPanel cards){

        this.cards=cards;

        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setLayout(null);
        Font font=new Font("楷体",Font.BOLD,40*Global.ScreenWidth/1920);
        startGame.setFont(font);
        freeGame.setFont(font);
        proveGame.setFont(font);
        helpButton.setFont(font);
        setButton.setFont(font);
        startGame.setBounds(720*Global.ScreenWidth/1920,300*Global.ScreenHeight/1080,500*Global.ScreenWidth/1920,100*Global.ScreenHeight/1080);
        freeGame.setBounds (720*Global.ScreenWidth/1920,420*Global.ScreenHeight/1080,500*Global.ScreenWidth/1920,100*Global.ScreenHeight/1080);
        proveGame.setBounds (720*Global.ScreenWidth/1920,540*Global.ScreenHeight/1080,500*Global.ScreenWidth/1920,100*Global.ScreenHeight/1080);
        helpButton.setBounds(720*Global.ScreenWidth/1920,660*Global.ScreenHeight/1080,500*Global.ScreenWidth/1920,100*Global.ScreenHeight/1080);
        setButton.setBounds(720*Global.ScreenWidth/1920,780*Global.ScreenHeight/1080,500*Global.ScreenWidth/1920,100*Global.ScreenHeight/1080);

        Start start =new Start();
        startGame.addActionListener(start);
        freeGame.addActionListener(start);
        proveGame.addActionListener(start);
        helpButton.addActionListener(start);
        setButton.addActionListener(start);
        panel.add(startGame);
        panel.add(freeGame);
        panel.add(proveGame);
        panel.add(helpButton);
        panel.add(setButton);

        JButton button =new JButton("退出游戏");
        button.setFont(new Font("宋体",Font.BOLD,14*Global.ScreenWidth/1920));
        button.setBounds(1750*Global.ScreenWidth/1920,4,150*Global.ScreenWidth/1920,45*Global.ScreenHeight/1080);
        button.addActionListener(e -> {
            Object[] bs=new Object[]{"忍痛拒绝","再玩一会儿"};
            JOptionPane.showOptionDialog(panel,"确定要退出吗？","退出游戏"  ,JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,bs,null);

        });
        panel.add(button);
    }
    private class Start implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            CardLayout cl=(CardLayout)(cards.getLayout());

            if(e.getSource().equals(startGame)){
                GamesChoosing games=new GamesChoosing(cards);
                cards.add(games.panel);
            }
            else if(e.getSource().equals(freeGame)){
                GameFreelyExplore explore=new GameFreelyExplore(cards);
                cards.add(explore.panel);
            }
            else if(e.getSource().equals(proveGame)){
                GameProved proved = new GameProved(cards);
                cards.add(proved.panel);
            }
            else if(e.getSource().equals(helpButton)){
                GameHelp gameHelp = new GameHelp(cards);
                cards.add(gameHelp.panel);
            }
            else if(e.getSource().equals(setButton)){
                GameSetting gameSetting=new GameSetting(cards);
                cards.add(gameSetting.panel);
            }


            cl.last(cards);
            cards.updateUI();
        }
    }
}
