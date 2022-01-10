package MemAndGC.No2_RuntimeDataArea.JVMStack;

public class DynamicLinkingTest {
    int num = 10;

    public void methodA() {
        System.out.println("methodA...");
    }

    public void methodB() {
        System.out.println("methodB...");

        methodA();

        num++;
    }
}
