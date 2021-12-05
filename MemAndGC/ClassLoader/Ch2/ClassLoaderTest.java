package MemAndGC.ClassLoader.Ch2;

public class ClassLoaderTest {
    public static void main(String[] args) {
        // 获取系统类加载器
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        // jdk.internal.loader.ClassLoaders$AppClassLoader@3fee733d
        System.out.println(systemClassLoader);

        // 获取上层加载器：扩展类加载器
        ClassLoader extClassLoader = systemClassLoader.getParent();
        // jdk.internal.loader.ClassLoaders$PlatformClassLoader@10f87f48
        System.out.println(extClassLoader);

        // 获取上层加载器：引导类加载器
        ClassLoader bootStrapClassLoader = extClassLoader.getParent();
        // null
        System.out.println(bootStrapClassLoader);

        // 获取用户自定义类的类加载器：默认使用系统类加载器
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();
        // jdk.internal.loader.ClassLoaders$AppClassLoader@3fee733d
        // 和系统类加载器完全一致
        System.out.println(classLoader);

        // 获取String的类加载器：使用引导类加载器
        ClassLoader stringCL = String.class.getClassLoader();
        // null
        System.out.println(stringCL);

        // 获取引导类加载器可以加载的

    }
}
