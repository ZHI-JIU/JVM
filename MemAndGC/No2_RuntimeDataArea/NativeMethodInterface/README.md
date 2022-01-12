# 本地方法接口
在jvm中的位置：

![image](NativeMethodInterface)

## 本地方法
- 一个java调用非java代码的接口。
- 定义本地方法时，并不提供实现体（就像接口一样），具体实现是由非java语言在外面实现的。
  ```java
    // java.lang.Object
    public final native Class<?> getClass();
    ```
- 存在的意义就是为了融合其他的编程语言。
- 使用native关键字修饰的方法就是本地方法。
- native可以和除了abstract外的所有java标识符连用。
  - native表示有方法体，只不过不是用java实现的。
  - abstract表示没有方法体
  - 两者是矛盾的，无法同时使用

  
【疑问】本地方法怎么确认对应的方法体究竟是什么？在native method lib中都有唯一对应的方法？
