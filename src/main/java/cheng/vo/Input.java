package cheng.vo;

public class Input {
    private String source;
    private String systemIn;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSystemIn() {
        return systemIn;
    }

    public void setSystemIn(String systemIn) {
        this.systemIn = systemIn;
    }

    @Override
    public String toString() {
        return "Input{" +
                "source='" + source + '\'' +
                ", systemIn='" + systemIn + '\'' +
                '}';
    }
}
