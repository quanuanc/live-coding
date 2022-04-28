package cheng.execute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JavaClassExecutor {
    public static String execute(byte[] bytes, String systemIn) {
        ClassModifier classModifier = new ClassModifier(bytes);
        byte[] modifyBytes = classModifier.modifyUTF8Constant("java/lang/System", "cheng/execute/HackSystem");
        modifyBytes = classModifier.modifyUTF8Constant("java/util/Scanner", "cheng/execute/HackScanner");

        ((HackInputStream) HackSystem.in).set(systemIn);

        HotSwapClassLoader classLoader = new HotSwapClassLoader();
        Class<?> clazz = classLoader.loadBytes(modifyBytes);

        try {
            Method mainMethod = clazz.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) null);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace(HackSystem.err);
        }

        String res = HackSystem.getBufferString();
        HackSystem.closeBuffer();
        return res;
    }
}
