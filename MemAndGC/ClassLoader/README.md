# 类加载器子系统
- [内存结构概述](#内存结构概述)
- [类加载器与类加载过程]()
- [类加载器分类]()
- [ClassLoader的使用说明]()
- [双亲委派机制]()
- [其他]()

## 内存结构概述
- 简化图
![image](https://github.com/ZHI-JIU/JVM/blob/main/MemAndGC/pic/JVMStructure.jpg)

- 细化图
![image](https://github.com/ZHI-JIU/JVM/blob/main/MemAndGC/pic/JVMStructure2.jpg)

### 类加载器子系统
![image](https://github.com/ZHI-JIU/JVM/blob/main/MemAndGC/pic/ClassLoaderSystem.jpg)

#### 整体作用：
- 负责从本地或者网络中加载符合JVM规范的Class文件。开头有特定的文件标识，在链接的验证阶段校验。
- ClassLoader只负责class文件的加载，是否可以运行由执行引擎来决定。
- 加载的类信息放在方法区中。方法区中还会放运行时常量池信息（字符串字面量、数字常量）。

#### 三个环节
- 加载： 通过类加载器将class文件加载到内存中。分为三类加载器，也可以自定义加载器。
- 链接： 分为验证、准备、解析三个环境。
- 初始化：静态变量的显示初始化。
        
### 运行时数据区
- pc计数器
- 栈（虚拟机栈），每个线程一份，分为栈针、局部变量表、操作栈、动态链接、方法返回地址。
- 本地方法栈
- 堆，存储创建的对象实例，线程间共享，gc主要考虑的空间。
- 方法区，存储类的信息、常量、方法信息等。只有HotSpot虚机才有。

### 执行引擎  
将指令翻译成机器指令，供cpu执行。分为：
- 解释器
- 即时编译器
- 垃圾回收器
    
如果要手写一个JVM，主要需要考虑**类加载器子系统**和**执行引擎**

## 类加载器与类加载过程
### 初步感知
![image](https://github.com/ZHI-JIU/JVM/blob/main/MemAndGC/pic/ClassLoaderProcess.jpg)

### 加载
