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
    private JButton helpButton=new JButton("帮助");
    private JButton setButton=new JButton("设置");
    GameWelcome(JPanel cards){

        this.cards=cards;

        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setLayout(null);
        Font font=new Font("楷体",Font.BOLD,40);
        startGame.setFont(font);
        freeGame.setFont(font);
        helpButton.setFont(font);
        setButton.setFont(font);
        startGame.setBounds(720,400,500,100);
        freeGame.setBounds (720,520,500,100);
        helpButton.setBounds(720,640,500,100);
        setButton.setBounds(720,760,500,100);

        Start start =new Start();
        startGame.addActionListener(start);
        freeGame.addActionListener(start);
        helpButton.addActionListener(start);
        setButton.addActionListener(start);
        panel.add(startGame);
        panel.add(freeGame);
        panel.add(helpButton);
        panel.add(setButton);

        JButton button =new JButton("退出游戏");
        button.setBounds(1800,4,99,37);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] bs=new Object[]{"忍痛拒绝","再玩一会儿"};
                JOptionPane.showOptionDialog(panel,"确定要退出吗？","退出游戏"  ,JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,bs,null);

            }
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
                GameFreelyExplore printTree_gui=new GameFreelyExplore(cards);
                cards.add(printTree_gui.panel);
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
