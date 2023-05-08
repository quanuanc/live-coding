package cheng.execute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JavaClassExecutor {

    private static final String jdkSystemClass = "java/lang/System";
    private static final String jdkScannerClass = "java/util/Scanner";
    private static final String customSystemClass = "cheng/execute/HackSystem";
    private static final String customScannerClass = "cheng/execute/HackScanner";

    private static final String invokeMethodName = "main";

    public static String execute(byte[] bytes, String systemIn) {
        ClassModifier classModifier = new ClassModifier(bytes);
        classModifier.modifyUTF8Constant(jdkSystemClass, customSystemClass);
        classModifier.modifyUTF8Constant(jdkScannerClass, customScannerClass);
        byte[] modifiedBytes = classModifier.getClassByte();

        ((HackInputStream) HackSystem.in).set(systemIn);

        HotSwapClassLoader classLoader = new HotSwapClassLoader();
        Class<?> clazz = classLoader.loadBytes(modifiedBytes);

        try {
            Method mainMethod = clazz.getMethod(invokeMethodName, String[].class);
            mainMethod.invoke(null, (Object) null);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            String err = "No such method: " + invokeMethodName;
            HackSystem.err.println(err);
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace(HackSystem.err);
        }

        String res = HackSystem.getBufferString();
        HackSystem.closeBuffer();
        return res;
    }
}
