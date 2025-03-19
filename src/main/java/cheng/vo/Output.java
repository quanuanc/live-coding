package cheng.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Output {
    private Input input;
    private String runResult;

    @Override
    public String toString() {
        return "Output{" +
                "input=" + input +
                ", runResult='" + runResult + '\'' +
                '}';
    }
}
