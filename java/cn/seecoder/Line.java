package cn.seecoder;

import javax.swing.*;
import java.awt.*;

public class Line extends JPanel {//这仅仅是一条线段而已，用法是直接 new 即可
    private int x1,x2,y1,y2;
    Color color=Color.BLACK;
    Line(int x1,int y1,int x2,int y2){
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
        this.setBounds(0,0,Math.max(x1,x2)+67,Math.max(y1,y2)+67);//理论上加 1 就 ok
    }
    Line(int x1,int y1,int x2,int y2,Color color){
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
        this.setBounds(0,0,Math.max(x1,x2)+67,Math.max(y1,y2)+67);//理论上加 1 就 ok
        this.color=color;
    }
    protected void paintComponent(Graphics g){
        g.setColor(this.color);
        g.drawLine(x1,y1,x2,y2);
    }
}