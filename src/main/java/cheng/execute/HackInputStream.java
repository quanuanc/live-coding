package cheng.execute;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HackInputStream extends InputStream {
    public static final ThreadLocal<InputStream> holdInputStream = new ThreadLocal<>();

    @Override
    public int read() {
        return 0;
    }

    @Override
    public void close() {
        holdInputStream.remove();
    }

    public InputStream get() {
        return holdInputStream.get();
    }

    public void set(String systemIn) {
        holdInputStream.set(new ByteArrayInputStream(systemIn.getBytes()));
    }
}
