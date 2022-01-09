package MemAndGC.No2_RuntimeDataArea.JVMStack;

public class OperateStackTest {
    public void test() {
        byte i = 15;
        int j = 8;
        int k = i + j;
    }

    public int getSum() {
        int m = 80000;
        int n = 700000;
        int k = m + n;
        return k;
    }

    public void testGetSum() {
        int i = getSum();
        int j = 10;
    }
}
