package cheng.execute;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ByteUtilsTest {

    @Test
    void byte2Int() {
        byte[] bytes = {1,1};
        int num = ByteUtils.byte2Int(bytes,0,bytes.length);
        System.out.println(num);
    }
}