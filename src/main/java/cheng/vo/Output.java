package cheng.vo;

public class Output {
    private Input input;
    private String runResult;

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public String getRunResult() {
        return runResult;
    }

    public void setRunResult(String runResult) {
        this.runResult = runResult;
    }

    @Override
    public String toString() {
        return "Output{" +
                "input=" + input +
                ", runResult='" + runResult + '\'' +
                '}';
    }
}
