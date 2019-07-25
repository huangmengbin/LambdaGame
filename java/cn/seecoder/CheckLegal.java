package cn.seecoder;

import java.lang.*;
import java.util.ArrayList;

abstract class CheckLegal {

    static void check_source_string(String source) throws Exception{
        int number = 0;

            for (int i = 0; i < source.length(); i++) {
                if(number<0){throw new Exception("。。。");}
                switch (source.charAt(i)) {
                    case '(':
                        number++;
                        break;
                    case ')':
                        number--;
                        break;
                    case '（':
                    case '）':
                        throw new Exception("怎么会有中文的括号？？？");
                }
            }
        if(number!=0){
            throw new Exception("括号未匹配！");
        }
    }

    static void check_IdentifyName(String string)throws Exception{
        if(string==null ){
            throw new Exception("不允许空的字符串");
        }
        if(string.equals("")){
            throw new Exception("不允许长度为0的字符串");
        }
        for (int i = 0; i < string.length(); i++) {
            switch (string.charAt(i)){
                case '(': case ')': case '）': case '（': case '.':case '\\': case ' ':
                    throw new Exception("不支持此类字符：  '"+string.charAt(i)+"'");
            }
        }
    }

    static void check_tokens(ArrayList<String> token) throws Exception{
        for(int i=0;i<token.size();i++){
            if(token.get(i).equals("(")&&   (token.get(i+1).equals(")")||token.get(i+2).equals(")") )  ){
                throw new Exception("可能存在冗余的括号");
            }
            if(token.get(i).equals("\\")&& !token.get(i+2).equals(".")){
                throw new Exception("\\"+token.get(i+1)+" 后面是不是漏了一个点");
            }
        }
    }
}