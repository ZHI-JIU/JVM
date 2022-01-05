package MemAndGC.ClassLoader.Ch2;

import java.io.FileNotFoundException;
import java.util.Objects;

public class UserDefineCL {
    public class CustomClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] result = getClassFromCustomPath(name);
            if (Objects.isNull(result)) {
                try {
                    throw new FileNotFoundException();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                return defineClass(name, result, 0, result.length);
            }
            return null;
        }

        private byte[] getClassFromCustomPath(String name) {
            // 從用戶指定的路徑中讀取字節碼文件
            return null;
        }


    }
}
