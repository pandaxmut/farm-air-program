package com.example.api.controller;

/**
 * 抽象类测试：
 * 发现：类的初始化顺序为：父类静态代码块 > 当前类静态代码块 > 父类构造器 >当前类构造器
  */

public abstract class AAA {
    int good=0;
    static {
        System.out.println("aaa静态代码块");
    }

    public AAA() {
        System.out.println("aaa构造器good->"+good);
    }

    public String eat(){
        System.out.println("aaa->"+good);
        return "eat";
    }

    public String run(){
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return "run";
    }
    public abstract String sleep();
}

class bbb extends AAA{
    int good=1;

    public bbb() {
        super();
        System.out.println("bbb构造器good->"+good);
    }

    static {
        System.out.println("bbb静态代码块");
    }

    @Override
    public String sleep() {

        System.out.println("bbb-> "+good);
        return "bbb-> sleep";
    }
    public String eat(){
        return "bbb-> eat";
    }

    public static void main(String[] args) {
//        bbb b = new bbb();
//        System.out.println(b.sleep());
        String a = null;
        if (a==null){
            System.out.println();
        }
    }
}
