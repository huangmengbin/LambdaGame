package cn.seecoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class GameFreelyExplore {

    int step=-1;

    Lexer lexer;
    Parser parser;
    AST_RootSentry ast;
    private int height;

    JPanel cards;
    JPanel panel=new JPanel();
    Box inputBox=Box.createHorizontalBox();
    JTextField inputTextField;
    JButton inputButton=new JButton("绘制");
    private JButton nextStepButton =new JButton("下一步");
    private JButton printButton=new JButton("打印");
    private JButton returnButton =new JButton("返回");

    private JPanel outputPanel =new JPanel();
    private JScrollPane outputScrollpane=new JScrollPane();
    private ArrayList<JButton>buttonArrayList=new ArrayList<>();

    boolean passSuccessfully=false;

    private JPanel treePanel=new JPanel();
    JPanel verticalPanel = new JPanel();

    JScrollPane overScrollpane;
    JTextArea overTextArea;
    JPanel overPanel=new JPanel();




    GameFreelyExplore(JPanel cards){

        this.cards=cards;

        panel.setLayout(null);//还不如自己手动布局。。。

        returnButton.addActionListener(new Ret());
        returnButton.setBounds(1,5,60*Global.ScreenWidth/1920,30*Global.ScreenHeight/1080);
        panel.add(returnButton);

        inputBox.setBounds(167*Global.ScreenWidth/1920,30*Global.ScreenHeight/1080,1650*Global.ScreenWidth/1920,40*Global.ScreenHeight/1080);
        inputTextField =new JTextField(67*Global.ScreenWidth/1920);
        //inputTextField.setEditable(true);
        inputTextField.setFont(new Font("宋体",Font.BOLD,20*Global.ScreenWidth/1920));
        inputButton.addActionListener(new InputSure());
        nextStepButton.addActionListener(new NextOne());
        printButton.addActionListener(new Print());
        inputBox.add(inputTextField);
        inputBox.add(Box.createHorizontalStrut(30*Global.ScreenWidth/1920));
        inputBox.add(inputButton);
        inputBox.add(Box.createHorizontalStrut(20*Global.ScreenWidth/1920));
        inputBox.add(nextStepButton);
        inputBox.add(Box.createHorizontalStrut(160*Global.ScreenWidth/1920));
        inputBox.add(printButton);


        panel.add(inputBox);

        outputPanel.setLayout(new BoxLayout(outputPanel,BoxLayout.Y_AXIS));
        outputScrollpane.createHorizontalScrollBar();
        outputScrollpane.createVerticalScrollBar();
        outputScrollpane.setViewportView(outputPanel);
        outputScrollpane.setBounds(30*Global.ScreenWidth/1920,75*Global.ScreenHeight/1080,1800*Global.ScreenWidth/1920,230*Global.ScreenHeight/1080);
        panel.add(outputScrollpane);

        treePanel.setLayout(new BorderLayout());
        verticalPanel.setLayout(null);
        treePanel.add(verticalPanel);
        JScrollPane treeScrollPane=new JScrollPane();
        treeScrollPane.createHorizontalScrollBar();
        treeScrollPane.createVerticalScrollBar();
        treeScrollPane.setViewportView(treePanel);
        treeScrollPane.setBounds(30*Global.ScreenWidth/1920,320*Global.ScreenHeight/1080,1800*Global.ScreenWidth/1920,660*Global.ScreenHeight/1080);
        panel.add(treeScrollPane);


        overPanel.setLayout(new BorderLayout());
        overTextArea =new JTextArea();
        overTextArea.setEditable(false);
        overScrollpane =new JScrollPane(overTextArea);
        overScrollpane.createVerticalScrollBar();
        overScrollpane.createHorizontalScrollBar();
        overPanel.add(overScrollpane);
    }



    void updateMessage(){//进行下一步后，更新界面

        try {
            step++;
            ast.calculate_node_distance();

            String astString = ast.toString(AST.printMode);

            JButton historyButton = new JButton(step + ". \t" + astString);//括号模式
            historyButton.setFont(new Font("黑体", Font.BOLD, 15*Global.ScreenHeight/1080));
            historyButton.addActionListener(new History(astString));//括号模式
            outputPanel.add(historyButton);
            buttonArrayList.add(historyButton);

            //输出框自动滚到最底部
            outputScrollpane.getViewport().setViewPosition(new Point(0, Integer.MAX_VALUE));//输出框自动滚到最底部

            overTextArea.setText(overTextArea.getText() + step + ".\t" + astString + "\n");//括号模式
            verticalPanel.removeAll();//清除旧的，每次都是新的vertical
            height = ast.print_lines();
            treePanel.setPreferredSize(new Dimension((ast.width + ast.left_distance + ast.right_distance + 10)*Global.ScreenWidth/1920, (height + 10)*Global.ScreenHeight/1080));
            panel.updateUI();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void checkHistory(AST history_ast,boolean enable){
        history_ast.calculate_node_distance();
        verticalPanel.removeAll();//清除旧的，每次都是新的vertical
        height=history_ast.print_lines(enable);
        treePanel.setPreferredSize(new Dimension((history_ast.width +history_ast.left_distance+history_ast.right_distance+10)*Global.ScreenWidth/1920,(height+10)*Global.ScreenHeight/1080));
        panel.updateUI();
    }

    void printMessage(){
        JFrame frame=new JFrame();
        frame.setBounds(50*Global.ScreenWidth/1920,50*Global.ScreenHeight/1080,1800*Global.ScreenWidth/1920,800*Global.ScreenHeight/1080);
        frame.setVisible(true);
        frame.add(overScrollpane);
    }

    void ret(){

        panel.removeAll();
        cards.remove(panel);
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.last(cards);
        cards.updateUI();
    }

    void inputSure(){
        step=-1;///////////////////////////////////////////////////
        passSuccessfully=false;
        String source = inputTextField.getText().trim();
        try {
            CheckLegal.check_source_string(source);
            lexer = new Lexer(source);//////////////////////////////////////////
            parser = new Parser(lexer);
            AST temp = parser.parse(GameFreelyExplore.this);
            ast=new AST_RootSentry(temp,GameFreelyExplore.this);
            outputPanel.removeAll();
            overTextArea.setText("");//清空
            updateMessage();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    class NextOne implements ActionListener{//下一步键
        public void actionPerformed(ActionEvent e) {
            Bool bool=new Bool(false);
            if(ast!=null) {
                ast = (AST_RootSentry) ast.find_and_B_change(bool);
            }
            if(bool.isTrue()) {
                updateMessage();
            }
        }
    }


    class InputSure implements ActionListener{//确定键
        public void actionPerformed(ActionEvent actionEvent) {
            inputSure();
        }
    }

    class Print implements ActionListener{//打印键
        public void actionPerformed(ActionEvent e) {
            printMessage();
        }
    }

    class Ret implements ActionListener{//返回键
        public void actionPerformed(ActionEvent e) {
            ret();
        }
    }

    class History implements ActionListener{//历史
        String history;
        History (String history){
            this.history=history;
        }
        public void actionPerformed(ActionEvent e) {

            lexer=new Lexer(history);//要不要replace
            parser=new Parser(lexer);
            AST history_ast=parser.parse(GameFreelyExplore.this);
            if(e.getSource()==buttonArrayList.get(buttonArrayList.size()-1)&& !passSuccessfully){
                checkHistory(ast,true);
            }
            else {
                checkHistory(new AST_RootSentry(history_ast,GameFreelyExplore.this),false);
            }
        }
    }
}
