package cn.seecoder;

import java.util.Scanner;

class Global {
    //一些没什么用的东西


    static boolean to_seecoder =true;



    static final String help= "\n" +
            "set：修改或查看你的模式\n" +
            "   表达式打印模式：seecoder , simplified , medium , full\n" +
            "   规约模式：getline , process , final\n" +
            "   AST树打印模式：oblique , verticalBox\n" +
            "   check:查看当前模式\n" +
            "function：修改或查看你的函数\n" +
            "   add：添加一个新的至指定位置\n" +
            "   change：修改指定位置的函数\n"+
            "   delete：删除某个函数\n" +
            "   initial：还原成默认\n" +
            "   check：查看\n" +
            "tip：查看使用技巧\n" +
            "quit：退出程序\n" +
            "\n" +
            "规约时：\n" +
            "   tree：打印AST树\n" +
            "   step：输入正整数k并立刻进行k步B规约\n" +
            "   total：查看已经进行B规约的步数\n" +
            "   seecoder：临时改变打印模式至 seecoder\n" +
            "   simplified：临时改变打印模式至 simplified\n" +
            "   medium：临时改变打印模式至 medium\n" +
            "   full：临时改变打印模式至 full\n" +
            "   out：返回上一级\n" +
            "\n" +
            "\n";


    static void print_tip(){
        Scanner scanner=new Scanner(System.in);
        for(String i:tip){
            while (!scanner.nextLine().equals("")){
                System.out.println("wrong input!");
            }
            System.out.print(i);
        }
    }

    static private final String[] tip={
            "",
            "",
            "",
            "help中所有单词都不分大小写\n",
            "seecoder有那么长，所以可以尝试一下输入 67\n",
            "另外三种表达式打印模式可以用它们的首字母来简写\n",
            "oblique , vertical也是如此\n",
            "此外还有，t=tree,func=function,del=delete,init=initial\n",
            "",
            "第一次使用时可能还没有mode文件以及function文件,但是不必担心\n",
            "只要你去设置模式或修改函数,理论上它是会自动生成的\n",
            "比如试下输入 func \\n init \\n \n",
            "",
            "set，func，help,tip 以及lambda表达式 最好要看到 “Lambda-> ”时再输入\n",
            "不然会报错\n",
            "",
            "",
            "",
            "Lambda ：",
            "Lambda ：",
            "Lambda ：",
            "Lambda ：",
            "Lambda ：",
            "Lambda ：",
            "\n",
    };

    static final String left_parenthesis ="(";
    static final String right_parenthesis =")";
    static final String lambda ="\\";
    static final String dot = ".";


}
