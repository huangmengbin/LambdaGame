package cn.seecoder;

import javax.swing.*;
import java.awt.*;

public class GameStartNewGame {

    private JPanel cards=new JPanel(new CardLayout());

    GameStartNewGame(){
        JFrame frame=new JFrame();
        frame.setTitle("Lambda Interpreter");
        frame.setBounds(0,0,1920*Global.ScreenWidth/1920,1040*Global.ScreenHeight/1080);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点 X 关闭所有程序

        frame.add(cards);

        GameWelcome welcome=new GameWelcome(cards);

        cards.add(welcome.panel);
        CardLayout cl=(CardLayout)(cards.getLayout());
        cl.last(cards);

        cards.updateUI();

    }
}
