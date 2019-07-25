package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWelcome {
    JPanel panel=new JPanel();
    private JPanel cards;
    private JButton startGame=new JButton("冒险模式");
    private JButton freeGame=new JButton("探索模式");
    GameWelcome(JPanel cards){

        this.cards=cards;

        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        startGame.addActionListener(new start());
        freeGame.addActionListener(new start());
        panel.add(startGame);
        panel.add(freeGame);
    }
    class start implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            CardLayout cl=(CardLayout)(cards.getLayout());

            if(e.getSource().equals(startGame)){
                GamesChoosing games=new GamesChoosing(cards);
                cards.add(games.panel);
                cl.last(cards);
                cards.updateUI();
            }
            else if(e.getSource().equals(freeGame)){
                GameExploration printTree_gui=new GameExploration(cards);
                cards.add(printTree_gui.panel);
                cl.last(cards);
                cards.updateUI();
            }
        }
    }
}
