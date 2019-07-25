package cn.seecoder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

class GameExploration {

    int step=0;

    Lexer lexer;
    Parser parser;
    AST_Abstraction abstraction;
    private int height;

    JPanel cards;
    JPanel panel=new JPanel();
    Box inputBox=Box.createHorizontalBox();
    JTextField inputTextField;
    JButton inputButton=new JButton("绘制");
    private JButton nextStepButton =new JButton("下一步");
    private JButton printButton=new JButton("打印");

    private JPanel outputPanel =new JPanel();
    private JScrollPane outputScrollpane=new JScrollPane();
    private ArrayList<JButton>buttonArrayList=new ArrayList<>();

    boolean passSuccessfully=false;

    private JPanel treePanel=new JPanel();
    JPanel verticalPanel = new JPanel();

    JScrollPane overScrollpane;
    JTextArea overTextArea;
    JPanel overPanel=new JPanel();




    GameExploration(JPanel cards){

        this.cards=cards;

        panel.setLayout(null);//还不如自己手动布局。。。

        JButton returnButton =new JButton("返回");
        returnButton.addActionListener(new Ret());
        returnButton.setBounds(0,5,60,30);
        panel.add(returnButton);

        inputBox.setBounds(300,30,800,40);
        inputTextField =new JTextField(60);
        inputTextField.setEditable(true);
        inputTextField.setFont(new Font("宋体",Font.BOLD,20));
        inputButton.addActionListener(new InputSure());
        nextStepButton.addActionListener(new NextOne());
        printButton.addActionListener(new Print());
        inputBox.add(inputTextField);
        inputBox.add(Box.createHorizontalStrut(30));
        inputBox.add(inputButton);
        inputBox.add(Box.createHorizontalStrut(20));
        inputBox.add(nextStepButton);
        inputBox.add(Box.createHorizontalStrut(20));
        inputBox.add(printButton);



        panel.add(inputBox);

        outputPanel.setLayout(new BoxLayout(outputPanel,BoxLayout.Y_AXIS));
        outputScrollpane.createHorizontalScrollBar();
        outputScrollpane.createVerticalScrollBar();
        outputScrollpane.setViewportView(outputPanel);
        outputScrollpane.setBounds(30,75,1800,230);
        panel.add(outputScrollpane);

        treePanel.setLayout(new BorderLayout());
        verticalPanel.setLayout(null);
        treePanel.add(verticalPanel);
        JScrollPane treeScrollPane=new JScrollPane();
        treeScrollPane.createHorizontalScrollBar();
        treeScrollPane.createVerticalScrollBar();
        treeScrollPane.setViewportView(treePanel);
        treeScrollPane.setBounds(30,320,1800,660);
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

        step++;
        abstraction.body.calculate_node_distance();


        JButton historyButton=new JButton(step+". "+ abstraction.body.toString(3));//括号模式
        historyButton.addActionListener(new History(abstraction.body.toString(3)));//括号模式
        outputPanel.add(historyButton);
        buttonArrayList.add(historyButton);

        //输出框自动滚到最底部
        outputScrollpane.getViewport().setViewPosition(new Point(0, outputScrollpane.getVerticalScrollBar().getMaximum()));//输出框自动滚到最底部

        overTextArea.setText(overTextArea.getText()+step+".\t"+ abstraction.body.toString(3)+"\n");//括号模式
        verticalPanel.removeAll();//清除旧的，每次都是新的vertical
        height= abstraction.body.print_lines();
        treePanel.setPreferredSize(new Dimension(abstraction.body.width + abstraction.body.left_distance+ abstraction.body.right_distance+10,height+10));
        panel.updateUI();
    }

    void checkHistory(AST history_ast,boolean enable){
        history_ast.calculate_node_distance();
        verticalPanel.removeAll();//清除旧的，每次都是新的vertical
        height=history_ast.print_lines(enable);
        treePanel.setPreferredSize(new Dimension(history_ast.width +history_ast.left_distance+history_ast.right_distance+10,height+10));
        panel.updateUI();
    }

    void printMessage(){
        JFrame frame=new JFrame();
        frame.setBounds(50,50,1800,800);
        frame.setVisible(true);
        frame.add(overScrollpane);
    }

    class NextOne implements ActionListener{//下一步键
        public void actionPerformed(ActionEvent e) {
            Bool bool=new Bool(false);
            abstraction =(AST_Abstraction) abstraction.find_and_B_change(bool);
            if(bool.isTrue()) {
                updateMessage();
            }
        }

    }

    void inputSure(){
        step=0;///////////////////////////////////////////////////
        passSuccessfully=false;
        String source= inputTextField.getText().trim();
        try {
            CheckLegal.check_source_string(source);
            lexer = new Lexer(source);//////////////////////////////////////////
            parser = new Parser(lexer);
            AST_Identifier param = new AST_Identifier("abstraction", GameExploration.this);
            AST body = parser.parse(GameExploration.this);
            abstraction = new AST_Abstraction(param, body, GameExploration.this);
            outputPanel.removeAll();
            overTextArea.setText("");//清空
            updateMessage();
        }catch (Exception e){
            e.printStackTrace();
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
            cards.remove(panel);//真的不会出事吗？？？
            CardLayout cl=(CardLayout)(cards.getLayout());
            cl.last(cards);
            cards.updateUI();
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
            AST history_ast=parser.parse(GameExploration.this);
            if(e.getSource()==buttonArrayList.get(buttonArrayList.size()-1)&& !passSuccessfully){
                checkHistory(abstraction.body,true);
            }
            else {
                checkHistory(history_ast,false);
            }
        }
    }
}
