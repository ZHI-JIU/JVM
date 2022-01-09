package MemAndGC.No2_RuntimeDataArea.JVMStack;

import java.util.Date;

public class LocalVariablesTest {
    private int count = 0;

    public LocalVariablesTest() {
        count = 1;
    }

    public static void main(String[] args) {
        LocalVariablesTest test = new LocalVariablesTest();
        int num = 10;
        test.test1();
    }

    public void test1() {
        Date date = new Date();
        String name1 = "name1";
        String info = test2(date, name1);
        System.out.println(date + name1);
    }

    public String test2(Date date, String name) {
        date = null;
        name = "name";
        double weight = 130.5;
        return "";
    }

//    static public void test3() {
//        this.test1();
//    }

    public void test4() {
        int a = 0;
        {
            int b = 0;
            b = a + 1;
        }
        // b的作用域结束了，c复用b的槽位
        int c = a + 1;
    }
}










































