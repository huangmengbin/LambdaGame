package cn.seecoder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

class GameCreating {

    int step=0;

    Lexer lexer;
    Parser parser;
    AST ast;
    int height;

    JPanel cards;
    JPanel panel=new JPanel();
    Box inputBox=Box.createHorizontalBox();
    JTextField inputTextField;
    JButton inputButton=new JButton("确定");
    JButton returnButton =new JButton("返回");

    JPanel outputPanel =new JPanel();
    JScrollPane outputScrollpane=new JScrollPane();
    ArrayList<JButton>buttonArrayList=new ArrayList<>();


    JPanel treePanel=new JPanel();
    JPanel verticalPanel = new JPanel();

    JScrollPane overScrollpane;
    JTextArea overTextArea;
    JPanel overPanel=new JPanel();

    JButton nextStepButton =new JButton("下一步");


    GameCreating(JPanel cards){

        this.cards=cards;

        panel.setLayout(null);//还不如自己手动布局。。。

        inputBox.setBounds(300,30,800,40);
        inputTextField =new JTextField(60);
        inputTextField.setEditable(true);
        inputButton.addActionListener(new InputSure());
        nextStepButton.addActionListener(new NextOne());
        inputBox.add(inputTextField);
        inputBox.add(Box.createHorizontalStrut(30));
        inputBox.add(inputButton);
        inputBox.add(Box.createHorizontalStrut(20));
        inputBox.add(nextStepButton);
        inputBox.add(Box.createHorizontalStrut(20));
        inputBox.add(returnButton);
        returnButton.addActionListener(new Ret());
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



    void updateMessage(){//下一步
        step++;
        ast.calculate_node_distance();


        JButton historyButton=new JButton(step+". "+ast.toString(3));//括号模式
        historyButton.addActionListener(new History(ast.toString(3)));//括号模式
        outputPanel.add(historyButton);
        buttonArrayList.add(historyButton);

        //输出框自动滚到最底部
        outputScrollpane.getViewport().setViewPosition(new Point(0, outputScrollpane.getVerticalScrollBar().getMaximum()));//输出框自动滚到最底部

        overTextArea.setText(overTextArea.getText()+step+".\t"+ast.toString(3)+"\n");//括号模式
        verticalPanel.removeAll();//清除旧的，每次都是新的vertical
        height=ast.print_lines();
        treePanel.setPreferredSize(new Dimension(ast.width +ast.left_distance+ast.right_distance+10,height+10));
        panel.updateUI();
    }

    void checkHistory(AST history_ast,boolean enable){
        history_ast.calculate_node_distance();
        verticalPanel.removeAll();//清除旧的，每次都是新的vertical
        height=history_ast.print_lines(enable);
        treePanel.setPreferredSize(new Dimension(history_ast.width +history_ast.left_distance+history_ast.right_distance+10,height+10));
        panel.updateUI();
    }

    class NextOne implements ActionListener{//下一步键
        public void actionPerformed(ActionEvent e) {
            ast=ast.find_and_B_change(new Bool(false));
            updateMessage();
        }

    }
    class InputSure implements ActionListener{//确定键
        public void actionPerformed(ActionEvent e) {
            step=0;///////////////////////////////////////////////////
            String source= inputTextField.getText().trim();
            lexer=new Lexer(Function.replace(source));//要不要replace
            parser=new Parser(lexer);
            ast=parser.parse(GameCreating.this);
            outputPanel.removeAll();
            overTextArea.setText("");//清空
            updateMessage();
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

            lexer=new Lexer(Function.replace(history));//要不要replace
            parser=new Parser(lexer);
            AST history_ast=parser.parse(GameCreating.this);
            boolean enable;
            enable=(e.getSource()==buttonArrayList.get(buttonArrayList.size()-1));
            checkHistory(history_ast,enable);
        }
    }
}
