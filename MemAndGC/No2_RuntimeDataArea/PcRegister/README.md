# 程序计数器

## 介绍
- jvm中pc寄存器是对物理cpu集群器的模拟抽象。
- 存储指令相关的现场信息。cpu只有把数据挂载到寄存器上才能够运行。
- pc寄存器用来存储指向下一条指令（下一句代码）的地址，由执行引擎来读取下一条代码。
- pc寄存器是一块很小的内存空间，几乎可以忽略，也是运行速度最快的存储区域。
- pc寄存器线程私有。
- 任何时间一个线程只有一个方法在执行（成为**当前方法**）。程序计数器存储当前线程当前方法的JVM指令地址。如果当前方法时native方法，则是未指定值（undefined）
- pc寄存器是程序控制流的指示器。分支、循环、跳转、异常处理、线程恢复等基础功能都依赖它来完成。
- 字节码解释器工作时，通过改变计数器的值来选取下一条要执行的字节码指令。
- 它没有GC，也是唯一一个没有规定OOM的区域。

## 举例说明
源文件：
```java
public class PCRegisterTest {
    public static void main(String[] args) {
        int i = 10;
        int j = 20;
        int k = i + j;
    }
}
```
反编译:
1. 在out目录下进到PCRegisterTest.class的目录下
2. ``javap -v PCRegisterTest.class``

```\
  Last modified 2022年1月8日; size 533 bytes
  SHA-256 checksum 37a917dc9c5fb0f69a9403b9bbff7ad9f2dc5438a6bb6e0fe1413357a46e026e
  Compiled from "PCRegisterTest.java"
public class MemAndGC.No2_RuntimeDataArea.PcRegister.PCRegisterTest
  minor version: 0
  major version: 57
  flags: (0x0021) ACC_PUBLIC, ACC_SUPER
  this_class: #7                          // MemAndGC/No2_RuntimeDataArea/PcRegister/PCRegisterTest
  super_class: #2                         // java/lang/Object
  interfaces: 0, fields: 0, methods: 2, attributes: 1
Constant pool:
   #1 = Methodref          #2.#3          // java/lang/Object."<init>":()V
   #2 = Class              #4             // java/lang/Object
   #3 = NameAndType        #5:#6          // "<init>":()V
   #4 = Utf8               java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Class              #8             // MemAndGC/No2_RuntimeDataArea/PcRegister/PCRegisterTest
   #8 = Utf8               MemAndGC/No2_RuntimeDataArea/PcRegister/PCRegisterTest
   #9 = Utf8               Code
  #10 = Utf8               LineNumberTable
  #11 = Utf8               LocalVariableTable
  #12 = Utf8               this
  #13 = Utf8               LMemAndGC/No2_RuntimeDataArea/PcRegister/PCRegisterTest;
  #14 = Utf8               main
  #15 = Utf8               ([Ljava/lang/String;)V
  #16 = Utf8               args
  #17 = Utf8               [Ljava/lang/String;
  #18 = Utf8               i
  #19 = Utf8               I
  #20 = Utf8               j
  #21 = Utf8               k
  #22 = Utf8               SourceFile
  #23 = Utf8               PCRegisterTest.java
{
  public MemAndGC.No2_RuntimeDataArea.PcRegister.PCRegisterTest();
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 3: 0
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0       5     0  this   LMemAndGC/No2_RuntimeDataArea/PcRegister/PCRegisterTest;

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=4, args_size=1
         0: bipush        10        # 指令地址 操作指令
         2: istore_1
         3: bipush        20
         5: istore_2
         6: iload_1
         7: iload_2
         8: iadd
         9: istore_3
        10: return
      LineNumberTable:
        line 5: 0
        line 6: 3
        line 7: 6
        line 8: 10
      LocalVariableTable:
        Start  Length  Slot  Name   Signature
            0      11     0  args   [Ljava/lang/String;
            3       8     1     i   I
            6       5     2     j   I
           10       1     3     k   I
}
SourceFile: "PCRegisterTest.java"

```
上面反编译后的指令地址就是pc寄存器里存储的值。执行引擎读取这个值，执行它对应的操作指令。

## 常见问题