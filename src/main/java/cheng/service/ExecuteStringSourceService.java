package cheng.service;

import cheng.compile.StringSourceCompiler;
import cheng.execute.JavaClassExecutor;
import org.springframework.stereotype.Service;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ExecuteStringSourceService {

    private static final int RUN_TIME_LIMIT = 15;

    private static final int N_THREAD = 5;

    private static final String WAIT_WARNING = "服务器忙，请稍后提交";

    private static final String NO_OUTPUT = "Nothing.";

//    private static final ExecutorService pool = new ThreadPoolExecutor(N_THREAD, N_THREAD,
//            0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(N_THREAD));
    private static final ExecutorService pool = Executors.newFixedThreadPool(N_THREAD);

    public String execute(String source, String systemIn) {
        DiagnosticCollector<JavaFileObject> compileCollector = new DiagnosticCollector<>();

        byte[] classBytes = StringSourceCompiler.compile(source, compileCollector);

        if (classBytes == null) {
            List<Diagnostic<? extends JavaFileObject>> compileError = compileCollector.getDiagnostics();
            StringBuilder compileErrorRes = new StringBuilder();
            for (Diagnostic<? extends JavaFileObject> diagnostic : compileError) {
                compileErrorRes.append("Compilation error at ");
                compileErrorRes.append(diagnostic.getLineNumber());
                compileErrorRes.append(".");
                compileErrorRes.append(System.lineSeparator());
            }
            return compileErrorRes.toString();
        }

        Callable<String> runTask = () -> JavaClassExecutor.execute(classBytes, systemIn);

        Future<String> res;
        try {
            res = pool.submit(runTask);
        } catch (RejectedExecutionException e) {
            return WAIT_WARNING;
        }

        String runResult;
        try {
            runResult = res.get(RUN_TIME_LIMIT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return "Program interrupted.";
        } catch (ExecutionException e) {
            return e.getCause().getMessage();
        } catch (TimeoutException e) {
            runResult = "Time Limit Exceeded.";
        } finally {
            res.cancel(true);
        }
        return runResult != null ? runResult : NO_OUTPUT;
    }
}