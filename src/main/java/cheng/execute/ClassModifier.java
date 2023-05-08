package cheng.execute;

public class ClassModifier {
    private static final int CONSTANT_POOL_COUNT_INDEX = 8;
    private static final int CONSTANT_UTF8_INFO = 1;
    private static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5};
    private static final int u1 = 1;
    private static final int u2 = 2;

    private byte[] classByte;

    public ClassModifier(byte[] classByte) {
        this.classByte = classByte;
    }

    public int getConstantPoolCount() {
        return ByteUtils.byte2Int(classByte, CONSTANT_POOL_COUNT_INDEX, u2);
    }

    public void modifyUTF8Constant(String oldStr, String newStr) {
        int cpc = getConstantPoolCount(); //常量池中常量的数量
        int offset = CONSTANT_POOL_COUNT_INDEX + u2;
        for (int i = 1; i < cpc; i++) {
            int tag = ByteUtils.byte2Int(classByte, offset, u1);
            if (tag == CONSTANT_UTF8_INFO) {
                int len = ByteUtils.byte2Int(classByte, offset + u1, u2);
                offset += u1 + u2;
                String str = ByteUtils.byte2String(classByte, offset, len);
                if (str.equals(oldStr)) {
                    byte[] strReplaceBytes = ByteUtils.string2Byte(newStr);
                    byte[] intReplaceBytes = ByteUtils.int2Byte(strReplaceBytes.length, u2);
                    classByte = ByteUtils.byteReplace(classByte, offset - u2, u2, intReplaceBytes);
                    classByte = ByteUtils.byteReplace(classByte, offset, len, strReplaceBytes);
                    return;
                } else {
                    offset += len;
                }
            } else {
                offset += CONSTANT_ITEM_LENGTH[tag];
            }
        }
    }

    public byte[] getClassByte() {
        return classByte;
    }
}
