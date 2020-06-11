package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

class GameProved {
    private static ArrayList<myPair> arrayList=new ArrayList<>();

    static final String FILE_PROVE ="file/FILE_PROVE";
    JPanel panel=new JPanel(null);
    JPanel cards;
    private JTextArea textArea = new JTextArea();


    GameProved(JPanel cards){
        this.cards=cards;
        JButton returnButton=new JButton("返回");
        returnButton.setBounds(0,5,60*Global.ScreenWidth/1920,30*Global.ScreenHeight/1080);
        returnButton.addActionListener(e -> {
            cards.remove(panel);
            CardLayout cl=(CardLayout)(cards.getLayout());
            cl.last(cards);
            cards.updateUI();
        });
        panel.add(returnButton);


        textArea.setEditable(false);
        textArea.setFont(new Font("黑体",Font.BOLD,16*Global.ScreenWidth/1920));
        updateMessage();
        JScrollPane scrollPane =new JScrollPane(textArea);
        scrollPane.setBounds(180*Global.ScreenWidth/1920,75*Global.ScreenHeight/1080,750*Global.ScreenWidth/1920,880*Global.ScreenHeight/1080);
        scrollPane.createVerticalScrollBar();
        scrollPane.createHorizontalScrollBar();

        panel.add(scrollPane);

        JButton addButton=new JButton("新增证明");
        addButton.setBounds(1200*Global.ScreenWidth/1920,280*Global.ScreenHeight/1080,160*Global.ScreenWidth/1920,50*Global.ScreenHeight/1080);
        addButton.addActionListener( e -> {
            String destination =null;
            String source = JOptionPane.showInputDialog(panel,"source=","新建证明",JOptionPane.INFORMATION_MESSAGE);
            source = proved_init(source);
            if(could_be_proved(source)) {
                destination = JOptionPane.showInputDialog(panel, "destination=", "source=" + source, JOptionPane.INFORMATION_MESSAGE);
            }
            if(destination!=null && !destination.trim().equals("")) {
                destination = destination.trim();
                String[] strings = destination.split(" ");
                if (strings.length == 1 && Character.isUpperCase(strings[0].charAt(0))){
                    GameProving game = new GameProving(cards, source, destination, this);
                    cards.add(game.panel);
                    CardLayout cl = (CardLayout) (cards.getLayout());
                    cl.last(cards);
                }
            }
            });
        panel.add(addButton);

        JButton delButton=new JButton("删除证明");
        delButton.setBounds(1200*Global.ScreenWidth/1920,350*Global.ScreenHeight/1080,160*Global.ScreenWidth/1920,50*Global.ScreenHeight/1080);
        delButton.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(panel,"number=","删除",JOptionPane.INFORMATION_MESSAGE);
            try{
                if(s!=null && !s.equals("")) {
                    del(Integer.parseInt(s.trim()));
                }
            }
            catch (NumberFormatException n){
                n.printStackTrace();
            }
        });
        panel.add(delButton);
    }

    private void updateMessage(){
        String text = read();
        textArea.setText("已证：\n\n"+text);
        panel.updateUI();
    }

    private static class myPair{
        String key;
        String destination;
        int step;
        myPair(String key,String destination,int step){
            this.destination=destination;
            this.key=key;
            this.step=step;
        }
        @Override
        public boolean equals(Object obj) {
            return obj instanceof myPair && ((myPair) obj).key.equals(key);
        }
    }

    void add(String source,String destination,int step){
        adder(source,destination,step);
        updateMessage();
    }
    static void adder(String source,String destination,int step){
        myPair pair=new myPair(source,destination,step);
        arrayList.add(pair);
        arrayList.sort(Comparator.comparing(a -> a.key));
        write();
    }

    private static String proved_init(String source){
        if(source==null||source.equals(""))
            return "";
        source = source.trim();
        while (source.contains("  ")){
            source=source.replace("  "," ");
        }
        return source;
    }
    static boolean could_be_proved(String source){
        String[]all=source.split(" ");
        if(all.length!=2 )return false;
        if( !Character.isUpperCase(all[0].charAt(0))|| !Character.isUpperCase(all[1].charAt(0)))return false;

        for(myPair pair:arrayList){
            if(pair.key.equals(source)){
                return false;
            }
        }

        return true;
    }

    private void del(int num){
        if(num>=0 & num<arrayList.size()){
            if(0==JOptionPane.showConfirmDialog(panel,"   "+arrayList.get(num).key+"   =>   "+arrayList.get(num).destination,"确定删除",JOptionPane.YES_NO_OPTION)) {
                arrayList.remove(num);
            }
        }
        write();
        updateMessage();
    }
    private static String read(){
        arrayList.clear();
        File file=new File(FILE_PROVE);
        boolean b;
        try {
            if(!file.exists()){
                b=file.createNewFile();
            }

            Scanner in=new Scanner(file);
            if(in.hasNextLine()) {
                String[] all = in.nextLine().split(";");
                for (String i : all) {
                    String[] aPair = i.split(",");
                    if (aPair.length == 3) {
                        arrayList.add(new myPair(aPair[0], aPair[1], Integer.parseInt(aPair[2])));
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("尝试读取prove文件时发生错误");
        }
        StringBuilder res=new StringBuilder();
        for (int i = 0; i < arrayList.size(); i++) {
            myPair pair = arrayList.get(i);
            res.append(i).append(".\t").append(pair.key).append("\t\t->\t").append(pair.destination);
            res.append("\t\t").append(pair.step).append("步\n");
        }
        return res.toString();
    }
    private static void write(){
        try {
            FileWriter writer=new FileWriter(FILE_PROVE);
            StringBuilder result =new StringBuilder();
            for(myPair i:arrayList){
                result.append(i.key).append(',').append(i.destination).append(',').append(i.step).append(';');
            }
            writer.write(result.toString());
            System.out.println("file saved！");
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("尝试写prove文件失败");
        }
    }

    static int provedReplace(AST_Application app,AST_Identifier res){
        if(!(app.lhs instanceof AST_Identifier) || !(app.rhs instanceof AST_Identifier)){
            return -1;
        }
        if(!((AST_Identifier) app.lhs).isFunction()||!((AST_Identifier) app.rhs).isFunction()){
            return -1;
        }
        String left = ((AST_Identifier) app.lhs).getName(),right=((AST_Identifier) app.rhs).getName();
        if(arrayList.isEmpty()){
            read();
        }
        for(myPair pair:arrayList){
            String[] keys = pair.key.trim().split(" ");
            if(keys[0].equals(left)&&keys[1].equals(right)) {
                res.setName(pair.destination);
                return pair.step;
            }
        }
        return -67666;
    }
}
