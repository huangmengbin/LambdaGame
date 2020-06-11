package cn.seecoder;

import javax.swing.*;

public class GameProving extends GameFreelyExplore {
    private static final String FILE_PROVE =GameProved.FILE_PROVE;
    private String destination;
    private String source;
    private GameProved proved;

    GameProving(JPanel cards, String source, String destination,GameProved proved) {
        super(cards);
        this.source=source;
        this.destination=destination;
        this.proved=proved;
        JButton button=new JButton("求证 ： "+source+"      =>      "+destination);
        button.setEnabled(false);
        button.setBounds(600*Global.ScreenWidth/1920,3,600*Global.ScreenWidth/1920,24*Global.ScreenHeight/1080);
        panel.add(button);
        inputButton.setText("重证");

        lexer=new Lexer(source);//要不要replace
        parser=new Parser(lexer);
        AST temp = parser.parse(this);
        ast=new AST_RootSentry(temp,this);
        overTextArea.setText("");//清空？？？
        inputTextField.setText(source);
        inputTextField.setEditable(false);
        updateMessage();
    }
    void updateMessage() {
        super.updateMessage();
        if (ast.toString(1).equals(destination)) {//通关了,无需abstraction.body instanceof AST_Identifier &&


            inputButton.setText("证毕");
            inputButton.setEnabled(false);

            passSuccessfully = true;
            checkHistory(ast, false);

            if(proved==null){
                GameProved.adder(source, destination, step);
            }
            else {
                proved.add(source, destination, step);
            }
            Object[] bs = new Object[]{"返回"};//这个怎么搞？？？得修改。。。。
            int chooseMessage = JOptionPane.showOptionDialog(panel, "证明成功", step + "步", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, bs, bs[0]);

            if (chooseMessage == 0) {
                ret();
            }
        }
    }
}
