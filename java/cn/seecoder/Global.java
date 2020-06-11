package cn.seecoder;

import java.awt.*;

class Global {


    static final int ScreenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    static final int ScreenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();


    static final String help= "\n" +
            "\n" +
            "function：修改或查看你的函数\n" +
            "   add：添加一个新的至指定位置\n" +
            "   change：修改指定位置的函数\n"+
            "   delete：删除某个函数\n" +
            "   initial：还原成默认\n" +
            "   check：查看\n\n" +
            "DELETE-GAME：\n" +
            "    删除存档！\n" +
            "\n" +
            "\n";


    static final String left_parenthesis ="(";
    static final String right_parenthesis =")";
    static final String lambda ="\\";
    static final String dot = ".";


}
