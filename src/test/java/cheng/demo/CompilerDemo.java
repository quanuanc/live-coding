package cheng.demo;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

public class CompilerDemo {
    private static final String source = """
                public class HelloWorld{
                    public static void main(String[] args){
                        System.out.println("Hello world!");
                    }
                }
            """;

    public static void main(String[] args) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        JavaFileManager javaFileManager = new TmpJavaFileManager(compiler.getStandardFileManager(collector, null, null));

        String className = "HelloWorld";

        JavaFileObject sourceJavaFileObject = new TmpJavaFileObject(className, source);
        Boolean result = compiler.getTask(null, javaFileManager, collector, null, null, List.of(sourceJavaFileObject)).call();
        if (!result) {
            List<Diagnostic<? extends JavaFileObject>> diagnostics = collector.getDiagnostics();
            diagnostics.forEach(System.out::println);
        }
    }

    static class TmpJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

        protected TmpJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
            return new TmpJavaFileObject(className, source);
        }
    }

    static class TmpJavaFileObject extends SimpleJavaFileObject {
        private final String source;

        public TmpJavaFileObject(String name, String source) {
            super(URI.create("String:///" + name + Kind.SOURCE.extension), Kind.SOURCE);
            this.source = source;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            if (!"".equals(source))
                return source;
            else return "";
        }

        @Override
        public OutputStream openOutputStream() {
            return new ByteArrayOutputStream();
        }
    }
}
