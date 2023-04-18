package cheng.demo;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RunClassDemo {
    public static void main(String[] args) {
        String filePath = "target/classes/HelloWorld2.class";
        loadClassFile(filePath);

        String filePath2 = "target/classes/HelloWorld2.class";
        loadClassFile(filePath2);

    }

    static void loadClassFile(String filePath) {
        File classFile = new File(filePath);
        byte[] classBytes;
        try (FileInputStream fis = new FileInputStream(classFile)) {
            classBytes = fis.readAllBytes();
            HotSwapClassLoader classLoader = new HotSwapClassLoader();
            Class<?> aClass = classLoader.loadByte(classBytes);
            Method mainMethod = aClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object[]) new String[]{null});
        } catch (IOException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    static class HotSwapClassLoader extends ClassLoader {
        public HotSwapClassLoader() {
            super(HotSwapClassLoader.class.getClassLoader());
        }

        public Class<?> loadByte(byte[] classBytes) {
            return defineClass(null, classBytes, 0, classBytes.length);
        }
    }
}
