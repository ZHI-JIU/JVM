package MemAndGC.No2_RuntimeDataArea.Heap;

import java.util.ArrayList;
import java.util.Random;

public class HeapGCTest {
    // 创建一个较大的对象
    byte[] buffer = new byte[new Random().nextInt(1024 * 200)];

    public static void main(String[] args) {
        ArrayList<HeapGCTest> list = new ArrayList<>();
        while (true) {
            list.add(new HeapGCTest());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
