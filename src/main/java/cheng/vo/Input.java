package cheng.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Input {
    private String source;
    private String systemIn;

    @Override
    public String toString() {
        return "Input{" +
                "source='" + source + '\'' +
                ", systemIn='" + systemIn + '\'' +
                '}';
    }
}
