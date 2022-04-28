package cheng.compile;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringSourceCompiler {

    private static final Map<String, JavaFileObject> fileObjectMap = new ConcurrentHashMap<>();

    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z\\d]*)\\s*");

    public static byte[] compile(String source, DiagnosticCollector<JavaFileObject> diagnosticCollector) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileManager javaFileManager = new TmpJavaFileManager(compiler.getStandardFileManager(diagnosticCollector, null, null));
        Matcher matcher = CLASS_PATTERN.matcher(source);
        String className;
        if (matcher.find())
            className = matcher.group(1);
        else
            throw new IllegalArgumentException("No valid class");

        JavaFileObject sourceJavaFileObject = new TmpJavaFileObject(className, source);
        Boolean result = compiler.getTask(null, javaFileManager, diagnosticCollector, null, null, Collections.singletonList(sourceJavaFileObject)).call();

        JavaFileObject bytesJavaFileObject = fileObjectMap.get(className);
        if (result && bytesJavaFileObject != null)
            return ((TmpJavaFileObject) bytesJavaFileObject).getCompiledBytes();
        return null;
    }

    public static class TmpJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
        protected TmpJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        @Override
        public JavaFileObject getJavaFileForInput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind) throws IOException {
            JavaFileObject javaFileObject = fileObjectMap.get(className);
            if (javaFileObject == null) {
                return super.getJavaFileForInput(location, className, kind);
            }
            return javaFileObject;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
            JavaFileObject javaFileObject = new TmpJavaFileObject(className, kind);
            fileObjectMap.put(className, javaFileObject);
            return javaFileObject;
        }
    }

    public static class TmpJavaFileObject extends SimpleJavaFileObject {
        private final String source;
        private ByteArrayOutputStream outputStream;

        public TmpJavaFileObject(String name, String source) {
            super(URI.create("String:///" + name + Kind.SOURCE.extension), Kind.SOURCE);
            this.source = source;
        }

        public TmpJavaFileObject(String name, Kind kind) {
            super(URI.create("String:///" + name + Kind.SOURCE.extension), kind);
            this.source = null;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            if (source == null) {
                throw new IllegalArgumentException("source == null");
            }
            return source;
        }

        @Override
        public OutputStream openOutputStream() {
            outputStream = new ByteArrayOutputStream();
            return outputStream;
        }

        public byte[] getCompiledBytes() {
            return outputStream.toByteArray();
        }
    }
}
