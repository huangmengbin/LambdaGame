package cn.seecoder;

class Bool {     //我也不想的啊。。。

    private boolean value;

    Bool(){}

    Bool (boolean value){
        this.value=value;
    }

    void setValue(boolean value){
        this.value=value;
    }

    boolean isTrue(){return value;}

    boolean equals(boolean that){return value==that;}

}
