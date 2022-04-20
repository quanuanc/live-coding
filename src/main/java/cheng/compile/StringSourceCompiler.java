package cheng.compile;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public class StringSourceCompiler {
    public static byte[] compile(String source, DiagnosticCollector<JavaFileObject> diagnosticCollector) {
        return new byte[]{0};
    }
}
