package cheng.demo;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompilerDemo {
    private static final String source = """
                public class HelloWorld2 {
                    public static void main(String[] args){
                        System.out.println("Hello world!");
                        System.out.println("Hello world2!");
                        System.out.println("Hello world3!");
                    }
                }
            """;

    private static JavaFileObject tempJavaFileObject;
    private static final Pattern CLASS_PATTERN = Pattern.compile("(?:public\\s+)?class\\s+(\\w+)");

    public static void main(String[] args) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        JavaFileManager javaFileManager = new TmpJavaFileManager(compiler.getStandardFileManager(collector, null, null));

        String className = null;
        Matcher matcher = CLASS_PATTERN.matcher(source);
        if (matcher.find()) {
            className = matcher.group(1);
        }


        JavaFileObject sourceJavaFileObject = new TmpJavaFileObject(className, source);
        Boolean result = compiler.getTask(null, javaFileManager, collector, null, null, List.of(sourceJavaFileObject)).call();
        if (!result) {
            List<Diagnostic<? extends JavaFileObject>> diagnostics = collector.getDiagnostics();
            diagnostics.forEach(System.out::println);
        }

        if (tempJavaFileObject != null) {
            TmpJavaFileObject javaFileObject = (TmpJavaFileObject) tempJavaFileObject;
            byte[] classBytes = javaFileObject.getClassBytes();
            File file = new File(String.format("target/classes/%s.class", className));
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(classBytes, 0, classBytes.length);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class TmpJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

        protected TmpJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
            tempJavaFileObject = new TmpJavaFileObject(className, source);
            return tempJavaFileObject;
        }
    }

    static class TmpJavaFileObject extends SimpleJavaFileObject {
        private final String source;
        private final ByteArrayOutputStream outputStream;

        public TmpJavaFileObject(String name, String source) {
            super(URI.create("prefixDemo:/" + name + Kind.SOURCE.extension), Kind.SOURCE);
            this.source = source;
            outputStream = new ByteArrayOutputStream();
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            if (!"".equals(source))
                return source;
            else return "";
        }

        @Override
        public OutputStream openOutputStream() {
            return outputStream;
        }

        public byte[] getClassBytes() {
            return outputStream.toByteArray();
        }
    }
}
